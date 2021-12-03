package com.mrbysco.cursedloot.init;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.blocks.inventory.BaseChestInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CursedWorldData extends SavedData {
	private static final String DATA_NAME = Reference.MOD_ID + "_world_data";

	private Map<UUID, BaseChestInventory> baseChestMap = new HashMap<>();
	private Map<String, BaseChestInventory> teamChestMap = new HashMap<>();


	public CursedWorldData(Map<UUID, BaseChestInventory> baseMap, Map<String, BaseChestInventory> teamMap) {
		this.baseChestMap = baseMap;
		this.teamChestMap = teamMap;
	}

	public CursedWorldData() {
		this(new HashMap<>(), new HashMap<>());
	}

	public static CursedWorldData load(CompoundTag tag) {
		ListTag baseChestsList = tag.getList("baseChests", CompoundTag.TAG_COMPOUND);
		ListTag teamChestsList = tag.getList("teamChests", CompoundTag.TAG_COMPOUND);

		Map<UUID, BaseChestInventory> baseMap = new HashMap<>();
		Map<String, BaseChestInventory> teamMap = new HashMap<>();

		for (int i = 0; i < baseChestsList.size(); ++i) {
			CompoundTag listTag = baseChestsList.getCompound(i);
			UUID uuid = listTag.getUUID("Owner");
			int chestSize = listTag.getInt("ChestSize");
			ListTag chestTag = listTag.getList("BaseChest", 10);
			BaseChestInventory inventory = new BaseChestInventory(chestSize);
			inventory.fromTag(chestTag);

			baseMap.put(uuid, inventory);
		}
		for (int i = 0; i < teamChestsList.size(); ++i) {
			CompoundTag teamTag = teamChestsList.getCompound(i);
			String team = teamTag.getString("Team");
			int chestSize = teamTag.getInt("ChestSize");
			ListTag chestTag = teamTag.getList("BaseChest", 10);
			BaseChestInventory inventory = new BaseChestInventory(chestSize);
			inventory.fromTag(chestTag);

			teamMap.put(team, inventory);
		}
		return new CursedWorldData(baseMap, teamMap);
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		ListTag baseChestsList = new ListTag();
		for (Map.Entry<UUID, BaseChestInventory> entry : baseChestMap.entrySet()) {
			CompoundTag baseChestsTag = new CompoundTag();
			baseChestsTag.putUUID("Owner", entry.getKey());
			baseChestsTag.putInt("ChestSize", entry.getValue().getContainerSize());
			baseChestsTag.put("BaseChest", entry.getValue().createTag());
			baseChestsList.add(baseChestsTag);
		}
		compound.put("baseChests", baseChestsList);
		ListTag teamChestsList = new ListTag();
		for (Map.Entry<String, BaseChestInventory> entry : teamChestMap.entrySet()) {
			CompoundTag teamChestTag = new CompoundTag();
			teamChestTag.putString("Team", entry.getKey());
			teamChestTag.putInt("ChestSize", entry.getValue().getContainerSize());
			teamChestTag.put("BaseChest", entry.getValue().createTag());
			teamChestsList.add(teamChestTag);
		}
		compound.put("teamChests", teamChestsList);

		return compound;
	}

	public BaseChestInventory getInventoryFromUUID(UUID uuid) {
		if(baseChestMap.containsKey(uuid)) {
			return baseChestMap.get(uuid);
		} else {
			BaseChestInventory baseChestInventory = new BaseChestInventory(27);
			baseChestMap.put(uuid, baseChestInventory);
			return baseChestInventory;
		}
	}

	public BaseChestInventory getInventoryFromTeam(String teamName) {
		if(teamChestMap.containsKey(teamName)) {
			return teamChestMap.get(teamName);
		} else {
			BaseChestInventory baseChestInventory = new BaseChestInventory(27);
			teamChestMap.put(teamName, baseChestInventory);
			return baseChestInventory;
		}
	}

	public static CursedWorldData get(Level world) {
		if (!(world instanceof ServerLevel)) {
			throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
		}
		ServerLevel overworld = world.getServer().getLevel(Level.OVERWORLD);

		DimensionDataStorage storage = overworld.getDataStorage();
		return storage.computeIfAbsent(CursedWorldData::load, CursedWorldData::new, DATA_NAME);
	}
}
