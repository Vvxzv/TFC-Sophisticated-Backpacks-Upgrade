package net.vvxzv.tfcsbu.mixin;

import net.dries007.tfc.common.capabilities.size.IItemSize;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.vvxzv.tfcsbu.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.concurrent.atomic.AtomicBoolean;

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
    public Size getSize(ItemStack itemStack) {
        return Size.HUGE;
    }

    @Override
    public Weight getWeight(ItemStack itemStack) {
        AtomicBoolean veryHeavy = new AtomicBoolean(false);
        itemStack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance()).ifPresent((w) -> {
            int uSlots = w.getUpgradeHandler().getSlots();
            int uCount = 0;
            for (int i = 0; i < uSlots; i++){
                if(!w.getUpgradeHandler().getStackInSlot(i).isEmpty()){
                    uCount ++;
                }
            }
            if(uCount > getUpgradeCount() - 1) veryHeavy.set(true);

            if(!veryHeavy.get()){
                int iSlots = w.getInventoryHandler().getSlots();
                int iCount = 0;
                for (int i = 0; i < iSlots; i++){
                    if(!w.getInventoryHandler().getSlotStack(i).isEmpty()) iCount++;
                }
                if(iCount > getSlotsCount()) veryHeavy.set(true);
            }
        });
        return veryHeavy.get()? Weight.VERY_HEAVY: Weight.HEAVY;
    }

    @Override
    public int getDefaultStackSize(ItemStack stack) {
        return 1;
    }
}
