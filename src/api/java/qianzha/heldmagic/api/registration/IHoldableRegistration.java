package qianzha.heldmagic.api.registration;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.util.ResourceLocation;
import qianzha.heldmagic.api.magic.IHoldableMagic;

public interface IHoldableRegistration {
	
	public void register(@Nonnull IHoldableMagic magic);
	
	public Collection<IHoldableMagic> getMagicCollection();
	
	public Set<ResourceLocation> keySet();
	
	public boolean hasMagic(ResourceLocation registryName);
	
	public IHoldableMagic getMagic(ResourceLocation registryName);
	
}
