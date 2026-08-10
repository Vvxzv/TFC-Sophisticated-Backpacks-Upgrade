package net.vvxzv.tfcsbu;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.vvxzv.tfcsbu.common.registry.*;
import net.vvxzv.tfcsbu.common.utils.ItemTagKeys;
import org.slf4j.Logger;

import java.util.UUID;

@Mod(TFCSophisticatedBackpacksUpgrade.MODID)
public class TFCSophisticatedBackpacksUpgrade {
    public static final String MODID = "tfcsbu";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TFCSophisticatedBackpacksUpgrade(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(TFCSophisticatedBackpacksUpgrade::setup);
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

        IEventBus bus = NeoForge.EVENT_BUS;
        bus.addListener(this::vesselUUID);
    }

    private static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(Items::registerDispenseBehavior);
        event.enqueueWork(Items::registerCauldronInteractions);
    }

    public void vesselUUID(PlayerTickEvent.Pre event) {
        if(event.getEntity() instanceof ServerPlayer player) {
            if(player.level().getGameTime() % 20 == 0) {
                AbstractContainerMenu menu = player.containerMenu;
                menu.slots.forEach(slot -> {
                    ItemStack stack = slot.getItem();
                    int count = stack.getCount();
                    if(stack.is(ItemTagKeys.FIRED_VESSELS) && count == 1) {
                        CustomData data = stack.getOrDefault(DataComponent.TAG, CustomData.of(new CompoundTag()));
                        CompoundTag tag = data.copyTag();
                        if(!tag.contains("uuid")) {
                            tag.putUUID("uuid", UUID.randomUUID());
                            stack.set(DataComponent.TAG, CustomData.of(tag));
                        }
                    }
                });
            }
        }
    }
}
