package qianzha.heldmagic.api.registration;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import net.minecraft.util.ResourceLocation;
import qianzha.heldmagic.api.magic.IHoldableMagic;

public interface IHoldableRegistration {
	
	public void register(@Nonnull IHoldableMagic magic);
	
	public ImmutableList<IHoldableMagic> getMagicList();

	public ImmutableList<String> getNamespaces();
	
	public ImmutableList<ResourceLocation> getRegistryNames();

	public ImmutableList<ResourceLocation> getRegistryNames(String spacename);
	
	public boolean hasMagic(ResourceLocation registryName);
	
	public IHoldableMagic getMagic(ResourceLocation registryName);
	
}
