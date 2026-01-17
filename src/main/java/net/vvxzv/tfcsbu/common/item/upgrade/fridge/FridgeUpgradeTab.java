package net.vvxzv.tfcsbu.common.item.upgrade.fridge;

import net.minecraft.network.chat.Component;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;

public class FridgeUpgradeTab extends UpgradeSettingsTab<FridgeUpgradeContainer> {
    private final FridgeUpgradeLogicControl logicControl;
    public FridgeUpgradeTab(FridgeUpgradeContainer container, Position position, StorageScreenBase<?> screen) {
        super(container, position, screen, Component.translatable("upgrade.tab.fridge"), Component.translatable("upgrade.tab.fridge"));
        this.logicControl = this.addHideableChild(new FridgeUpgradeLogicControl(new Position(this.x + 3, this.y + 24), this.getContainer().getContainer()));
    }

    @Override
    protected void moveSlotsToTab() {
        this.logicControl.moveSlotsToView(this.screen.getGuiLeft(), this.screen.getGuiTop());
    }
}
