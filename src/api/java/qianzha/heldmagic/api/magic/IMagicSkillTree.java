package qianzha.heldmagic.api.magic;

import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;

public interface IMagicSkillTree {

	public PlayerEntity getPlayer();
	public UUID getPlayerUUID();
	
	public boolean addUnlocked(IHoldableMagic magic);
	
	public boolean removeUnlocked(IHoldableMagic magic);
	
	public boolean isUnlocked(IHoldableMagic magic);
	
}
