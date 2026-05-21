package net.vvxzv.tfcsbu.common.item.upgrade.oven;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.capabilities.size.IItemSize;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.common.capabilities.size.Size;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.common.gui.SlotSuppliedHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class OvenUpgradeLogicContainer {
    private final List<Slot> slots = new ArrayList<>();

    private final Supplier<OvenUpgradeLogic> logicSupplier;
    private final Player player;

    public  OvenUpgradeLogicContainer(final Player player, final Supplier<OvenUpgradeLogic> logicSupplier, Consumer<Slot> addSlot){
        this.player = player;
        this.logicSupplier = logicSupplier;
        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 0, -100, -100){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(TFCTags.Items.FIREPIT_FUEL);
            }
        });
        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 1, -100, -100){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
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
                IItemSize itemSize = ItemSizeManager.get(stack);
                return itemSize.getSize(stack).isSmallerThan(Size.LARGE);
            }
        });
        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 5, -100, -100){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                IItemSize itemSize = ItemSizeManager.get(stack);
                return itemSize.getSize(stack).isSmallerThan(Size.LARGE);
            }
        });
        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 6, -100, -100){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                IItemSize itemSize = ItemSizeManager.get(stack);
                return itemSize.getSize(stack).isSmallerThan(Size.LARGE);
            }
        });
        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 7, -100, -100){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                IItemSize itemSize = ItemSizeManager.get(stack);
                return itemSize.getSize(stack).isSmallerThan(Size.LARGE);
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

    public Supplier<OvenUpgradeLogic> getLogicSupplier() {
        return logicSupplier;
    }

    public Player getPlayer() {
        return player;
    }
}
