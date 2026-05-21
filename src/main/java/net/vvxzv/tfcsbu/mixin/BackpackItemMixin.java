package net.vvxzv.tfcsbu.mixin;

import net.dries007.tfc.common.component.size.IItemSize;
import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.common.component.size.Weight;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeHandler;
import net.vvxzv.tfcsbu.Config;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BackpackItem.class)
public class BackpackItemMixin implements IItemSize {
    @Unique
    private int getSlotsCount(){
        return Config.slotsCountToOverweight;
    }

    @Unique
    private int getUpgradeCount(){
        return Config.upgradesCountToOverweight;
    }

    @Override
    public Size getSize(@NotNull ItemStack itemStack) {
        return Size.HUGE;
    }

    @Override
    public Weight getWeight(@NotNull ItemStack itemStack) {
        boolean veryHeavy;
        IBackpackWrapper backpackWrapper = BackpackWrapper.fromStack(itemStack);
        UpgradeHandler upgradeHandler = backpackWrapper.getUpgradeHandler();
        int upgradeCount = 0;
        for (int i = 0; i < upgradeHandler.getSlots(); i++) {
            ItemStack stack = upgradeHandler.getStackInSlot(i);
            if(!stack.isEmpty()) {
                upgradeCount++;
            }
        }
        veryHeavy = upgradeCount >= getUpgradeCount();

        if(!veryHeavy) {
            try {
                InventoryHandler handler = backpackWrapper.getInventoryHandler();
                int count = 0;
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    if(!stack.isEmpty()) {
                        count++;
                    }
                    if(count > getSlotsCount()) {
                        veryHeavy = true;
                        break;
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return veryHeavy? Weight.VERY_HEAVY: Weight.HEAVY;
    }

    @Override
    public void modifyWeight(ItemStack stack) {
        stack.set(DataComponents.MAX_STACK_SIZE, 1);
    }
}
