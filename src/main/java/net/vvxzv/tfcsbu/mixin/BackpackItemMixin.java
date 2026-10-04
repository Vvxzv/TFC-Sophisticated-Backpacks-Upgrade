package net.vvxzv.tfcsbu.mixin;

import net.dries007.tfc.common.capabilities.size.IItemSize;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

@Mixin(BackpackItem.class)
public class BackpackItemMixin implements IItemSize {

    @Override
    public @NotNull Size getSize(@NotNull ItemStack itemStack) {
        return Size.HUGE;
    }

    @Override
    public @NotNull Weight getWeight(@NotNull ItemStack itemStack) {
        return Weight.HEAVY;
    }

    @Override
    public int getDefaultStackSize(@NotNull ItemStack stack) {
        return 1;
    }
}
