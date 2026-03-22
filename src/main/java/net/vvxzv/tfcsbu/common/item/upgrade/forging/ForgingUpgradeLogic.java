package net.vvxzv.tfcsbu.common.item.upgrade.forging;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import net.dries007.tfc.client.ClientHelpers;
import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.devices.AnvilBlock;
import net.dries007.tfc.common.capabilities.forge.*;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.IHeat;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.dries007.tfc.common.recipes.TFCRecipeTypes;
import net.dries007.tfc.common.recipes.WeldingRecipe;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.advancements.TFCAdvancements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.ItemStackHandler;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.*;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import net.vvxzv.tfcsbu.common.utils.CustomTooltipComponent;
import net.vvxzv.tfcsbu.common.utils.GuiUtils;
import net.vvxzv.tfcsbu.compat.maidforge.IForge;
import net.vvxzv.tfcsbu.compat.maidforge.MaidForgeHandle;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ForgingUpgradeLogic implements WeldingRecipe.Inventory, AnvilRecipe.Inventory{
    private final ItemStack upgrade;
    private final Consumer<ItemStack> saveHandler;
    @Nullable
    private ItemStackHandler inventory;
    private Level level;
    private List<AnvilRecipe> recipes;


    private static final TextureBlitData TARGET = new TextureBlitData(GuiUtils.FORGING_BACKGROUND, Dimension.SQUARE_256, new UV(181, 0), new Dimension(5, 5));
    private static final TextureBlitData WORK = new TextureBlitData(GuiUtils.FORGING_BACKGROUND, Dimension.SQUARE_256, new UV(176, 0), new Dimension(5, 5));

    private static final IForge MAID_FORGE = ModList.get().isLoaded("maidforge")? new MaidForgeHandle(): null;
    private static final UUID uuid = UUID.randomUUID();

    public ForgingUpgradeLogic(ItemStack upgrade, Consumer<ItemStack> saveHandler) {
        this.upgrade = upgrade;
        this.saveHandler = saveHandler;
    }

    public ItemStackHandler getInventory() {
        if (this.inventory == null) {
            this.inventory = new ItemStackHandler(5) {
                @Override
                public int getSlotLimit(int slot) {
                    if(slot == 3) {
                        return 64;
                    }
                    return 1;
                }

                @Override
                protected void onContentsChanged(int slot) {
                    super.onContentsChanged(slot);
                    upgrade.addTagElement("forging_inventory", this.serializeNBT());
                    save();
                }
            };
            NBTHelper.getCompound(upgrade, "forging_inventory").ifPresent(inventory::deserializeNBT);
        }

        return this.inventory;
    }

    public void show(GuiGraphics guiGraphics, int guiX, int guiY, int mouseX, int mouseY) {
        ItemStack stack = this.getItem();
        Forging forging = ForgingCapability.get(stack);
        if(forging != null) {
            int target = forging.getWorkTarget();
            int work = forging.getWork();
            GuiHelper.blit(guiGraphics, target + guiX - 1, guiY + 86, TARGET);
            GuiHelper.blit(guiGraphics, work + guiX - 1, guiY + 92, WORK);

            ForgeSteps steps = forging.getSteps();
            Level level = Minecraft.getInstance().level;
            if(level != null) {
                AnvilRecipe recipe = forging.getRecipe(level);
                if (recipe != null && recipe.isCorrectTier(this.getTier())) {
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
            GuiHelper.blit(guiGraphics, guiX + 126, guiY + 5, new TextureBlitData(GuiUtils.FORGING_BACKGROUND, Dimension.SQUARE_256, new UV(219, 51), Dimension.SQUARE_16));
        }
    }

    public void tick(Level level, Entity entity) {
        this.level = level;
        ItemStack stack = this.getItem();
        if(!stack.isEmpty()) {
            this.recipes = AnvilRecipe.getAll(level, stack, this.getTier());
            if(recipes.size() == 1) {
                this.chooseRecipe(recipes.get(0));
            }
        }

        if(MAID_FORGE != null) {
            ItemStack maidStack = getInventory().getStackInSlot(2);
            if(MAID_FORGE.isValidMaid(level, maidStack)) {
                if(level.getGameTime() % (MAID_FORGE.perTicks() * 2) == 0L) {
                    ItemStack hammer = MAID_FORGE.getMaidMainHandItem(level, maidStack);
                    if(hammer.getDamageValue() != hammer.getMaxDamage() -1) {
                        FakePlayer player = new FakePlayer((ServerLevel) level, new GameProfile(uuid, "Maid"));
                        player.setPos(entity.position());
                        player.setItemInHand(InteractionHand.MAIN_HAND, hammer);
                        Forging forging = ForgingCapability.get(stack);
                        if(forging != null && forging.getWorkTarget() != 0) {
                            AnvilRecipe recipe = forging.getRecipe(level);
                            if(recipe != null){
                                int currentWork = forging.getWork();
                                int targetWork = forging.getWorkTarget();
                                ForgeStep[] lastSteps = MAID_FORGE.getAutoLastSteps(recipe.getRules());
                                int last = this.getStepValue(lastSteps, 0);
                                int secondLast = this.getStepValue(lastSteps, 1);
                                int thirdLast = this.getStepValue(lastSteps, 2);
                                int delta = targetWork - last - secondLast - thirdLast - currentWork;
                                if (delta == 0) {
                                    this.handleAlign(player, lastSteps, maidStack);
                                } else {
                                    work(player, MAID_FORGE.findForgeStep(delta));
                                }
                            }
                        }
                        player.kill();
                    }
                }
            }
        }
    }

    private int getStepValue(ForgeStep[] lastSteps, int index) {
        return index < lastSteps.length && lastSteps[index] != null ? lastSteps[index].step() : 0;
    }

    private void handleAlign(ServerPlayer player, ForgeStep[] lastSteps, ItemStack maidStack) {
        int favorability = MAID_FORGE.getFavorability(player.level(), maidStack);
        double randomNum = Math.random();
        if (favorability < MAID_FORGE.getPerfectForgeFavorability(4) && randomNum < 0.2) {
            work(player, ForgeStep.DRAW);
        } else if (favorability < MAID_FORGE.getPerfectForgeFavorability(3) && randomNum < 0.4) {
            work(player, ForgeStep.HIT_HARD);
        } else if (favorability < MAID_FORGE.getPerfectForgeFavorability(2) && randomNum < 0.6) {
            work(player, ForgeStep.HIT_MEDIUM);
        } else if (favorability < MAID_FORGE.getPerfectForgeFavorability(1) && randomNum < 0.8) {
            work(player, ForgeStep.HIT_LIGHT);
        } else {
            if (this.getStepValue(lastSteps, 2) != 0) {
                work(player, lastSteps[2]);
            }

            if (this.getStepValue(lastSteps, 1) != 0) {
                work(player, lastSteps[1]);
            }

            if (this.getStepValue(lastSteps, 0) != 0) {
                work(player, lastSteps[0]);
            }
        }

    }

    @Override
    public ItemStack getLeft() {
        return this.getItem();
    }

    @Override
    public ItemStack getRight() {
        return this.getInventory().getStackInSlot(1);
    }

    @Override
    public ItemStack getItem() {
        return this.getInventory().getStackInSlot(0);
    }

    @Override
    public int getTier() {
        ItemStack base = this.getInventory().getStackInSlot(4);
        if(base.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AnvilBlock anvilBlock) {
            return anvilBlock.getTier();
        }
        return 1;
    }

    @Override
    public long getSeed() {
        Helpers.warnWhenCalledFromClientThread();
        long speed;
        if (this.level instanceof ServerLevel serverLevel) {
            speed = serverLevel.getSeed();
        } else {
            speed = 0L;
        }

        return speed;
    }

    public void work(ServerPlayer player, ForgeStep step) {
        if(getInventory() != null) {
            ItemStackHandler handler = getInventory();
            ItemStack stack = handler.getStackInSlot(0);
            Forging forge = ForgingCapability.get(stack);
            if(forge != null) {
                ItemStack hammer = handler.getStackInSlot(2);
                ItemStack handItem = player.getMainHandItem();
                if(handItem.is(TFCTags.Items.HAMMERS)) {
                    hammer = handItem;
                }
                if(!hammer.isEmpty()) {
                    if(!(!forge.getSteps().any() && forge.getWork() == 0 && step.step() < 0)) {
                        AnvilRecipe recipe = forge.getRecipe(this.level);
                        if(recipe != null && recipe.isCorrectTier(this.getTier())) {
                            IHeat heat = HeatCapability.get(stack);
                            if (heat != null && !heat.canWork()) {
                                return;
                            }
                            forge.addStep(step);
                            hammer.hurtAndBreak(1, player, (p) -> {});
                            this.level.playSound(null, player.getOnPos(), TFCSounds.ANVIL_HIT.get(), SoundSource.PLAYERS, 0.4F, 1.0F);
                            if (forge.getWork() < 0 || forge.getWork() > 150) {
                                handler.setStackInSlot(0, ItemStack.EMPTY);
                                this.level.playSound(null, player.getOnPos(), SoundEvents.ANVIL_DESTROY, SoundSource.PLAYERS, 0.4F, 1.0F);
                                return;
                            }

                            if (recipe.checkComplete(this)) {
                                ItemStack outputStack = recipe.assemble(this, this.level.registryAccess());
                                IHeat outputHeat = HeatCapability.get(outputStack);
                                if (outputHeat != null) {
                                    outputHeat.setTemperatureIfWarmer(heat);
                                }

                                if (recipe.shouldApplyForgingBonus()) {
                                    float ratio = (float)forge.getSteps().total() / (float) ForgeRule.calculateOptimalStepsToTarget(recipe.computeTarget(this), recipe.getRules());
                                    ForgingBonus bonus = ForgingBonus.byRatio(ratio);
                                    ForgingBonus.set(outputStack, bonus);
                                    if (bonus == ForgingBonus.PERFECTLY_FORGED) {
                                        TFCAdvancements.PERFECTLY_FORGED.trigger(player);
                                    }
                                }

                                handler.setStackInSlot(0, outputStack);
                            }
                        }
                    }
                }
            }
        }
    }

    public void welding(ServerPlayer player) {
        if (getInventory() != null) {
            ItemStackHandler handler = getInventory();
            ItemStack base = handler.getStackInSlot(0);
            ItemStack add = handler.getStackInSlot(1);
            ItemStack hammer = handler.getStackInSlot(2);
            if(!base.isEmpty() && !add.isEmpty() && !hammer.isEmpty()) {
                WeldingRecipe recipe = this.level.getRecipeManager().getRecipeFor(TFCRecipeTypes.WELDING.get(), this, this.level).orElse(null);
                if (recipe != null) {
                    if (recipe.isCorrectTier(this.getTier())) {
                        IHeat baseHeat = HeatCapability.get(base);
                        IHeat addHeat = HeatCapability.get(add);
                        if ((baseHeat == null || baseHeat.canWeld()) && (addHeat == null || addHeat.canWeld())) {
                            if (!handler.getStackInSlot(3).isEmpty()) {
                                ItemStack result = recipe.assemble(this, this.level.registryAccess());
                                IHeat resultHeat = HeatCapability.get(result);
                                handler.getStackInSlot(0).shrink(1);
                                handler.getStackInSlot(1).shrink(1);
                                handler.getStackInSlot(3).shrink(1);
                                if (handler.getStackInSlot(0).isEmpty()) {
                                    handler.setStackInSlot(0, result);
                                }

                                handler.setStackInSlot(0, result);
                                hammer.hurtAndBreak(1, player, (p) -> {});

                                if (resultHeat != null) {
                                    resultHeat.setTemperatureIfWarmer(baseHeat);
                                    resultHeat.setTemperatureIfWarmer(addHeat);
                                }

                                this.level.playSound(null, player.getOnPos(), SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 0.4F, 1.0F);
                            }
                        }
                    }
                }
            }
        }
    }

    public void selectRecipe() {
        ItemStack stack = this.getItem();
        if(stack.isEmpty()) {
            return;
        }

        if (recipes.isEmpty()) {
            return;
        }

        Forging forging = ForgingCapability.get(stack);
        int recipeNum = -1;
        if(forging != null) {
            for (int i = 0; i < recipes.size(); i++) {
                if(recipes.get(i).equals(forging.getRecipe(this.level))){
                    recipeNum = i;
                }
            }
        }
        int newRecipeNum = recipeNum + 1;
        if(newRecipeNum >= recipes.size()) {
            newRecipeNum = 0;
        }
        AnvilRecipe recipe = recipes.get(newRecipeNum);
        this.chooseRecipe(recipe);
    }

    public void chooseRecipe(AnvilRecipe recipe) {
        ItemStackHandler handler = getInventory();
        ItemStack stack = this.getItem();
        if (!stack.isEmpty()) {
            Forging forge = ForgingCapability.get(stack);
            if (forge != null) {
                if (stack.getCount() != 1) {
                    ItemStack overflow = stack.split(stack.getCount() - 1);
                    if (handler.getStackInSlot(1).isEmpty()) {
                        handler.setStackInSlot(1, overflow);
                    }
                }

                forge.setRecipe(recipe, this);
            }
        }
    }

    private void save() {
        this.saveHandler.accept(this.upgrade);
    }
}
