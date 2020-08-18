package qianzha.heldmagic.common.network;

import java.util.function.Supplier;

import net.minecraft.network.NetworkManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import qianzha.heldmagic.common.HMConstants;
import qianzha.heldmagic.util.HMLogger;

public class HeldMagicPacketHandler {
	private static final String PROTOCOL_VERSION = "1.0";
	public static final SimpleChannel CHANNEL;
	@SuppressWarnings("unused")
	private static int cnt = 0;
	
	static {
		CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(HMConstants.MODID, "main"),
			PROTOCOL_VERSION::toString,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
		);
	}
	
	public static void checkD(Supplier<NetworkEvent.Context> ctx, String packet, NetworkDirection direction, Runnable work) {
		if(ctx.get().getDirection() != direction) {
			HMLogger.DEFAULT.error("This packet <{}> is sent by {}, which is not allow. ", 
					packet, ctx.get().getDirection());
			ctx.get().setPacketHandled(false);
		}
		else {
			ctx.get().enqueueWork(work);
			ctx.get().setPacketHandled(true);
		}
	}
	
	public <MSG> void send(PacketTarget target, MSG message) {
		CHANNEL.send(target, message);
	}
	
	public <MSG> void sendTo(MSG message, NetworkManager manager, NetworkDirection direction) {
		CHANNEL.sendTo(message, manager, direction);
	}
}
