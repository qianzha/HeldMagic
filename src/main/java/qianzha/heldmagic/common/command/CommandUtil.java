package qianzha.heldmagic.common.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import qianzha.heldmagic.api.magic.IHoldableMagic;
import qianzha.heldmagic.api.unit.HMUtils;

public class CommandUtil {
	static ServerPlayerEntity getPlayer(CommandContext<CommandSource> context, boolean hasArgument) throws CommandSyntaxException {
		return hasArgument ? EntityArgument.getPlayer(context, "player") : context.getSource().asPlayer();
	}
	
	static String getBoolResKey(boolean res) {
		return res ? ".true" : ".false";
	}
	
	static ITextComponent feedback(String key, boolean res, PlayerEntity player, IHoldableMagic magic) {
		return feedback(key + getBoolResKey(res), player, magic);
	}
	
	static ITextComponent feedback(String key, PlayerEntity player, IHoldableMagic magic) {
		return new TranslationTextComponent(
				key,
				player.getName(),
				HMUtils.getMagicTranslatedName(magic)
		);
	}
}
