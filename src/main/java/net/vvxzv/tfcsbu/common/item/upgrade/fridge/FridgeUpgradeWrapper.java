package net.vvxzv.tfcsbu.common.item.upgrade.fridge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ITickableUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class FridgeUpgradeWrapper extends UpgradeWrapperBase<FridgeUpgradeWrapper, FridgeUpgradeItem> implements ITickableUpgrade {
    private final FridgeUpgradeLogic logic;

    public FridgeUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
        super(storageWrapper, upgrade, upgradeSaveHandler);
        this.logic = new FridgeUpgradeLogic(storageWrapper, upgrade, upgradeSaveHandler);
    }


    public FridgeUpgradeLogic getLogic() {
        return logic;
    }

    @Override
    public void tick(@Nullable Entity entity, @NotNull Level level, @NotNull BlockPos pos) {
        this.logic.tick(level);
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }
}
