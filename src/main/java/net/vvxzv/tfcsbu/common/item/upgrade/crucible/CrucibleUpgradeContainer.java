package net.vvxzv.tfcsbu.common.item.upgrade.crucible;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;

public class CrucibleUpgradeContainer extends UpgradeContainerBase<CrucibleUpgradeWrapper, CrucibleUpgradeContainer> {
    private final CrucibleUpgradeLogicContainer container;

    public CrucibleUpgradeContainer(Player player, int upgradeContainerId, CrucibleUpgradeWrapper upgradeWrapper, UpgradeContainerType<CrucibleUpgradeWrapper, CrucibleUpgradeContainer> type) {
        super(player, upgradeContainerId, upgradeWrapper, type);
        Supplier<CrucibleUpgradeLogic> logicSupplier = () -> this.upgradeWrapper.getLogic();
        ArrayList<Slot> slots = this.slots;
        Objects.requireNonNull(slots);
        this.container = new CrucibleUpgradeLogicContainer(player, logicSupplier, slots::add);
    }

    @Override
    public void handlePacket(CompoundTag data) {
        if(data.contains("crucible")) {
            Player player = this.container.getPlayer();
            switch (data.getString("crucible")) {
                case "bellows":
                    this.container.getLogicSupplier().get().activateBellows(player);
                    break;

                case "power":
                    this.container.getLogicSupplier().get().handlePowerSwitch();
                    break;

                case "lock":
                    this.container.getLogicSupplier().get().poweredMaxTemperature();
                    break;
            }
        }
    }

    public CrucibleUpgradeLogicContainer getContainer() {
        return container;
    }
}
