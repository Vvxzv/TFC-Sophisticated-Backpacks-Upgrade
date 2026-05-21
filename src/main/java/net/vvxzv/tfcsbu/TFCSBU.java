package net.vvxzv.tfcsbu;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.vvxzv.tfcsbu.common.registry.*;
import org.slf4j.Logger;

@Mod(TFCSBU.MODID)
public class TFCSBU {
    public static final String MODID = "tfcsbu";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TFCSBU(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(TFCSBU::setup);
        modEventBus.addListener(Items::registerCapabilities);
        modEventBus.addListener(BlockEntities::registerCapabilities);
        modEventBus.addListener(Items::registerContainers);

        if(FMLEnvironment.dist.isClient()){
            modEventBus.addListener(Items::onMenuScreenRegister);
        }

        FoodTraits.TRAITS.register(modEventBus);
        Items.ITEMS.register(modEventBus);
        Blocks.BLOCKS.register(modEventBus);
        BlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        CreativeTab.TABS.register(modEventBus);
        DataComponent.DATA_COMPONENT_TYPES.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(Items::registerDispenseBehavior);
        event.enqueueWork(Items::registerCauldronInteractions);
    }
}
