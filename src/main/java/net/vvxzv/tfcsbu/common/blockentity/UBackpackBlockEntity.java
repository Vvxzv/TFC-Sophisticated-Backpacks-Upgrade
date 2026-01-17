package net.vvxzv.tfcsbu.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlockEntity;
import net.vvxzv.tfcsbu.common.register.UBlockEntity;

public class UBackpackBlockEntity extends BackpackBlockEntity {
    private final BlockEntityType<?> type;

    public UBackpackBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
        this.type = UBlockEntity.BACKPACK.get();
    }

    @Override
    public BlockEntityType<?> getType() {
        return type != null ? type : UBlockEntity.BACKPACK.get();
    }
}
