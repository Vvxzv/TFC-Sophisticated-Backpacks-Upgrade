package net.vvxzv.tfcsbu.common.item.upgrade.oven;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.CompositeWidgetBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.WidgetBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.*;
import net.vvxzv.tfcsbu.TFCSophisticatedBackpacksUpgrade;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OvenUpgradeLogicControl extends CompositeWidgetBase<WidgetBase> {
    private final OvenUpgradeLogicContainer container;

    @SuppressWarnings("removal")
    private static final TextureBlitData OVEN_BACKGROUND = new TextureBlitData(new ResourceLocation("tfcsbu:textures/gui/oven_background.png"), new Dimension(128, 128), new UV(0, 0), new Dimension(80, 80));

    protected OvenUpgradeLogicControl(Position position, OvenUpgradeLogicContainer container) {
        super(position, new Dimension(80, 80));
        this.container = container;
    }


    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, @NotNull Minecraft minecraft, int mouseX, int mouseY) {
        GuiHelper.blit(guiGraphics, this.x, this.y, OVEN_BACKGROUND);
        container.getLogicSupplier().get().showTemperature(guiGraphics, this.x, this.y, mouseX, mouseY);
    }

    public void moveSlotsToView(int screenGuiLeft, int screenGuiTop) {
        List<Slot> slots = this.container.getSlots();
        this.positionSlot(slots.get(0), screenGuiLeft, screenGuiTop, 1, 2);
        this.positionSlot(slots.get(1), screenGuiLeft, screenGuiTop, 1, 20);
        this.positionSlot(slots.get(2), screenGuiLeft, screenGuiTop, 1, 38);
        this.positionSlot(slots.get(3), screenGuiLeft, screenGuiTop, 1, 56);
        this.positionSlot(slots.get(4), screenGuiLeft, screenGuiTop, 44, 20);
        this.positionSlot(slots.get(5), screenGuiLeft, screenGuiTop, 62, 20);
        this.positionSlot(slots.get(6), screenGuiLeft, screenGuiTop, 44, 38);
        this.positionSlot(slots.get(7), screenGuiLeft, screenGuiTop, 62, 38);
    }

    private void positionSlot(Slot slot, int screenGuiLeft, int screenGuiTop, int xOffset, int yOffset) {
        try {
            slot.x = this.x - screenGuiLeft + xOffset;
            slot.y = this.y - screenGuiTop + yOffset;
        } catch (Exception e) {
            TFCSophisticatedBackpacksUpgrade.LOGGER.error("修改Slot坐标失败", e);
        }
    }
}
