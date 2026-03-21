package net.vvxzv.tfcsbu.common.item.upgrade.forging;

import net.dries007.tfc.common.capabilities.forge.ForgeStep;
import net.dries007.tfc.util.Helpers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.Button;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinition;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.Label;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.*;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import net.vvxzv.tfcsbu.common.utils.GuiUtils;

import java.util.Locale;
import java.util.Map;

public class ForgingUpgradeTab extends UpgradeSettingsTab<ForgingUpgradeContainer> {
    private final ForgingUpgradeLogicControl logicControl;

    private static final TextureBlitData WELDING = new TextureBlitData(GuiUtils.rl("textures/gui/welding.png"), Dimension.SQUARE_16, new UV(0, 0), Dimension.SQUARE_16);
    private static final ButtonDefinition WELDING_BUTTON = new ButtonDefinition(Dimension.SQUARE_18, GuiHelper.DEFAULT_BUTTON_BACKGROUND, GuiHelper.DEFAULT_BUTTON_HOVERED_BACKGROUND, WELDING, Component.translatable("button.forging.welding"));

    private static final ButtonDefinition RECIPE_BUTTON = new ButtonDefinition(Dimension.SQUARE_18, GuiHelper.DEFAULT_BUTTON_BACKGROUND, GuiHelper.DEFAULT_BUTTON_HOVERED_BACKGROUND, null);

    public static final Map<ForgeStep, TextureBlitData> FORGING_ICONS = Helpers.mapOfKeys(
            ForgeStep.class,
            forgeStep -> new TextureBlitData(
                    GuiUtils.rl("tfcsbu:textures/gui/forging_background.png"),
                    new Dimension(128, 128),
                    new UV(forgeStep.iconX() / 2, forgeStep.iconY() / 2),
                    new Dimension(16, 16)
            )
    );
    private static final Map<ForgeStep, ButtonDefinition> FORGING_BUTTONS = Helpers.mapOfKeys(
            ForgeStep.class,
            (step) -> new ButtonDefinition(
                    Dimension.SQUARE_18,
                    FORGING_ICONS.get(step),
                    FORGING_ICONS.get(step),
                    FORGING_ICONS.get(step),
                    Component.translatable("tfc.enum.forgestep." + step.name().toLowerCase(Locale.ROOT))
            )
    );

    public ForgingUpgradeTab(ForgingUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen) {
        super(upgradeContainer, position, screen, Component.translatable("upgrade.tab.forging"), Component.translatable("upgrade.tab.forging"));
        this.addHideableChild(new Button(new Position(this.x + 128, this.y + 29), RECIPE_BUTTON, (b) -> {
            if(b == 0) {
                this.getContainer().sendDataToServer(() -> NBTHelper.putString(new CompoundTag(), "forging", "recipe"));
            }
        }));
        this.addHideableChild(new Button(new Position(this.x + 128, this.y + 65), WELDING_BUTTON, (b) -> {
            if(b == 0) {
                this.getContainer().sendDataToServer(() -> NBTHelper.putString(new CompoundTag(), "forging", "welding"));
            }
        }));
        FORGING_BUTTONS.forEach((forgeStep, buttonDefinition) -> {
            this.addHideableChild(new Button(new Position(this.x + forgeStep.buttonX() - 9, this.y + forgeStep.buttonY() + 16), buttonDefinition, (b) -> {
                if(b == 0) {
                    this.getContainer().sendDataToServer(() -> NBTHelper.putString(new CompoundTag(), "forging", forgeStep.name().toLowerCase(Locale.ROOT)));
                }
            }));
        });

        this.logicControl = this.addHideableChild(new ForgingUpgradeLogicControl(new Position(this.x + 3, this.y + 24), this.getContainer().getContainer()));
        this.addHideableChild(new Label(new Position(this.x + 20, this.y + 8), Component.translatable("upgrade.tab.forging")));
    }

    @Override
    protected void moveSlotsToTab() {
        this.logicControl.moveSlotsToView(this.screen.getGuiLeft(), this.screen.getGuiTop());
    }
}
