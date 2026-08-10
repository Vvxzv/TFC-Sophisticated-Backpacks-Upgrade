package net.vvxzv.tfcsbu.common.registry;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlock;
import net.vvxzv.tfcsbu.TFCSophisticatedBackpacksUpgrade;
import net.vvxzv.tfcsbu.common.block.TFCBackpackBlock;

public class Blocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(TFCSophisticatedBackpacksUpgrade.MODID);

    public static final DeferredHolder<Block, BackpackBlock> BISMUTH_BRONZE_BACKPACK = BLOCKS.register("bismuth_bronze_backpack", () -> new TFCBackpackBlock(0.8F));

    public static final DeferredHolder<Block, BackpackBlock> BRONZE_BACKPACK = BLOCKS.register("bronze_backpack", () -> new TFCBackpackBlock(0.8F));

    public static final DeferredHolder<Block, BackpackBlock> BLACK_BRONZE_BACKPACK = BLOCKS.register("black_bronze_backpack", () -> new TFCBackpackBlock(0.8F));

    public static final DeferredHolder<Block, BackpackBlock> WROUGHT_IRON_BACKPACK = BLOCKS.register("wrought_iron_backpack", () -> new TFCBackpackBlock(0.8F));

    public static final DeferredHolder<Block, BackpackBlock> STEEL_BACKPACK  = BLOCKS.register("steel_backpack", () -> new TFCBackpackBlock(0.8F));

    public static final DeferredHolder<Block, BackpackBlock> BLACK_STEEL_BACKPACK  = BLOCKS.register("black_steel_backpack", () -> new TFCBackpackBlock(1200.0F));
}
