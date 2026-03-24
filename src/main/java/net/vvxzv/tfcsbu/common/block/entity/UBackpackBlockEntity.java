package net.vvxzv.tfcsbu.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlockEntity;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ITickableUpgrade;
import net.vvxzv.tfcsbu.common.registry.UBlockEntity;

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

    public static void serverTick(Level level, BlockPos blockPos, UBackpackBlockEntity backpackBlockEntity) {
        if (!level.isClientSide) {
            backpackBlockEntity.getBackpackWrapper().getUpgradeHandler().getWrappersThatImplement(ITickableUpgrade.class).forEach((upgrade) -> upgrade.tick(null, level, blockPos));
        }
    }
}
