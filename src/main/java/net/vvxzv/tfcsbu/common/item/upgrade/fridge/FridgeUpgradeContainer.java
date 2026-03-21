package net.vvxzv.tfcsbu.common.item.upgrade.fridge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;

public class FridgeUpgradeContainer extends UpgradeContainerBase<FridgeUpgradeWrapper, FridgeUpgradeContainer> {
    private final FridgeUpgradeLogicContainer container;

    public FridgeUpgradeContainer(Player player, int upgradeContainerId, FridgeUpgradeWrapper upgradeWrapper, UpgradeContainerType<FridgeUpgradeWrapper, FridgeUpgradeContainer> type) {
        super(player, upgradeContainerId, upgradeWrapper, type);
        Supplier<FridgeUpgradeLogic> logicSupplier = () -> this.upgradeWrapper.getLogic();
        ArrayList<Slot> slots = this.slots;
        Objects.requireNonNull(slots);
        this.container = new FridgeUpgradeLogicContainer(player, logicSupplier, slots::add);
    }

    @Override
    public void handleMessage(CompoundTag data) {
        if(data.contains("fridge_switch")){
            this.container.getLogicSupplier().get().handlePowerSwitch();
        }
    }

    public FridgeUpgradeLogicContainer getContainer() {
        return container;
    }
}
