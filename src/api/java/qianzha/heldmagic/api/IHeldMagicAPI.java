package qianzha.heldmagic.api;

import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import qianzha.heldmagic.api.magic.IHoldableMagic;
import qianzha.heldmagic.api.magic.IMagicSkillTree;
import qianzha.heldmagic.api.registration.IHoldableRegistration;

public interface IHeldMagicAPI {
	public IHoldableRegistration getHoldableRegistration();
	
	public IMagicSkillTree getSkillTree(PlayerEntity player);
	public IMagicSkillTree getSkillTree(World worldIn, UUID player);
	
	default public String getTranslateKey(IHoldableMagic magic) {
		ResourceLocation key = magic.getRegistryName();
		return "heldmagic." + key.getNamespace() + "." + key.getPath().replace('/', '.');
	}
	
	default IHoldableMagic getMagic(ResourceLocation registryName) {
		return getHoldableRegistration().getMagic(registryName);
	}
	
}
