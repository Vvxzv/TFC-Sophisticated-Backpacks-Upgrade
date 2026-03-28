package net.vvxzv.tfcsbu.compat.firmalife;

import com.eerussianguy.firmalife.common.items.FLFoodTraits;
import com.eerussianguy.firmalife.common.recipes.WrappedHeatingRecipe;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.minecraft.world.item.ItemStack;

public class FLOvenBakingHandler implements OvenBakingHandler{
    @Override
    public ItemStack handleBaking(ItemStack itemStack, float itemTemp) {
        try {
            WrappedHeatingRecipe recipe = WrappedHeatingRecipe.getRecipe(itemStack);
            if (recipe != null && recipe.isValidTemperature(itemTemp)) {
                ItemStack output = recipe.assemble(itemStack);
                FoodCapability.applyTrait(output, FLFoodTraits.OVEN_BAKED);
                return output;
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
