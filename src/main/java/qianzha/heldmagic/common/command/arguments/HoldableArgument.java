package qianzha.heldmagic.common.command.arguments;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import qianzha.heldmagic.api.magic.IHoldableMagic;
import qianzha.heldmagic.api.unit.HMUtils;

public class HoldableArgument implements ArgumentType<HoldableInput>{
	public static final Collection<String> EXAMPLES = Arrays.asList("heldmagic:example_holdable");
	public static final DynamicCommandExceptionType MAGIC_UNKNOWN_TYPE = new DynamicCommandExceptionType((arg) -> {
		return new TranslationTextComponent("text.heldmagic.magic_unknown", arg);
	});
	
	public static final SuggestionProvider<CommandSource> SUGGEST_ALL = (context, builder) -> {
		Stream<String> stream = HMUtils.listMagic(null).map(HMUtils::magicStringId);
		return ISuggestionProvider.suggest(stream, builder);
	};
	public static final SuggestionProvider<CommandSource> SUGGEST_PLAYER = (context, builder) -> {
		CommandSource source = context.getSource();
		Stream<String> stream;
		try {
			PlayerEntity player = source.asPlayer();
			stream = HMUtils.listMagic(player).map(HMUtils::magicStringId);
		} catch (CommandSyntaxException e) {
			stream = HMUtils.listMagic(null).map(HMUtils::magicStringId);
		}
		return ISuggestionProvider.suggest(stream, builder);
	};
	
	
	private boolean listAll;
	
	public static HoldableArgument holdable(boolean listAll) {
		return new HoldableArgument(listAll);
	}
	
	public HoldableArgument(boolean listAll) {
		this.listAll = listAll;
	}

	
	public static SuggestionProvider<CommandSource> getSuggestion(boolean listAll) {
		return listAll ? SUGGEST_ALL : SUGGEST_PLAYER;
	}
	
	@Override
	public HoldableInput parse(StringReader reader) throws CommandSyntaxException {
		ResourceLocation id = ResourceLocation.read(reader);
		IHoldableMagic magic = HMUtils.getMagic(id);
		if(magic == null) throw MAGIC_UNKNOWN_TYPE.create(id);
		return new HoldableInput(listAll, magic);
	}
	
	public static <S> HoldableInput getHoldable(CommandContext<S> context, String name) {
		return context.getArgument(name, HoldableInput.class);
	}
	
	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}


	
}
