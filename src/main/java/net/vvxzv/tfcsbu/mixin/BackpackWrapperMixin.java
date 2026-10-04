package net.vvxzv.tfcsbu.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * 解决开发环境无法启动游戏而存在
 */
@Mixin(BackpackWrapper.class)
public abstract class BackpackWrapperMixin {

    @Final
    @Shadow(remap = false)
    private static String INVENTORY_SLOTS_TAG;

    @Final
    @Shadow(remap = false)
    private static String UPGRADE_SLOTS_TAG = "upgradeSlots";

    @Shadow(remap = false)
    private ItemStack backpack;

    @Shadow(remap = false)
    protected abstract void setNumberOfInventorySlots(int itemInventorySlots);

    @Shadow(remap = false)
    protected abstract void setNumberOfUpgradeSlots(int numberOfUpgradeSlots);

    @Inject(method = "getNumberOfInventorySlots", at = @At("HEAD"), cancellable = true, remap = false)
    private void getNumberOfInventorySlots(CallbackInfoReturnable<Integer> cir) {
        if(FMLEnvironment.production) return;

        Optional<Integer> inventorySlots = NBTHelper.getInt(this.backpack, INVENTORY_SLOTS_TAG);

        if (inventorySlots.isPresent()) {
            cir.setReturnValue(inventorySlots.get());
            cir.cancel();
            return;
        }

        int itemInventorySlots = ((BackpackItem) this.backpack.getItem()).getNumberOfSlots();
        setNumberOfInventorySlots(itemInventorySlots);
        cir.setReturnValue(itemInventorySlots);
        cir.cancel();
    }

    @Inject(method = "getNumberOfUpgradeSlots", at = @At("HEAD"), cancellable = true, remap = false)
    private void getNumberOfUpgradeSlots(CallbackInfoReturnable<Integer> cir) {
        if(FMLEnvironment.production) return;

        Optional<Integer> upgradeSlots = NBTHelper.getInt(backpack, UPGRADE_SLOTS_TAG);

        if (upgradeSlots.isPresent()) {
            cir.setReturnValue(upgradeSlots.get());
            cir.cancel();
            return;
        }

        int itemUpgradeSlots = ((BackpackItem) backpack.getItem()).getNumberOfUpgradeSlots();
        setNumberOfUpgradeSlots(itemUpgradeSlots);
        cir.setReturnValue(itemUpgradeSlots);
        cir.cancel();
    }

    @Inject(method = "cacheSlotNumbers", at = @At("HEAD"), cancellable = true, remap = false)
    private void cacheSlotNumbers(CallbackInfo ci) {
        if(!FMLEnvironment.production) {
            ci.cancel();
        }
    }
}
