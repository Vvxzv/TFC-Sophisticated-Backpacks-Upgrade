package net.vvxzv.tfcsbu.common.item.upgrade.oven;

import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.heat.Heat;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.IHeat;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.dries007.tfc.util.Fuel;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.ItemStackHandler;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Dimension;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.TextureBlitData;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.UV;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import net.vvxzv.tfcsbu.common.registry.UFoodTrait;
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
    private ItemStackHandler inventory;
    private float temperature = 0.0F;
    private float maxTemperature = 0.0F;
    private long burnTime = 0L;

    private static final OvenBakingHandler BAKING_HANDLER =
            ModList.get().isLoaded("firmalife")?
                    new FLOvenBakingHandler() : null;

    public OvenUpgradeLogic(ItemStack upgrade, Consumer<ItemStack> saveHandler){
        this.upgrade = upgrade;
        this.saveHandler = saveHandler;
    }

    public ItemStackHandler getInventory(){
        if (this.inventory == null) {
            this.inventory = new ItemStackHandler(8){
                @Override
                public int getSlotLimit(int slot)
                {
                    return 1;
                }

                @Override
                protected void onContentsChanged(int slot) {
                    super.onContentsChanged(slot);
                    upgrade.addTagElement("oven_inventory", this.serializeNBT());
                    save();
                }
            };
            NBTHelper.getCompound(upgrade, "oven_inventory").ifPresent(inventory::deserializeNBT);
        }

        return this.inventory;
    }

    @SuppressWarnings("removal")
    public void showTemperature(@NotNull GuiGraphics guiGraphics, int guiX, int guiY, int mouseX, int mouseY){
        float currentTemp = getTemperature();
        Heat heatLevel = Heat.getHeat(currentTemp);
        ChatFormatting textColor = heatLevel != null ? heatLevel.getColor() : ChatFormatting.GRAY;
        int dy = Mth.clamp((int)(51.0F * temperature / Heat.BRILLIANT_WHITE.getMax()), 0, 51);
        new CustomTooltipComponent(26, 11, 8, 51){
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
            GuiHelper.blit(guiGraphics, guiX + 24, guiY + 62 - dy, new TextureBlitData(new ResourceLocation("tfcsbu:textures/gui/temperature_indicator.png"), Dimension.SQUARE_16, new UV(0, 0), new Dimension(13, 3)));
        }
    }

    public void tick(Level level){
        ICalendar iCalendar = Calendars.get();
        long calendarTick = iCalendar.getCalendarTicks();

        if(getInventory() != null){
            ItemStackHandler handler = getInventory();
            LogicHelper.setFuel(handler, 0, 3);

            ItemStack item = handler.getStackInSlot(3);
            Fuel fuel = Fuel.get(item);
            if (fuel != null && calendarTick - this.getBurnTime() > 0) {
                item.shrink(1);
                this.setMaxTemperature(fuel.getTemperature());
                this.setBurnTime(calendarTick + fuel.getDuration());
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
                        ItemStackInventory inventory = new ItemStackInventory(itemStack);
                        HeatingRecipe recipe = HeatingRecipe.getRecipe(inventory);
                        if (recipe!= null && recipe.isValidTemperature(itemTemp)) {
                            ItemStack output = recipe.assemble(inventory, level.registryAccess());
                            FoodCapability.applyTrait(output, UFoodTrait.OVEN_BAKED);
                            handler.setStackInSlot(i, output);
                        } else {
                            if(BAKING_HANDLER != null) {
                                ItemStack bakedOutput = BAKING_HANDLER.handleBaking(itemStack, itemTemp, inventory, level);
                                if (bakedOutput != null) {
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
        CompoundTag tag = this.upgrade.getOrCreateTag();
        if(tag.contains("temperature")) {
            this.temperature = tag.getFloat("temperature");
        } else {
            this.setTemperature(0);
        }
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
        if(tag.contains("maxTemperature")) {
            this.maxTemperature = tag.getFloat("maxTemperature");
        } else {
            this.setMaxTemperature(0);
        }
        return this.maxTemperature;
    }

    public void setMaxTemperature(float setNumber){
        CompoundTag tag = this.upgrade.getOrCreateTag();
        tag.putFloat("maxTemperature", setNumber);
        this.maxTemperature = setNumber;
        save();
    }

    public long getBurnTime(){
        CompoundTag tag = this.upgrade.getOrCreateTag();
        if(tag.contains("burnTime")) {
            this.burnTime = tag.getLong("burnTime");
        } else {
            this.setBurnTime(0);
        }
        return this.burnTime;
    }

    public void setBurnTime(long setNumber){
        CompoundTag tag = this.upgrade.getOrCreateTag();
        tag.putLong("burnTime", setNumber);
        this.burnTime = setNumber;
        save();
    }

    private void save() {
        this.saveHandler.accept(this.upgrade);
    }
}