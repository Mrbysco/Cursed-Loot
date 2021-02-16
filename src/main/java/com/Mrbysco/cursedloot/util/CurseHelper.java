package com.mrbysco.cursedloot.util;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.util.info.CurseLocation;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Random;

public class CurseHelper {

	public static CurseTags getRandomTag() {
		int random = new Random().nextInt(CurseTags.values().length);
		return CurseTags.values()[random];
	}

	public static CurseDirection getRandomLocation() {
		int random = new Random().nextInt(CurseDirection.values().length);
		return CurseDirection.values()[random];
	}

	public static void addLore(List<ITextComponent> tooltips, CompoundNBT compound) {
		for(CurseTags tag : CurseTags.values()) {
			if(compound.getBoolean(tag.getCurseTag())) {
				tooltips.add(Reference.emptyComponent);
				tooltips.add(Reference.emptyComponent);
				tooltips.add(Reference.emptyComponent);
				tooltips.add(Reference.emptyComponent);
				tooltips.add(new TranslationTextComponent(tag.getLowercaseCurseTag() + ".lore").mergeStyle(TextFormatting.YELLOW));
				if(tag == CurseTags.HITS_BREAK_ITEM) {
					if(compound.getInt(CurseTags.HITS_TAG) > 0) {
						int hits = compound.getInt(CurseTags.HITS_TAG);
						tooltips.add(new TranslationTextComponent(CurseTags.HITS_TAG.toLowerCase() + ".lore", hits).mergeStyle(TextFormatting.YELLOW));
					}
				}
			}
		}
	}

	public static CurseLocation getIconLocation(CompoundNBT compound) {
		for(CurseTags curseTag : CurseTags.values()) {
			String tag = curseTag.getCurseTag();
			ResourceLocation textureLocation = curseTag.getTextureLocation();
			if(compound.getBoolean(tag)) {
				if(curseTag.isDirectional()) {
					return new CurseLocation(textureLocation, CurseDirection.getDirectionFromTag(compound));
				} else {
					return new CurseLocation(textureLocation);
				}
			}
		}
		return null;
	}

	public static boolean hasCurse(CompoundNBT compound) {
		boolean hasCurse = false;
		for(CurseTags curseTag : CurseTags.values()) {
			if(compound.getBoolean(curseTag.getCurseTag())) {
				hasCurse = true;
			}
		}
		return hasCurse;
	}

	public static CompoundNBT removeCurse(CompoundNBT compound) {
		for(CurseTags curseTag : CurseTags.values()) {
			String tag = curseTag.getCurseTag();
			if(compound.getBoolean(tag)) {
				compound.remove(tag);
			}
		}
		removeDirections(compound);
		compound.remove(CurseTags.used_destroy_curse);
		compound.remove("cursedLoot");
		return compound;
	}

	public static CompoundNBT removeDirections(CompoundNBT compound) {
		for(CurseDirection curseDirection : CurseDirection.values()) {
			String tag = curseDirection.getDirectionTag();
			if(compound.getBoolean(tag)) {
				compound.remove(tag);
			}
		}
		return compound;
	}

	public static CurseTags getCurse(CompoundNBT compound) {
		CurseTags foundCurse = null;
		for(CurseTags curseTag : CurseTags.values()) {
			String tag = curseTag.getCurseTag();
			if(compound.getBoolean(tag)) {
				foundCurse = CurseTags.valueOf(tag);
			}
		}
		return foundCurse;
	}

	public static void executeCurse(ServerPlayerEntity player, int slot) {
//		player.getCapability(BaseChestCapProvider.BASE_CHEST_CAP, null).ifPresent(c -> {
//			BaseInventory baseInv = c.getInventory();
//			ItemStack stack = baseInv.getStackInSlot(slot);
//		});
	}
}
