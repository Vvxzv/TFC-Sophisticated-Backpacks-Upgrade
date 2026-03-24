package net.vvxzv.tfcsbu.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.tfcsbu.TFCSBU;
import net.vvxzv.tfcsbu.common.block.entity.UBackpackBlockEntity;

import java.util.function.Supplier;

public class UBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TFCSBU.MODID);

    public static final Supplier<BlockEntityType<UBackpackBlockEntity>> BACKPACK = BLOCK_ENTITY_TYPES.register("backpack", () -> BlockEntityType.Builder.of(UBackpackBlockEntity::new, new Block[]{
            UBlock.BISMUTH_BRONZE_BACKPACK.get(),
            UBlock.BRONZE_BACKPACK.get(),
            UBlock.BLACK_BRONZE_BACKPACK.get(),
            UBlock.WROUGHT_IRON_BACKPACK.get(),
            UBlock.STEEL_BACKPACK.get(),
            UBlock.BLACK_STEEL_BACKPACK.get()
    }).build(null));
}
