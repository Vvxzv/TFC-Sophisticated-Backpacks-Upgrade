package net.vvxzv.tfcsbu.compat.maidforge;

import net.dries007.tfc.common.component.forge.ForgeRule;
import net.dries007.tfc.common.component.forge.ForgeStep;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public interface IForge {

    boolean isValidMaid(Level level, ItemStack maidStack);

    ItemStack getMaidMainHandItem(Level level, ItemStack maidStack);

    long perTicks();

    ForgeStep[] getAutoLastSteps(List<ForgeRule> rules);

    int getPerfectForgeFavorability(int stage);

     int getFavorability(Level level, ItemStack maidStack);

    ForgeStep findForgeStep(int delta);
}
