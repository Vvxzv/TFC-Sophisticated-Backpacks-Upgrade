package net.vvxzv.tfcsbu;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = TFCSophisticatedBackpacksUpgrade.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue SLOTS_COUNT_TO_OVERWEIGHT = BUILDER.comment(" ").comment("背包被占据超过了多少格物品槽才会超重").comment("How many item slots occupied by a backpack will cause it to become overweight.").comment("9").defineInRange("slotsCountToOverweight", 9, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue UPGRADES_COUNT_TO_OVERWEIGHT = BUILDER.comment(" ").comment("背包被占据了多少格升级槽开始超重").comment("How many upgrade slots have been occupied in the backpack before it becomes overweight").comment("1").defineInRange("upgradesCountToOverweight", 1, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue FRIDGE_PRESERVED_DECAY_MODIFIER = BUILDER.comment(" ").comment("冰箱封存，食物的腐烂速度").comment("Fridge Preserved, the decay modifier of food").defineInRange("fridgePreservedDecayModifier", 0.4, 0, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue ELECTRICITY_FRIDGE_PRESERVED_DECAY_MODIFIER = BUILDER.comment(" ").comment("冰箱冷藏，食物的腐烂速度").comment("Electricity Fridge Preserved, the decay modifier of food").defineInRange("electricityFridgePreservedDecayModifier", 0.25, 0, Double.MAX_VALUE);

    private static final ModConfigSpec.BooleanValue REMOVE_BACKPACK_ON_MONSTER = BUILDER.comment(" ", "移除怪物身上的背包", "Remove backpack on monster").define("removeBackpackOnMonster", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int slotsCountToOverweight;
    public static int upgradesCountToOverweight;
    public static double fridgePreservedDecayModifier;
    public static double electricityFridgePreservedDecayModifier;
    public static boolean removeBackpackOnMonster;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        slotsCountToOverweight = SLOTS_COUNT_TO_OVERWEIGHT.get();
        upgradesCountToOverweight = UPGRADES_COUNT_TO_OVERWEIGHT.get();
        fridgePreservedDecayModifier = FRIDGE_PRESERVED_DECAY_MODIFIER.get();
        electricityFridgePreservedDecayModifier = ELECTRICITY_FRIDGE_PRESERVED_DECAY_MODIFIER.get();
        removeBackpackOnMonster = REMOVE_BACKPACK_ON_MONSTER.get();
    }
}
