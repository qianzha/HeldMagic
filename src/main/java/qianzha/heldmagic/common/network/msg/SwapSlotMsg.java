package qianzha.heldmagic.common.network.msg;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SwapSlotMsg {
	public int aSlot;
	public int bSlot;
	
	public SwapSlotMsg(int aSlot, int bSlot) {
		this.aSlot = aSlot;
		this.bSlot = bSlot;
	}
	
	public SwapSlotMsg(PacketBuffer buf) {
		this(buf.readInt(), buf.readInt());
	}
	
	public void encode(PacketBuffer buf) {
		buf.writeInt(aSlot);
		buf.writeInt(bSlot);
	}
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity player = ctx.get().getSender();
			PlayerInventory inv = player.inventory;
			ItemStack aStack = inv.getStackInSlot(aSlot);
			inv.setInventorySlotContents(aSlot, inv.getStackInSlot(bSlot));
			inv.setInventorySlotContents(bSlot, aStack);
			inv.markDirty();
		});
		ctx.get().setPacketHandled(true);
	}
}
