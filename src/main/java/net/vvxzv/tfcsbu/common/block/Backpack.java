package net.vvxzv.tfcsbu.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vvxzv.tfcsbu.common.block.entity.BackpackEntity;
import net.vvxzv.tfcsbu.common.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;

public class Backpack extends net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlock {
    public Backpack(){
        this(0.8F);
    }

    public Backpack(float explosionResistance){
        super(explosionResistance);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new BackpackEntity(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide ? createTickerHelper(pBlockEntityType, BlockEntities.BACKPACK.get(), (level, blockPos, blockState, backpackBlockEntity) -> BackpackEntity.serverTick(level, blockPos, backpackBlockEntity)) : null;
    }
}
