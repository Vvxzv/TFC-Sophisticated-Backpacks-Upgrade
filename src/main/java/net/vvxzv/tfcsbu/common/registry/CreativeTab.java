package net.vvxzv.tfcsbu.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vvxzv.tfcsbu.TFCSophisticatedBackpacksUpgrade;

public class CreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TFCSophisticatedBackpacksUpgrade.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = TABS.register("tab", () -> CreativeModeTab.builder()
            .icon(() -> Items.FRIDGE_UPGRADE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                Items.ITEMS.getEntries().forEach(entries -> {
                    output.accept(entries.get());
                });
            })
            .title(Component.translatable("tfcsbu.tab"))
            .build()
    );
}
