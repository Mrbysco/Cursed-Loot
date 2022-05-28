package com.mrbysco.cursedloot.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mrbysco.cursedloot.init.CursedRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CursedDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();

		if (event.includeServer()) {
			generator.addProvider(new Loots(generator));
		}
	}

	private static class Loots extends LootTableProvider {

		public Loots(DataGenerator gen) {
			super(gen);
		}

		@Override
		protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootParameterSet>> getTables() {
			return ImmutableList.of(Pair.of(CursedBlockTables::new, LootParameterSets.BLOCK));
		}

		public static class CursedBlockTables extends BlockLootTables {
			@Override
			protected void addTables() {
				dropSelf(CursedRegistry.BASE_CHEST.get());
			}

			@Override
			protected Iterable<Block> getKnownBlocks() {
				return (Iterable<Block>) CursedRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
			}
		}

		@Override
		protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationTracker) {
			map.forEach((name, table) -> LootTableManager.validate(validationTracker, name, table));
		}
	}
}
