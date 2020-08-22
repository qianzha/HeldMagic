package qianzha.heldmagic.common;

import net.minecraft.block.Block;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import qianzha.heldmagic.api.unit.HMUtils;
import qianzha.heldmagic.common.gui.SkillTreeContainer;
import qianzha.heldmagic.common.plugin.test.item.ExampleHoldableMagicItem;
import qianzha.heldmagic.util.HMLogger;

public class EventHandler {
	
	@SubscribeEvent
	public void playerHeldItemStack(PlayerTickEvent event) {
		if(event.phase == Phase.START) return;
		ItemStack stack = event.player.inventory.getItemStack();
		if(HMUtils.isMagicStack(stack)) {
			event.player.inventory.setItemStack(HMUtils.getSavingStack(stack));
		}
	}
	
	@SubscribeEvent
	public void ItemToss(ItemTossEvent event) {
		HMLogger.DEBUG.info(event.getEntityItem().toString() + ", " + event.getPlayer().toString());
		ItemEntity item = event.getEntityItem();
		ItemStack srcStack = item.getItem();
		if(HMUtils.isMagicStack(srcStack)) {
			item.setItem(HMUtils.getSavingStack(srcStack));
		}
	}
	
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
        }
        
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
    		try {
				Class.forName("qianzha.heldmagic.api.IHeldMagicAPI");
				// register Holdable Magic Item when API has been loaded
				event.getRegistry().register(new ExampleHoldableMagicItem().setRegistryName(HMConstants.MODID, "held_item"));
			} catch (ClassNotFoundException e) {
				HMLogger.DEFAULT.info("HeldMagic API has not been loaded. ");
			}
        }
        
    	@SubscribeEvent
    	public static void registerContainers (RegistryEvent.Register<ContainerType<?>> event) {
            event.getRegistry().register(IForgeContainerType.create(SkillTreeContainer::createByPacket)
            		.setRegistryName(HMConstants.MODID, "skilltree_container"));
        }
    }
    
}
