package net.vvxzv.tfcsbu.mixin;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.compat.curios.BackpackCurioRenderer;
import net.p3pp3rf1y.sophisticatedbackpacks.compat.curios.CuriosCompat;
import net.vvxzv.tfcsbu.TFCSophisticatedBackpacksUpgrade;
import net.vvxzv.tfcsbu.common.registry.Items;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(CuriosCompat.class)
public class CuriosCompatMixin {
    @SuppressWarnings("removal")
    @Inject(
            method = "onAttachCapabilities",
            at = @At("HEAD"),
            remap = false
    )
    private void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> evt, CallbackInfo ci) {
        final ItemStack stack = evt.getObject();
        Item item = stack.getItem();
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(item);
        if (registryName != null && registryName.getNamespace().equals(TFCSophisticatedBackpacksUpgrade.MODID) && item instanceof BackpackItem) {
            evt.addCapability(new ResourceLocation(TFCSophisticatedBackpacksUpgrade.MODID, registryName.getPath() + "_curios"), new ICapabilityProvider() {
                @Nonnull
                public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                    return CuriosCapability.ITEM.orEmpty(cap, LazyOptional.of(() -> () -> stack));
                }
            });
        }
    }

    @Inject(
            method = "setup",
            at = @At("HEAD"),
            remap = false
    )
    private void setup(CallbackInfo ci) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            CuriosRendererRegistry.register(Items.BISMUTH_BRONZE_BACKPACK.get(), BackpackCurioRenderer::new);
            CuriosRendererRegistry.register(Items.BRONZE_BACKPACK.get(), BackpackCurioRenderer::new);
            CuriosRendererRegistry.register(Items.BLACK_BRONZE_BACKPACK.get(), BackpackCurioRenderer::new);
            CuriosRendererRegistry.register(Items.WROUGHT_IRON_BACKPACK.get(), BackpackCurioRenderer::new);
            CuriosRendererRegistry.register(Items.STEEL_BACKPACK.get(), BackpackCurioRenderer::new);
            CuriosRendererRegistry.register(Items.BLACK_STEEL_BACKPACK.get(), BackpackCurioRenderer::new);
        });
    }
}
