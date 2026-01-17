package net.vvxzv.tfcsbu.common.register;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.tfcsbu.TFCSBU;
import net.vvxzv.tfcsbu.common.blockentity.UBackpackBlockEntity;

public class UBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TFCSBU.MODID);

    public static final RegistryObject<BlockEntityType<UBackpackBlockEntity>> BACKPACK = BLOCK_ENTITY_TYPES.register("backpack", () -> BlockEntityType.Builder.of(UBackpackBlockEntity::new, new Block[]{
            UBlock.BISMUTH_BRONZE_BACKPACK.get(),
            UBlock.BRONZE_BACKPACK.get(),
            UBlock.BLACK_BRONZE_BACKPACK.get(),
            UBlock.WROUGHT_IRON_BACKPACK.get(),
            UBlock.STEEL_BACKPACK.get(),
            UBlock.BLACK_STEEL_BACKPACK.get()
    }).build(null));
}
