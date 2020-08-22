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
	EXAMPLE(HMItems.HELD_ITEM, "example_holdable"),
	MORE_1(HMItems.HELD_ITEM, "hm_test_1", "test_1"),
	MORE_2(HMItems.HELD_ITEM, "hm_test_1", "test_2"),
	MORE_3(HMItems.HELD_ITEM, "hm_test_2", "test_1"),
	MORE_4(HMItems.HELD_ITEM, "hm_test_2", "test_2"),
	MORE_5(HMItems.HELD_ITEM, "hm_test_3", "test_1"),
	MORE_6(HMItems.HELD_ITEM, "hm_test_4", "test_1"),
	MORE_7(HMItems.HELD_ITEM, "hm_test_5", "test_1"),
	MORE_8(HMItems.HELD_ITEM, "hm_test_6", "test_1"),
	MORE_9(HMItems.HELD_ITEM, "hm_test_6", "test_2"),
	MORE_10(HMItems.HELD_ITEM, "hm_test_5", "test_10"),
	MORE_11(HMItems.HELD_ITEM, "hm_test_5", "test_11"),
	MORE_12(HMItems.HELD_ITEM, "hm_test_5", "test_12"),
	MORE_13(HMItems.HELD_ITEM, "hm_test_5", "test_13"),
	MORE_14(HMItems.HELD_ITEM, "hm_test_5", "test_14"),
	MORE_15(HMItems.HELD_ITEM, "hm_test_5", "test_15"),
	MORE_16(HMItems.HELD_ITEM, "hm_test_5", "test_16"),
	MORE_17(HMItems.HELD_ITEM, "hm_test_5", "test_17"),
	MORE_18(HMItems.HELD_ITEM, "hm_test_5", "test_18"),
	MORE_19(HMItems.HELD_ITEM, "hm_test_5", "test_19"),
	MORE_20(HMItems.HELD_ITEM, "hm_test_5", "test_20"),
	MORE_21(HMItems.HELD_ITEM, "hm_test_5", "test_21"),
	MORE_22(HMItems.HELD_ITEM, "hm_test_5", "test_22"),
	MORE_23(HMItems.HELD_ITEM, "hm_test_5", "test_23"),
	MORE_24(HMItems.HELD_ITEM, "hm_test_5", "test_24"),
	MORE_25(HMItems.HELD_ITEM, "hm_test_5", "test_25"),
	MORE_26(HMItems.HELD_ITEM, "hm_test_5", "test_26"),
	MORE_27(HMItems.HELD_ITEM, "hm_test_5", "test_27"),
	MORE_28(HMItems.HELD_ITEM, "hm_test_5", "test_28"),
	MORE_29(HMItems.HELD_ITEM, "hm_test_5", "test_29"),
	MORE_30(HMItems.HELD_ITEM, "hm_test_5", "test_30"),
	MORE_31(HMItems.HELD_ITEM, "hm_test_5", "test_31"),
	MORE_32(HMItems.HELD_ITEM, "hm_test_5", "test_32"),
	MORE_33(HMItems.HELD_ITEM, "hm_test_5", "test_33"),
	MORE_34(HMItems.HELD_ITEM, "hm_test_5", "test_34"),
	MORE_35(HMItems.HELD_ITEM, "hm_test_5", "test_35"),
	MORE_36(HMItems.HELD_ITEM, "hm_test_5", "test_36"),
	MORE_37(HMItems.HELD_ITEM, "hm_test_5", "test_37"),
	MORE_38(HMItems.HELD_ITEM, "hm_test_5", "test_38"),
	MORE_39(HMItems.HELD_ITEM, "hm_test_5", "test_39"),
	MORE_40(HMItems.HELD_ITEM, "hm_test_5", "test_40"),
	MORE_41(HMItems.HELD_ITEM, "hm_test_5", "test_41"),
	MORE_42(HMItems.HELD_ITEM, "hm_test_5", "test_42"),
	MORE_43(HMItems.HELD_ITEM, "hm_test_5", "test_43"),
	MORE_44(HMItems.HELD_ITEM, "hm_test_5", "test_44"),
	MORE_45(HMItems.HELD_ITEM, "hm_test_5", "test_45"),
	MORE_46(HMItems.HELD_ITEM, "hm_test_5", "test_46"),
	MORE_47(HMItems.HELD_ITEM, "hm_test_5", "test_47"),
	MORE_48(HMItems.HELD_ITEM, "hm_test_5", "test_48"),
	MORE_49(HMItems.HELD_ITEM, "hm_test_5", "test_49"),
    MORE_50(HMItems.HELD_ITEM, "hm_test_5", "test_50"),
    MORE_51(HMItems.HELD_ITEM, "hm_test_5", "test_51"),
    MORE_52(HMItems.HELD_ITEM, "hm_test_5", "test_52"),
    MORE_53(HMItems.HELD_ITEM, "hm_test_5", "test_53"),
    MORE_54(HMItems.HELD_ITEM, "hm_test_5", "test_54"),
    MORE_55(HMItems.HELD_ITEM, "hm_test_5", "test_55"),
    MORE_56(HMItems.HELD_ITEM, "hm_test_5", "test_56"),
    MORE_57(HMItems.HELD_ITEM, "hm_test_5", "test_57"),
    MORE_58(HMItems.HELD_ITEM, "hm_test_5", "test_58"),
    MORE_59(HMItems.HELD_ITEM, "hm_test_5", "test_59"),
    MORE_60(HMItems.HELD_ITEM, "hm_test_5", "test_60"),
    MORE_61(HMItems.HELD_ITEM, "hm_test_5", "test_61"),
    MORE_62(HMItems.HELD_ITEM, "hm_test_5", "test_62"),
    MORE_63(HMItems.HELD_ITEM, "hm_test_5", "test_63"),
    MORE_64(HMItems.HELD_ITEM, "hm_test_5", "test_64"),
    MORE_65(HMItems.HELD_ITEM, "hm_test_5", "test_65"),
    MORE_66(HMItems.HELD_ITEM, "hm_test_5", "test_66"),
    MORE_67(HMItems.HELD_ITEM, "hm_test_5", "test_67"),
    MORE_68(HMItems.HELD_ITEM, "hm_test_5", "test_68"),
    MORE_69(HMItems.HELD_ITEM, "hm_test_5", "test_69"),
    MORE_70(HMItems.HELD_ITEM, "hm_test_5", "test_70"),
    MORE_71(HMItems.HELD_ITEM, "hm_test_5", "test_71"),
    MORE_72(HMItems.HELD_ITEM, "hm_test_5", "test_72"),
    MORE_73(HMItems.HELD_ITEM, "hm_test_5", "test_73"),
    MORE_74(HMItems.HELD_ITEM, "hm_test_5", "test_74"),
    MORE_75(HMItems.HELD_ITEM, "hm_test_5", "test_75"),
    MORE_76(HMItems.HELD_ITEM, "hm_test_5", "test_76"),
    MORE_77(HMItems.HELD_ITEM, "hm_test_5", "test_77"),
    MORE_78(HMItems.HELD_ITEM, "hm_test_5", "test_78"),
    MORE_79(HMItems.HELD_ITEM, "hm_test_5", "test_79"),
    MORE_80(HMItems.HELD_ITEM, "hm_test_5", "test_80"),
    MORE_81(HMItems.HELD_ITEM, "hm_test_5", "test_81"),
    MORE_82(HMItems.HELD_ITEM, "hm_test_5", "test_82"),
    MORE_83(HMItems.HELD_ITEM, "hm_test_5", "test_83"),
    MORE_84(HMItems.HELD_ITEM, "hm_test_5", "test_84"),
    MORE_85(HMItems.HELD_ITEM, "hm_test_5", "test_85"),
    MORE_86(HMItems.HELD_ITEM, "hm_test_5", "test_86"),
    MORE_87(HMItems.HELD_ITEM, "hm_test_5", "test_87"),
    MORE_88(HMItems.HELD_ITEM, "hm_test_5", "test_88"),
    MORE_89(HMItems.HELD_ITEM, "hm_test_5", "test_89"),
	;
	
	private Item item;
	private ResourceLocation registryName;
	
	private HoldableMagic(Item item, String namespace, String pathName) {
		this.item = item;
		this.registryName = new ResourceLocation(namespace, pathName);
	}
	
	private HoldableMagic(Item item, String pathName) {
		this.item = item;
		this.registryName = new ResourceLocation(HMConstants.MODID, pathName);
	}
	
	@Override
	public boolean isUnlockedByDefault() {
		return true;
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
