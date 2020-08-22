package qianzha.heldmagic.api.unit;

import java.util.Comparator;

import qianzha.heldmagic.api.magic.IHoldableMagic;

public class MagicComparator implements Comparator<IHoldableMagic> {
	public static final MagicComparator INSTANCE = new MagicComparator();
	
	@Override
	public int compare(IHoldableMagic o1, IHoldableMagic o2) {
		return o1.getRegistryName().compareTo(o2.getRegistryName());
	}

}
