package net.vvxzv.tfcsbu.compat.firmalife;

import net.minecraft.world.item.ItemStack;

public interface OvenBakingHandler {
    ItemStack handleBaking(ItemStack itemStack, float itemTemp);
}
