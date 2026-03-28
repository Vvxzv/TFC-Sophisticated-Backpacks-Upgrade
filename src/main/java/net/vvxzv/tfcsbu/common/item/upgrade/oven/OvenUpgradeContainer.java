package net.vvxzv.tfcsbu.common.item.upgrade.oven;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;

public class OvenUpgradeContainer extends UpgradeContainerBase<OvenUpgradeWrapper, OvenUpgradeContainer> {
    private final OvenUpgradeLogicContainer container;

    public OvenUpgradeContainer(Player player, int upgradeContainerId, OvenUpgradeWrapper upgradeWrapper, UpgradeContainerType<OvenUpgradeWrapper, OvenUpgradeContainer> type) {
        super(player, upgradeContainerId, upgradeWrapper, type);
        Supplier<OvenUpgradeLogic> logicSupplier = () -> this.upgradeWrapper.getLogic();
        ArrayList<Slot> slots = this.slots;
        Objects.requireNonNull(slots);
        this.container = new OvenUpgradeLogicContainer(player, logicSupplier, slots::add);
    }

    @Override
    public void handlePacket(CompoundTag compoundTag) {

    }

    public OvenUpgradeLogicContainer getContainer() {
        return container;
    }
}
