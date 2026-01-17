package net.vvxzv.tfcsbu.common.item.upgrade.oven;

import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface OvenBakingHandler {
    /**
     * 处理烤箱烘焙逻辑
     * @param itemStack 待烘焙物品
     * @param itemTemp 物品当前温度
     * @param inventory 物品栈容器
     * @param level 游戏世界
     * @return 烘焙后的物品（null 表示未烘焙）
     */
    ItemStack handleBaking(ItemStack itemStack, float itemTemp, ItemStackInventory inventory, Level level);
}
