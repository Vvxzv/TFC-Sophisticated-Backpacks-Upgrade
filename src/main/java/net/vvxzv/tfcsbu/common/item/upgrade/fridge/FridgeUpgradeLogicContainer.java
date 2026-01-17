package net.vvxzv.tfcsbu.common.item.upgrade.fridge;

import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.size.IItemSize;
import net.dries007.tfc.common.capabilities.size.ItemSize;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.common.capabilities.size.Size;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedcore.common.gui.SlotSuppliedHandler;
import net.vvxzv.tfcsbu.common.UFoodTrait;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class FridgeUpgradeLogicContainer {
    private final List<Slot> slots = new ArrayList<>();

    private final Supplier<FridgeUpgradeLogic> logicSupplier;
    private final Player player;

    public FridgeUpgradeLogicContainer(final Player player, final Supplier<FridgeUpgradeLogic> logicSupplier, Consumer<Slot> addSlot) {
        this.player = player;
        this.logicSupplier = logicSupplier;
        for (int i = 0; i < 9; i++) {
            int slotIndex = i;
            this.addSlot(addSlot, new SlotSuppliedHandler(
                    () -> logicSupplier.get().getInventory(), slotIndex, -100, -100
            ) {
                @Override
                public @NotNull ItemStack remove(int amount) {
                    IItemHandler handler = this.getItemHandler();
                    ItemStack originalStack = handler.getStackInSlot(slotIndex);
                    if (!originalStack.isEmpty()) {
                        FoodCapability.removeTrait(originalStack, UFoodTrait.FRIDGE_PRESERVED);
                    }
                    ItemStack extracted = super.remove(amount);
                    this.setChanged();

                    return extracted;
                }

                @Override
                public boolean mayPlace(ItemStack stack) {
                    IItemSize itemSize = ItemSizeManager.get(stack);
                    return itemSize.getSize(stack).isSmallerThan(Size.LARGE);
                }

                @Override
                public void set(ItemStack stack) {
                    super.set(stack);
                    if (!stack.isEmpty()) {
                        FoodCapability.applyTrait(stack, UFoodTrait.FRIDGE_PRESERVED);
                        this.setChanged();
                    }
                }

                @Override
                public boolean mayPickup(Player playerIn) {
                    ItemStack stack = this.getItem();
                    if (!stack.isEmpty()) {
                        FoodCapability.removeTrait(stack, UFoodTrait.FRIDGE_PRESERVED);
                    }
                    return super.mayPickup(playerIn);
                }
            });
        }
    }


    private void addSlot(Consumer<Slot> addSlot, Slot slot) {
        addSlot.accept(slot);
        this.slots.add(slot);
    }

    public List<Slot> getSlots() {
        return this.slots;
    }

    public Supplier<FridgeUpgradeLogic> getLogicSupplier() {
        return logicSupplier;
    }

    public Player getPlayer() {
        return player;
    }
}
