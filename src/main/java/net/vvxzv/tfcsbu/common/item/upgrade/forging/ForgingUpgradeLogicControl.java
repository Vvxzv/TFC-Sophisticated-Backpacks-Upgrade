package net.vvxzv.tfcsbu.common.item.upgrade.forging;

import com.mojang.blaze3d.systems.RenderSystem;
import net.dries007.tfc.client.ClientHelpers;
import net.dries007.tfc.common.capabilities.forge.*;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.CompositeWidgetBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.WidgetBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.*;
import net.vvxzv.tfcsbu.TFCSophisticatedBackpacksUpgrade;
import net.vvxzv.tfcsbu.client.gui.GuiUtils;
import net.vvxzv.tfcsbu.common.utils.CustomTooltipComponent;
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
        ForgingUpgradeTab.FORGING_ICONS.forEach((s, t) -> {
            GuiHelper.blit(guiGraphics, this.x + s.buttonX() - 12, this.y + s.buttonY() - 8, t);
        });
        //this.container.getLogicSupplier().get().show(guiGraphics, this.x, this.y, mouseX, mouseY);
        this.show(guiGraphics, this.x, this.y, mouseX, mouseY);
    }

    public void show(GuiGraphics guiGraphics, int guiX, int guiY, int mouseX, int mouseY) {
        ForgingUpgradeLogic logic = this.container.getLogicSupplier().get();
        ItemStack stack = logic.getItem();
        Forging forging = ForgingCapability.get(stack);
        if(forging != null) {
            this.showTargetAndWork(forging, guiGraphics, guiX, guiY);

            Level level = Minecraft.getInstance().level;
            if(level != null) {
                AnvilRecipe recipe = forging.getRecipe(level);
                if (recipe != null && recipe.isCorrectTier(logic.getTier())) {
                    this.showRule(recipe, forging, guiGraphics, guiX, guiY, mouseX, mouseY);

                    RegistryAccess access = ClientHelpers.getLevelOrThrow().registryAccess();
                    ItemStack resultItem = recipe.getResultItem(access);
                    guiGraphics.renderItem(resultItem, guiX + 126, guiY + 6);
                    new CustomTooltipComponent(126, 0, 17, 17) {
                        @Override
                        public Component[] getComponents() {
                            return new Component[]{resultItem.getHoverName()};
                        }

                        @Override
                        public boolean hasTooltip() {
                            return true;
                        }
                    }.draw(guiGraphics, guiX, guiY, mouseX, mouseY);
                } else {
                    GuiHelper.blit(guiGraphics, guiX + 126, guiY + 6, new TextureBlitData(GuiUtils.FORGING_BACKGROUND, Dimension.SQUARE_256, new UV(236, 0), Dimension.SQUARE_16));
                }

                ForgeSteps steps = forging.getSteps();
                ForgeStep[] stepSequence = new ForgeStep[]{steps.last(), steps.secondLast(), steps.thirdLast()};
                for(int i = 0; i < 3; ++i) {
                    ForgeStep step = stepSequence[i];
                    if (step != null) {
                        int xOffset = i * 19;
                        guiGraphics.blit(GuiUtils.FORGING_BACKGROUND, guiX + 52 + xOffset, guiY + 23, 10, 10, step.iconX(), step.iconY(), 32, 32, 256, 256);
                    }
                }
            }
        } else {
            GuiHelper.blit(guiGraphics, guiX + 126, guiY + 6, new TextureBlitData(GuiUtils.FORGING_BACKGROUND, Dimension.SQUARE_256, new UV(219, 51), Dimension.SQUARE_16));
        }
    }

    private void showTargetAndWork(Forging forging, GuiGraphics guiGraphics, int guiX, int guiY) {
        int target = forging.getWorkTarget();
        int work = forging.getWork();
        GuiHelper.blit(guiGraphics, target + guiX + 1, guiY + 86, GuiUtils.TARGET);
        GuiHelper.blit(guiGraphics, work + guiX + 1, guiY + 92, GuiUtils.WORK);
    }

    private void showRule(AnvilRecipe recipe, Forging forging, GuiGraphics guiGraphics, int guiX, int guiY, int mouseX, int mouseY) {
        ForgeSteps steps = forging.getSteps();
        ForgeRule[] rules = recipe.getRules();
        for(int i = 0; i < rules.length; ++i) {
            ForgeRule rule = rules[i];
            if (rule != null) {
                int xOffset = i * 19;
                guiGraphics.blit(GuiUtils.FORGING_BACKGROUND, guiX + 52 + xOffset, guiY, 10, 10, rule.iconX(), rule.iconY(), 32, 32, 256, 256);
                if (rule.matches(steps)) {
                    RenderSystem.setShaderColor(0.0F, 0.6F, 0.2F, 1.0F);
                } else {
                    RenderSystem.setShaderColor(1.0F, 0.4F, 0.0F, 1.0F);
                }

                guiGraphics.blit(GuiUtils.FORGING_BACKGROUND, guiX + 47 + xOffset, guiY - 3, 198, rule.overlayY(), 20, 22);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
        for(int i = 0; i < rules.length; ++i) {
            ForgeRule rule = rules[i];
            if (rule != null) {
                int xOffset = i * 19;
                new CustomTooltipComponent(52 + xOffset, 0, 10, 10) {
                    @Override
                    public Component[] getComponents() {
                        return new Component[]{rule.getDescriptionId()};
                    }

                    @Override
                    public boolean hasTooltip() {
                        return true;
                    }
                }.draw(guiGraphics, guiX, guiY, mouseX, mouseY);
            }
        }
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
