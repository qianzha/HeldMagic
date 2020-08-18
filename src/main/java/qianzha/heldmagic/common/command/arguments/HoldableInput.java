package qianzha.heldmagic.common.command.arguments;

import java.util.function.Predicate;

import qianzha.heldmagic.api.magic.IHoldableMagic;
import qianzha.heldmagic.common.api.impl.HeldMagicAPI;

public class HoldableInput implements Predicate<IHoldableMagic> {
	private final boolean listAll;
	private final IHoldableMagic holdable;
	
	public HoldableInput(boolean listAll, IHoldableMagic holdable) {
		this.listAll = listAll;
		this.holdable = holdable;
	}
	
	@Override
	public boolean test(IHoldableMagic t) {
		return HeldMagicAPI.INSTANCE.getHoldableRegistration().hasMagic(t.getRegistryName());
	}
	
	public IHoldableMagic getHoldable() {
		return holdable;
	}
	
	public boolean isListAll() {
		return this.listAll;
	}
	
}
