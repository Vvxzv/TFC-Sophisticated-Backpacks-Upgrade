package net.vvxzv.tfcsbu.common.item.upgrade.oven;

import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.heat.Heat;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.IHeat;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.data.Fuel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.fml.ModList;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Dimension;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.TextureBlitData;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.UV;
import net.p3pp3rf1y.sophisticatedcore.init.ModCoreDataComponents;
import net.p3pp3rf1y.sophisticatedcore.inventory.StatefulComponentItemHandler;
import net.vvxzv.tfcsbu.TFCSophisticatedBackpacksUpgrade;
import net.vvxzv.tfcsbu.common.registry.DataComponent;
import net.vvxzv.tfcsbu.common.registry.FoodTraits;
import net.vvxzv.tfcsbu.common.utils.CustomTooltipComponent;
import net.vvxzv.tfcsbu.common.utils.LogicHelper;
import net.vvxzv.tfcsbu.compat.firmalife.FLOvenBakingHandler;
import net.vvxzv.tfcsbu.compat.firmalife.OvenBakingHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class OvenUpgradeLogic {
    private final ItemStack upgrade;
    private final Consumer<ItemStack> saveHandler;
    @Nullable
    private StatefulComponentItemHandler inventory;
    private float temperature = 0.0F;
    private float maxTemperature = 0.0F;
    private long burnTime = 0L;

    private static final OvenBakingHandler BAKING_HANDLER =
            ModList.get().isLoaded("firmalife")
                    ? new FLOvenBakingHandler()
                    : null;

    public OvenUpgradeLogic(ItemStack upgrade, Consumer<ItemStack> saveHandler){
        this.upgrade = upgrade;
        this.saveHandler = saveHandler;
    }

    public StatefulComponentItemHandler getInventory(){
        if (this.inventory == null) {
            this.inventory = new StatefulComponentItemHandler(upgrade, ModCoreDataComponents.LENIENT_CONTAINER.get(),8){
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

    public void showTemperature(@NotNull GuiGraphics guiGraphics, int guiX, int guiY, int mouseX, int mouseY){
        float currentTemp = getTemperature();
        //Heat heatLevel = Heat.getHeat(currentTemp);
        //ChatFormatting textColor = heatLevel != null ? heatLevel.getColor() : ChatFormatting.GRAY;
        int dy = Mth.clamp((int)(51.0F * temperature / Heat.BRILLIANT_WHITE.getMax()), 0, 51);
        new CustomTooltipComponent(26, 11, 8, 51){
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

        if(currentTemp > 30){
            GuiHelper.blit(guiGraphics, guiX + 24, guiY + 62 - dy, new TextureBlitData(ResourceLocation.fromNamespaceAndPath(TFCSophisticatedBackpacksUpgrade.MODID, "textures/gui/temperature_indicator.png"), Dimension.SQUARE_16, new UV(0, 0), new Dimension(13, 3)));
        }
    }

    public void tick(){
        ICalendar iCalendar = Calendars.get();
        long calendarTick = iCalendar.getCalendarTicks();

        if(getInventory() != null){
            StatefulComponentItemHandler handler = getInventory();
            LogicHelper.setFuel(handler, 0, 3);

            ItemStack item = handler.getStackInSlot(3);
            Fuel fuel = Fuel.get(item);
            if (fuel != null && calendarTick - this.getBurnTime() > 0) {
                item.shrink(1);
                this.setMaxTemperature(fuel.temperature());
                this.setBurnTime(calendarTick + fuel.duration());
            }

            if(calendarTick - this.getBurnTime() > 0){
                this.setMaxTemperature(0F);
                if(this.getTemperature() > 0){
                    this.handleTemperature(-1);
                    if (this.getTemperature() < 0 || calendarTick - getBurnTime() > 1000) {
                        this.setTemperature(0F);
                    }
                }
            }
            else {
                this.handleTemperature(1);
                if(this.getTemperature() > this.getMaxTemperature()) {
                    this.setTemperature(this.getMaxTemperature());
                }
            }

            if(this.getTemperature() > 0.0F) {
                for(int i = 4; i < 8; i++){
                    ItemStack itemStack = handler.getStackInSlot(i);
                    IHeat iHeat = HeatCapability.get(itemStack);
                    if(iHeat != null){
                        float itemTemp = iHeat.getTemperature();
                        HeatCapability.addTemp(iHeat, this.getTemperature());
                        HeatingRecipe recipe = HeatingRecipe.getRecipe(itemStack);
                        if (recipe!= null && recipe.isValidTemperature(itemTemp)) {
                            ItemStack output = recipe.assembleItem(itemStack);
                            FoodCapability.applyTrait(output, FoodTraits.OVEN_BAKED);
                            handler.setStackInSlot(i, output);
                        } else {
                            if(BAKING_HANDLER != null) {
                                ItemStack bakedOutput = BAKING_HANDLER.handleBaking(itemStack, itemTemp);
                                if(bakedOutput != null) {
                                    handler.setStackInSlot(i, bakedOutput);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleTemperature(float added){
        this.setTemperature(this.getTemperature() + added);
    }

    public float getTemperature(){
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        if(tag.contains("temperature")) {
            this.temperature = tag.getFloat("temperature");
        } else {
            this.temperature = 0;
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
            this.maxTemperature = 0;
            this.setMaxTemperature(0);
        }
        return this.maxTemperature;
    }

    public void setMaxTemperature(float setNumber){
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        tag.putFloat("maxTemperature", setNumber);
        this.maxTemperature = setNumber;
        this.upgrade.set(DataComponent.TAG, CustomData.of(tag));
        save();
    }

    public long getBurnTime(){
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        if(tag.contains("burnTime")) {
            this.burnTime = tag.getLong("burnTime");
        } else {
            this.burnTime = 0;
            this.setBurnTime(0);
        }
        return this.burnTime;
    }

    public void setBurnTime(long setNumber){
        CustomData data = this.upgrade.get(DataComponent.TAG);
        CompoundTag tag = LogicHelper.getOrCreateTag(data);
        tag.putLong("burnTime", setNumber);
        this.burnTime = setNumber;
        this.upgrade.set(DataComponent.TAG, CustomData.of(tag));
        save();
    }

    private void save() {
        this.saveHandler.accept(this.upgrade);
    }
}