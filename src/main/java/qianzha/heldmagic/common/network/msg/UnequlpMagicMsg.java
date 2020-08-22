package qianzha.heldmagic.common.network.msg;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import qianzha.heldmagic.api.unit.HMUtils;

public class UnequlpMagicMsg {
	private int index;
	
	public UnequlpMagicMsg(int index) {
		this.index = index;
	}
	
	public UnequlpMagicMsg(PacketBuffer buf) {
		this(buf.readInt());
	}
	
	public void encode(PacketBuffer buf) {
		buf.writeInt(index);
	}
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity player = ctx.get().getSender();
			ItemStack stack = player.inventory.getStackInSlot(index);
			if(HMUtils.isMagicStack(stack)) {
				player.inventory.setInventorySlotContents(index, HMUtils.getSavingStack(stack));
			}
		});
	}
	
}
