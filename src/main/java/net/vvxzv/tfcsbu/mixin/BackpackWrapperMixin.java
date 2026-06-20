package net.vvxzv.tfcsbu.mixin;

import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(BackpackWrapper.class)
public abstract class BackpackWrapperMixin {

    @Final
    @Shadow(remap = false)
    private static String INVENTORY_SLOTS_TAG;

    @Final
    @Shadow(remap = false)
    private static String UPGRADE_SLOTS_TAG = "upgradeSlots";

    @Final
    @Shadow(remap = false)
    private ItemStack backpack;

    @Shadow(remap = false)
    protected abstract void setNumberOfInventorySlots(int itemInventorySlots);

    @Shadow(remap = false)
    protected abstract void setNumberOfUpgradeSlots(int numberOfUpgradeSlots);

    /**
     * @author Vvxzv
     * @reason 1
     */
    @Overwrite(remap = false)
    private int getNumberOfInventorySlots() {
        Optional<Integer> inventorySlots = NBTHelper.getInt(this.backpack, INVENTORY_SLOTS_TAG);

        if (inventorySlots.isPresent()) {
            return inventorySlots.get();
        }

        int itemInventorySlots = ((BackpackItem) this.backpack.getItem()).getNumberOfSlots();
        setNumberOfInventorySlots(itemInventorySlots);
        return itemInventorySlots;
    }

    /**
     * @author Vvxzv
     * @reason 1
     */
    @Overwrite(remap = false)
    private int getNumberOfUpgradeSlots() {
        Optional<Integer> upgradeSlots = NBTHelper.getInt(backpack, UPGRADE_SLOTS_TAG);

        if (upgradeSlots.isPresent()) {
            return upgradeSlots.get();
        }

        int itemUpgradeSlots = ((BackpackItem) backpack.getItem()).getNumberOfUpgradeSlots();
        setNumberOfUpgradeSlots(itemUpgradeSlots);
        return itemUpgradeSlots;
    }

    /**
     * @author Vvxzv
     * @reason 1
     */
    @Overwrite(remap = false)
    private void cacheSlotNumbers() {
    }
}
