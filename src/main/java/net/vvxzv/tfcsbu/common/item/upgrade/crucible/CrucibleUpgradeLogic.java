package net.vvxzv.tfcsbu.common.item.upgrade.crucible;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodTraits;
import net.dries007.tfc.common.capabilities.heat.Heat;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.IHeat;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.*;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
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
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.*;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import net.vvxzv.tfcsbu.common.utils.CustomTooltipComponent;
import net.vvxzv.tfcsbu.common.utils.LogicHelper;
import net.vvxzv.tfcsbu.common.utils.GuiUtils;
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
    private ItemStackHandler inventory;
    private float temperature = 0.0F;
    private float maxTemperature = 0.0F;
    private long burnTime = 0L;
    private final Alloy alloy;
    private Level level;
    private boolean hasBellows;
    private boolean isTriggerBellows;
    private int bellowsTicks;
    private boolean isPowered;
    private boolean isLocked;

    private static final int BELLOWS_DURATION = 150;
    private static final TextureBlitData LOCK = new TextureBlitData(GuiUtils.CRUCIBLE_BACKGROUND, new Dimension(128, 128), new UV(112, 96), Dimension.SQUARE_16);
    private static final TextureBlitData UNLOCK = new TextureBlitData(GuiUtils.CRUCIBLE_BACKGROUND, new Dimension(128, 128), new UV(112, 112), Dimension.SQUARE_16);

    public CrucibleUpgradeLogic(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> saveHandler) {
        this.storageWrapper = storageWrapper;
        this.upgrade = upgrade;
        this.saveHandler = saveHandler;
        this.alloy = new Alloy(TFCConfig.SERVER.crucibleCapacity.get());
    }

    public ItemStackHandler getInventory(){
        if (this.inventory == null) {
            this.inventory = new ItemStackHandler(15) {
                @Override
                public int getSlotLimit(int slot) {
                    return 1;
                }

                @Override
                protected void onContentsChanged(int slot) {
                    super.onContentsChanged(slot);
                    upgrade.addTagElement("crucible_inventory", this.serializeNBT());
                    save();
                }
            };
            NBTHelper.getCompound(upgrade, "crucible_inventory").ifPresent(inventory::deserializeNBT);
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
        Heat heatLevel = Heat.getHeat(currentTemp);
        ChatFormatting textColor = heatLevel != null ? heatLevel.getColor() : ChatFormatting.GRAY;
        int dy = Mth.clamp((int)(51.0F * temperature / Heat.BRILLIANT_WHITE.getMax()), 0, 51);
        new CustomTooltipComponent(25, 18, 8, 51){
            @Override
            public Component[] getComponents() {
                return new Component[]{Component.literal( (int)currentTemp + "°C").withStyle(textColor)};
            }

            @Override
            public boolean hasTooltip() {
                return currentTemp != 0;
            }
        }.draw(guiGraphics, guiX, guiY, mouseX, mouseY);

        if(currentTemp > 30){
            GuiHelper.blit(guiGraphics, guiX + 23, guiY + 69 - dy, GuiUtils.TEMPERATURE_INDICATOR);
        }
    }

    private void showAlloy(GuiGraphics guiGraphics, int guiX, int guiY, int mouseX, int mouseY, ResourceLocation texture) {
        getAlloy();
        AlloyView alloy = this.alloy;
        if(alloy.getAmount() > 0) {
            FluidStack fluidStack = alloy.getResultAsFluidStack();
            TextureAtlasSprite sprite = RenderHelpers.getAndBindFluidSprite(alloy.getResultAsFluidStack());
            int fillHeight = (int)Math.ceil(31.0F * (float)alloy.getAmount() / (float)alloy.getMaxUnits());
            RenderHelpers.fillAreaWithSprite(guiGraphics, sprite, guiX + 50, guiY + 89 - fillHeight, 36, fillHeight, 16, 16);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, texture);

            Object2DoubleMap<Metal> metals = alloy.getMetals();
            List<Component> texts = new ArrayList<>(List.of(Component.translatable(fluidStack.getTranslationKey()).withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD)));
            metals.forEach((m, d) -> {
                texts.add(
                        Component.translatable(m.getTranslationKey())
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
            GuiHelper.blit(guiGraphics, guiX + 106, guiY + 37, LOCK);
            lockText.append(Component.literal(": "))
                    .append((Component.translatable("button.power_switch.true")));
        } else {
            GuiHelper.blit(guiGraphics, guiX + 106, guiY + 37, UNLOCK);
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
        ICalendar iCalendar = Calendars.get();
        long calendarTick = iCalendar.getCalendarTicks();
        getAlloy();
        this.getTemperature();

        if(getInventory() != null) {
            ItemStackHandler handler = getInventory();
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
                        this.setMaxTemperature(fuel.getTemperature());
                        this.setBurnTime(calendarTick + fuel.getDuration());
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
                        ItemStackInventory inventory = new ItemStackInventory(itemStack);
                        HeatingRecipe recipe = HeatingRecipe.getRecipe(inventory);
                        if (recipe!= null && recipe.isValidTemperature(itemTemp)) {
                            ItemStack outputItem = recipe.assemble(inventory, level.registryAccess());
                            FluidStack outputFluid = recipe.assembleFluid(inventory);
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
        CompoundTag tag = this.upgrade.getOrCreateTag();
        this.temperature = tag.getFloat("temperature") + added;
        tag.putFloat("temperature", this.temperature);
        save();
    }

    public float getTemperature() {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        this.temperature = tag.getFloat("temperature");
        return this.temperature;
    }

    public void setTemperature(float setNumber) {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        tag.putFloat("temperature", setNumber);
        this.temperature = setNumber;
        save();
    }

    public float getMaxTemperature() {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        this.maxTemperature = tag.getFloat("maxTemperature");
        return this.maxTemperature;
    }

    public void setMaxTemperature(float setNumber) {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        tag.putFloat("maxTemperature", setNumber);
        this.maxTemperature = setNumber;
        save();
    }

    public long getBurnTime() {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        this.burnTime = tag.getLong("burnTime");
        return this.burnTime;
    }

    public void setBurnTime(long setNumber) {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        tag.putLong("burnTime", setNumber);
        this.burnTime = setNumber;
        save();
    }

    private void getAlloy() {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        this.alloy.deserializeNBT(tag.getCompound("alloy"));
    }

    private void setAlloy() {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        tag.put("alloy", this.alloy.serializeNBT());
        save();
    }

    public boolean hasBellows() {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        this.hasBellows = tag.getBoolean("hasBellows");
        return this.hasBellows;
    }

    private void setHasBellows(boolean b) {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        tag.putBoolean("hasBellows", b);
        this.hasBellows = b;
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
        CompoundTag tag = this.upgrade.getOrCreateTag();
        this.isTriggerBellows = tag.getBoolean("isTriggerBellows");
        return this.isTriggerBellows;
    }

    public void setTriggerBellows(boolean t) {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        tag.putBoolean("isTriggerBellows", t);
        this.isTriggerBellows = t;
        save();
    }

    public int getBellowsTicks() {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        this.bellowsTicks = tag.getInt("bellowsTicks");
        return this.bellowsTicks;
    }

    public void setBellowsTicks(int t) {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        tag.putInt("bellowsTicks", t);
        this.bellowsTicks = t;
        save();
    }

    public void handlePowerSwitch() {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        if(tag.contains("isPowered")){
            boolean isPowered = tag.getBoolean("isPowered");
            setPowered(!isPowered);
        } else {
            tag.putBoolean("isPowered", true);
            setPowered(true);
        }

        if(this.isPowered()) {
            this.setMaxTemperature(2800);
        } else {
            this.setMaxTemperature(0);
            setLocked(false);
        }

        save();
    }

    public void setPowered(boolean power) {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        tag.putBoolean("isPowered", power);
        this.isPowered = power;
        save();
    }

    public boolean isPowered() {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        if(tag.contains("isPowered")){
            boolean isPowered = tag.getBoolean("isPowered");
            setPowered(isPowered);
        } else {
            tag.putBoolean("isPowered", false);
            setPowered(false);
        }
        return this.isPowered;
    }

    public void poweredMaxTemperature() {
        if(this.isLocked()){
            this.setMaxTemperature(2800);
            setLocked(false);
        } else {
            int energyToConsume = (int) (this.getTemperature() * 0.02F) + 1;
            if(LogicHelper.consumeElectricity(this.storageWrapper, energyToConsume, this.isPowered(), true)){
                this.setMaxTemperature(this.getTemperature());
                setLocked(true);
            }
        }
    }

    public boolean isLocked() {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        if(tag.contains("isLocked")){
            boolean isLocked = tag.getBoolean("isLocked");
            setLocked(isLocked);
        } else {
            tag.putBoolean("isLocked", false);
            setLocked(false);
        }
        return this.isLocked;
    }

    public void setLocked(boolean lock) {
        CompoundTag tag = this.upgrade.getOrCreateTag();
        tag.putBoolean("isLocked", lock);
        this.isLocked = lock;
        save();
    }

    public boolean isMolten() {
        return this.getTemperature() > this.alloy.getResult(this.level).getMeltTemperature();
    }

    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        Metal metal = Metal.get(resource.getFluid());
        return metal != null ? this.alloy.add(metal, resource.getAmount(), action.simulate()) : 0;
    }

    public @NotNull FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        if (this.isMolten()) {
            Metal result = this.alloy.getResult(this.level);
            int amount = this.alloy.removeAlloy(maxDrain, action.simulate());

            return new FluidStack(result.getFluid(), amount);
        } else {
            return FluidStack.EMPTY;
        }
    }

    private void save() {
        this.saveHandler.accept(this.upgrade);
    }
}
