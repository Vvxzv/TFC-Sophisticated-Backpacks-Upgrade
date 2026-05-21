package net.vvxzv.tfcsbu.common.item.upgrade.fridge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.Button;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.*;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import net.vvxzv.tfcsbu.client.gui.GuiUtils;

public class FridgeUpgradeTab extends UpgradeSettingsTab<FridgeUpgradeContainer> {
    private final FridgeUpgradeLogicControl logicControl;

    public FridgeUpgradeTab(FridgeUpgradeContainer container, Position position, StorageScreenBase<?> screen) {
        super(container, position, screen, Component.translatable("upgrade.tab.fridge"), Component.translatable("upgrade.tab.fridge"));
        this.addHideableChild(new Button(new Position(this.x + 60, this.y + 43), GuiUtils.BUTTON_BASE, (b) -> {
            if(b == 0) {
                this.getContainer().sendDataToServer(() -> NBTHelper.putString(new CompoundTag(), "fridge_switch", "switch"));
            }
        }));
        this.logicControl = this.addHideableChild(new FridgeUpgradeLogicControl(new Position(this.x + 3, this.y + 24), this.getContainer().getContainer()));
    }

    @Override
    protected void moveSlotsToTab() {
        this.logicControl.moveSlotsToView(this.screen.getGuiLeft(), this.screen.getGuiTop());
    }
}
