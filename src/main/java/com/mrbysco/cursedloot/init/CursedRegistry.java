package com.mrbysco.cursedloot.init;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.blocks.BaseChestBlock;
import com.mrbysco.cursedloot.item.HiddenItem;
import com.mrbysco.cursedloot.tileentity.BaseChestTile;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CursedRegistry {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Reference.MOD_ID);
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Reference.MOD_ID);

//    public static final RegistryObject<ContainerType<AltarContainer>> ALTAR_CONTAINER = CONTAINERS.register("leveling_altar",
//            () -> IForgeContainerType.create((windowId, inv, data) -> new AltarContainer(windowId, inv)));

	public static final RegistryObject<Block> BASE_CHEST = BLOCKS.register("base_chest", () -> new BaseChestBlock(Block.Properties.of(Material.WOOD)
			.strength(2.5F, 1000.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Item> BASE_CHEST_ITEM = ITEMS.register("base_chest", () -> new BlockItem(BASE_CHEST.get(), new Item.Properties().setISTER(() -> CurseISTERProvider.chest())));

	public static final RegistryObject<Item> HIDDEN_ITEM = ITEMS.register("hidden_item", () -> new HiddenItem(new Item.Properties()));

	public static final RegistryObject<TileEntityType<BaseChestTile>> BASE_CHEST_TILE = TILE_ENTITIES.register("base_chest", () -> TileEntityType.Builder.of(BaseChestTile::new, CursedRegistry.BASE_CHEST.get()).build(null));
}
