package qianzha.heldmagic.api.unit;

import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
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
	public static final MagicComparator MAGIC_COMPARATOR = MagicComparator.INSTANCE;
	public static final Dynamic2CommandExceptionType DCET_NOT_UNLOCKED = new Dynamic2CommandExceptionType((player, magic) -> {
		return new TranslationTextComponent("text.heldmagic.fail.magic_locked", player, magic);
	});
	
	
	public static boolean isUnlocked(ServerPlayerEntity player, IHoldableMagic magic) {
		return API.getSkillTree(player).isUnlocked(magic);
	}
	
	public static boolean addUnlock(ServerPlayerEntity player, IHoldableMagic magic) {
		return API.getSkillTree(player).addUnlocked(magic);
	}
	
	public static boolean removeUnlocked(ServerPlayerEntity player, IHoldableMagic magic) {
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
	
	public static Stream<IHoldableMagic> listMagic(ServerPlayerEntity player) {
		if(player == null)
			return getRegistedMagic().stream();
		else
			return getRegistedMagic().stream().filter(magic -> API.getSkillTree(player).isUnlocked(magic));
	}
	
	public static ImmutableList<IHoldableMagic>  getRegistedMagic() {
		return API.getHoldableRegistration().getMagicList();
	}
	
	public static void checkMagic(IHoldableMagic magic, ServerPlayerEntity player) throws CommandException, CommandSyntaxException {
		if(!isUnlocked(player, magic)) throw DCET_NOT_UNLOCKED.create(player.getName(), magic.getRegistryName());
		if(!magic.canEquip(player)) throw new CommandException(magic.cannontEquipInfo(player));
	}
	
	
	public static void equipHoldable(IHoldableMagic holdable, ServerPlayerEntity player, Hand hand) throws CommandException, CommandSyntaxException {
		checkMagic(holdable, player);
		equipHoldableWithoutCheck(holdable, player, hand);
	}
	
	public static void equipHoldableWithoutCheck(IHoldableMagic holdable, PlayerEntity player, Hand hand) {	
		ItemStack toHold = replace(holdable, player.getHeldItem(hand));
		player.setHeldItem(hand, toHold);
		holdable.equipFeedback(player);
	}
	
	public static void equipMagic(IHoldableMagic magic, ServerPlayerEntity player, int slotIndex) throws CommandException, CommandSyntaxException {
		checkMagic(magic, player);
		PlayerInventory inv = player.inventory;
		inv.setInventorySlotContents(slotIndex, replace(magic, inv.getStackInSlot(slotIndex)));
		magic.equipFeedback(player);
	}
	
	public static ItemStack replace(IHoldableMagic holdable, ItemStack toReplace) {
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
		return toHold;
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
