package net.vvxzv.tfcsbu.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.tfcsbu.TFCSBU;

public class CreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TFCSBU.MODID);

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("tab", () -> CreativeModeTab.builder()
            .icon(() -> Items.STACK_UPGRADE_TIER_RED_STEEL.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                Items.ITEMS.getEntries().forEach(entries -> {
                    output.accept(entries.get());
                });
            })
            .title(Component.translatable("tfcsbu.tab"))
            .build()
    );
}
