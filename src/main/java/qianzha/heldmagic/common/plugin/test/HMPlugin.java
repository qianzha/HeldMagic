package qianzha.heldmagic.common.plugin.test;

import qianzha.heldmagic.api.HeldMagicPlugin;
import qianzha.heldmagic.api.IHeldMagicAPI;
import qianzha.heldmagic.api.IHeldMagicPlugin;
import qianzha.heldmagic.common.plugin.test.heldmagic.HoldableMagic;

@HeldMagicPlugin
public class HMPlugin implements IHeldMagicPlugin {
	@HeldMagicPlugin.ApiInstance
	public static IHeldMagicAPI API = null;
	
	@Override
	public void register(IHeldMagicAPI api) {
		api.getHoldableRegistration().register(HoldableMagic.EXAMPLE);
	}
	
}
