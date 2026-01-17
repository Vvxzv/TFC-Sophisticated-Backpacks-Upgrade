package net.vvxzv.tfcsbu.common;

import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.minecraft.resources.ResourceLocation;
import net.vvxzv.tfcsbu.TFCSBU;

@SuppressWarnings("removal")
public class UFoodTrait {
    private static FoodTrait register(String name, float decayModifier) {
        return FoodTrait.register(new ResourceLocation(TFCSBU.MODID, name), new FoodTrait(() -> decayModifier, "tfcsbu.tooltip.food_trait." + name));
    }

    public static final FoodTrait FRIDGE_PRESERVED = register("fridge_preserved", 0.4F);
    public static final FoodTrait OVEN_BAKED = register("oven_baked", 0.9F);

}
