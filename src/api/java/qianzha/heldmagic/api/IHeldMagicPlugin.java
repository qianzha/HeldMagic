package qianzha.heldmagic.api;

public interface IHeldMagicPlugin {

	/**
     * Register mod content with the API. Called during {@link net.minecraftforge.fml.common.event.FMLInitializationEvent}.
     *
     * @param api The active instance of the {@link IBloodMagicAPI}
     */
    void register(IHeldMagicAPI api);
	
	
}
