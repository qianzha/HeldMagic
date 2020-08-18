package qianzha.heldmagic.api.magic;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public interface IHoldableMagic {
	
	boolean isUnlockedByDefault();
	
	boolean canEquip(PlayerEntity player);
	
	default @Nullable ITextComponent cannontEquipInfo(PlayerEntity player) {
		return null;
	}
	
	default void equipFeedback(PlayerEntity player) {
	}
	
	ResourceLocation getRegistryName();
	
	public ITextComponent getInfo();
	
	public Item getItem();
	
}
