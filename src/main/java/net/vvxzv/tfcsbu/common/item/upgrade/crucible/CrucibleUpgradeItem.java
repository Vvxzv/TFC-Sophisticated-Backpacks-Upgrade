package net.vvxzv.tfcsbu.common.item.upgrade.crucible;

import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeCountLimitConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CrucibleUpgradeItem extends UpgradeItemBase<CrucibleUpgradeWrapper> {
    private static final UpgradeType<CrucibleUpgradeWrapper> TYPE = new UpgradeType<>(CrucibleUpgradeWrapper::new);

    public CrucibleUpgradeItem(IUpgradeCountLimitConfig upgradeTypeLimitConfig) {
        super(upgradeTypeLimitConfig);
    }

    @Override
    public @NotNull UpgradeType<CrucibleUpgradeWrapper> getType() {
        return TYPE;
    }

    @Override
    public @NotNull List<UpgradeConflictDefinition> getUpgradeConflicts() {
        return List.of();
    }
}
