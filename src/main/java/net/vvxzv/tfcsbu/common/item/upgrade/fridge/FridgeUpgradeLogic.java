package net.vvxzv.tfcsbu.common.item.upgrade.fridge;

import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.p3pp3rf1y.sophisticatedcore.util.NBTHelper;
import net.vvxzv.tfcsbu.common.UFoodTrait;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class FridgeUpgradeLogic {
    private final ItemStack upgrade;
    private final Consumer<ItemStack> saveHandler;
    @Nullable
    private ItemStackHandler inventory;

    public FridgeUpgradeLogic(ItemStack upgrade, Consumer<ItemStack> saveHandler) {
        this.upgrade = upgrade;
        this.saveHandler = saveHandler;
    }

    public ItemStackHandler getInventory(){
        if (this.inventory == null) {
            this.inventory = new ItemStackHandler(9){
                @Override
                protected void onContentsChanged(int slot) {
                    super.onContentsChanged(slot);
                    upgrade.addTagElement("fridge_inventory", this.serializeNBT());
                    save();
                }
            };
            NBTHelper.getCompound(upgrade, "fridge_inventory").ifPresent(inventory::deserializeNBT);
        }

        return this.inventory;
    }

    public void tick(){
        if(getInventory() != null){
            ItemStackHandler handler = getInventory();
            for (int i = 0; i < handler.getSlots(); i++){
                ItemStack itemStack = handler.getStackInSlot(i);
                FoodCapability.applyTrait(itemStack, UFoodTrait.FRIDGE_PRESERVED);
            }
        }
    }

    private void save() {
        this.saveHandler.accept(this.upgrade);
    }
}
