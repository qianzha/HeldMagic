package qianzha.heldmagic.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import qianzha.heldmagic.api.magic.IHoldableMagic;
import qianzha.heldmagic.api.unit.HMUtils;
import qianzha.heldmagic.common.command.arguments.HoldableArgument;

public class SkillTreeCommand {
	public static final String FEEDBACK_CHECK_H = "command.heldmagic.skilltree.check";
	public static final String FEEDBACK_ADD_H = "command.heldmagic.skilltree.add";
	public static final String FEEDBACK_REMOVE_H = "command.heldmagic.skilltree.remove";

	public static ArgumentBuilder<CommandSource, ?>  register() {
		return Commands.literal("skilltree")
			.then(Commands.literal("list")
				.executes(context -> list(context, false))
				.then(Commands.argument("player", EntityArgument.player())
						.executes(context -> list(context, true))
				)
			).then(Commands.literal("check")
					.then(Commands.argument("magic", HoldableArgument.holdable(false))
							.suggests(HoldableArgument.getSuggestion(false))
							.executes(context -> check(context, false))
							.then(Commands.argument("player", EntityArgument.player())
									.executes(context -> check(context, true))
							)
					)
			).then(Commands.literal("add").requires(cs -> cs.hasPermissionLevel(2))
					.then(Commands.argument("magic", HoldableArgument.holdable(true))
							.suggests(HoldableArgument.getSuggestion(true))
							.executes(context -> add(context, false))
							.then(Commands.argument("player", EntityArgument.player())
									.executes(context -> add(context, true))
							)
					)
			).then(Commands.literal("remove").requires(cs -> cs.hasPermissionLevel(2))
					.then(Commands.argument("magic", HoldableArgument.holdable(true))
							.suggests(HoldableArgument.getSuggestion(true))
							.executes(context -> remove(context, false))
							.then(Commands.argument("player", EntityArgument.player())
									.executes(context -> remove(context, true))
							)
					)
			);
	}

	private static int list(CommandContext<CommandSource> context, boolean hasPlayerArg) throws CommandSyntaxException {
		ServerPlayerEntity player = CommandUtil.getPlayer(context, hasPlayerArg);
		context.getSource().sendFeedback(new TranslationTextComponent("command.heldmagic.skilltree.list"), true);
		HMUtils.listMagic(player).forEach(magic -> {
			context.getSource().sendFeedback(HMUtils.getMagicTranslatedName(magic).appendText(": ").appendSibling(magic.getInfo()), true);
		});;
		return Command.SINGLE_SUCCESS;
	}
	
	private static int check(CommandContext<CommandSource> context, boolean hasPlayerArg) throws CommandSyntaxException {
		ServerPlayerEntity player = CommandUtil.getPlayer(context, hasPlayerArg);
		IHoldableMagic magic = HoldableArgument.getHoldable(context, "magic").getHoldable();
		boolean res = HMUtils.isUnlocked(player, magic);
		context.getSource().sendFeedback(CommandUtil.feedback(FEEDBACK_CHECK_H, res, player, magic), true);
		return Command.SINGLE_SUCCESS;
	}
	
	
	private static int add(CommandContext<CommandSource> context, boolean hasPlayerArg) throws CommandSyntaxException {
		ServerPlayerEntity player = CommandUtil.getPlayer(context, hasPlayerArg);
		IHoldableMagic magic = HoldableArgument.getHoldable(context, "magic").getHoldable();
		boolean res = HMUtils.addUnlock(player, HoldableArgument.getHoldable(context, "magic").getHoldable());
		context.getSource().sendFeedback(CommandUtil.feedback(FEEDBACK_ADD_H, res, player, magic), true);
		return Command.SINGLE_SUCCESS;
	}
	
	private static int remove(CommandContext<CommandSource> context, boolean hasPlayerArg) throws CommandSyntaxException {
		ServerPlayerEntity player = CommandUtil.getPlayer(context, hasPlayerArg);
		IHoldableMagic magic = HoldableArgument.getHoldable(context, "magic").getHoldable();
		boolean res = HMUtils.removeUnlocked(player, HoldableArgument.getHoldable(context, "magic").getHoldable());
		context.getSource().sendFeedback(CommandUtil.feedback(FEEDBACK_REMOVE_H, res, player, magic), true);
		return Command.SINGLE_SUCCESS;
	}
	
	
}
