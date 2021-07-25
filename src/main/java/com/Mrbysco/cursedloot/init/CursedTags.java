package com.mrbysco.cursedloot.init;

import com.mrbysco.cursedloot.Reference;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public class CursedTags {
    public static final Tag.Named<Item> CURRENCY_ITEMS = ItemTags.bind(Reference.MOD_ID + ":currency_items");
}
