package qianzha.heldmagic.util;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PlayerUtil {

	public static ServerPlayerEntity getPlayerByUUID(UUID uuid) {
		if(Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
			return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(uuid);
		}
		return null;
	}
	

	public static ServerPlayerEntity getPlayerByName(String name) {
		if(Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
			return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUsername(name);
		}
		return null;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static UUID getUUID(String name) {
		NetworkPlayerInfo info = Minecraft.getInstance().getConnection().getPlayerInfo(name);
		return PlayerEntity.getUUID(info.getGameProfile());
	}
	
}
