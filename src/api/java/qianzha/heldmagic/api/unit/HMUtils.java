package qianzha.heldmagic.api.unit;

import java.util.stream.Stream;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import qianzha.heldmagic.api.HeldMagicPlugin;
import qianzha.heldmagic.api.IHeldMagicAPI;
import qianzha.heldmagic.api.magic.IHoldableMagic;

public class HMUtils {
	public static final String NBT_MAGIC_ITEM = "qzhm_holdable";
	public static final String NBT_HELDING_SAVED = "qzhm_replace";
	@HeldMagicPlugin.ApiInstance
	public static IHeldMagicAPI API;
	public static Dynamic2CommandExceptionType DCET_NOT_UNLOCKED = new Dynamic2CommandExceptionType((player, magic) -> {
		return new TranslationTextComponent("text.heldmagic.fail.magic_locked", player, magic);
	});
	
	
	public static boolean isUnlocked(PlayerEntity player, IHoldableMagic magic) {
		return API.getSkillTree(player).isUnlocked(magic);
	}
	
	public static boolean addUnlock(PlayerEntity player, IHoldableMagic magic) {
		return API.getSkillTree(player).addUnlocked(magic);
	}
	
	public static boolean removeUnlocked(PlayerEntity player, IHoldableMagic magic) {
		return API.getSkillTree(player).removeUnlocked(magic);
	}
	
	public static boolean isMagicRegisted(ResourceLocation registryName) {
		return API.getHoldableRegistration().hasMagic(registryName);
	}
	
	public static IHoldableMagic getMagic(ResourceLocation id) {
		return API.getHoldableRegistration().getMagic(id);
	}
	
	public static TranslationTextComponent getMagicTranslatedName(IHoldableMagic magic) {
		return new TranslationTextComponent(API.getTranslateKey(magic));
	}
	
	public static Stream<IHoldableMagic> listMagic(PlayerEntity player) {
		if(player == null)
			return Stream.of(getRegistedMagic());
		else
			return Stream.of(getRegistedMagic()).filter(magic -> API.getSkillTree(player).isUnlocked(magic));
	}
	
	public static IHoldableMagic[] getRegistedMagic() {
		final IHoldableMagic[] values = API.getHoldableRegistration().getMagicCollection().toArray(new IHoldableMagic[] {});
		return values;
	}
	
	public static void equipHoldable(IHoldableMagic holdable, PlayerEntity player, Hand hand) throws CommandException, CommandSyntaxException {
		if(!isUnlocked(player, holdable)) throw DCET_NOT_UNLOCKED.create(player.getName(), holdable.getRegistryName());
		if(!holdable.canEquip(player)) throw new CommandException(holdable.cannontEquipInfo(player));
		equipHoldableWithoutCheck(holdable, player, hand);
	}
	
	public static void equipHoldableWithoutCheck(IHoldableMagic holdable, PlayerEntity player, Hand hand) {	
		ItemStack toReplace = player.getHeldItem(hand);
		ItemStack toHold = new ItemStack(holdable.getItem(), 1);
		toHold.setDisplayName(new TranslationTextComponent(API.getTranslateKey(holdable)).applyTextStyle(TextFormatting.LIGHT_PURPLE));
		CompoundNBT nbtRoot = toHold.getOrCreateTag();
		nbtRoot.putString(NBT_MAGIC_ITEM, holdable.getRegistryName().toString());
		if(isMagicStack(toReplace)) {
			if(isSavingStuff(toReplace)) {
				nbtRoot.put(NBT_HELDING_SAVED, toReplace.getChildTag(NBT_HELDING_SAVED));
			}
		}
		else if (!toReplace.isEmpty()) {
			toHold.getOrCreateTag().put(NBT_HELDING_SAVED, toReplace.serializeNBT());
		}
		player.setHeldItem(hand, toHold);
		holdable.equipFeedback(player);
	}
	
	public static ItemStack getSavingStack(ItemStack stack) {
		if(isSavingStuff(stack)) {
			return ItemStack.read(stack.getChildTag(NBT_HELDING_SAVED));
		}
		return ItemStack.EMPTY;
	}
	
	public static IHoldableMagic getMagicFormStack(ItemStack stack) {
		if(isMagicStack(stack)) {
			return getMagic(new ResourceLocation(stack.getTag().getString(NBT_MAGIC_ITEM)));
		}
		return null;
	}
	
	public static void replaceBack(NonNullList<ItemStack> inventory, int index) {
		ItemStack srcStack = inventory.get(index);
		if(isMagicStack(srcStack)) {
			inventory.set(index, getSavingStack(srcStack));
		}
	}
	
	public static boolean isSavingStuff(ItemStack stack) {
		return !stack.isEmpty() && stack.hasTag() && stack.getTag().contains(NBT_HELDING_SAVED);
	}
	
	public static boolean isMagicStack(ItemStack stack) {
		return !stack.isEmpty() && stack.hasTag() && stack.getTag().contains(NBT_MAGIC_ITEM);
	}
	
	public static String magicStringId(IHoldableMagic magic) {
		return magic.getRegistryName().toString();
	}
	
}
