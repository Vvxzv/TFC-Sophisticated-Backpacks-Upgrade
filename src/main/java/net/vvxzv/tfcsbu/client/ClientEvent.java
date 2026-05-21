package net.vvxzv.tfcsbu.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.client.render.BackpackBlockEntityRenderer;
import net.p3pp3rf1y.sophisticatedcore.util.WorldHelper;
import net.vvxzv.tfcsbu.TFCSBU;
import net.vvxzv.tfcsbu.common.block.entity.BackpackEntity;
import net.vvxzv.tfcsbu.common.registry.Blocks;
import net.vvxzv.tfcsbu.common.registry.BlockEntities;
import net.vvxzv.tfcsbu.common.registry.Items;

@Mod.EventBusSubscriber(modid = TFCSBU.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((backpack, layer) -> {
            if (layer > 1 || !(backpack.getItem() instanceof BackpackItem)) {
                return -1;
            }
            return backpack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance()).map(backpackWrapper -> {
                if (layer == 0) {
                    return backpackWrapper.getMainColor();
                } else if (layer == 1) {
                    return backpackWrapper.getAccentColor();
                }
                return -1;
            }).orElse(BackpackWrapper.DEFAULT_CLOTH_COLOR);
        }, Items.BISMUTH_BRONZE_BACKPACK.get(), Items.BRONZE_BACKPACK.get(), Items.BLACK_BRONZE_BACKPACK.get(), Items.WROUGHT_IRON_BACKPACK.get(), Items.STEEL_BACKPACK.get(), Items.BLACK_STEEL_BACKPACK.get());
    }

    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((state, blockDisplayReader, pos, tintIndex) -> {
            if (tintIndex < 0 || tintIndex > 1 || pos == null) {
                return -1;
            }
            return WorldHelper.getBlockEntity(blockDisplayReader, pos, BackpackEntity.class)
                    .map(te -> tintIndex == 0 ? te.getBackpackWrapper().getMainColor() : te.getBackpackWrapper().getAccentColor())
                    .orElse(getDefaultColor(tintIndex));
        }, Blocks.BISMUTH_BRONZE_BACKPACK.get(), Blocks.BRONZE_BACKPACK.get(), Blocks.BLACK_BRONZE_BACKPACK.get(), Blocks.WROUGHT_IRON_BACKPACK.get(), Blocks.STEEL_BACKPACK.get(), Blocks.BLACK_STEEL_BACKPACK.get());
    }

    private static int getDefaultColor(int tintIndex) {
        return tintIndex == 0 ? BackpackWrapper.DEFAULT_CLOTH_COLOR : BackpackWrapper.DEFAULT_BORDER_COLOR;
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntities.BACKPACK.get(), (context) -> new BackpackBlockEntityRenderer());
    }
}
