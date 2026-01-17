package net.vvxzv.tfcsbu.common.item.upgrade.oven;

import com.eerussianguy.firmalife.common.items.FLFoodTraits;
import com.eerussianguy.firmalife.common.recipes.OvenRecipe;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.recipes.inventory.ItemStackInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FLOvenBakingHandler implements OvenBakingHandler{
    @Override
    public ItemStack handleBaking(ItemStack itemStack, float itemTemp, ItemStackInventory inventory, Level level) {
        try {
            OvenRecipe ovenRecipe = OvenRecipe.getRecipe(itemStack);
            if (ovenRecipe != null && ovenRecipe.isValidTemperature(itemTemp)) {
                ItemStack output = ovenRecipe.assemble(inventory, level.registryAccess());
                FoodCapability.applyTrait(output, FLFoodTraits.OVEN_BAKED);
                return output;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
