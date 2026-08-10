package net.vvxzv.tfcsbu.common.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.tfcsbu.TFCSophisticatedBackpacksUpgrade;
import net.vvxzv.tfcsbu.common.block.entity.BackpackEntity;

public class BlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TFCSophisticatedBackpacksUpgrade.MODID);

    public static final RegistryObject<BlockEntityType<BackpackEntity>> BACKPACK = BLOCK_ENTITY_TYPES.register("backpack", () -> BlockEntityType.Builder.of(BackpackEntity::new, new Block[]{
            Blocks.BISMUTH_BRONZE_BACKPACK.get(),
            Blocks.BRONZE_BACKPACK.get(),
            Blocks.BLACK_BRONZE_BACKPACK.get(),
            Blocks.WROUGHT_IRON_BACKPACK.get(),
            Blocks.STEEL_BACKPACK.get(),
            Blocks.BLACK_STEEL_BACKPACK.get()
    }).build(null));
}
