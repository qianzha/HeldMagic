package qianzha.heldmagic.common.plugin.test.heldmagic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import qianzha.heldmagic.api.magic.IHoldableMagic;
import qianzha.heldmagic.common.HMConstants;
import qianzha.heldmagic.common.api.impl.HeldMagicAPI;
import qianzha.heldmagic.common.plugin.test.HMStuffs.HMItems;

public enum HoldableMagic implements IHoldableMagic {
	EXAMPLE(HMItems.HELD_ITEM, "example_holdable");
	
	private Item item;
	private ResourceLocation registryName;
	
	private HoldableMagic(Item item, String pathName) {
		this.item = item;
		this.registryName = new ResourceLocation(HMConstants.MODID, pathName);
	}
	
	@Override
	public boolean isUnlockedByDefault() {
		return false;
	}

	@Override
	public Item getItem() {
		return this.item;
	}

	@Override
	public ResourceLocation getRegistryName() {
		return this.registryName;
	}
	
	@Override
	public void equipFeedback(PlayerEntity player) {
		player.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
		player.sendMessage(new TranslationTextComponent("text.heldmagic.hm_feedback.equip"
				, new TranslationTextComponent(HeldMagicAPI.INSTANCE.getTranslateKey(this))));
	}

	@Override
	public boolean canEquip(PlayerEntity player) {
		return true;
	}

	@Override
	public ITextComponent getInfo() {
		return new TranslationTextComponent(HeldMagicAPI.INSTANCE.getTranslateKey(this) + ".info");
	}
	
}
