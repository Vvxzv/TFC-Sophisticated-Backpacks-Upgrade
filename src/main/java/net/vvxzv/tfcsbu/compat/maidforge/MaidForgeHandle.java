package net.vvxzv.tfcsbu.compat.maidforge;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.item.ItemSmartSlab;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.component.forge.ForgeRule;
import net.dries007.tfc.common.component.forge.ForgeStep;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.vvxzv.maidforge.Config;
import net.vvxzv.maidforge.MaidForge;
import net.vvxzv.maidforge.utils.ForgeUtil;
import net.vvxzv.tfcsbu.common.utils.LogicHelper;

import java.util.List;

public class MaidForgeHandle implements IForge{
    private EntityMaid getMaid(Level level, ItemStack maidStack) {
        EntityMaid maid = InitEntities.MAID.get().create(level);
        if(maidStack.getItem() instanceof ItemSmartSlab) {
            CustomData compoundData = maidStack.get(InitDataComponent.MAID_INFO);
            if(compoundData != null) {
                CompoundTag maidData = compoundData.copyTag();
                if (maid != null) {
                    maid.load(maidData);
                    return maid;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isValidMaid(Level level, ItemStack maidStack) {
        EntityMaid maid = this.getMaid(level, maidStack);
        if(maid != null) {
            boolean isTask = maid.getTask().getUid().equals(LogicHelper.rl(MaidForge.MODID, "anvil_forge_task"));
            boolean isTool = maid.getMainHandItem().is(TFCTags.Items.TOOLS_HAMMER);
            return isTask && isTool;
        }
        return false;
    }

    @Override
    public ItemStack getMaidMainHandItem(Level level, ItemStack maidStack) {
        EntityMaid maid = this.getMaid(level, maidStack);
        if (maid != null) {
            return maid.getMainHandItem();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public long perTicks() {
        return Config.maxDelayTimeToForge;
    }

    @Override
    public ForgeStep[] getAutoLastSteps(List<ForgeRule> rules) {
        return ForgeUtil.AdjustedForgeRule.autoLastSteps(rules);
    }

    @Override
    public int getPerfectForgeFavorability(int stage) {
        return Config.favorabilityToPerfectForge * stage / 4;
    }

    @Override
    public int getFavorability(Level level, ItemStack maidStack) {
        EntityMaid maid = this.getMaid(level, maidStack);
        if (maid != null) {
            return maid.getFavorability();
        }
        return 0;
    }

    @Override
    public ForgeStep findForgeStep(int delta) {
        return ForgeUtil.findForgeStep(delta);
    }
}
