package com.mrbysco.cursedloot.init;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.blockentity.BaseChestBlockEntity;
import com.mrbysco.cursedloot.blocks.BaseChestBlock;
import com.mrbysco.cursedloot.item.BaseChestBlockItem;
import com.mrbysco.cursedloot.item.HiddenItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CursedRegistry {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Reference.MOD_ID);

	public static final DeferredBlock<Block> BASE_CHEST = BLOCKS.register("base_chest", () -> new BaseChestBlock(Block.Properties.ofFullCopy(Blocks.CHEST)
			.strength(2.5F, 1000.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<Item> BASE_CHEST_ITEM = ITEMS.register("base_chest", () -> new BaseChestBlockItem(BASE_CHEST.get(), new Item.Properties()));

	public static final DeferredItem<Item> HIDDEN_ITEM = ITEMS.register("hidden_item", () -> new HiddenItem(new Item.Properties()));

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BaseChestBlockEntity>> BASE_CHEST_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("base_chest", () -> BlockEntityType.Builder.of(BaseChestBlockEntity::new, CursedRegistry.BASE_CHEST.get()).build(null));
}
