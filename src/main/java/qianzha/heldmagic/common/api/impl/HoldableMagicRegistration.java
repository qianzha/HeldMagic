package qianzha.heldmagic.common.api.impl;

import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.util.ResourceLocation;
import qianzha.heldmagic.api.magic.IHoldableMagic;
import qianzha.heldmagic.api.registration.IHoldableRegistration;
import qianzha.heldmagic.util.HMLogger;

public class HoldableMagicRegistration implements IHoldableRegistration {
	private final Map<String, Map<ResourceLocation, IHoldableMagic>> HOLDABLE_MAGIC = Maps.newTreeMap();
	private final Set<ResourceLocation> REGISTRYS = Sets.newTreeSet();
	private ImmutableList<IHoldableMagic> MAGICS = null;
	private boolean registerDone = false;
	
	// TODO 注册事件
	public void register(@Nonnull IHoldableMagic magic) {
		if(registerDone) {
			throw new RuntimeException("Not allow register at this time");
		}
		ResourceLocation registryName = magic.getRegistryName();
		if(!REGISTRYS.contains(registryName)) {
			HMLogger.API_VERBOSE.info("HoldableMagic: Added {}", registryName);
			REGISTRYS.add(registryName);
			Map<ResourceLocation,IHoldableMagic> space = HOLDABLE_MAGIC.getOrDefault(registryName.getNamespace(), Maps.newTreeMap());
			space.put(registryName, magic);
			HOLDABLE_MAGIC.putIfAbsent(registryName.getNamespace(), space);
		}
	}
	
	public void registerDone() {
		this.registerDone = true;
	}

	public ImmutableList<IHoldableMagic> getMagicList() {
		if (MAGICS == null) {
			ImmutableList.Builder<IHoldableMagic> builder = ImmutableList.<IHoldableMagic>builder();
			for(Map<ResourceLocation, IHoldableMagic> magics : HOLDABLE_MAGIC.values()) {
				builder.addAll(magics.values().iterator());
			}
			MAGICS = builder.build();
		}
		return MAGICS;
	}
	
	@Override
	public ImmutableList<String> getNamespaces() {
		final ImmutableList<String> list = ImmutableList.copyOf(HOLDABLE_MAGIC.keySet());
		return list;
	}

	@Override
	public ImmutableList<ResourceLocation> getRegistryNames() {
		final ImmutableList<ResourceLocation> list = ImmutableList.copyOf(REGISTRYS);
		return list;
	}
	
	@Override
	public ImmutableList<ResourceLocation> getRegistryNames(String spacename) {
		if(!HOLDABLE_MAGIC.containsKey(spacename)) return null;
		return ImmutableList.copyOf(HOLDABLE_MAGIC.get(spacename).keySet());
	}
	
	
	public boolean hasMagic(ResourceLocation registryName) {
		return REGISTRYS.contains(registryName);
	}
	
	public IHoldableMagic getMagic(ResourceLocation registryName) {
		try {
			return HOLDABLE_MAGIC.get(registryName.getNamespace()).get(registryName);
		} catch (NullPointerException e) {
			return null;
		}
	}


}
