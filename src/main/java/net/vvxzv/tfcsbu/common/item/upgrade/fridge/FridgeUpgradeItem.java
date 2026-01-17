package net.vvxzv.tfcsbu.common.item.upgrade.fridge;

import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeCountLimitConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;

import java.util.List;

public class FridgeUpgradeItem extends UpgradeItemBase<FridgeUpgradeWrapper> {
    private static final UpgradeType<FridgeUpgradeWrapper> TYPE = new UpgradeType<>(FridgeUpgradeWrapper::new);

    public FridgeUpgradeItem(IUpgradeCountLimitConfig upgradeTypeLimitConfig) {
        super(upgradeTypeLimitConfig);
    }

    @Override
    public UpgradeType<FridgeUpgradeWrapper> getType() {
        return TYPE;
    }

    @Override
    public List<UpgradeConflictDefinition> getUpgradeConflicts() {
        return List.of();
    }
}
