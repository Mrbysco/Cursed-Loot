package com.mrbysco.cursedloot.handlers;

import com.mrbysco.cursedloot.blocks.inventory.BaseChestInventory;
import com.mrbysco.cursedloot.init.CursedWorldData;
import com.mrbysco.cursedloot.util.CurseHelper;
import com.mrbysco.cursedloot.util.CurseTags;
import com.mrbysco.cursedloot.util.InvHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ItemHandler {
	/*
	 * Sided Curse Pickup Handling
	 */
	@SubscribeEvent
	public void pickupEvent(EntityItemPickupEvent event) {
		final Player player = event.getEntity();
		final ItemEntity entity = event.getItem();
		final ItemStack stack = entity.getItem();
		if (stack.hasTag() && stack.getTag() != null) {
			CompoundTag tag = stack.getTag();
			Inventory inv = player.getInventory();

			if (tag.getBoolean(CurseTags.LEFT_OR_RIGHT.getCurseTag())) {
				if (InvHelper.getFirstEmptySideStack(inv.items) == -1) {
					event.setCanceled(true);
				}
			}
			if (tag.getBoolean(CurseTags.TOP_OR_BOTTOM.getCurseTag())) {
				if (InvHelper.getFirstEmptyTopStack(inv.items) == -1) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void inventoryEvent(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			final Player player = event.player;
			final Level level = player.level();

			Inventory inv = player.getInventory();
			for (int i = 0; i < inv.items.size(); i++) {
				if (!inv.getItem(i).isEmpty()) {
					ItemStack stack = inv.getItem(i);
					if (stack.hasTag() && stack.getTag() != null) {
						CompoundTag tag = stack.getTag();
						//TopOrBottomTag
						if (tag.getBoolean(CurseTags.TOP_OR_BOTTOM.getCurseTag())) {
							if (!InvHelper.isTop(i)) {
								ItemStack notTopStack = inv.getItem(i);
								player.drop(notTopStack, false);
								inv.setItem(i, ItemStack.EMPTY);
							}
						}
						//LeftOrRightTag
						if (tag.getBoolean(CurseTags.LEFT_OR_RIGHT.getCurseTag())) {
							if (!InvHelper.isSide(inv.items.size(), i)) {
								ItemStack notSideStack = inv.getItem(i);
								player.drop(notSideStack, false);
								inv.setItem(i, ItemStack.EMPTY);
							}
						}
						//DestroyTag
						if (tag.getBoolean(CurseTags.DESTROY_CURSE.getCurseTag())) {
							if (tag.getBoolean(CurseTags.used_destroy_curse)) {
								CompoundTag tag2 = tag.copy();
								tag2 = CurseHelper.removeCurse(tag2);
								ItemStack stack2 = stack.copy();

								stack2.setTag(tag2);
								inv.setItem(i, stack2);
							} else {
								int directionalSlot = InvHelper.getDirectionalSlotNumber(stack, i);
								if (directionalSlot != -1) {
									ItemStack directionalStack = inv.getItem(directionalSlot);
									if (directionalStack.hasTag() && directionalStack.getTag() != null) {
										CompoundTag dirTag = directionalStack.getTag();
										if (CurseHelper.hasCurse(dirTag)) {
											if (dirTag.getBoolean(CurseTags.REMAIN_HIDDEN.getCurseTag())) {
												List<ItemStack> revealedStacks = CurseHelper.revealStacks(directionalStack, dirTag);
												if (!revealedStacks.isEmpty()) {
													for (int s = 0; s < revealedStacks.size(); s++) {
														if (s == 0) {
															inv.setItem(directionalSlot, revealedStacks.get(s));
														} else {
															if (!inv.add(revealedStacks.get(s))) {
																player.drop(revealedStacks.get(s), false);
															}
														}
													}
												}
											} else {
												CompoundTag curseLessTag = CurseHelper.removeCurse(dirTag);

												directionalStack.setTag(curseLessTag);
											}
											tag.putBoolean(CurseTags.used_destroy_curse, true);
										}
									}
								}
							}
						}
						if (tag.getBoolean(CurseTags.ITEM_TO_SHOP.getCurseTag())) {
							if (tag.getBoolean(CurseTags.USED_TO_SHOP_TAG)) {
								CompoundTag tag2 = tag.copy();
								tag2 = CurseHelper.removeCurse(tag2);
								ItemStack stack2 = stack.copy();

								stack2.setTag(tag2);
								inv.setItem(i, stack2);
							} else {
								int directionalSlot = InvHelper.getDirectionalSlotNumber(stack, i);
								if (directionalSlot != -1) {
									ItemStack directionalStack = inv.getItem(directionalSlot);
									if (!directionalStack.isEmpty()) {
										BaseChestInventory inventory = InvHelper.getChestInventory(player, level);
										if (directionalStack.hasTag() && directionalStack.getTag() != null) {
											CompoundTag dirTag = directionalStack.getTag();
											if (CurseHelper.hasCurse(dirTag)) {
												if (dirTag.getBoolean(CurseTags.REMAIN_HIDDEN.getCurseTag())) {
													List<ItemStack> revealedStacks = CurseHelper.revealStacks(directionalStack, dirTag);
													if (!revealedStacks.isEmpty()) {
														for (ItemStack revealedStack : revealedStacks) {
															inventory.addItemStackToInventory(revealedStack);
														}
													}
												} else {
													CompoundTag curseLessTag = CurseHelper.removeCurse(dirTag);
													directionalStack.setTag(curseLessTag);
													inventory.addItemStackToInventory(directionalStack);
												}
											} else {
												inventory.addItemStackToInventory(directionalStack);
											}
										} else {
											inventory.addItemStackToInventory(directionalStack);
										}
										directionalStack.setCount(0);

										tag.putBoolean(CurseTags.USED_TO_SHOP_TAG, true);
										CursedWorldData.get(level).setDirty();
									}
								}
							}
						}
					}
				}
			}
		}
	}


	@SubscribeEvent
	public void damageEvent(LivingHurtEvent event) {
		if (event.getEntity() instanceof Player player) {
			if (event.getSource() != null) {
				Inventory inv = player.getInventory();
				for (int i = 0; i < inv.items.size(); i++) {
					if (!inv.getItem(i).isEmpty()) {
						ItemStack stack = inv.getItem(i);
						if (stack.hasTag() && stack.getTag() != null) {
							CompoundTag tag = stack.getTag();
							if (tag.getBoolean(CurseTags.HITS_BREAK_ITEM.getCurseTag())) {
								int hits = tag.getInt(CurseTags.HITS_TAG);
								if (hits > 5) {
									player.displayClientMessage(Component.translatable("cursedloot:hits.broken.item").append(stack.getHoverName()), true);
									inv.setItem(i, ItemStack.EMPTY);
								} else {
									tag.putInt(CurseTags.HITS_TAG, hits + 1);
									stack.setTag(tag);

									inv.setItem(i, stack);
								}
							}
						}
					}
				}
			}
		}
	}
}
