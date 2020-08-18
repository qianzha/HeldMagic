package qianzha.heldmagic.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
	public static final ForgeConfigSpec SPEC;
	public static final General GENERAL;
	
	static {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		GENERAL = new General(builder);
		SPEC = builder.build();
	}
	
	public static class General {
		public ForgeConfigSpec.BooleanValue canHotkey;
		
		public General(ForgeConfigSpec.Builder builder) {
			builder.comment("General settings").push("general");
			canHotkey = builder.define("canHotKey", true);
			builder.pop();
		}
	}
	
}
