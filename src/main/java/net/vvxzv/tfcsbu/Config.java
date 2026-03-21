package net.vvxzv.tfcsbu;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = TFCSBU.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue SLOTS_COUNT_TO_OVERWEIGHT = BUILDER.comment(" ").comment("背包被占据超过了多少格物品槽才会超重").comment("How many item slots occupied by a backpack will cause it to become overweight.").comment("9").defineInRange("slotsCountToOverweight", 9, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue UPGRADES_COUNT_TO_OVERWEIGHT = BUILDER.comment(" ").comment("背包被占据了多少格升级槽开始超重").comment("How many upgrade slots have been occupied in the backpack before it becomes overweight").comment("1").defineInRange("upgradesCountToOverweight", 1, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue FRIDGE_PRESERVED_DECAY_MODIFIER = BUILDER.comment(" ").comment("冰箱封存，食物的腐烂速度").comment("Fridge Preserved, the decay modifier of food").defineInRange("fridgePreservedDecayModifier", 0.4, 0, Double.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue ELECTRICITY_FRIDGE_PRESERVED_DECAY_MODIFIER = BUILDER.comment(" ").comment("冰箱冷藏，食物的腐烂速度").comment("Electricity Fridge Preserved, the decay modifier of food").defineInRange("electricityFridgePreservedDecayModifier", 0.25, 0, Double.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int slotsCountToOverweight;
    public static int upgradesCountToOverweight;
    public static double fridgePreservedDecayModifier;
    public static double electricityFridgePreservedDecayModifier;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        slotsCountToOverweight = SLOTS_COUNT_TO_OVERWEIGHT.get();
        upgradesCountToOverweight = UPGRADES_COUNT_TO_OVERWEIGHT.get();
        fridgePreservedDecayModifier = FRIDGE_PRESERVED_DECAY_MODIFIER.get();
        electricityFridgePreservedDecayModifier = ELECTRICITY_FRIDGE_PRESERVED_DECAY_MODIFIER.get();
    }
}
