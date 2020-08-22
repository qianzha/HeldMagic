package qianzha.heldmagic.common.gui;

import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import qianzha.heldmagic.api.magic.IHoldableMagic;
import qianzha.heldmagic.api.unit.HMUtils;

public class SkillTreeContainer extends Container {
	private PlayerEntity treeOwner;
	private SortedSet<IHoldableMagic> unlocked;
	private SortedSet<IHoldableMagic> locked;
	private ImmutableList<String> namespaces = HMUtils.API.getHoldableRegistration().getNamespaces();
	private String selectedNamespace = null;
	private IHoldableMagic select = null;
	

	public static SkillTreeContainer createByPacket(int windowId, PlayerInventory inv, PacketBuffer data) {
		byte[] bytes = data.readByteArray();
		SortedSet<IHoldableMagic> unlocked = Sets.newTreeSet(HMUtils.MAGIC_COMPARATOR);
		SortedSet<IHoldableMagic> locked = Sets.newTreeSet(HMUtils.MAGIC_COMPARATOR);
		for(int i=0; i<bytes.length; i++) {
			IHoldableMagic magic = HMUtils.getRegistedMagic().get(i);
			if(bytes[i] != 0) {
				unlocked.add(magic);
			}
			else {
				locked.add(magic);
			}
		}
		return new SkillTreeContainer(windowId, Minecraft.getInstance().player, unlocked, locked);
	}
	
	public SkillTreeContainer(int id, PlayerEntity player, SortedSet<IHoldableMagic> unlocked, SortedSet<IHoldableMagic> locked) {
		super(HeldMagicContainers.SKILLTREE_CONTAINER, id);
		this.treeOwner = player;
		this.unlocked = unlocked;
		this.locked = locked;

		for(int i=0; i<9; i++) {
			addSlot(new Slot(player.inventory, i, i*18, 0));
		}
		addSlot(new Slot(player.inventory, 40, 164, 0) {
	       @OnlyIn(Dist.CLIENT)
	       public Pair<ResourceLocation, ResourceLocation> getBackground() {
	          return Pair.of(PlayerContainer.LOCATION_BLOCKS_TEXTURE, PlayerContainer.EMPTY_ARMOR_SLOT_SHIELD);
	       }
	    });
	}
	
	
	public List<IHoldableMagic> getMagicList(boolean isUnlocked) {
		Stream<IHoldableMagic> stream = (isUnlocked ? unlocked : locked).stream();
		if(getSelectedNamespace() == null) return stream.collect(Collectors.toList());
		return stream.filter(m -> m.getRegistryName().getNamespace().compareTo(getSelectedNamespace())==0)
				.collect(Collectors.toList());
	}
	
	public ImmutableList<String> getNamespaces() {
		return namespaces;
	}
	
	public void setSelectedNamespace(String selectedNamespace) {
		this.selectedNamespace = selectedNamespace;
	}
	
	public String getSelectedNamespace() {
		return selectedNamespace;
	}
	
	public void setSelect(IHoldableMagic select) {
		this.select = select;
	}
	
	public IHoldableMagic getSelect() {
		return select;
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return treeOwner.getGameProfile().equals(playerIn.getGameProfile());
	}
	
}
