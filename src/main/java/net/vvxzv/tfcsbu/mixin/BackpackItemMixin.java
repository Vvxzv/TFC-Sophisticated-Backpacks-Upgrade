package net.vvxzv.tfcsbu.mixin;

import net.dries007.tfc.common.component.size.IItemSize;
import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.common.component.size.Weight;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.vvxzv.tfcsbu.Config;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

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
    public void modifyWeight(ItemStack stack) {
        stack.set(DataComponents.MAX_STACK_SIZE, 1);
    }
}
