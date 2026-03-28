package net.vvxzv.tfcsbu.common.item.upgrade.oven;

import net.minecraft.network.chat.Component;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;

public class OvenUpgradeTab extends UpgradeSettingsTab<OvenUpgradeContainer> {
    private final OvenUpgradeLogicControl logicControl;

    public OvenUpgradeTab(OvenUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen) {
        super(upgradeContainer, position, screen, Component.translatable("upgrade.tab.oven"), Component.translatable("upgrade.tab.oven"));
        this.logicControl = this.addHideableChild(new OvenUpgradeLogicControl(new Position(this.x + 3, this.y + 24), this.getContainer().getContainer()));
    }

    @Override
    protected void moveSlotsToTab() {
        this.logicControl.moveSlotsToView(this.screen.getGuiLeft(), this.screen.getGuiTop());
    }
}
