package qianzha.heldmagic.common.data;

import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Sets;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.server.permission.PermissionAPI;
import qianzha.heldmagic.api.magic.IHoldableMagic;
import qianzha.heldmagic.api.magic.IMagicSkillTree;
import qianzha.heldmagic.common.HMConstants.Permission;
import qianzha.heldmagic.util.HMLogger;
import qianzha.heldmagic.util.PlayerUtil;

public class MagicSkillTree implements IMagicSkillTree, INBTSerializable<CompoundNBT> {
	private HMWorldSavedData parent;
	private ServerPlayerEntity player;
	private UUID playerUUID;
	private Set<ResourceLocation> unlocked = Sets.newTreeSet();
//	private Map<MagicType, Set<ResourceLocation>> unlockeds = ImmutableMap.of(MagicType.HOLDABLE, Sets.newTreeSet());
	
	public static MagicSkillTree create(UUID player) {
		MagicSkillTree skilltree = new MagicSkillTree();
		skilltree.playerUUID = player;
		return skilltree;
	}
	
	public static MagicSkillTree fromNBT(CompoundNBT nbt) {
		MagicSkillTree skilltree = new MagicSkillTree();
		skilltree.deserializeNBT(nbt);
		return skilltree;
	}
	
	public static Set<ResourceLocation> setFromNBT(CompoundNBT nbt) {
		MagicSkillTree skilltree = new MagicSkillTree();
		skilltree.deserializeNBT(nbt);
		return skilltree.unlocked;
	}
	
	private MagicSkillTree() {
		// No-op - For creation via NBT only
	}
	
	public void markDirty() {
		if(parent != null) parent.markDirty();
		else HMLogger.DEFAULT.error("A MagicSkillTree was created, but a WorldSavedData was not set for saving. ");
	}
	
	
	
	public MagicSkillTree setParent(HMWorldSavedData parent) {
		this.parent = parent;
		markDirty();
		return this;
	}
	
	
	public ServerPlayerEntity getPlayer() {
		if(player == null) {
			player = PlayerUtil.getPlayerByUUID(this.playerUUID);
		}
		return player;
	}
	
	public UUID getPlayerUUID() {
		return playerUUID;
	}
	
	public boolean addUnlocked(IHoldableMagic magic) {
		if(!magic.isUnlockedByDefault()) {
			if(unlocked.add(magic.getRegistryName())) {
				markDirty();
				return true;
			}
		}
		else {
			HMLogger.API.warn("Cannot unlock magic '{}' because it is unlocked by default. ", magic.getRegistryName());
		}
		return false;
	}
	
	public boolean removeUnlocked(IHoldableMagic magic) {
		if(unlocked.remove(magic.getRegistryName())) {
			markDirty();
			return true;
		}
		return false;
	}
	
	public boolean isUnlocked(IHoldableMagic magic) {
		if(PermissionAPI.hasPermission(getPlayer(), Permission.UNLOCKED_ALL))
			return true;
		return magic.isUnlockedByDefault() ? true : unlocked.contains(magic.getRegistryName());
	}
	
	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("player", playerUUID.toString());
		nbt.put("unlockeds", serializeUnlocked());
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.playerUUID = UUID.fromString(nbt.getString("player"));
		CompoundNBT unlockedsNBT = nbt.getCompound("unlockeds");
		deserializeUnlocked(unlocked, unlockedsNBT.getList("holdable", NBT.TAG_STRING));
	}
	
	public CompoundNBT serializeUnlocked() {
		CompoundNBT unlockedsNBT = new CompoundNBT();
		ListNBT list = new ListNBT();
		for(ResourceLocation obj : unlocked) {
			list.add(StringNBT.valueOf(obj.toString()));
		}
		unlockedsNBT.put("holdable", list);
		return unlockedsNBT;
	}
	
	private void deserializeUnlocked(Set<ResourceLocation> set, ListNBT nbt) {
		nbt.forEach((obj) -> {
			set.add(new ResourceLocation(obj.getString()));
		});
	}

}
