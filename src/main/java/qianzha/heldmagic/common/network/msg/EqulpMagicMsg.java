package qianzha.heldmagic.common.network.msg;

import java.util.function.Supplier;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import qianzha.heldmagic.api.magic.IHoldableMagic;
import qianzha.heldmagic.api.unit.HMUtils;

public class EqulpMagicMsg {
	private ResourceLocation magicId;
	private int slotIndex;
	
	public EqulpMagicMsg(IHoldableMagic magic, int slotIndex) {
		this(magic.getRegistryName(), slotIndex);
	}
	
	public EqulpMagicMsg(ResourceLocation magicId, int slotIndex) {
		this.magicId = magicId;
		this.slotIndex = slotIndex;
	}
	
	public EqulpMagicMsg(PacketBuffer buf) {
		this.slotIndex = buf.readInt();
		this.magicId = buf.readResourceLocation();
	}
	
	public void encode(PacketBuffer buf) {
		buf.writeInt(slotIndex);
		buf.writeResourceLocation(magicId);
	}
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity player = ctx.get().getSender();
			IHoldableMagic magic = HMUtils.getMagic(magicId);
			try {
				HMUtils.equipMagic(magic, player, slotIndex);
			} catch (CommandException e) {
				player.sendMessage(e.getComponent());
			} catch (CommandSyntaxException e) {
				player.sendMessage(new StringTextComponent(e.getMessage()));
			}
		});
		ctx.get().setPacketHandled(true);
	}
	
	
}
