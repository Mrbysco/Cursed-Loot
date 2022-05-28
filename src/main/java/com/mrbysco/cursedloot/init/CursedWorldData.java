package com.mrbysco.cursedloot.init;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.blocks.inventory.BaseChestInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CursedWorldData extends WorldSavedData {
	private static final String DATA_NAME = Reference.MOD_ID + "_world_data";

	private Map<UUID, BaseChestInventory> baseChestMap = new HashMap<>();
	private Map<String, BaseChestInventory> teamChestMap = new HashMap<>();

	public CursedWorldData() {
		super(DATA_NAME);
	}

	@Override
	public void load(CompoundNBT nbt) {
		ListNBT baseChestsList = nbt.getList("baseChests", Constants.NBT.TAG_COMPOUND);
		ListNBT teamChestsList = nbt.getList("teamChests", Constants.NBT.TAG_COMPOUND);

		baseChestMap.clear();
		teamChestMap.clear();

		for (int i = 0; i < baseChestsList.size(); ++i) {
			CompoundNBT tag = baseChestsList.getCompound(i);
			UUID uuid = tag.getUUID("Owner");
			int chestSize = tag.getInt("ChestSize");
			ListNBT chestTag = tag.getList("BaseChest", 10);
			BaseChestInventory inventory = new BaseChestInventory(chestSize);
			inventory.fromTag(chestTag);

			baseChestMap.put(uuid, inventory);
		}
		for (int i = 0; i < teamChestsList.size(); ++i) {
			CompoundNBT tag = teamChestsList.getCompound(i);
			String team = tag.getString("Team");
			int chestSize = tag.getInt("ChestSize");
			ListNBT chestTag = tag.getList("BaseChest", 10);
			BaseChestInventory inventory = new BaseChestInventory(chestSize);
			inventory.fromTag(chestTag);

			teamChestMap.put(team, inventory);
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		ListNBT baseChestsList = new ListNBT();
		for (Map.Entry<UUID, BaseChestInventory> entry : baseChestMap.entrySet()) {
			CompoundNBT baseChestsTag = new CompoundNBT();
			baseChestsTag.putUUID("Owner", entry.getKey());
			baseChestsTag.putInt("ChestSize", entry.getValue().getContainerSize());
			baseChestsTag.put("BaseChest", entry.getValue().createTag());
			baseChestsList.add(baseChestsTag);
		}
		compound.put("baseChests", baseChestsList);
		ListNBT teamChestsList = new ListNBT();
		for (Map.Entry<String, BaseChestInventory> entry : teamChestMap.entrySet()) {
			CompoundNBT teamChestTag = new CompoundNBT();
			teamChestTag.putString("Team", entry.getKey());
			teamChestTag.putInt("ChestSize", entry.getValue().getContainerSize());
			teamChestTag.put("BaseChest", entry.getValue().createTag());
			teamChestsList.add(teamChestTag);
		}
		compound.put("teamChests", teamChestsList);

		return compound;
	}

	public BaseChestInventory getInventoryFromUUID(UUID uuid) {
		if (baseChestMap.containsKey(uuid)) {
			return baseChestMap.get(uuid);
		} else {
			BaseChestInventory baseChestInventory = new BaseChestInventory(27);
			baseChestMap.put(uuid, baseChestInventory);
			return baseChestInventory;
		}
	}

	public BaseChestInventory getInventoryFromTeam(String teamName) {
		if (teamChestMap.containsKey(teamName)) {
			return teamChestMap.get(teamName);
		} else {
			BaseChestInventory baseChestInventory = new BaseChestInventory(27);
			teamChestMap.put(teamName, baseChestInventory);
			return baseChestInventory;
		}
	}

	public static CursedWorldData get(World world) {
		if (!(world instanceof ServerWorld)) {
			throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
		}
		ServerWorld overworld = world.getServer().getLevel(World.OVERWORLD);

		DimensionSavedDataManager storage = overworld.getDataStorage();
		return storage.computeIfAbsent(CursedWorldData::new, DATA_NAME);
	}
}
