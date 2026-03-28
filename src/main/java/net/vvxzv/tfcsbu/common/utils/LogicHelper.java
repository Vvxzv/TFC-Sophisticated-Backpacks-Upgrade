package net.vvxzv.tfcsbu.common.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.StatefulComponentItemHandler;

public class LogicHelper {
    public static CompoundTag getOrCreateTag(CustomData data) {
        if(data != null) {
            return data.copyTag();
        }
        return new CompoundTag();
    }

    public static void setFuel(StatefulComponentItemHandler handler, int fromSlot, int toSlot) {
        for (int i = fromSlot; i < toSlot; i++) {
            ItemStack itemStack1 = handler.getStackInSlot(i);
            ItemStack itemStack2 = handler.getStackInSlot(i + 1);
            if(!itemStack1.isEmpty() && itemStack2.isEmpty()){
                handler.setStackInSlot(i + 1, itemStack1.copy());
                handler.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }

    public static boolean consumeElectricity(IStorageWrapper storageWrapper, int energyToConsume, boolean allowConsume, boolean simulate) {
        IEnergyStorage energyStorage = storageWrapper.getEnergyStorage().orElse(null);

        if (energyStorage == null) {
            return false;
        }

        if (allowConsume) {
            int extractedSimulate = energyStorage.extractEnergy(energyToConsume, true);
            if (extractedSimulate > 0) {
                energyStorage.extractEnergy(energyToConsume, simulate);
                return true;
            }
        }

        return false;
    }
}
