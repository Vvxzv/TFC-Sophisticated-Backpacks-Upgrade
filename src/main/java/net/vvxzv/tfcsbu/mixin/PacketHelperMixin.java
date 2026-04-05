package net.vvxzv.tfcsbu.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.network.PacketHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PacketHelper.class)
public class PacketHelperMixin {
    /**
     * @author Vvxzv
     * @reason Save ForgeCap
     */
    @Overwrite(remap = false)
    public static ItemStack readItemStack(FriendlyByteBuf packetBuffer) {
        if (!packetBuffer.readBoolean()) {
            return ItemStack.EMPTY;
        }
        CompoundTag tag = packetBuffer.readNbt();
        if (tag == null) {
            return ItemStack.EMPTY;
        }
        int count = packetBuffer.readInt();
        ItemStack stack = ItemStack.of(tag);
        stack.setCount(count);
        return stack;
    }

    /**
     * @author Vvxzv
     * @reason Save ForgeCap
     */
    @Overwrite(remap = false)
    public static void writeItemStack(ItemStack stack, FriendlyByteBuf packetBuffer) {
        if (stack.isEmpty()) {
            packetBuffer.writeBoolean(false);
        } else {
            packetBuffer.writeBoolean(true);
            packetBuffer.writeNbt(stack.save(new CompoundTag()));
            packetBuffer.writeInt(stack.getCount());
        }
    }
}
