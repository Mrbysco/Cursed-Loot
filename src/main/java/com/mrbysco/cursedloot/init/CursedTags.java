package com.mrbysco.cursedloot.init;

import com.mrbysco.cursedloot.Reference;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class CursedTags {
    public static final ITag.INamedTag<Item> CURRENCY_ITEMS = ItemTags.bind(Reference.MOD_ID + ":currency_items");
}
