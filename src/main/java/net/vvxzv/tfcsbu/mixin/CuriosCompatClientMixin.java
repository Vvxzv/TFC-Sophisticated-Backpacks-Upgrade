package net.vvxzv.tfcsbu.mixin;

import net.p3pp3rf1y.sophisticatedbackpacks.compat.curios.BackpackCurioRenderer;
import net.p3pp3rf1y.sophisticatedbackpacks.compat.curios.CuriosCompatClient;
import net.vvxzv.tfcsbu.common.registry.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mixin(CuriosCompatClient.class)
public class CuriosCompatClientMixin {

    @Inject(method = "registerRenderers", at = @At("HEAD"))
    private static void registerRenderers(CallbackInfo ci) {
        CuriosRendererRegistry.register(Items.BISMUTH_BRONZE_BACKPACK.get(), BackpackCurioRenderer::new);
        CuriosRendererRegistry.register(Items.BRONZE_BACKPACK.get(), BackpackCurioRenderer::new);
        CuriosRendererRegistry.register(Items.BLACK_BRONZE_BACKPACK.get(), BackpackCurioRenderer::new);
        CuriosRendererRegistry.register(Items.WROUGHT_IRON_BACKPACK.get(), BackpackCurioRenderer::new);
        CuriosRendererRegistry.register(Items.STEEL_BACKPACK.get(), BackpackCurioRenderer::new);
        CuriosRendererRegistry.register(Items.BLACK_STEEL_BACKPACK.get(), BackpackCurioRenderer::new);
    }
}
