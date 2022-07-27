package com.mrbysco.cursedloot.util;

import com.mrbysco.cursedloot.init.CursedRegistry;
import com.mrbysco.cursedloot.util.info.CurseLocation;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CurseHelper {

	public static ItemStack applyRandomCurse(ItemStack stack) {
		return applyCurse(stack, getRandomTag());
	}

	public static ItemStack applyCurse(ItemStack stack, CurseTags curseTag) {
		CompoundTag tag = new CompoundTag();

		if (curseTag != null) {
			if (curseTag == CurseTags.REMAIN_HIDDEN) {
				ItemStack hiddenStack = new ItemStack(CursedRegistry.HIDDEN_ITEM.get());

				CompoundTag hiddenTag = new CompoundTag();
				hiddenTag.put(CurseTags.HIDDEN_TAG, stack.save(new CompoundTag()));
				hiddenTag.putBoolean(curseTag.getCurseTag(), true);

				hiddenStack.setTag(hiddenTag);
				return hiddenStack;
			} else {
				tag.putBoolean(curseTag.getCurseTag(), true);

				if (curseTag == CurseTags.DESTROY_CURSE) {
					tag.putBoolean(CurseTags.used_destroy_curse, false);
				}
				if (curseTag.isDirectional()) {
					String locationTag = CurseHelper.getRandomLocation().getDirectionTag();
					if (locationTag != null)
						tag.putBoolean(locationTag, true);
				}
				tag.putBoolean("cursedLoot", true);
				stack.setTag(tag);
			}
		}
		return stack;
	}

	public static CurseTags getRandomTag() {
		int random = new Random().nextInt(CurseTags.values().length);
		return CurseTags.values()[random];
	}

	public static List<ItemStack> revealStacks(ItemStack hiddenStack, @Nonnull CompoundTag tag) {
		ItemStack revealedStack = ItemStack.of(tag.getCompound(CurseTags.HIDDEN_TAG));
		if (hiddenStack.getCount() > 1) {
			int total = revealedStack.getCount() * hiddenStack.getCount();
			int stackAmount = (int) Math.ceil((double) total / (double) revealedStack.getMaxStackSize());
			if (stackAmount > 1) {
				List<ItemStack> stacks = new ArrayList<>();
				for (int s = 0; s < stackAmount; s++) {
					ItemStack stackCopy = revealedStack.copy();
					if (s == 0) {
						stackCopy.setCount(stackCopy.getMaxStackSize());
						total -= revealedStack.getMaxStackSize();
					} else {
						if (total > revealedStack.getMaxStackSize()) {
							stackCopy.setCount(stackCopy.getMaxStackSize());
							total -= revealedStack.getMaxStackSize();
						} else {
							stackCopy.setCount(total);
							total -= total;
						}
					}
					stacks.add(stackCopy);
				}
				return stacks;
			} else {
				return Collections.singletonList(revealedStack);
			}
		}
		return Collections.singletonList(revealedStack);
	}

	public static CurseDirection getRandomLocation() {
		int random = new Random().nextInt(CurseDirection.values().length);
		return CurseDirection.values()[random];
	}

	public static void addLore(List<Component> tooltips, CompoundTag compound) {
		for (CurseTags tag : CurseTags.values()) {
			if (compound.getBoolean(tag.getCurseTag())) {
				tooltips.add(Component.translatable(tag.getLowercaseCurseTag() + ".lore").withStyle(ChatFormatting.YELLOW));
				if (tag == CurseTags.HITS_BREAK_ITEM) {
					if (compound.getInt(CurseTags.HITS_TAG) > 0) {
						int hits = compound.getInt(CurseTags.HITS_TAG);
						tooltips.add(Component.translatable(CurseTags.HITS_TAG.toLowerCase() + ".lore", hits).withStyle(ChatFormatting.YELLOW));
					}
				}
			}
		}
	}

	public static CurseLocation getIconLocation(CompoundTag compound) {
		for (CurseTags curseTag : CurseTags.values()) {
			String tag = curseTag.getCurseTag();
			ResourceLocation textureLocation = curseTag.getTextureLocation();
			if (compound.getBoolean(tag)) {
				if (curseTag.isDirectional()) {
					return new CurseLocation(textureLocation, CurseDirection.getDirectionFromTag(compound));
				} else {
					return new CurseLocation(textureLocation);
				}
			}
		}
		return null;
	}

	public static boolean hasCurse(CompoundTag compound) {
		boolean hasCurse = false;
		for (CurseTags curseTag : CurseTags.values()) {
			if (compound.getBoolean(curseTag.getCurseTag())) {
				hasCurse = true;
			}
		}
		return hasCurse;
	}

	public static CompoundTag removeCurse(CompoundTag compound) {
		for (CurseTags curseTag : CurseTags.values()) {
			String tag = curseTag.getCurseTag();
			if (compound.getBoolean(tag)) {
				compound.remove(tag);
			}
		}
		removeDirections(compound);
		compound.remove(CurseTags.used_destroy_curse);
		compound.remove(CurseTags.USED_TO_SHOP_TAG);
		compound.remove("cursedLoot");
		if (compound.isEmpty()) {
			compound = null;
		} else {
			compound = compound.copy();
		}
		return compound;
	}

	public static CompoundTag removeDirections(CompoundTag compound) {
		for (CurseDirection curseDirection : CurseDirection.values()) {
			String tag = curseDirection.getDirectionTag();
			if (compound.getBoolean(tag)) {
				compound.remove(tag);
			}
		}
		return compound;
	}

	public static CurseTags getCurse(CompoundTag compound) {
		CurseTags foundCurse = null;
		for (CurseTags curseTag : CurseTags.values()) {
			String tag = curseTag.getCurseTag();
			if (compound.getBoolean(tag)) {
				foundCurse = CurseTags.valueOf(tag);
			}
		}
		return foundCurse;
	}
}
