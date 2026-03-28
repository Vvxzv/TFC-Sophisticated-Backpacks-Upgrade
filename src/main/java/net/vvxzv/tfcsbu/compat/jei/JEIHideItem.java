package net.vvxzv.tfcsbu.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems;
import net.vvxzv.tfcsbu.TFCSBU;
import net.vvxzv.tfcsbu.common.utils.JEIUtil;

import java.util.List;

@JeiPlugin
public class JEIHideItem implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(TFCSBU.MODID, "hide_item");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime){
        JEIUtil util = new JEIUtil(jeiRuntime);
        util.removeItemStacks(List.of(
                ModItems.STACK_UPGRADE_STARTER_TIER.get(),
                ModItems.STACK_UPGRADE_TIER_1.get(),
                ModItems.STACK_UPGRADE_TIER_2.get(),
                ModItems.STACK_UPGRADE_TIER_3.get(),
                ModItems.STACK_UPGRADE_TIER_4.get(),
                ModItems.IRON_BACKPACK.get(),
                ModItems.GOLD_BACKPACK.get(),
                ModItems.DIAMOND_BACKPACK.get(),
                ModItems.NETHERITE_BACKPACK.get()
        ));
    }

}
