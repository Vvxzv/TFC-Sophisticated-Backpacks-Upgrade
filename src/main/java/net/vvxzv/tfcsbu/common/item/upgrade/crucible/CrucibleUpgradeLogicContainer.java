package net.vvxzv.tfcsbu.common.item.upgrade.crucible;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.IHeat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.common.gui.SlotSuppliedHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CrucibleUpgradeLogicContainer {
    private final List<Slot> slots = new ArrayList<>();

    private final Supplier<CrucibleUpgradeLogic> logicSupplier;
    private final Player player;

    @SuppressWarnings("removal")
    public CrucibleUpgradeLogicContainer(final Player player, final Supplier<CrucibleUpgradeLogic> logicSupplier, Consumer<Slot> addSlot) {
        this.player = player;
        this.logicSupplier = logicSupplier;
        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 0, -100, -100){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(TFCBlocks.BELLOWS.get().asItem());
            }
        });
        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 1, -100, -100){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(TFCTags.Items.FORGE_FUEL);
            }
        });
        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 2, -100, -100){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });
        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 3, -100, -100){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });
        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 4, -100, -100){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });

        for (int i = 5; i < 14; i++) {
            this.addSlot(addSlot, new SlotSuppliedHandler(
                    () -> logicSupplier.get().getInventory(), i, -100, -100
            ) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    IHeat iHeat = HeatCapability.get(stack);
                    return iHeat != null;
                }
            });
        }

        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 14, -100, -100){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ItemTags.create(ResourceLocation.fromNamespaceAndPath("tfc", "fired_molds")));
            }
        });
    }

    private void addSlot(Consumer<Slot> addSlot, Slot slot) {
        addSlot.accept(slot);
        this.slots.add(slot);
    }

    public List<Slot> getSlots() {
        return this.slots;
    }

    public Supplier<CrucibleUpgradeLogic> getLogicSupplier() {
        return logicSupplier;
    }

    public Player getPlayer() {
        return player;
    }
}
