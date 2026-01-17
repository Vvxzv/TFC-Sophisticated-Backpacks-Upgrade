package net.vvxzv.tfcsbu.common.item.upgrade.oven;

import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DefaultOvenBakingHandler implements OvenBakingHandler {
    @Override
    public ItemStack handleBaking(ItemStack itemStack, float itemTemp, ItemStackInventory inventory, Level level) {
        return null;
    }
}
