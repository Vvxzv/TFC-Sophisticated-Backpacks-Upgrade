package net.vvxzv.tfcsbu.compat.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Predicate;

public class CuriosHelper {
    public static Optional<SlotResult> findFirstCurio(LivingEntity entity, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosInventory(entity).flatMap(handler -> handler.findFirstCurio(predicate));
    }

    public static boolean hasCurio(LivingEntity entity, Predicate<ItemStack> predicate) {
        return findFirstCurio(entity, predicate).isPresent();
    }
}
