package net.vvxzv.tfcsbu.common.registry;

import net.dries007.tfc.common.capabilities.food.FoodTrait;
import net.minecraft.resources.ResourceLocation;
import net.vvxzv.tfcsbu.Config;
import net.vvxzv.tfcsbu.TFCSBU;

public class FoodTraits {
    public static void registerFoodTrait() {
    }

    @SuppressWarnings("removal")
    private static FoodTrait register(String name, float decayModifier) {
        return FoodTrait.register(new ResourceLocation(TFCSBU.MODID, name), new FoodTrait(() -> decayModifier, "tfcsbu.tooltip.food_trait." + name));
    }

    private static float getFridgePreservedDecayModifier() {
        return (float) Config.fridgePreservedDecayModifier;
    }

    private static float getElectricityFridgePreservedDecayModifier() {
        return (float) Config.electricityFridgePreservedDecayModifier;
    }

    public static final FoodTrait FRIDGE_PRESERVED = register("fridge_preserved", getFridgePreservedDecayModifier());
    public static final FoodTrait ELECTRICITY_FRIDGE_PRESERVED = register("electricity_fridge_preserved", getElectricityFridgePreservedDecayModifier());
    public static final FoodTrait OVEN_BAKED = register("oven_baked", 0.9F);

}
