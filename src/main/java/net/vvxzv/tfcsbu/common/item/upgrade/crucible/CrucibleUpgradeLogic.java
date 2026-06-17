package net.vvxzv.tfcsbu.common.item.upgrade.crucible;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.component.fluid.FluidContainerInfo;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.FoodTraits;
import net.dries007.tfc.common.component.heat.Heat;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.IHeat;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.FluidAlloy;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.data.FluidHeat;
import net.dries007.tfc.util.data.Fuel;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper;
import net.p3pp3rf1y.sophisticatedcore.init.ModCoreDataComponents;
import net.p3pp3rf1y.sophisticatedcore.inventory.StatefulComponentItemHandler;
import net.vvxzv.tfcsbu.common.registry.DataComponent;
import net.vvxzv.tfcsbu.common.utils.CustomTooltipComponent;
import net.vvxzv.tfcsbu.client.gui.GuiUtils;
import net.vvxzv.tfcsbu.common.utils.LogicHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CrucibleUpgradeLogic {
    private final IStorageWrapper storageWrapper;
    private final ItemStack upgrade;
    private final Consumer<ItemStack> saveHandler;
    @Nullable
    private StatefulComponentItemHandler inventory;
    private float temperature = 0.0F;
    private float maxTemperature = 0.0F;
    private long burnTime = 0L;
    private final FluidAlloy alloy;
    private Level level;
    private boolean hasBellows;
    private boolean isTriggerBellows;
    private int bellowsTicks;
    private boolean isPowered;
    private boolean isLocked;

    private static final int BELLOWS_DURATION = 150;

    public CrucibleUpgradeLogic(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> saveHandler) {
        this.storageWrapper = storageWrapper;
        this.upgrade = upgrade;
        this.saveHandler = saveHandler;
        this.alloy = FluidAlloy.empty();
    }

    public StatefulComponentItemHandler getInventory(){
        if (this.inventory == null) {
            this.inventory = new StatefulComponentItemHandler(upgrade, ModCoreDataComponents.LENIENT_CONTAINER.get(),15) {
                @Override
                public int getSlotLimit(int slot) {
                    return 1;
                }

                @Override
                protected void onContentsChanged(int slot, @NotNull ItemStack oldStack, @NotNull ItemStack newStack) {
                    super.onContentsChanged(slot, oldStack, newStack);
                    save();
                }

                @Override
                public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                    return true;
                }
            };
        }

        return this.inventory;
    }

    public void show(GuiGraphics guiGraphics, int guiX, int guiY, int mouseX, int mouseY, ResourceLocation texture) {
        this.showTemperature(guiGraphics, guiX, guiY, mouseX, mouseY);
        this.showAlloy(guiGraphics, guiX, guiY, mouseX, mouseY, texture);
        this.showPowerSwitchTooltip(guiGraphics, guiX, guiY, mouseX, mouseY);
    }

    private void showTemperature(GuiGraphics guiGraphics, int guiX, int guiY, int mouseX, int mouseY) {
        float currentTemp = this.getTemperature();
        //Heat heatLevel = Heat.getHeat(currentTemp);
        //ChatFormatting textColor = heatLevel != null ? heatLevel.getColor() : ChatFormatting.GRAY;
        int dy = Mth.clamp((int)(51.0F * temperature / Heat.BRILLIANT_WHITE.getMax()), 0, 51);
        new CustomTooltipComponent(25, 18, 8, 51){
            @Override
            public Component[] getComponents() {
                //return new Component[]{Component.literal( (int)currentTemp + "°C").withStyle(textColor)};
                return new Component[]{TFCConfig.CLIENT.heatTooltipStyle.get().formatColored((int)currentTemp)};
            }

            @Override
            public boolean hasTooltip() {
                return currentTemp != 0;
            }
        }.draw(guiGraphics, guiX, guiY, mouseX, mouseY);

        if(currentTemp > 30) {
            GuiHelper.blit(guiGraphics, guiX + 23, guiY + 69 - dy, GuiUtils.TEMPERATURE_INDICATOR);
        }
    }

    private void showAlloy(GuiGraphics guiGraphics, int guiX, int guiY, int mouseX, int mouseY, ResourceLocation texture) {
        try {
            getAlloy();
        } catch (Exception e) {
            setAlloy();
            getAlloy();
        }

        FluidStack fluidStack = alloy.getResult();
        if(!fluidStack.isEmpty()) {
            TextureAtlasSprite sprite = RenderHelpers.getAndBindFluidSprite(fluidStack);
            int fillHeight = (int)Math.ceil(31.0F * (float)alloy.getAmount() / (float)this.containerInfo().fluidCapacity());
            RenderHelpers.fillAreaWithSprite(guiGraphics, sprite, guiX + 50, guiY + 89 - fillHeight, 36, fillHeight, 16, 16);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, texture);

            Object2DoubleMap<Fluid> fluids = alloy.getContent();
            List<Component> texts = new ArrayList<>(List.of(Component.translatable(fluidStack.getFluidType().getDescriptionId()).withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD)));
            fluids.forEach((f, d) -> {
                texts.add(
                        Component.translatable(f.getFluidType().getDescriptionId())
                                .append(Component.literal(" (" + String.format("%.1f", d) + "mb)"))
                );
            });

            new CustomTooltipComponent(50, 58, 36, 51){
                @Override
                public Component[] getComponents() {
                    return texts.toArray(new Component[0]);
                }

                @Override
                public boolean hasTooltip() {
                    return true;
                }
            }.draw(guiGraphics, guiX, guiY, mouseX, mouseY);
        }
    }

    private void showPowerSwitchTooltip (GuiGraphics guiGraphics,int guiX, int guiY, int mouseX, int mouseY) {
        MutableComponent powerText = Component.translatable("button.power_switch");
        MutableComponent lockText = Component.translatable("button.crucible.temperature_lock");

        if(this.isPowered()) {
            GuiHelper.blit(guiGraphics, guiX + 106, guiY + 19, GuiUtils.SWITCH_ON);
            powerText.append(Component.literal(": "))
                    .append((Component.translatable("button.power_switch.true")));
        } else {
            GuiHelper.blit(guiGraphics, guiX + 106, guiY + 19, GuiUtils.SWITCH_OFF);
            powerText.append(Component.literal(": "))
                    .append(Component.translatable("button.power_switch.false"));
        }

        if(this.isLocked()) {
            GuiHelper.blit(guiGraphics, guiX + 106, guiY + 37, GuiUtils.LOCK);
            lockText.append(Component.literal(": "))
                    .append((Component.translatable("button.power_switch.true")));
        } else {
            GuiHelper.blit(guiGraphics, guiX + 106, guiY + 37, GuiUtils.UNLOCK);
            lockText.append(Component.literal(": "))
                    .append(Component.translatable("button.power_switch.false"));

        }

        new CustomTooltipComponent(105 , 19, 17, 17) {
            @Override
            public Component[] getComponents() {
                return new Component[]{powerText, Component.translatable("button.power_switch.tooltip").withStyle(ChatFormatting.GRAY)};
            }

            @Override
            public boolean hasTooltip() {
                return true;
            }
        }.draw(guiGraphics, guiX, guiY, mouseX, mouseY);

        new CustomTooltipComponent(105 , 37, 17, 17) {
            @Override
            public Component[] getComponents() {
                return new Component[]{lockText, Component.translatable("button.crucible.temperature_lock.tooltip").withStyle(ChatFormatting.GRAY)};
            }

            @Override
            public boolean hasTooltip() {
                return true;
            }
        }.draw(guiGraphics, guiX, guiY, mouseX, mouseY);
    }

    public void tick(Level level) {
        this.level = level;
        try {
            getAlloy();
        } catch (Exception e) {
            setAlloy();
            getAlloy();
        }
        ICalendar iCalendar = Calendars.get();
        long calendarTick = iCalendar.getCalendarTicks();
        this.getTemperature();

        if(getInventory() != null) {
            StatefulComponentItemHandler handler = getInventory();
            LogicHelper.setFuel(handler, 1, 4);

            ItemStack bellows = handler.getStackInSlot(0);
            this.setHasBellows(bellows.is(TFCBlocks.BELLOWS.get().asItem()));
            if(this.hasBellows && this.isTriggerBellows()) {
                this.setBellowsTicks(this.getBellowsTicks() + 1);
            }
            if (this.getBellowsTicks() >= BELLOWS_DURATION) {
                this.setTriggerBellows(false);
                this.setBellowsTicks(0);
            }

            float extraMaxTemp = 0;
            float temp = 1;

            if(this.isPowered()) {
                int energyToConsume = (int) (this.getTemperature() * 0.02F) + 1;
                if(LogicHelper.consumeElectricity(this.storageWrapper, energyToConsume, this.isPowered(), false)) {
                    this.setBurnTime(calendarTick + 10L);
                    temp = 3;
                } else {
                    this.setMaxTemperature(0);
                }
            } else {
                if(calendarTick - this.getBurnTime() > 0){
                    this.setMaxTemperature(0);
                    ItemStack item = handler.getStackInSlot(4);
                    Fuel fuel = Fuel.get(item);
                    if (fuel != null) {
                        item.shrink(1);
                        this.setMaxTemperature(fuel.temperature());
                        this.setBurnTime(calendarTick + fuel.duration());
                    }
                } else if (this.hasBellows && this.isTriggerBellows() && this.getBellowsTicks() < BELLOWS_DURATION) {
                    temp = 2;
                    extraMaxTemp = 600;
                    this.setBurnTime(this.getBurnTime() - 1L);
                }
            }

            if(this.temperature < this.getMaxTemperature() + extraMaxTemp) {
                this.handleTemperature(temp);
            }

            if(this.temperature > this.getMaxTemperature() + extraMaxTemp) {
                this.handleTemperature(-1);
            }

            if (this.temperature < 0 || calendarTick - this.getBurnTime() > 2800) {
                this.setTemperature(0F);
            }

            if(this.temperature > 0) {
                for(int i = 5; i < 14; i++){
                    ItemStack itemStack = handler.getStackInSlot(i);
                    IHeat iHeat = HeatCapability.get(itemStack);
                    if(iHeat != null){
                        float itemTemp = iHeat.getTemperature();
                        HeatCapability.addTemp(iHeat, this.temperature, 2.0F + this.temperature * 0.0025F);
                        HeatingRecipe recipe = HeatingRecipe.getRecipe(itemStack);
                        if (recipe!= null && recipe.isValidTemperature(itemTemp)) {
                            ItemStack outputItem = recipe.assembleItem(itemStack);
                            FluidStack outputFluid = recipe.assembleFluid(itemStack);
                            FoodCapability.applyTrait(outputItem, FoodTraits.BURNT_TO_A_CRISP);
                            handler.setStackInSlot(i, outputItem);
                            this.fill(outputFluid, IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
            }

            if (this.isMolten()) {
                FluidStack outputDrop = this.drain(1, IFluidHandler.FluidAction.SIMULATE);
                FluidStack outputRemainder = Helpers.mergeOutputFluidIntoSlot(handler, outputDrop, getTemperature(), 14);
                if (outputRemainder.isEmpty()) {
                    this.drain(1, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }

        setAlloy();
    }

    private void handleTemperature(float added) {
        this.setTemperature(this.getTemperature() + added);
    }

    public float getTemperature() {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        if(tag.contains("temperature")) {
            this.temperature = tag.getFloat("temperature");
        } else {
            this.setTemperature(0);
        }
        return this.temperature;
    }

    public void setTemperature(float setNumber) {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        tag.putFloat("temperature", setNumber);
        this.temperature = setNumber;
        this.upgrade.set(DataComponent.TAG, CustomData.of(tag));
        save();
    }

    public float getMaxTemperature() {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        if(tag.contains("maxTemperature")) {
            this.maxTemperature = tag.getFloat("maxTemperature");
        } else {
            this.setMaxTemperature(0);
        }
        return this.maxTemperature;
    }

    public void setMaxTemperature(float setNumber) {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        tag.putFloat("maxTemperature", setNumber);
        this.maxTemperature = setNumber;
        this.upgrade.set(DataComponent.TAG, CustomData.of(tag));
        save();
    }

    public long getBurnTime() {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        if(tag.contains("burnTime")) {
            this.burnTime = tag.getLong("burnTime");
        } else {
            this.setBurnTime(0);
        }
        return this.burnTime;
    }

    public void setBurnTime(long setNumber) {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        tag.putLong("burnTime", setNumber);
        this.burnTime = setNumber;
        this.upgrade.set(DataComponent.TAG, CustomData.of(tag));
        save();
    }

    private void getAlloy() {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        if(!tag.contains("alloy")) {
            this.setAlloy();
        }
        this.alloy.deserializeNBT(tag.getCompound("alloy"));
    }

    private void setAlloy() {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        tag.put("alloy", this.alloy.serializeNBT());
        this.upgrade.set(DataComponent.TAG, CustomData.of(tag));
        save();
    }

    public boolean hasBellows() {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        if(tag.contains("hasBellows")) {
            this.hasBellows = tag.getBoolean("hasBellows");
        } else {
            this.setHasBellows(false);
        }
        return this.hasBellows;
    }

    private void setHasBellows(boolean b) {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        tag.putBoolean("hasBellows", b);
        this.hasBellows = b;
        this.upgrade.set(DataComponent.TAG, CustomData.of(tag));
        save();
    }

    public void activateBellows(Player player) {
        if (this.hasBellows() && (!this.isTriggerBellows() || this.getBellowsTicks() > 20)) {
            this.level.playSound(null, player.getOnPos(), TFCSounds.BELLOWS_BLOW.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            this.setTriggerBellows(true);
            this.setBellowsTicks(0);
        }
    }

    public boolean isTriggerBellows() {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        if(tag.contains("isTriggerBellows")) {
            this.isTriggerBellows = tag.getBoolean("isTriggerBellows");
        } else {
            this.setTriggerBellows(false);
        }
        return this.isTriggerBellows;
    }

    public void setTriggerBellows(boolean t) {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        tag.putBoolean("isTriggerBellows", t);
        this.isTriggerBellows = t;
        this.upgrade.set(DataComponent.TAG, CustomData.of(tag));
        save();
    }

    public int getBellowsTicks() {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        if(tag.contains("bellowsTicks")) {
            this.bellowsTicks = tag.getInt("bellowsTicks");
        } else {
            this.setBellowsTicks(0);
        }
        return this.bellowsTicks;
    }

    public void setBellowsTicks(int t) {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        tag.putInt("bellowsTicks", t);
        this.bellowsTicks = t;
        this.upgrade.set(DataComponent.TAG, CustomData.of(tag));
        save();
    }

    public void handlePowerSwitch() {
        this.setPowered(!this.isPowered());

        if(this.isPowered()) {
            this.setMaxTemperature(2800);
        } else {
            this.setMaxTemperature(0);
            this.setLocked(false);
        }

        save();
    }

    public void setPowered(boolean power) {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        tag.putBoolean("isPowered", power);
        this.isPowered = power;
        this.upgrade.set(DataComponent.TAG, CustomData.of(tag));
        save();
    }

    public boolean isPowered() {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        if(tag.contains("isPowered")){
            this.isPowered = tag.getBoolean("isPowered");
        } else {
            this.setPowered(false);
        }
        return this.isPowered;
    }

    public void poweredMaxTemperature() {
        if(this.isLocked()){
            this.setMaxTemperature(2800);
            this.setLocked(false);
        } else {
            int energyToConsume = (int) (this.getTemperature() * 0.02F) + 1;
            if(LogicHelper.consumeElectricity(this.storageWrapper, energyToConsume, this.isPowered(), true)){
                this.setMaxTemperature(this.getTemperature());
                this.setLocked(true);
            }
        }
    }

    public boolean isLocked() {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        if(tag.contains("isLocked")){
            this.isLocked = tag.getBoolean("isLocked");
        } else {
            this.setLocked(false);
        }
        return this.isLocked;
    }

    public void setLocked(boolean lock) {
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        tag.putBoolean("isLocked", lock);
        this.isLocked = lock;
        this.upgrade.set(DataComponent.TAG, CustomData.of(tag));
        save();
    }

    public boolean isMolten() {
        FluidHeat metal = FluidHeat.get(this.alloy.getResult(this.level).getFluid());
        return metal == null || this.getTemperature() > metal.meltTemperature();
    }

    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        return this.alloy.fill(resource, action, INFO);
    }

    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        if (this.isMolten()) {
            return this.alloy.drain(this.level, maxDrain, action);
        } else {
            return FluidStack.EMPTY;
        }
    }

    private void save() {
        this.saveHandler.accept(this.upgrade);
    }

    public FluidContainerInfo containerInfo() {
        return INFO;
    }

    private static final FluidContainerInfo INFO = new FluidContainerInfo() {
        public boolean canContainFluid(@NotNull Fluid input) {
            return FluidHeat.get(input) != null;
        }

        public int fluidCapacity() {
            return TFCConfig.SERVER.crucibleCapacity.get();
        }
    };
}
