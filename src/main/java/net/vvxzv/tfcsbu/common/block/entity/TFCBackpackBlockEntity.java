package net.vvxzv.tfcsbu.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlockEntity;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ITickableUpgrade;
import net.vvxzv.tfcsbu.common.registry.BlockEntities;
import org.jetbrains.annotations.NotNull;

public class TFCBackpackBlockEntity extends BackpackBlockEntity {
    private final BlockEntityType<?> type;

    public TFCBackpackBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
        this.type = BlockEntities.BACKPACK.get();
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return type != null ? type : BlockEntities.BACKPACK.get();
    }

    public static void serverTick(Level level, BlockPos blockPos, TFCBackpackBlockEntity backpackBlockEntity) {
        if (!level.isClientSide) {
            backpackBlockEntity.getBackpackWrapper().getUpgradeHandler().getWrappersThatImplement(ITickableUpgrade.class).forEach((upgrade) -> upgrade.tick(null, level, blockPos));
        }
    }
}
