package qianzha.heldmagic.common.api.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import net.minecraft.util.ResourceLocation;
import qianzha.heldmagic.api.magic.IHoldableMagic;
import qianzha.heldmagic.api.registration.IHoldableRegistration;
import qianzha.heldmagic.util.HMLogger;

public class HoldableMagicRegistration implements IHoldableRegistration {
	private final Map<ResourceLocation, IHoldableMagic> HOLDABLE_MAGIC = Maps.newTreeMap();
	
	public void register(@Nonnull IHoldableMagic magic) {
		ResourceLocation registryName = magic.getRegistryName();
		if(!HOLDABLE_MAGIC.containsKey(registryName)) {
			HMLogger.API_VERBOSE.info("HoldableMagic: Added {}", registryName);
			HOLDABLE_MAGIC.put(registryName, magic);
		}
	}

	public Collection<IHoldableMagic> getMagicCollection() {
		final Collection<IHoldableMagic> values = ImmutableSet.copyOf(HOLDABLE_MAGIC.values());
		return values;
	}
	
	public Set<ResourceLocation> keySet() {
		final Set<ResourceLocation> keySet = ImmutableSet.copyOf(HOLDABLE_MAGIC.keySet());
		return keySet;
	}
	
	public boolean hasMagic(ResourceLocation registryName) {
		return HOLDABLE_MAGIC.containsKey(registryName);
	}
	
	public IHoldableMagic getMagic(ResourceLocation registryName) {
		return HOLDABLE_MAGIC.get(registryName);
	}

}
