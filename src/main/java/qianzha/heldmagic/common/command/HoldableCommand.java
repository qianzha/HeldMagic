package qianzha.heldmagic.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.server.command.EnumArgument;
import qianzha.heldmagic.api.unit.HMUtils;
import qianzha.heldmagic.common.command.arguments.HoldableArgument;
import qianzha.heldmagic.common.command.arguments.HoldableInput;

public class HoldableCommand {
	
	public static ArgumentBuilder<CommandSource, ?> register(boolean listAll) {
		return Commands.literal("holdable")
			.then(Commands.argument("magic", HoldableArgument.holdable(listAll))
				.suggests(HoldableArgument.getSuggestion(listAll))
				.executes(HoldableCommand::doTestDefault)
				.then(Commands.argument("hand", EnumArgument.enumArgument(Hand.class)).executes(HoldableCommand::doTest)));
	}
	
	private static int doTest(CommandContext<CommandSource> context) throws CommandSyntaxException {
		return doTest(context, HoldableArgument.getHoldable(context, "magic"), context.getArgument("hand", Hand.class));
	}
	
	private static int doTestDefault(CommandContext<CommandSource> context) throws CommandSyntaxException {
		return doTest(context, HoldableArgument.getHoldable(context, "magic"), Hand.MAIN_HAND);
	}
	
	public static int doTest(CommandContext<CommandSource> context, HoldableInput magic, Hand hand) throws CommandSyntaxException {
		PlayerEntity player = context.getSource().asPlayer();
		if(magic.isListAll()) {
			HMUtils.equipHoldableWithoutCheck(magic.getHoldable(), player, hand);
		}
		else {
			HMUtils.equipHoldable(magic.getHoldable(), player, hand);
		}
		return Command.SINGLE_SUCCESS;
	}
	
}
