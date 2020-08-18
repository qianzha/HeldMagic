package qianzha.heldmagic.common.api.impl;

import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import qianzha.heldmagic.api.IHeldMagicAPI;
import qianzha.heldmagic.api.magic.IMagicSkillTree;
import qianzha.heldmagic.api.registration.IHoldableRegistration;
import qianzha.heldmagic.common.data.HMWorldSavedData;

public enum HeldMagicAPI implements IHeldMagicAPI {
	INSTANCE();
	
	private final HoldableMagicRegistration holdableManager = new HoldableMagicRegistration();

	@Override
	public IHoldableRegistration getHoldableRegistration() {
		return holdableManager;
	}

	public IMagicSkillTree getSkillTree(PlayerEntity player) {
		return getSkillTree(player.world, player.getGameProfile().getId());
	}
	
	public IMagicSkillTree getSkillTree(World worldIn, UUID player) {
		HMWorldSavedData data = HMWorldSavedData.getSavedData(worldIn);
		return data.getSkillTree(player);
	}
	
}
