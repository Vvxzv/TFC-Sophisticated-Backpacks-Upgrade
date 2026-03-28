package net.vvxzv.tfcsbu.common.utils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinition;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Dimension;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.GuiHelper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.TextureBlitData;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.UV;

public class GuiUtils {
    public static final ButtonDefinition BUTTON_BASE = new ButtonDefinition(Dimension.SQUARE_18, GuiHelper.DEFAULT_BUTTON_BACKGROUND, GuiHelper.DEFAULT_BUTTON_HOVERED_BACKGROUND, null);
    public static final TextureBlitData SWITCH_OFF = new TextureBlitData(rl("textures/gui/switch_off.png"), Dimension.SQUARE_16, new UV(0, 0), Dimension.SQUARE_16);
    public static final TextureBlitData SWITCH_ON = new TextureBlitData(rl("textures/gui/switch_on.png"), Dimension.SQUARE_16, new UV(0, 0), Dimension.SQUARE_16);

    public static final TextureBlitData TEMPERATURE_INDICATOR = new TextureBlitData(rl("textures/gui/temperature_indicator.png"), Dimension.SQUARE_16, new UV(0, 0), new Dimension(13, 3));
    public static final ResourceLocation CRUCIBLE_BACKGROUND = GuiUtils.rl("textures/gui/crucible_background.png");
    public static final ResourceLocation FORGING_BACKGROUND = GuiUtils.rl("textures/gui/forging_background.png");

    public static ResourceLocation rl(String s) {
        if(s.contains(":")) {
            String[] strings = s.split(":");
            return ResourceLocation.fromNamespaceAndPath(strings[0], strings[1]);
        }
        return ResourceLocation.fromNamespaceAndPath("tfcsbu", s);
    }

    public static ResourceLocation rl(String nameSpace, String path) {
        return rl(nameSpace + ":" + path);
    }

    public static void getLeftBackground(GuiGraphics guiGraphics, int guiX, int guiY, int width, int height) {
        TextureBlitData leftTopBorder = getBackgroundTexture(new UV(128, 0), new Dimension(4, height/2));
        TextureBlitData leftBottomBorder = getBackgroundTexture(new UV(128,  256 - (int)Math.ceil((double) height / 2)), new Dimension(4, (int)Math.ceil((double) height / 2)));
        TextureBlitData middleTop = getBackgroundTexture(new UV(132, 0), new Dimension(width - 4, height / 2));
        TextureBlitData middleBottom = getBackgroundTexture(new UV(132, 256 - (int)Math.ceil((double) height / 2)), new Dimension(width - 4, (int)Math.ceil((double) height / 2)));
        GuiHelper.blit(guiGraphics, guiX - 6, guiY - 24, leftTopBorder);
        GuiHelper.blit(guiGraphics, guiX - 6, guiY - 24 + height/2, leftBottomBorder);
        GuiHelper.blit(guiGraphics, guiX - 2, guiY - 24, middleTop);
        GuiHelper.blit(guiGraphics, guiX - 2, guiY - 24 + height/2, middleBottom);
    }

    private static TextureBlitData getBackgroundTexture(UV uv, Dimension dimension) {
        return new TextureBlitData(GuiHelper.GUI_CONTROLS, new Dimension(256, 256), uv, dimension);
    }
}
