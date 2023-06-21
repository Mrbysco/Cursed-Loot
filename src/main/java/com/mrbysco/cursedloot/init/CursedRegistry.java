package com.mrbysco.cursedloot.init;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.blockentity.BaseChestBlockEntity;
import com.mrbysco.cursedloot.blocks.BaseChestBlock;
import com.mrbysco.cursedloot.item.BaseChestBlockItem;
import com.mrbysco.cursedloot.item.HiddenItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CursedRegistry {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Reference.MOD_ID);

	public static final RegistryObject<Block> BASE_CHEST = BLOCKS.register("base_chest", () -> new BaseChestBlock(Block.Properties.copy(Blocks.CHEST)
			.strength(2.5F, 1000.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Item> BASE_CHEST_ITEM = ITEMS.register("base_chest", () -> new BaseChestBlockItem(BASE_CHEST.get(), new Item.Properties()));

	public static final net.minecraftforge.registries.RegistryObject<Item> HIDDEN_ITEM = ITEMS.register("hidden_item", () -> new HiddenItem(new Item.Properties()));

	public static final RegistryObject<BlockEntityType<BaseChestBlockEntity>> BASE_CHEST_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("base_chest", () -> BlockEntityType.Builder.of(BaseChestBlockEntity::new, CursedRegistry.BASE_CHEST.get()).build(null));
}
