package qianzha.heldmagic.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import qianzha.heldmagic.common.gui.HeldMagicContainers;

public class GuiCommand {

	public static ArgumentBuilder<CommandSource, ?>  register() {
		return Commands.literal("gui").executes(GuiCommand::openGui);
	}
	
	public static int openGui(CommandContext<CommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().asPlayer();
		HeldMagicContainers.openGuiSkillTree(player);
		return Command.SINGLE_SUCCESS;
	}
}
