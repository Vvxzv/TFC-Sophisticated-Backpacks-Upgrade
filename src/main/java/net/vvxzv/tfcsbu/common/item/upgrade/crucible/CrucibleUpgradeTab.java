package net.vvxzv.tfcsbu.common.item.upgrade.crucible;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.Button;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinition;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.Label;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Dimension;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import net.vvxzv.tfcsbu.client.gui.GuiUtils;

public class CrucibleUpgradeTab extends UpgradeSettingsTab<CrucibleUpgradeContainer> {
    private final CrucibleUpgradeLogicControl logicControl;

    private static final ButtonDefinition BELOWS_TRIGGER = new ButtonDefinition(Dimension.SQUARE_18, GuiHelper.DEFAULT_BUTTON_BACKGROUND, GuiHelper.DEFAULT_BUTTON_HOVERED_BACKGROUND, null, Component.translatable("tfcsbu.crucible_upgrade.bellows.trigger"));

    public CrucibleUpgradeTab(CrucibleUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen) {
        super(upgradeContainer, position, screen, Component.translatable("upgrade.tab.crucible"), Component.translatable("upgrade.tab.crucible"));
        this.addHideableChild(new Button(new Position(this.x + 108, this.y + 24), BELOWS_TRIGGER, (b) -> {
            if(b == 0) {
                this.getContainer().sendDataToServer(() -> NBTHelper.putString(new CompoundTag(), "crucible", "bellows"));
            }
        }));

        this.addHideableChild(new Button(new Position(this.x + 108, this.y + 42), GuiUtils.BUTTON_BASE, (b) -> {
            if(b == 0) {
                this.getContainer().sendDataToServer(() -> NBTHelper.putString(new CompoundTag(), "crucible", "power"));
            }
        }));

        this.addHideableChild(new Button(new Position(this.x + 108, this.y + 60), GuiUtils.BUTTON_BASE, (b) -> {
            if(b == 0) {
                this.getContainer().sendDataToServer(() -> NBTHelper.putString(new CompoundTag(), "crucible", "lock"));
            }
        }));


        this.logicControl = this.addHideableChild(new CrucibleUpgradeLogicControl(new Position(this.x + 3, this.y + 24), this.getContainer().getContainer()));
        this.addHideableChild(new Label(new Position(this.x + 20, this.y + 8), Component.translatable("upgrade.tab.crucible")));
    }

    @Override
    protected void moveSlotsToTab() {
        this.logicControl.moveSlotsToView(this.screen.getGuiLeft(), this.screen.getGuiTop());
    }
}
