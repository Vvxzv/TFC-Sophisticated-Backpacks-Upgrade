package net.vvxzv.tfcsbu.common.item.upgrade.oven;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ITickableUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class OvenUpgradeWrapper extends UpgradeWrapperBase<OvenUpgradeWrapper, OvenUpgradeItem> implements ITickableUpgrade {
    private final OvenUpgradeLogic logic;

    protected OvenUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        this.logic = new OvenUpgradeLogic(upgrade, upgradeSaveHandler);
    }

    public OvenUpgradeLogic getLogic() {
        return logic;
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }

    @Override
    public void tick(@Nullable Entity entity, @NotNull Level level, @NotNull BlockPos blockPos) {
        this.logic.tick();
    }
}
