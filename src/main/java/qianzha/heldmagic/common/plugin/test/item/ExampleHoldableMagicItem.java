package qianzha.heldmagic.common.plugin.test.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import qianzha.heldmagic.api.magic.IHoldableMagic;
import qianzha.heldmagic.api.unit.HMUtils;

public class ExampleHoldableMagicItem extends Item {
	
	public ExampleHoldableMagicItem() {
		super(new Properties().maxStackSize(1));
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		IHoldableMagic magic = HMUtils.getMagicFormStack(stack);
		ItemStack saving = HMUtils.getSavingStack(stack);
		
		if(magic != null) {
			tooltip.add(magic.getInfo());
		}
		if(saving != null) {
			tooltip.add(saving.getDisplayName().appendText(" x" + saving.getCount()));
		}
	}
	
	
}
