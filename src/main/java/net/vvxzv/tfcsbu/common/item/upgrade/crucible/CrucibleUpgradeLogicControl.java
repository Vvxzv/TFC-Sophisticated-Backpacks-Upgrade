package net.vvxzv.tfcsbu.common.item.upgrade.crucible;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.CompositeWidgetBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.WidgetBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.*;
import net.vvxzv.tfcsbu.TFCSBU;
import net.vvxzv.tfcsbu.client.gui.GuiUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CrucibleUpgradeLogicControl extends CompositeWidgetBase<WidgetBase> {
    private final CrucibleUpgradeLogicContainer container;

    private static final TextureBlitData BACKGROUND = new TextureBlitData(GuiUtils.CRUCIBLE_BACKGROUND, new Dimension(128, 128), new UV(0, 0), new Dimension(128, 91));
    private static final TextureBlitData BELLOWS_BUTTON = new TextureBlitData(GuiUtils.CRUCIBLE_BACKGROUND, new Dimension(128, 128), new UV(0, 112), Dimension.SQUARE_16);

    protected CrucibleUpgradeLogicControl(Position position, CrucibleUpgradeLogicContainer container) {
        super(position, new Dimension(128, 91));
        this.container = container;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, @NotNull Minecraft minecraft, int mouseX, int mouseY) {
        GuiUtils.getLeftBackground(guiGraphics, this.x, this.y, 105, 121);
        GuiHelper.blit(guiGraphics, this.x, this.y, BACKGROUND);
        GuiHelper.blit(guiGraphics, this.x + 106, this.y + 1, BELLOWS_BUTTON);

        CrucibleUpgradeLogic logic = container.getLogicSupplier().get();
        logic.show(guiGraphics, this.x, this.y, mouseX, mouseY, BACKGROUND.getTextureName());
    }

    public void moveSlotsToView(int screenGuiLeft, int screenGuiTop) {
        List<Slot> slots = this.container.getSlots();
        this.positionSlot(slots.get(0), screenGuiLeft, screenGuiTop, 1, 1);

        this.positionSlot(slots.get(1), screenGuiLeft, screenGuiTop, 1, 19);
        this.positionSlot(slots.get(2), screenGuiLeft, screenGuiTop, 1, 37);
        this.positionSlot(slots.get(3), screenGuiLeft, screenGuiTop, 1, 55);
        this.positionSlot(slots.get(4), screenGuiLeft, screenGuiTop, 1, 73);

        this.positionSlot(slots.get(5), screenGuiLeft, screenGuiTop, 42, 1);
        this.positionSlot(slots.get(6), screenGuiLeft, screenGuiTop, 60, 1);
        this.positionSlot(slots.get(7), screenGuiLeft, screenGuiTop, 78, 1);
        this.positionSlot(slots.get(8), screenGuiLeft, screenGuiTop, 42, 19);
        this.positionSlot(slots.get(9), screenGuiLeft, screenGuiTop, 60, 19);
        this.positionSlot(slots.get(10), screenGuiLeft, screenGuiTop, 78, 19);
        this.positionSlot(slots.get(11), screenGuiLeft, screenGuiTop, 42, 37);
        this.positionSlot(slots.get(12), screenGuiLeft, screenGuiTop, 60, 37);
        this.positionSlot(slots.get(13), screenGuiLeft, screenGuiTop, 78, 37);

        this.positionSlot(slots.get(14), screenGuiLeft, screenGuiTop, 111, 66);
    }

    private void positionSlot(Slot slot, int screenGuiLeft, int screenGuiTop, int xOffset, int yOffset) {
        try {
            slot.x = this.x - screenGuiLeft + xOffset;
            slot.y = this.y - screenGuiTop + yOffset;
        } catch (Exception e) {
            TFCSBU.LOGGER.error("Fail to modify slot pos ", e);
        }
    }
}
