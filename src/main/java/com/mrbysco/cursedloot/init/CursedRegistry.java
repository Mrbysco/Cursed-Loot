package com.mrbysco.cursedloot.init;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.blockentity.BaseChestBlockEntity;
import com.mrbysco.cursedloot.blocks.BaseChestBlock;
import com.mrbysco.cursedloot.item.BaseChestBlockItem;
import com.mrbysco.cursedloot.item.HiddenItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CursedRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Reference.MOD_ID);

    public static final RegistryObject<Block> BASE_CHEST = BLOCKS.register("base_chest", () -> new BaseChestBlock(Block.Properties.of(Material.WOOD)
            .strength(2.5F, 1000.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Item> BASE_CHEST_ITEM = ITEMS.register("base_chest", () -> new BaseChestBlockItem(BASE_CHEST.get(), new Item.Properties()));

    public static final RegistryObject<Item> HIDDEN_ITEM = ITEMS.register("hidden_item", () -> new HiddenItem(new Item.Properties()));

    public static final RegistryObject<BlockEntityType<BaseChestBlockEntity>> BASE_CHEST_BLOCK_ENTITY = BLOCK_ENTITIES.register("base_chest", () -> BlockEntityType.Builder.of(BaseChestBlockEntity::new, CursedRegistry.BASE_CHEST.get()).build(null));
}
