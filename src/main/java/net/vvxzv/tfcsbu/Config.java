package net.vvxzv.tfcsbu;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = TFCSophisticatedBackpacksUpgrade.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.DoubleValue FRIDGE_PRESERVED_DECAY_MODIFIER = BUILDER.comment(" ").comment("冰箱封存，食物的腐烂速度").comment("Fridge Preserved, the decay modifier of food").defineInRange("fridgePreservedDecayModifier", 0.4, 0, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue ELECTRICITY_FRIDGE_PRESERVED_DECAY_MODIFIER = BUILDER.comment(" ").comment("冰箱冷藏，食物的腐烂速度").comment("Electricity Fridge Preserved, the decay modifier of food").defineInRange("electricityFridgePreservedDecayModifier", 0.25, 0, Double.MAX_VALUE);

    private static final ModConfigSpec.BooleanValue REMOVE_BACKPACK_ON_MONSTER = BUILDER.comment(" ", "移除怪物身上的背包", "Remove backpack on monster").define("removeBackpackOnMonster", true);

    private static final ModConfigSpec.BooleanValue BACKPACK_OVERWEIGHT = BUILDER.comment(" ", "是否开启背包重量", "Whether enable backpack weight").define("backpackOverweight", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static double fridgePreservedDecayModifier;
    public static double electricityFridgePreservedDecayModifier;
    public static boolean removeBackpackOnMonster;
    public static boolean backpackOverweight;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        fridgePreservedDecayModifier = FRIDGE_PRESERVED_DECAY_MODIFIER.get();
        electricityFridgePreservedDecayModifier = ELECTRICITY_FRIDGE_PRESERVED_DECAY_MODIFIER.get();
        removeBackpackOnMonster = REMOVE_BACKPACK_ON_MONSTER.get();
        backpackOverweight = BACKPACK_OVERWEIGHT.get();
    }
}
