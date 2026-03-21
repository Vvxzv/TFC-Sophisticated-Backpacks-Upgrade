package net.vvxzv.tfcsbu.common.utils;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.*;


public class LogicHelper {
    public static void setFuel(ItemStackHandler handler, int fromSlot, int toSlot) {
        for (int i = fromSlot; i < toSlot; i++){
            ItemStack itemStack1 = handler.getStackInSlot(i);
            ItemStack itemStack2 = handler.getStackInSlot(i + 1);
            if(!itemStack1.isEmpty() && itemStack2.isEmpty()){
                handler.setStackInSlot(i + 1, itemStack1.copy());
                handler.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }

    public static ItemStack findBattery(IStorageWrapper storageWrapper) {
        ItemStack battery = null;
        int slots = storageWrapper.getUpgradeHandler().getSlots();
        for (int i = 0; i < slots; i++){
            ItemStack upgrade = storageWrapper.getUpgradeHandler().getStackInSlot(i);
            if(upgrade.is(ModItems.BATTERY_UPGRADE.get())){
                battery = upgrade;
                break;
            }
        }
        return battery;
    }
}
