package net.vvxzv.tfcsbu.common.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.*;

public class LogicHelper {
    public static void setFuel(ItemStackHandler handler, int fromSlot, int toSlot) {
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


    @SuppressWarnings("removal")
    public static ResourceLocation rl(String s) {
        if(s.contains(":")) {
            return new ResourceLocation(s);
        }
        return new ResourceLocation("tfcsbu", s);
    }

    public static ResourceLocation rl(String nameSpace, String path) {
        return rl(nameSpace + ":" + path);
    }
}
