package net.vvxzv.tfcsbu.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlock;
import net.vvxzv.tfcsbu.common.block.entity.TFCBackpackBlockEntity;
import net.vvxzv.tfcsbu.common.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;

public class TFCBackpackBlock extends BackpackBlock {
    public TFCBackpackBlock(float explosionResistance){
        super(explosionResistance);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new TFCBackpackBlockEntity(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide ? createTickerHelper(pBlockEntityType, BlockEntities.BACKPACK.get(), (level, blockPos, blockState, backpackBlockEntity) -> TFCBackpackBlockEntity.serverTick(level, blockPos, backpackBlockEntity)) : null;
    }
}
