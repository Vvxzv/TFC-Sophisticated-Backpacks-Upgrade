package net.vvxzv.tfcsbu.common.registry;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlock;
import net.vvxzv.tfcsbu.TFCSBU;
import net.vvxzv.tfcsbu.common.block.UBackpackBlock;

public class UBlock {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TFCSBU.MODID);

    public static final RegistryObject<BackpackBlock> BISMUTH_BRONZE_BACKPACK = BLOCKS.register("bismuth_bronze_backpack", UBackpackBlock::new);
    public static final RegistryObject<BackpackBlock> BRONZE_BACKPACK = BLOCKS.register("bronze_backpack", UBackpackBlock::new);
    public static final RegistryObject<BackpackBlock> BLACK_BRONZE_BACKPACK = BLOCKS.register("black_bronze_backpack", UBackpackBlock::new);

    public static final RegistryObject<BackpackBlock> WROUGHT_IRON_BACKPACK = BLOCKS.register("wrought_iron_backpack", UBackpackBlock::new);

    public static final RegistryObject<BackpackBlock> STEEL_BACKPACK  = BLOCKS.register("steel_backpack", UBackpackBlock::new);

    public static final RegistryObject<BackpackBlock> BLACK_STEEL_BACKPACK  = BLOCKS.register("black_steel_backpack",() -> new UBackpackBlock(1200.0F));
}
