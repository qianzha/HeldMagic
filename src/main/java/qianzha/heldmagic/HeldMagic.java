package qianzha.heldmagic;

import java.util.stream.Collectors;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import qianzha.heldmagic.common.EventHandler;
import qianzha.heldmagic.common.HMConstants;
import qianzha.heldmagic.common.command.CommandHandler;
import qianzha.heldmagic.config.CommonConfig;
import qianzha.heldmagic.util.HMLogger;
import qianzha.heldmagic.util.PluginUtil;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HMConstants.MODID)
public class HeldMagic
{

    public HeldMagic() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        
        // Config
    	ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
    	
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        // Register Commands
        MinecraftForge.EVENT_BUS.addListener(CommandHandler::serverStarting);
        
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        HMLogger.DEBUG.info("HELLO FROM PREINIT");
        PluginUtil.load();
        PermissionAPI.registerNode(HMConstants.Permission.UNLOCKED_ALL, DefaultPermissionLevel.NONE, "Unlocked all magic. ");
        PermissionAPI.registerNode(HMConstants.Permission.ALTER, DefaultPermissionLevel.OP, "Alter skill tree. ");
        
//        HeldMagicPacketHandler.registerMessage();
//        HMLogger.DEBUG.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    	HMLogger.DEBUG.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }
    
    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo(HMConstants.MODID, "helloworld", () -> {
        	HMLogger.DEBUG.info("Hello world from the MDK");
        	return "Hello world";
    	});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
    	HMLogger.DEBUG.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
}
