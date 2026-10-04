package net.vvxzv.tfcsbu;

import net.dries007.tfc.common.effect.TFCEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.vvxzv.tfcsbu.common.registry.DataComponent;
import net.vvxzv.tfcsbu.common.utils.ItemTagKeys;
import net.vvxzv.tfcsbu.compat.curios.CuriosHelper;

import java.util.UUID;

public class NeoForgeEventHandler {

    public static void init() {
        IEventBus bus = NeoForge.EVENT_BUS;
        bus.addListener(NeoForgeEventHandler::vesselUUID);
        bus.addListener(NeoForgeEventHandler::backpackOverweight);
    }

    public static void vesselUUID(PlayerTickEvent.Pre event) {
        if(event.getEntity() instanceof ServerPlayer player) {
            if(player.level().getGameTime() % 20 == 0) {
                AbstractContainerMenu menu = player.containerMenu;
                menu.slots.forEach(slot -> {
                    ItemStack stack = slot.getItem();
                    int count = stack.getCount();
                    if(stack.is(ItemTagKeys.FIRED_VESSELS) && count == 1) {
                        CustomData data = stack.getOrDefault(DataComponent.TAG, CustomData.of(new CompoundTag()));
                        CompoundTag tag = data.copyTag();
                        if(!tag.contains("uuid")) {
                            tag.putUUID("uuid", UUID.randomUUID());
                            stack.set(DataComponent.TAG, CustomData.of(tag));
                        }
                    }
                });
            }
        }
    }

    public static void backpackOverweight(PlayerTickEvent.Pre event) {
        if(!Config.backpackOverweight) {
            return;
        }

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        if (player.level().getGameTime() % 20 != 0) {
            return;
        }

        int equippedCount = 0;

        if (ModList.get().isLoaded("curios")) {
            if(CuriosHelper.hasCurio(player, item -> item.getItem() instanceof BackpackItem)) {
                equippedCount++;
            }
        }

        if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof BackpackItem) {
            equippedCount++;
        }

        int carriedCount = 0;
        for (ItemStack item : player.getInventory().items) {
            if (item.getItem() instanceof BackpackItem) {
                carriedCount++;
            }
        }

        int total = equippedCount + carriedCount;

        if (total < 2) {
            return;
        }

        if (total == 2 && equippedCount > 0) {
            player.addEffect(new MobEffectInstance(TFCEffects.EXHAUSTED.holder(), 40, 1, true, false));
        } else {
            player.addEffect(new MobEffectInstance(TFCEffects.OVERBURDENED.holder(), 40, 0, true, false));
        }
    }
}
