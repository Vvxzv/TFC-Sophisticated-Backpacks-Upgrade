package net.vvxzv.tfcsbu.common.item.upgrade.fridge;

import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import net.vvxzv.tfcsbu.common.UFoodTrait;
import net.vvxzv.tfcsbu.common.utils.CustomTooltipComponent;
import net.vvxzv.tfcsbu.common.utils.LogicHelper;
import net.vvxzv.tfcsbu.common.utils.GuiUtils;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class FridgeUpgradeLogic {
    private final IStorageWrapper storageWrapper;
    private final ItemStack upgrade;
    private final Consumer<ItemStack> saveHandler;
    @Nullable
    private ItemStackHandler inventory;
    private boolean isPowered = false;

    public FridgeUpgradeLogic(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> saveHandler) {
        this.storageWrapper = storageWrapper;
        this.upgrade = upgrade;
        this.saveHandler = saveHandler;
    }

    public ItemStackHandler getInventory(){
        if (this.inventory == null) {
            this.inventory = new ItemStackHandler(9){
                @Override
                protected void onContentsChanged(int slot) {
                    super.onContentsChanged(slot);
                    upgrade.addTagElement("fridge_inventory", this.serializeNBT());
                    save();
                }
            };
            NBTHelper.getCompound(upgrade, "fridge_inventory").ifPresent(inventory::deserializeNBT);
        }

        return this.inventory;
    }

    public void showPowerSwitchTooltip (GuiGraphics guiGraphics,int guiX, int guiY, int mouseX, int mouseY) {
        MutableComponent text = Component.translatable("button.power_switch");

        if(this.isPowered()) {
            GuiHelper.blit(guiGraphics, guiX + 61, guiY + 20, GuiUtils.SWITCH_ON);
            text.append(Component.literal(": "))
                    .append((Component.translatable("button.power_switch.true")));
        } else {
            GuiHelper.blit(guiGraphics, guiX + 61, guiY + 20, GuiUtils.SWITCH_OFF);
            text.append(Component.literal(": "))
                    .append(Component.translatable("button.power_switch.false"));
        }

        new CustomTooltipComponent(60 , 19, 17, 17) {
            @Override
            public Component[] getComponents() {
                return new Component[]{text, Component.translatable("button.power_switch.tooltip").withStyle(ChatFormatting.GRAY)};
            }

            @Override
            public boolean hasTooltip() {
                return true;
            }
        }.draw(guiGraphics, guiX, guiY, mouseX, mouseY);
    }

    public void tick(Level level) {
        if(getInventory() != null && level.getGameTime() % 20L == 0L){
            ItemStackHandler handler = getInventory();

            boolean isConsumeElectricity = LogicHelper.consumeElectricity(this.storageWrapper, 10, this.isPowered(), false);

            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack itemStack = handler.getStackInSlot(i);
                if(isConsumeElectricity) {
                    FoodCapability.removeTrait(itemStack, UFoodTrait.FRIDGE_PRESERVED);
                    FoodCapability.applyTrait(itemStack, UFoodTrait.ELECTRICITY_FRIDGE_PRESERVED);
                } else {
                    FoodCapability.removeTrait(itemStack, UFoodTrait.ELECTRICITY_FRIDGE_PRESERVED);
                    FoodCapability.applyTrait(itemStack, UFoodTrait.FRIDGE_PRESERVED);
                }
            }
        }
    }

    public void handlePowerSwitch() {
        this.setPowered(!this.isPowered());
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
            this.isPowered = tag.getBoolean("isPowered");
        } else {
            this.setPowered(false);
        }
        return this.isPowered;
    }

    private void save() {
        this.saveHandler.accept(this.upgrade);
    }
}
