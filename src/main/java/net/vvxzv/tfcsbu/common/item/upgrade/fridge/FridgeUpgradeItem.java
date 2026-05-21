package net.vvxzv.tfcsbu.common.item.upgrade.fridge;

import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeCountLimitConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FridgeUpgradeItem extends UpgradeItemBase<FridgeUpgradeWrapper> {
    private static final UpgradeType<FridgeUpgradeWrapper> TYPE = new UpgradeType<>(FridgeUpgradeWrapper::new);

    public FridgeUpgradeItem(IUpgradeCountLimitConfig upgradeTypeLimitConfig) {
        super(upgradeTypeLimitConfig);
    }

    @Override
    public @NotNull UpgradeType<FridgeUpgradeWrapper> getType() {
        return TYPE;
    }

    @Override
    public @NotNull List<UpgradeConflictDefinition> getUpgradeConflicts() {
        return List.of();
    }
}
