package net.vvxzv.tfcsbu.common.item.upgrade.fridge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.CompositeWidgetBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.WidgetBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.*;
import net.vvxzv.tfcsbu.TFCSBU;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FridgeUpgradeLogicControl extends CompositeWidgetBase<WidgetBase> {
    private final FridgeUpgradeLogicContainer container;
    private static final TextureBlitData SLOTS_BACKGROUND = new TextureBlitData(GuiHelper.SLOTS_BACKGROUND, Dimension.SQUARE_256, new UV(0, 0), new Dimension(54, 54));

    protected FridgeUpgradeLogicControl(Position position, FridgeUpgradeLogicContainer upgradeLogicContainer) {
        super(position,  new Dimension(75, 65));
        this.container = upgradeLogicContainer;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, @NotNull Minecraft minecraft, int mouseX, int mouseY) {
        GuiHelper.blit(guiGraphics, this.x, this.y, SLOTS_BACKGROUND);
        this.container.getLogicSupplier().get().showPowerSwitchTooltip(guiGraphics, this.x -3, this.y, mouseX, mouseY);
    }

    public void moveSlotsToView(int screenGuiLeft, int screenGuiTop) {
        List<Slot> slots = this.container.getSlots();

        int[][] offsets = {
                {1, 1}, {19, 1}, {37, 1},
                {1, 19}, {19, 19}, {37, 19},
                {1, 37}, {19, 37}, {37, 37}
        };

        for (int i = 0; i < slots.size() && i < offsets.length; i++) {
            this.positionSlot(slots.get(i), screenGuiLeft, screenGuiTop, offsets[i][0], offsets[i][1]);
        }
    }

    private void positionSlot(Slot slot, int screenGuiLeft, int screenGuiTop, int xOffset, int yOffset) {
        try {
            slot.x = this.x - screenGuiLeft + xOffset;
            slot.y = this.y - screenGuiTop + yOffset;
        } catch (Exception e) {
            TFCSBU.LOGGER.error("修改Slot坐标失败", e);
        }
    }
}
