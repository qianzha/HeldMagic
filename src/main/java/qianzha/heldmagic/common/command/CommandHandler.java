package qianzha.heldmagic.common.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class CommandHandler {
    private static final Logger LOGGER = LogManager.getLogger();

	public static void serverStarting(FMLServerStartingEvent event) {
		LOGGER.info("qzhm, Command here");
		CommandDispatcher<CommandSource> dispatcher = event.getCommandDispatcher();
		LiteralCommandNode<CommandSource> cmd = dispatcher.register(Commands.literal("heldmagic")
				.then(Commands.literal("test").requires(source -> source.hasPermissionLevel(2))
						.then(HoldableCommand.register(true)))
				.then(HoldableCommand.register(false))
				.then(SkillTreeCommand.register())
		);
		dispatcher.register(Commands.literal("hm").redirect(cmd));
	}
	
}
