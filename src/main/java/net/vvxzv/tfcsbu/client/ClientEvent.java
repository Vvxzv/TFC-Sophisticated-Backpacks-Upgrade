package net.vvxzv.tfcsbu.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.client.render.BackpackBlockEntityRenderer;
import net.p3pp3rf1y.sophisticatedbackpacks.client.render.BackpackItemStackRenderer;
import net.p3pp3rf1y.sophisticatedcore.util.WorldHelper;
import net.vvxzv.tfcsbu.TFCSophisticatedBackpacksUpgrade;
import net.vvxzv.tfcsbu.common.block.entity.TFCBackpackBlockEntity;
import net.vvxzv.tfcsbu.common.registry.Blocks;
import net.vvxzv.tfcsbu.common.registry.BlockEntities;
import net.vvxzv.tfcsbu.common.registry.Items;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = TFCSophisticatedBackpacksUpgrade.MODID, bus = EventBusSubscriber.Bus.MOD , value = Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((backpack, layer) -> {
            if (layer > 1 || !(backpack.getItem() instanceof BackpackItem)) {
                return -1;
            }
            IBackpackWrapper backpackWrapper = BackpackWrapper.fromStack(backpack);
            if (layer == 0) {
                return backpackWrapper.getMainColor();
            } else if (layer == 1) {
                return backpackWrapper.getAccentColor();
            }
            return -1;
        }, Items.BISMUTH_BRONZE_BACKPACK.get(), Items.BRONZE_BACKPACK.get(), Items.BLACK_BRONZE_BACKPACK.get(), Items.WROUGHT_IRON_BACKPACK.get(), Items.STEEL_BACKPACK.get(), Items.BLACK_STEEL_BACKPACK.get());
    }

    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((state, blockDisplayReader, pos, tintIndex) -> {
            if (tintIndex < 0 || tintIndex > 1 || pos == null) {
                return -1;
            }
            return WorldHelper.getBlockEntity(blockDisplayReader, pos, TFCBackpackBlockEntity.class)
                    .map(te -> tintIndex == 0 ? te.getBackpackWrapper().getMainColor() : te.getBackpackWrapper().getAccentColor())
                    .orElse(getDefaultColor(tintIndex));
        }, Blocks.BISMUTH_BRONZE_BACKPACK.get(), Blocks.BRONZE_BACKPACK.get(), Blocks.BLACK_BRONZE_BACKPACK.get(), Blocks.WROUGHT_IRON_BACKPACK.get(), Blocks.STEEL_BACKPACK.get(), Blocks.BLACK_STEEL_BACKPACK.get());
    }

    private static int getDefaultColor(int tintIndex) {
        return tintIndex == 0 ? BackpackWrapper.DEFAULT_MAIN_COLOR : BackpackWrapper.DEFAULT_ACCENT_COLOR;
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntities.BACKPACK.get(), (context) -> new BackpackBlockEntityRenderer());
    }

    @SubscribeEvent
    public static void registerBackpackClientExtension(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            private final Lazy<BlockEntityWithoutLevelRenderer> ister = Lazy.of(() -> new BackpackItemStackRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.ister.get();
            }
        }, Items.BISMUTH_BRONZE_BACKPACK.get(), Items.BRONZE_BACKPACK.get(), Items.BLACK_BRONZE_BACKPACK.get(), Items.WROUGHT_IRON_BACKPACK.get(), Items.STEEL_BACKPACK.get(), Items.BLACK_STEEL_BACKPACK.get());
    }
}
