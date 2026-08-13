package net.vvxzv.tfcsbu.common.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.items.wrapper.EmptyItemHandler;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.p3pp3rf1y.sophisticatedbackpacks.Config;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeGuiManager;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerRegistry;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.stack.StackUpgradeConversionItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.stack.StackUpgradeItem;
import net.vvxzv.tfcsbu.TFCSophisticatedBackpacksUpgrade;
import net.vvxzv.tfcsbu.common.item.upgrade.StackUpgradeConversionExtendedItem;
import net.vvxzv.tfcsbu.common.item.upgrade.crucible.CrucibleUpgradeContainer;
import net.vvxzv.tfcsbu.common.item.upgrade.crucible.CrucibleUpgradeItem;
import net.vvxzv.tfcsbu.common.item.upgrade.crucible.CrucibleUpgradeTab;
import net.vvxzv.tfcsbu.common.item.upgrade.crucible.CrucibleUpgradeWrapper;
import net.vvxzv.tfcsbu.common.item.upgrade.forging.ForgingUpgradeContainer;
import net.vvxzv.tfcsbu.common.item.upgrade.forging.ForgingUpgradeItem;
import net.vvxzv.tfcsbu.common.item.upgrade.forging.ForgingUpgradeTab;
import net.vvxzv.tfcsbu.common.item.upgrade.forging.ForgingUpgradeWrapper;
import net.vvxzv.tfcsbu.common.item.upgrade.fridge.FridgeUpgradeContainer;
import net.vvxzv.tfcsbu.common.item.upgrade.fridge.FridgeUpgradeItem;
import net.vvxzv.tfcsbu.common.item.upgrade.fridge.FridgeUpgradeTab;
import net.vvxzv.tfcsbu.common.item.upgrade.fridge.FridgeUpgradeWrapper;
import net.vvxzv.tfcsbu.common.item.upgrade.oven.OvenUpgradeContainer;
import net.vvxzv.tfcsbu.common.item.upgrade.oven.OvenUpgradeItem;
import net.vvxzv.tfcsbu.common.item.upgrade.oven.OvenUpgradeTab;
import net.vvxzv.tfcsbu.common.item.upgrade.oven.OvenUpgradeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public class Items {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TFCSophisticatedBackpacksUpgrade.MODID);

    public static final DeferredItem<BackpackItem> BISMUTH_BRONZE_BACKPACK = ITEMS.register("bismuth_bronze_backpack", () -> new BackpackItem(Config.SERVER.ironBackpack.inventorySlotCount::get, Config.SERVER.ironBackpack.upgradeSlotCount::get, Blocks.BISMUTH_BRONZE_BACKPACK));

    public static final DeferredItem<BackpackItem> BRONZE_BACKPACK = ITEMS.register("bronze_backpack", () -> new BackpackItem(Config.SERVER.ironBackpack.inventorySlotCount::get, Config.SERVER.ironBackpack.upgradeSlotCount::get, Blocks.BRONZE_BACKPACK));

    public static final DeferredItem<BackpackItem> BLACK_BRONZE_BACKPACK = ITEMS.register("black_bronze_backpack", () -> new BackpackItem(Config.SERVER.ironBackpack.inventorySlotCount::get, Config.SERVER.ironBackpack.upgradeSlotCount::get, Blocks.BLACK_BRONZE_BACKPACK));

    public static final DeferredItem<BackpackItem> WROUGHT_IRON_BACKPACK = ITEMS.register("wrought_iron_backpack", () -> new BackpackItem(Config.SERVER.goldBackpack.inventorySlotCount::get, Config.SERVER.goldBackpack.upgradeSlotCount::get, Blocks.WROUGHT_IRON_BACKPACK));

    public static final DeferredItem<BackpackItem> STEEL_BACKPACK = ITEMS.register("steel_backpack", () -> new BackpackItem(Config.SERVER.diamondBackpack.inventorySlotCount::get, Config.SERVER.diamondBackpack.upgradeSlotCount::get, Blocks.STEEL_BACKPACK));

    public static final DeferredItem<BackpackItem> BLACK_STEEL_BACKPACK = ITEMS.register("black_steel_backpack", () -> new BackpackItem(Config.SERVER.netheriteBackpack.inventorySlotCount::get, Config.SERVER.netheriteBackpack.upgradeSlotCount::get, Blocks.BLACK_STEEL_BACKPACK, Item.Properties::fireResistant));

    public static void registerDispenseBehavior() {
        DispenserBlock.registerBehavior(BISMUTH_BRONZE_BACKPACK.get(), new BackpackDispenseBehavior());
        DispenserBlock.registerBehavior(BRONZE_BACKPACK.get(), new BackpackDispenseBehavior());
        DispenserBlock.registerBehavior(BLACK_BRONZE_BACKPACK.get(), new BackpackDispenseBehavior());
        DispenserBlock.registerBehavior(WROUGHT_IRON_BACKPACK.get(), new BackpackDispenseBehavior());
        DispenserBlock.registerBehavior(STEEL_BACKPACK.get(), new BackpackDispenseBehavior());
        DispenserBlock.registerBehavior(BLACK_STEEL_BACKPACK.get(), new BackpackDispenseBehavior());
    }

    public static void registerCauldronInteractions() {
        CauldronInteraction.WATER.map().put(BISMUTH_BRONZE_BACKPACK.get(), new BackpackCauldronInteraction());
        CauldronInteraction.WATER.map().put(BRONZE_BACKPACK.get(), new BackpackCauldronInteraction());
        CauldronInteraction.WATER.map().put(BLACK_BRONZE_BACKPACK.get(), new BackpackCauldronInteraction());
        CauldronInteraction.WATER.map().put(WROUGHT_IRON_BACKPACK.get(), new BackpackCauldronInteraction());
        CauldronInteraction.WATER.map().put(STEEL_BACKPACK.get(), new BackpackCauldronInteraction());
        CauldronInteraction.WATER.map().put(BLACK_STEEL_BACKPACK.get(), new BackpackCauldronInteraction());
    }

    public static final DeferredItem<Item> FRIDGE_UPGRADE = ITEMS.register("fridge_upgrade", () -> new FridgeUpgradeItem(Config.SERVER.maxUpgradesPerStorage));

    private static final UpgradeContainerType<FridgeUpgradeWrapper, FridgeUpgradeContainer> FRIDGE_TYPE = new UpgradeContainerType<>(FridgeUpgradeContainer::new);

    public static final DeferredItem<Item> OVEN_UPGRADE = ITEMS.register("oven_upgrade", () -> new OvenUpgradeItem(Config.SERVER.maxUpgradesPerStorage));

    private static final UpgradeContainerType<OvenUpgradeWrapper, OvenUpgradeContainer> OVEN_TYPE = new UpgradeContainerType<>(OvenUpgradeContainer::new);

    public static final DeferredItem<Item> CRUCIBLE_UPGRADE = ITEMS.register("crucible_upgrade", () -> new CrucibleUpgradeItem(Config.SERVER.maxUpgradesPerStorage));

    private static final UpgradeContainerType<CrucibleUpgradeWrapper, CrucibleUpgradeContainer> CRUCIBLE_TYPE = new UpgradeContainerType<>(CrucibleUpgradeContainer::new);

    public static final DeferredItem<Item> FORGING_UPGRADE = ITEMS.register("forging_upgrade", () -> new ForgingUpgradeItem(Config.SERVER.maxUpgradesPerStorage));

    private static final UpgradeContainerType<ForgingUpgradeWrapper, ForgingUpgradeContainer> FORGING_TYPE = new UpgradeContainerType<>(ForgingUpgradeContainer::new);

    public static void registerContainers(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.MENU)) {
            UpgradeContainerRegistry.register(FRIDGE_UPGRADE.getId(), FRIDGE_TYPE);
            UpgradeContainerRegistry.register(OVEN_UPGRADE.getId(), OVEN_TYPE);
            UpgradeContainerRegistry.register(CRUCIBLE_UPGRADE.getId(), CRUCIBLE_TYPE);
            UpgradeContainerRegistry.register(FORGING_UPGRADE.getId(), FORGING_TYPE);
        }
    }

    public static void onMenuScreenRegister(RegisterMenuScreensEvent event) {
        UpgradeGuiManager.registerTab(FRIDGE_TYPE, FridgeUpgradeTab::new);
        UpgradeGuiManager.registerTab(OVEN_TYPE, OvenUpgradeTab::new);
        UpgradeGuiManager.registerTab(CRUCIBLE_TYPE, CrucibleUpgradeTab::new);
        UpgradeGuiManager.registerTab(FORGING_TYPE, ForgingUpgradeTab::new);
    }

    public static final DeferredItem<StackUpgradeItem> STACK_UPGRADE_TIER_COPPER = ITEMS.register("stack_upgrade_tier_copper", () -> new StackUpgradeItem(2.0, Config.SERVER.maxUpgradesPerStorage));

    public static final DeferredItem<StackUpgradeItem> STACK_UPGRADE_TIER_BISMUTH_BRONZE = ITEMS.register("stack_upgrade_tier_bismuth_bronze", () -> new StackUpgradeItem(4.0, Config.SERVER.maxUpgradesPerStorage));

    public static final DeferredItem<StackUpgradeItem> STACK_UPGRADE_TIER_BRONZE = ITEMS.register("stack_upgrade_tier_bronze", () -> new StackUpgradeItem(4.0, Config.SERVER.maxUpgradesPerStorage));

    public static final DeferredItem<StackUpgradeItem> STACK_UPGRADE_TIER_BLACK_BRONZE = ITEMS.register("stack_upgrade_tier_black_bronze", () -> new StackUpgradeItem(4.0, Config.SERVER.maxUpgradesPerStorage));

    public static final DeferredItem<StackUpgradeItem> STACK_UPGRADE_TIER_WROUGHT_IRON = ITEMS.register("stack_upgrade_tier_wrought_iron", () -> new StackUpgradeItem(8.0, Config.SERVER.maxUpgradesPerStorage));

    public static final DeferredItem<StackUpgradeItem> STACK_UPGRADE_TIER_STEEL = ITEMS.register("stack_upgrade_tier_steel", () -> new StackUpgradeItem(16.0, Config.SERVER.maxUpgradesPerStorage));

    public static final DeferredItem<StackUpgradeItem> STACK_UPGRADE_TIER_BLACK_STEEL = ITEMS.register("stack_upgrade_tier_black_steel", () -> new StackUpgradeItem(32.0, Config.SERVER.maxUpgradesPerStorage));

    public static final DeferredItem<StackUpgradeItem> STACK_UPGRADE_TIER_BLUE_STEEL = ITEMS.register("stack_upgrade_tier_blue_steel", () -> new StackUpgradeItem(64.0, Config.SERVER.maxUpgradesPerStorage));

    public static final DeferredItem<StackUpgradeItem> STACK_UPGRADE_TIER_RED_STEEL = ITEMS.register("stack_upgrade_tier_red_steel", () -> new StackUpgradeItem(64.0, Config.SERVER.maxUpgradesPerStorage));

    public static final DeferredItem<StackUpgradeConversionItem> STACK_UPGRADE_COPPER_TO_BISMUTH_BRONZE_TIER_CONVERSION = ITEMS.register("stack_upgrade_copper_to_bismuth_bronze_tier_conversion", () -> new StackUpgradeConversionItem(STACK_UPGRADE_TIER_COPPER, STACK_UPGRADE_TIER_BISMUTH_BRONZE));

    public static final DeferredItem<StackUpgradeConversionItem> STACK_UPGRADE_COPPER_TO_BRONZE_TIER_CONVERSION = ITEMS.register("stack_upgrade_copper_to_bronze_tier_conversion", () -> new StackUpgradeConversionItem(STACK_UPGRADE_TIER_COPPER, STACK_UPGRADE_TIER_BRONZE));

    public static final DeferredItem<StackUpgradeConversionItem> STACK_UPGRADE_COPPER_TO_BLACK_BRONZE_TIER_CONVERSION = ITEMS.register("stack_upgrade_copper_to_black_bronze_tier_conversion", () -> new StackUpgradeConversionItem(STACK_UPGRADE_TIER_COPPER, STACK_UPGRADE_TIER_BLACK_BRONZE));

    public static final DeferredItem<StackUpgradeConversionExtendedItem> STACK_UPGRADE_COPPER_ALLOY_TO_WROUGHT_IRON_TIER_CONVERSION = ITEMS.register("stack_upgrade_copper_alloy_to_wrought_iron_tier_conversion", () -> new StackUpgradeConversionExtendedItem(List.of(STACK_UPGRADE_TIER_BISMUTH_BRONZE, STACK_UPGRADE_TIER_BRONZE, STACK_UPGRADE_TIER_BLACK_BRONZE), STACK_UPGRADE_TIER_WROUGHT_IRON));

    public static final DeferredItem<StackUpgradeConversionItem> STACK_UPGRADE_WROUGHT_IRON_TO_STEEL_TIER_CONVERSION = ITEMS.register("stack_upgrade_wrought_iron_to_steel_tier_conversion", () -> new StackUpgradeConversionItem(STACK_UPGRADE_TIER_WROUGHT_IRON, STACK_UPGRADE_TIER_STEEL));

    public static final DeferredItem<StackUpgradeConversionItem> STACK_UPGRADE_STEEL_TO_BLACK_STEEL_TIER_CONVERSION = ITEMS.register("stack_upgrade_steel_to_black_steel_tier_conversion", () -> new StackUpgradeConversionItem(STACK_UPGRADE_TIER_STEEL, STACK_UPGRADE_TIER_BLACK_STEEL));

    public static final DeferredItem<StackUpgradeConversionItem> STACK_UPGRADE_BLACK_STEEL_TO_BLUE_STEEL_TIER_CONVERSION = ITEMS.register("stack_upgrade_black_steel_to_blue_steel_tier_conversion", () -> new StackUpgradeConversionItem(STACK_UPGRADE_TIER_BLACK_STEEL, STACK_UPGRADE_TIER_BLUE_STEEL));

    public static final DeferredItem<StackUpgradeConversionItem> STACK_UPGRADE_BLACK_STEEL_TO_RED_STEEL_TIER_CONVERSION = ITEMS.register("stack_upgrade_black_steel_to_red_steel_tier_conversion", () -> new StackUpgradeConversionItem(STACK_UPGRADE_TIER_BLACK_STEEL, STACK_UPGRADE_TIER_RED_STEEL));

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.ItemHandler.ITEM,
                (stack, v) -> {
                    IBackpackWrapper backpackWrapper = BackpackWrapper.fromStack(stack);
                    return backpackWrapper.getContentsUuid().isEmpty() ? EmptyItemHandler.INSTANCE : backpackWrapper.getInventoryForInputOutput();
                },
                BISMUTH_BRONZE_BACKPACK.get(),
                BRONZE_BACKPACK.get(),
                BLACK_BRONZE_BACKPACK.get(),
                WROUGHT_IRON_BACKPACK.get(),
                STEEL_BACKPACK.get(),
                BLACK_STEEL_BACKPACK.get()
        );

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, v) -> !Config.SERVER.itemFluidHandlerEnabled.get() ? null : BackpackWrapper.fromStack(stack).getItemFluidHandler().orElse(null),
                BISMUTH_BRONZE_BACKPACK.get(),
                BRONZE_BACKPACK.get(),
                BLACK_BRONZE_BACKPACK.get(),
                WROUGHT_IRON_BACKPACK.get(),
                STEEL_BACKPACK.get(),
                BLACK_STEEL_BACKPACK.get()
        );

        event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, v) -> BackpackWrapper.fromStack(stack).getEnergyStorage().orElse(null),
                BISMUTH_BRONZE_BACKPACK.get(),
                BRONZE_BACKPACK.get(),
                BLACK_BRONZE_BACKPACK.get(),
                WROUGHT_IRON_BACKPACK.get(),
                STEEL_BACKPACK.get(),
                BLACK_STEEL_BACKPACK.get()
        );
    }

    private static class BackpackCauldronInteraction implements CauldronInteraction {
        private static boolean hasDefaultColor(IStorageWrapper wrapper) {
            return wrapper.getAccentColor() == BackpackWrapper.DEFAULT_ACCENT_COLOR && wrapper.getMainColor() == BackpackWrapper.DEFAULT_MAIN_COLOR;
        }

        @Override
        public @NotNull ItemInteractionResult interact(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull ItemStack stack) {
            IBackpackWrapper backpackWrapper = BackpackWrapper.fromStack(stack);
            if (hasDefaultColor(backpackWrapper)) {
                return ItemInteractionResult.FAIL;
            }

            if (!level.isClientSide) {
                backpackWrapper.setColors(BackpackWrapper.DEFAULT_MAIN_COLOR, BackpackWrapper.DEFAULT_ACCENT_COLOR);
                LayeredCauldronBlock.lowerFillLevel(state, level, pos);
            }

            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    private static class BackpackDispenseBehavior extends OptionalDispenseItemBehavior {
        @Override
        protected @NotNull ItemStack execute(@NotNull BlockSource source, ItemStack stack) {
            setSuccess(false);
            Item item = stack.getItem();
            if (item instanceof BackpackItem backpackItem) {
                Direction dispenserDirection = source.state().getValue(DispenserBlock.FACING);
                BlockPos blockpos = source.pos().relative(dispenserDirection);
                Direction against = source.level().isEmptyBlock(blockpos.below()) ? dispenserDirection.getOpposite() : Direction.UP;

                setSuccess(backpackItem.tryPlace(null, dispenserDirection.getAxis() == Direction.Axis.Y ? Direction.NORTH : dispenserDirection.getOpposite(), new DirectionalPlaceContext(source.level(), blockpos, dispenserDirection, stack, against)).consumesAction());
            }

            return stack;
        }
    }
}
