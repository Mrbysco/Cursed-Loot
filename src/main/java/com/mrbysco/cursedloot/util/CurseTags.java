package com.mrbysco.cursedloot.util;

import com.mrbysco.cursedloot.Reference;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public enum CurseTags {
	DESTROY_CURSE(0, "destroyCurse", "destroy_curse", true),
	REMAIN_HIDDEN(1, "remainHidden", "remain_hidden"),
	TOP_OR_BOTTOM(2, "topOrBottom", "top_or_bottom"),
	LEFT_OR_RIGHT(3, "leftOrRight", "left_or_right"),
	DESTROY_ITEM(4, "destroyItem", "destroy_item", true),
	ITEM_BECOMES_THIS(5, "itemBecomesThis", "item_becomes_this", true),
	ITEM_TO_SHOP(6, "itemToShop", "item_to_shop", true),
	HITS_BREAK_ITEM(7, "hitsBreaksItem", "hits_breaks_item");

	public static final String used_destroy_curse = Reference.MOD_PREFIX + "used_destroyCurse";
	public static final String HIDDEN_TAG = Reference.MOD_PREFIX + "hiddenItem";
	public static final String USED_TO_SHOP_TAG = Reference.MOD_PREFIX + "usedToShop";
	public static final String HITS_TAG = Reference.MOD_PREFIX + "hitsTaken";
	public static final String broken_item = Reference.MOD_PREFIX + "broken";

	private final int curseID;
	private final String curseTag;
	private final ResourceLocation textureLocation;
	private final ResourceLocation registryLocation;
	private final boolean directional;

	CurseTags(int curseId, String name, String registryPath) {
		this.curseID = curseId;
		this.curseTag = Reference.MOD_PREFIX + name;
		this.textureLocation = new ResourceLocation(Reference.MOD_ID, "textures/gui/" + registryPath + ".png");
		this.registryLocation = new ResourceLocation(Reference.MOD_ID, registryPath);
		this.directional = false;
	}

	CurseTags(int curseId, String name, String registryPath, boolean directional) {
		this.curseID = curseId;
		this.curseTag = Reference.MOD_PREFIX + name;
		this.textureLocation = new ResourceLocation(Reference.MOD_ID, "textures/gui/" + registryPath + ".png");
		this.registryLocation = new ResourceLocation(Reference.MOD_ID, registryPath);
		this.directional = directional;
	}

	public int getCurseID() {
		return this.curseID;
	}

	public String getCurseTag() {
		return this.curseTag;
	}

	public String getLowercaseCurseTag() {
		return this.curseTag.toLowerCase();
	}

	public ResourceLocation getTextureLocation() {
		return this.textureLocation;
	}

	public ResourceLocation getRegistryLocation() {
		return this.registryLocation;
	}

	public boolean isDirectional() {
		return this.directional;
	}

	public static CurseTags getByID(int ID) {
		return values()[ID];
	}

	public static CurseTags getByName(String name) {
		return CurseTags.valueOf(name);
	}

	@Nullable
	public static CurseTags getByRegistryName(ResourceLocation name) {
		for (CurseTags tag : values()) {
			if (tag.getRegistryLocation().equals(name)) {
				return tag;
			}
		}
		return null;
	}
}
