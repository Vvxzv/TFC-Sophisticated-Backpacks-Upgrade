package net.vvxzv.tfcsbu.common.item.upgrade;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.upgrades.stack.StackUpgradeConversionItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class StackUpgradeConversionExtendedItem extends StackUpgradeConversionItem {
    private final List<Supplier<? extends Item>> sourceUpgrade;

    public StackUpgradeConversionExtendedItem(List<Supplier<? extends Item>> sourceUpgrade, Supplier<? extends Item> targetUpgrade) {
        super(sourceUpgrade.get(0), targetUpgrade);
        this.sourceUpgrade = sourceUpgrade;
    }

    @Override
    public boolean canConvert(@NotNull ItemStack upgradeStack) {
        for (Supplier<? extends Item> supplier : this.sourceUpgrade) {
            if (upgradeStack.is(supplier.get())) {
                return true;
            }
        }
        return false;
    }
}
