package net.vvxzv.tfcsbu.common.item.upgrade.oven;

import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeCountLimitConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;

import java.util.List;

public class OvenUpgradeItem extends UpgradeItemBase<OvenUpgradeWrapper> {
    private static final UpgradeType<OvenUpgradeWrapper> TYPE = new UpgradeType<>(OvenUpgradeWrapper::new);

    public OvenUpgradeItem(IUpgradeCountLimitConfig upgradeTypeLimitConfig) {
        super(upgradeTypeLimitConfig);
    }

    @Override
    public UpgradeType<OvenUpgradeWrapper> getType() {
        return TYPE;
    }

    @Override
    public List<UpgradeConflictDefinition> getUpgradeConflicts() {
        return List.of();
    }
}
