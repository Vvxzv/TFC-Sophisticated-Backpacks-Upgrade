package net.vvxzv.tfcsbu.common.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.vvxzv.tfcsbu.TFCSBU;

public class UCreativeTAB {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TFCSBU.MODID);

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("tab", () -> CreativeModeTab.builder()
            .icon(() -> UItem.FRIDGE_UPGRADE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                UItem.ITEMS.getEntries().forEach(entries -> {
                    output.accept(entries.get());
                });
            })
            .title(Component.translatable("tfcsbu.tab"))
            .build()
    );
}
