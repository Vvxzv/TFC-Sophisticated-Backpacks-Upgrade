package net.vvxzv.tfcsbu.common.item.upgrade.forging;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ITickableUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ForgingUpgradeWrapper extends UpgradeWrapperBase<ForgingUpgradeWrapper, ForgingUpgradeItem> implements ITickableUpgrade {
    private final ForgingUpgradeLogic logic;

    protected ForgingUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        this.logic = new ForgingUpgradeLogic(upgrade, upgradeSaveHandler);
    }

    public ForgingUpgradeLogic getLogic() {
        return logic;
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }

    @Override
    public void tick(@Nullable Entity entity, Level level, BlockPos blockPos) {
        this.logic.tick(level, entity);
    }
}
