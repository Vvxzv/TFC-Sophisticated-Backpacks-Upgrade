package net.vvxzv.tfcsbu;

import com.mojang.logging.LogUtils;
import net.dries007.tfc.common.TFCEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.vvxzv.tfcsbu.common.registry.*;
import net.vvxzv.tfcsbu.common.utils.ItemTagKeys;
import org.slf4j.Logger;

import java.util.UUID;

@Mod(TFCSophisticatedBackpacksUpgrade.MODID)
public class TFCSophisticatedBackpacksUpgrade {
    public static final String MODID = "tfcsbu";
    public static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("removal")
    public TFCSophisticatedBackpacksUpgrade() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Items.ITEMS.register(modEventBus);
        BlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        Blocks.BLOCKS.register(modEventBus);
        CreativeTab.TABS.register(modEventBus);

        modEventBus.addListener(Items::registerContainers);
        modEventBus.addListener(TFCSophisticatedBackpacksUpgrade::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ForgeEventHandler.init();
    }

    private static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(Items::registerDispenseBehavior);
        event.enqueueWork(Items::registerCauldronInteractions);
        event.enqueueWork(FoodTraits::registerFoodTrait);
    }
}
