package net.vvxzv.tfcsbu.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.tfcsbu.TFCSBU;
import net.vvxzv.tfcsbu.common.block.entity.TFCBackpackBlockEntity;

import java.util.function.Supplier;

public class BlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TFCSBU.MODID);

    public static final Supplier<BlockEntityType<TFCBackpackBlockEntity>> BACKPACK = BLOCK_ENTITY_TYPES.register("backpack", () -> BlockEntityType.Builder.of(TFCBackpackBlockEntity::new, new Block[]{
            Blocks.BISMUTH_BRONZE_BACKPACK.get(),
            Blocks.BRONZE_BACKPACK.get(),
            Blocks.BLACK_BRONZE_BACKPACK.get(),
            Blocks.WROUGHT_IRON_BACKPACK.get(),
            Blocks.STEEL_BACKPACK.get(),
            Blocks.BLACK_STEEL_BACKPACK.get()
    }).build(null));

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BACKPACK.get(), net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlockEntity::getExternalItemHandler);

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, BACKPACK.get(), net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlockEntity::getExternalFluidHandler);

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, BACKPACK.get(), net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlockEntity::getExternalEnergyStorage);
    }
}
