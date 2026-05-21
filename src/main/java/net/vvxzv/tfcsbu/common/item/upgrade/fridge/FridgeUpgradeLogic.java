package net.vvxzv.tfcsbu.common.item.upgrade.fridge;

import net.dries007.tfc.common.component.food.FoodCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper;
import net.p3pp3rf1y.sophisticatedcore.init.ModCoreDataComponents;
import net.p3pp3rf1y.sophisticatedcore.inventory.StatefulComponentItemHandler;
import net.vvxzv.tfcsbu.common.registry.DataComponent;
import net.vvxzv.tfcsbu.common.registry.FoodTraits;
import net.vvxzv.tfcsbu.common.utils.CustomTooltipComponent;
import net.vvxzv.tfcsbu.client.gui.GuiUtils;
import net.vvxzv.tfcsbu.common.utils.LogicHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class FridgeUpgradeLogic {
    private final IStorageWrapper storageWrapper;
    private final ItemStack upgrade;
    private final Consumer<ItemStack> saveHandler;
    @Nullable
    private StatefulComponentItemHandler inventory;
    private boolean isPowered = false;

    public FridgeUpgradeLogic(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> saveHandler) {
        this.storageWrapper = storageWrapper;
        this.upgrade = upgrade;
        this.saveHandler = saveHandler;
    }

    public StatefulComponentItemHandler getInventory(){
        if (this.inventory == null) {
            this.inventory = new StatefulComponentItemHandler(upgrade, ModCoreDataComponents.LENIENT_CONTAINER.get(), 9) {
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
            StatefulComponentItemHandler handler = getInventory();

            boolean isConsumeElectricity = LogicHelper.consumeElectricity(this.storageWrapper, 10, this.isPowered(), false);

            for (int i = 0; i < handler.getSlots(); i++){
                ItemStack itemStack = handler.getStackInSlot(i);
                if(isConsumeElectricity){
                    FoodCapability.removeTrait(itemStack, FoodTraits.FRIDGE_PRESERVED);
                    FoodCapability.applyTrait(itemStack, FoodTraits.ELECTRICITY_FRIDGE_PRESERVED);
                } else {
                    FoodCapability.removeTrait(itemStack, FoodTraits.ELECTRICITY_FRIDGE_PRESERVED);
                    FoodCapability.applyTrait(itemStack, FoodTraits.FRIDGE_PRESERVED);
                }
            }
        }
    }

    public void handlePowerSwitch() {
        this.setPowered(!this.isPowered());
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
            boolean isPowered = tag.getBoolean("isPowered");
            setPowered(isPowered);
        } else {
            tag.putBoolean("isPowered", false);
            setPowered(false);
        }
        this.upgrade.set(DataComponent.TAG, CustomData.of(tag));
        return this.isPowered;
    }

    private void save() {
        this.saveHandler.accept(this.upgrade);
    }
}
