package net.vvxzv.tfcsbu;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vvxzv.tfcsbu.common.registry.*;
import org.slf4j.Logger;

@Mod(TFCSBU.MODID)
public class TFCSBU {
    public static final String MODID = "tfcsbu";
    public static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("removal")
    public TFCSBU() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(Items::registerContainers);
        modEventBus.addListener(TFCSBU::setup);

        Items.ITEMS.register(modEventBus);
        BlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        Blocks.BLOCKS.register(modEventBus);
        CreativeTab.TABS.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(Items::registerDispenseBehavior);
        event.enqueueWork(Items::registerCauldronInteractions);
        event.enqueueWork(FoodTraits::registerFoodTrait);
    }
}
