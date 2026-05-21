package net.vvxzv.tfcsbu.mixin;

import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelAccessor;
import net.p3pp3rf1y.sophisticatedbackpacks.common.EntityBackpackAdditionHandler;
import net.vvxzv.tfcsbu.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityBackpackAdditionHandler.class)
public class EntityBackpackAdditionHandlerMixin {

    @Inject(method = "addBackpack", at = @At("HEAD"), cancellable = true, remap = false)
    private static void disableAddBackpack(Monster monster, LevelAccessor level, CallbackInfo ci) {
        if(Config.removeBackpackOnMonster) {
            ci.cancel();
        }
    }
}
