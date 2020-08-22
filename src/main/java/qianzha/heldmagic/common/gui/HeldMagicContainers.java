package qianzha.heldmagic.common.gui;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.ScreenManager.IScreenFactory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ObjectHolder;
import qianzha.heldmagic.api.unit.HMUtils;
import qianzha.heldmagic.client.gui.SkillTreeContainerScreen;
import qianzha.heldmagic.common.HMConstants;
import qianzha.heldmagic.common.data.MagicSkillTree;

@ObjectHolder(HMConstants.MODID)
public class HeldMagicContainers {
	public static final ContainerType<SkillTreeContainer> SKILLTREE_CONTAINER = null;
	
	public static void registerScreenFactory() {
		ScreenManager.registerFactory(SKILLTREE_CONTAINER, new IScreenFactory<SkillTreeContainer, SkillTreeContainerScreen>() {
			@Override
			public SkillTreeContainerScreen create(SkillTreeContainer container, PlayerInventory inv, ITextComponent title) {
				return new SkillTreeContainerScreen(container, inv, title);
			}
		});
	}
	
	public static void openGuiSkillTree(ServerPlayerEntity player) {
		NetworkHooks.openGui(player, (MagicSkillTree) HMUtils.API.getSkillTree(player), (buf) -> {
				byte[] unlocked = new byte[HMUtils.getRegistedMagic().size()];
				for(int i=0; i<unlocked.length; i++) {
					unlocked[i] = (byte) (HMUtils.isUnlocked(player, HMUtils.getRegistedMagic().get(i)) ? 1 : 0);
				}
				buf.writeByteArray(unlocked);
		});
	}
}
