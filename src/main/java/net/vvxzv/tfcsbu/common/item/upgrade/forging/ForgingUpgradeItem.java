package net.vvxzv.tfcsbu.common.item.upgrade.forging;

import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeCountLimitConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;

import java.util.List;

public class ForgingUpgradeItem extends UpgradeItemBase<ForgingUpgradeWrapper> {
    private static final UpgradeType<ForgingUpgradeWrapper> TYPE = new UpgradeType<>(ForgingUpgradeWrapper::new);

    public ForgingUpgradeItem(IUpgradeCountLimitConfig upgradeTypeLimitConfig) {
        super(upgradeTypeLimitConfig);
    }

    @Override
    public UpgradeType<ForgingUpgradeWrapper> getType() {
        return TYPE;
    }

    @Override
    public List<UpgradeConflictDefinition> getUpgradeConflicts() {
        return List.of();
    }
}
