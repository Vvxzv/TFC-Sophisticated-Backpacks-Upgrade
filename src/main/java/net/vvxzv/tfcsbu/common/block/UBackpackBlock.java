package net.vvxzv.tfcsbu.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlock;
import net.vvxzv.tfcsbu.common.block.entity.UBackpackBlockEntity;
import net.vvxzv.tfcsbu.common.registry.UBlockEntity;

public class UBackpackBlock extends BackpackBlock {
    public UBackpackBlock(){
        this(0.8F);
    }

    public UBackpackBlock(float explosionResistance){
        super(explosionResistance);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new UBackpackBlockEntity(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide ? createTickerHelper(pBlockEntityType, UBlockEntity.BACKPACK.get(), (level, blockPos, blockState, backpackBlockEntity) -> UBackpackBlockEntity.serverTick(level, blockPos, backpackBlockEntity)) : null;
    }
}
