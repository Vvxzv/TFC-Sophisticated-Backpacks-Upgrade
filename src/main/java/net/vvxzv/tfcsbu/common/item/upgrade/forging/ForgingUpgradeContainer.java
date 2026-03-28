package net.vvxzv.tfcsbu.common.item.upgrade.forging;

import net.dries007.tfc.common.component.forge.ForgeStep;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;

public class ForgingUpgradeContainer extends UpgradeContainerBase<ForgingUpgradeWrapper, ForgingUpgradeContainer> {
    private final ForgingUpgradeLogicContainer container;

    public ForgingUpgradeContainer(Player player, int upgradeContainerId, ForgingUpgradeWrapper upgradeWrapper, UpgradeContainerType<ForgingUpgradeWrapper, ForgingUpgradeContainer> type) {
        super(player, upgradeContainerId, upgradeWrapper, type);
        Supplier<ForgingUpgradeLogic> logicSupplier = () -> this.upgradeWrapper.getLogic();
        ArrayList<Slot> slots = this.slots;
        Objects.requireNonNull(slots);
        this.container = new ForgingUpgradeLogicContainer(player, logicSupplier, slots::add);
    }

    @Override
    public void handlePacket(CompoundTag data) {
        if(data.contains("forging")) {
            ServerPlayer player = (ServerPlayer) this.container.getPlayer();
            switch (data.getString("forging")) {
                case "welding":
                    this.container.getLogicSupplier().get().welding(player);
                    break;

                case "hit_light":
                    this.container.getLogicSupplier().get().work(player, ForgeStep.HIT_LIGHT);
                    break;

                case "hit_medium":
                    this.container.getLogicSupplier().get().work(player, ForgeStep.HIT_MEDIUM);
                    break;

                case "hit_hard":
                    this.container.getLogicSupplier().get().work(player, ForgeStep.HIT_HARD);
                    break;

                case "draw":
                    this.container.getLogicSupplier().get().work(player, ForgeStep.DRAW);
                    break;

                case "punch":
                    this.container.getLogicSupplier().get().work(player, ForgeStep.PUNCH);
                    break;

                case "bend":
                    this.container.getLogicSupplier().get().work(player, ForgeStep.BEND);
                    break;

                case "upset":
                    this.container.getLogicSupplier().get().work(player, ForgeStep.UPSET);
                    break;

                case "shrink":
                    this.container.getLogicSupplier().get().work(player, ForgeStep.SHRINK);
                    break;

                case "recipe":
                    this.container.getLogicSupplier().get().selectRecipe();
                    break;
            }
        }

    }

    public ForgingUpgradeLogicContainer getContainer() {
        return container;
    }
}
