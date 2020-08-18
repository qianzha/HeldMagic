package qianzha.heldmagic.common.data;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

public class HMWorldSavedData extends WorldSavedData {
	public static final String NAME = "HeldMagic-Magic";
	private Map<UUID, MagicSkillTree> skillTrees = Maps.newHashMap();
	
	public HMWorldSavedData() {
		super(NAME);
	}
	
	public static HMWorldSavedData getSavedData(World worldIn) {
		if(worldIn.isRemote) {
			throw new RuntimeException("Attempted to get the data from a client world. ");
		}
		ServerWorld world = worldIn.getServer().getWorld(DimensionType.OVERWORLD);
		DimensionSavedDataManager manager = world.getSavedData();
		return manager.getOrCreate(HMWorldSavedData::new, NAME);
	}
	
	public MagicSkillTree getSkillTree(UUID playerUUID) {
		if(!skillTrees.containsKey(playerUUID)) {
			skillTrees.put(playerUUID, MagicSkillTree.create(playerUUID).setParent(this));
		}
		return skillTrees.get(playerUUID);
	}
	
	@Override
	public void read(CompoundNBT nbt) {
		ListNBT skillTreesNBT = nbt.getList("skillTrees", NBT.TAG_COMPOUND);
		skillTreesNBT.forEach((obj) -> {
			MagicSkillTree tree = MagicSkillTree.fromNBT((CompoundNBT) obj).setParent(this);
			skillTrees.put(tree.getPlayerUUID(), tree);
		});
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		ListNBT skillTreesNBT = new ListNBT();
		for(MagicSkillTree tree : skillTrees.values()) {
			skillTreesNBT.add(tree.serializeNBT());
		}
		compound.put("skillTrees", skillTreesNBT);
		return compound;
	}

}
