package net.vvxzv.tfcsbu;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.vvxzv.tfcsbu.common.registry.CreativeTab;
import net.vvxzv.tfcsbu.common.registry.UBlock;
import net.vvxzv.tfcsbu.common.registry.UBlockEntity;
import net.vvxzv.tfcsbu.common.registry.UItem;
import org.slf4j.Logger;

@Mod(TFCSBU.MODID)
public class TFCSBU {
    public static final String MODID = "tfcsbu";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TFCSBU(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(TFCSBU::setup);

        UItem.ITEMS.register(modEventBus);
        UBlock.BLOCKS.register(modEventBus);
        UBlockEntity.BLOCK_ENTITY_TYPES.register(modEventBus);
        CreativeTab.TABS.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        modEventBus.addListener(UItem::registerCapabilities);
    }

    private static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(UItem::registerDispenseBehavior);
        event.enqueueWork(UItem::registerCauldronInteractions);
    }
}
