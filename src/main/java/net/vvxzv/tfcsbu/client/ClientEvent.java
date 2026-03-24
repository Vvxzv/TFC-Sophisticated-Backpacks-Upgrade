package net.vvxzv.tfcsbu.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.client.render.BackpackBlockEntityRenderer;
import net.p3pp3rf1y.sophisticatedcore.util.WorldHelper;
import net.vvxzv.tfcsbu.TFCSBU;
import net.vvxzv.tfcsbu.common.block.entity.UBackpackBlockEntity;
import net.vvxzv.tfcsbu.common.registry.UBlock;
import net.vvxzv.tfcsbu.common.registry.UBlockEntity;
import net.vvxzv.tfcsbu.common.registry.UItem;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = TFCSBU.MODID, bus = EventBusSubscriber.Bus.MOD , value = Dist.CLIENT)
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
        }, UItem.BISMUTH_BRONZE_BACKPACK.get(), UItem.BRONZE_BACKPACK.get(), UItem.BLACK_BRONZE_BACKPACK.get(), UItem.WROUGHT_IRON_BACKPACK.get(), UItem.STEEL_BACKPACK.get(), UItem.BLACK_STEEL_BACKPACK.get());
    }

    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((state, blockDisplayReader, pos, tintIndex) -> {
            if (tintIndex < 0 || tintIndex > 1 || pos == null) {
                return -1;
            }
            return WorldHelper.getBlockEntity(blockDisplayReader, pos, UBackpackBlockEntity.class)
                    .map(te -> tintIndex == 0 ? te.getBackpackWrapper().getMainColor() : te.getBackpackWrapper().getAccentColor())
                    .orElse(getDefaultColor(tintIndex));
        }, UBlock.BISMUTH_BRONZE_BACKPACK.get(), UBlock.BRONZE_BACKPACK.get(), UBlock.BLACK_BRONZE_BACKPACK.get(), UBlock.WROUGHT_IRON_BACKPACK.get(), UBlock.STEEL_BACKPACK.get(), UBlock.BLACK_STEEL_BACKPACK.get());
    }

    private static int getDefaultColor(int tintIndex) {
        return tintIndex == 0 ? BackpackWrapper.DEFAULT_MAIN_COLOR : BackpackWrapper.DEFAULT_ACCENT_COLOR;
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(UBlockEntity.BACKPACK.get(), (context) -> new BackpackBlockEntityRenderer());
    }
}
