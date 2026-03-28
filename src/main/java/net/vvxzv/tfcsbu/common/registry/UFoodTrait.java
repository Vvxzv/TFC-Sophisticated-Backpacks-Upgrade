package net.vvxzv.tfcsbu.common.registry;

import net.dries007.tfc.common.component.food.FoodTrait;
import net.dries007.tfc.common.component.food.FoodTraits;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.tfcsbu.Config;
import net.vvxzv.tfcsbu.TFCSBU;

import java.util.function.Supplier;

public class UFoodTrait {
    public static final DeferredRegister<FoodTrait> TRAITS = DeferredRegister.create(FoodTraits.KEY, TFCSBU.MODID);

    private static DeferredHolder<FoodTrait, FoodTrait> register(String name, Supplier<Double> decayModifier) {
        return TRAITS.register(name, () -> new FoodTrait(decayModifier, "tfcsbu.tooltip.food_trait." + name));
    }

    private static Supplier<Double> getFridgePreservedDecayModifier() {
        return () -> Config.fridgePreservedDecayModifier;
    }

    private static Supplier<Double> getElectricityFridgePreservedDecayModifier() {
        return () -> Config.electricityFridgePreservedDecayModifier;
    }

    public static final DeferredHolder<FoodTrait, FoodTrait> FRIDGE_PRESERVED = register("fridge_preserved", getFridgePreservedDecayModifier());

    public static final DeferredHolder<FoodTrait, FoodTrait> ELECTRICITY_FRIDGE_PRESERVED = register("electricity_fridge_preserved", getElectricityFridgePreservedDecayModifier());

    public static final DeferredHolder<FoodTrait, FoodTrait> OVEN_BAKED = register("oven_baked", () -> 0.9);

}
