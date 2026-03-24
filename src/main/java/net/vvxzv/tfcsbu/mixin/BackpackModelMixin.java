package net.vvxzv.tfcsbu.mixin;

import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.p3pp3rf1y.sophisticatedbackpacks.client.render.BackpackModel;
import net.vvxzv.tfcsbu.TFCSBU;
import net.vvxzv.tfcsbu.common.registry.UItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(BackpackModel.class)
public class BackpackModelMixin {
    @Unique
    @SuppressWarnings("removal")
    private static final ResourceLocation BACKPACK_ENTITY_TEXTURE = new ResourceLocation(TFCSBU.MODID, "textures/entity/backpack.png");

    @Inject(
            method = "getBackpackItems",
            at = @At("RETURN"),
            remap = false,
            cancellable = true
    )
    private static void addBackpackItems(CallbackInfoReturnable<Map<Integer, Item>> cir) {
        Map<Integer, Item> originalMap = cir.getReturnValue();
        LinkedHashMap<Integer, Item> newMap = new LinkedHashMap<>(originalMap);

        newMap.put(12, UItem.BISMUTH_BRONZE_BACKPACK.get());
        newMap.put(13, UItem.BRONZE_BACKPACK.get());
        newMap.put(14, UItem.BLACK_BRONZE_BACKPACK.get());
        newMap.put(15, UItem.WROUGHT_IRON_BACKPACK.get());
        newMap.put(16, UItem.STEEL_BACKPACK.get());
        newMap.put(17, UItem.BLACK_STEEL_BACKPACK.get());

        cir.setReturnValue(newMap);
    }

    @Inject(method = "createBodyLayer", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, remap = false, cancellable = true)
    private static void injectCreateBodyLayer(CallbackInfoReturnable<LayerDefinition> cir, MeshDefinition meshdefinition, PartDefinition partDefinition) {
        cir.setReturnValue(LayerDefinition.create(meshdefinition, 128, 128));
    }

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderType;entityCutoutNoCull(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;",
                    ordinal = 0
            ),
            index = 0
    )
    private ResourceLocation modifyBackpackRenderTexture(ResourceLocation pLocation) {
        return BACKPACK_ENTITY_TEXTURE;
    }

    @ModifyArg(
            method = "renderBatteryCharge",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderType;entityTranslucent(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"
            ),
            index = 0
    )
    private ResourceLocation modifyRenderBatteryCharge(ResourceLocation pLocation) {
        return BACKPACK_ENTITY_TEXTURE;
    }
}
