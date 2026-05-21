package net.vvxzv.tfcsbu.common.item.upgrade.forging;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.component.TFCComponents;
import net.dries007.tfc.common.component.forge.Forging;
import net.dries007.tfc.common.component.forge.ForgingCapability;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.common.gui.SlotSuppliedHandler;
import net.vvxzv.tfcsbu.common.utils.LogicHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ForgingUpgradeLogicContainer {
    private final List<Slot> slots = new ArrayList<>();

    private final Supplier<ForgingUpgradeLogic> logicSupplier;
    private final Player player;

    public ForgingUpgradeLogicContainer(final Player player, final Supplier<ForgingUpgradeLogic> logicSupplier, Consumer<Slot> addSlot) {
        this.player = player;
        this.logicSupplier = logicSupplier;
        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 0, -100, -100) {
            @Override
            public @NotNull ItemStack remove(int amount) {
                ItemStack stack = this.getItemHandler().getStackInSlot(0);
                Forging forging = ForgingCapability.get(stack);
                if (forging.totalWorked() == 0) {
                    stack.remove(TFCComponents.FORGING);
                }
                ItemStack extracted = super.remove(amount);
                this.setChanged();

                return extracted;
            }

            @Override
            public boolean mayPickup(@NotNull Player playerIn) {
                ItemStack stack = this.getItemHandler().getStackInSlot(0);
                Forging forging = ForgingCapability.get(stack);
                if (forging.totalWorked() == 0) {
                    stack.remove(TFCComponents.FORGING);
                }
                return super.mayPickup(playerIn);
            }
        });
        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 1, -100, -100));
        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 2, -100, -100) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ItemTags.create(LogicHelper.rl("forge_tools"))) || stack.is(TFCTags.Items.TOOLS_HAMMER);
            }
        });
        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 3, -100, -100) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(TFCItems.POWDERS.get(Powder.FLUX).get());
            }
        });
        this.addSlot(addSlot, new SlotSuppliedHandler(() -> logicSupplier.get().getInventory(), 4, -100, -100) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(TFCTags.Items.ANVILS);
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

    public Supplier<ForgingUpgradeLogic> getLogicSupplier() {
        return logicSupplier;
    }

    public Player getPlayer() {
        return player;
    }
}
