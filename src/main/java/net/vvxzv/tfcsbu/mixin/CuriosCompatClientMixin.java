package net.vvxzv.tfcsbu.mixin;

import net.p3pp3rf1y.sophisticatedbackpacks.compat.curios.BackpackCurioRenderer;
import net.p3pp3rf1y.sophisticatedbackpacks.compat.curios.CuriosCompatClient;
import net.vvxzv.tfcsbu.common.registry.UItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mixin(CuriosCompatClient.class)
public class CuriosCompatClientMixin {

    @Inject(method = "registerRenderers", at = @At("HEAD"))
    private static void registerRenderers(CallbackInfo ci) {
        CuriosRendererRegistry.register(UItem.BISMUTH_BRONZE_BACKPACK.get(), BackpackCurioRenderer::new);
        CuriosRendererRegistry.register(UItem.BRONZE_BACKPACK.get(), BackpackCurioRenderer::new);
        CuriosRendererRegistry.register(UItem.BLACK_BRONZE_BACKPACK.get(), BackpackCurioRenderer::new);
        CuriosRendererRegistry.register(UItem.WROUGHT_IRON_BACKPACK.get(), BackpackCurioRenderer::new);
        CuriosRendererRegistry.register(UItem.STEEL_BACKPACK.get(), BackpackCurioRenderer::new);
        CuriosRendererRegistry.register(UItem.BLACK_STEEL_BACKPACK.get(), BackpackCurioRenderer::new);
    }
}
