package net.vvxzv.tfcsbu.common.item.upgrade.forging;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.CompositeWidgetBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.WidgetBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.*;
import net.vvxzv.tfcsbu.TFCSophisticatedBackpacksUpgrade;
import net.vvxzv.tfcsbu.client.gui.GuiUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ForgingUpgradeLogicControl extends CompositeWidgetBase<WidgetBase> {
    private final ForgingUpgradeLogicContainer container;

    private static final TextureBlitData FORGING_BACKGROUND = new TextureBlitData(GuiUtils.FORGING_BACKGROUND, new Dimension(256, 256), new UV(0, 0), new Dimension(152, 95));

    protected ForgingUpgradeLogicControl(Position position, ForgingUpgradeLogicContainer container) {
        super(position, new Dimension(152, 95));
        this.container = container;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, @NotNull Minecraft minecraft, int mouseX, int mouseY) {
        GuiUtils.getLeftBackground(guiGraphics, this.x, this.y, 40, 125);
        GuiHelper.blit(guiGraphics, this.x, this.y, FORGING_BACKGROUND);
        this.container.getLogicSupplier().get().show(guiGraphics, this.x, this.y, mouseX, mouseY);
    }

    public void moveSlotsToView(int screenGuiLeft, int screenGuiTop) {
        List<Slot> slots = this.container.getSlots();
        this.positionSlot(slots.get(0), screenGuiLeft, screenGuiTop, 12, 60);
        this.positionSlot(slots.get(1), screenGuiLeft, screenGuiTop, 12, 24);
        this.positionSlot(slots.get(2), screenGuiLeft, screenGuiTop, 126, 60);
        this.positionSlot(slots.get(3), screenGuiLeft, screenGuiTop, 12, 42);
        this.positionSlot(slots.get(4), screenGuiLeft, screenGuiTop, 12, 6);
    }

    private void positionSlot(Slot slot, int screenGuiLeft, int screenGuiTop, int xOffset, int yOffset) {
        try {
            slot.x = this.x - screenGuiLeft + xOffset;
            slot.y = this.y - screenGuiTop + yOffset;
        } catch (Exception e) {
            TFCSophisticatedBackpacksUpgrade.LOGGER.error("Fail to modify slot pos ", e);
        }
    }
}
