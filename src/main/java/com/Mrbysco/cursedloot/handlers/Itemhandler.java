package com.mrbysco.cursedloot.handlers;

import com.mrbysco.cursedloot.blocks.inventory.BaseChestInventory;
import com.mrbysco.cursedloot.init.CursedWorldData;
import com.mrbysco.cursedloot.util.CurseHelper;
import com.mrbysco.cursedloot.util.CurseTags;
import com.mrbysco.cursedloot.util.InvHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
		PlayerEntity player = event.getPlayer();
		ItemEntity entity = event.getItem();
		ItemStack stack = entity.getItem();
		if(stack.hasTag() && stack.getTag() != null) {
			CompoundNBT tag = stack.getTag();
			PlayerInventory inv = player.inventory;
			
			if(tag.getBoolean(CurseTags.LEFT_OR_RIGHT.getCurseTag())) {
				if(InvHelper.getFirstEmptySideStack(inv.mainInventory) == -1) {
					event.setCanceled(true);
				}
			}
			if(tag.getBoolean(CurseTags.TOP_OR_BOTTOM.getCurseTag())) {
				if(InvHelper.getFirstEmptyTopStack(inv.mainInventory) == -1) {
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void inventoryEvent(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			PlayerEntity player = event.player;
			World world = player.world;

			PlayerInventory inv = player.inventory;
			for(int i = 0; i < inv.mainInventory.size(); i++) {
				if(!inv.getStackInSlot(i).isEmpty()) {
					ItemStack stack = inv.getStackInSlot(i);
					if(stack.hasTag() && stack.getTag() != null) {
						CompoundNBT tag = stack.getTag();
						//TopOrBottomTag
						if(tag.getBoolean(CurseTags.TOP_OR_BOTTOM.getCurseTag())) {
							if(!InvHelper.isTop(i)) {
								ItemStack notTopStack = inv.getStackInSlot(i);
								player.dropItem(notTopStack, false);
								inv.setInventorySlotContents(i, ItemStack.EMPTY);
							}
						}
						//LeftOrRightTag
						if(tag.getBoolean(CurseTags.LEFT_OR_RIGHT.getCurseTag())) {
							if(!InvHelper.isSide(inv.mainInventory.size(), i)) {
								ItemStack notSideStack = inv.getStackInSlot(i);
								player.dropItem(notSideStack, false);
								inv.setInventorySlotContents(i, ItemStack.EMPTY);
							}
						}
						//DestroyTag
						if(tag.getBoolean(CurseTags.DESTROY_CURSE.getCurseTag())) {
							if(tag.getBoolean(CurseTags.used_destroy_curse)) {
								CompoundNBT tag2 = tag.copy();
								tag2 = CurseHelper.removeCurse(tag2);
								ItemStack stack2 = stack.copy();

								stack2.setTag(tag2);
								inv.setInventorySlotContents(i, stack2);
							} else {
								int directionalSlot = InvHelper.getDirectionalSlotNumber(stack, i);
								if(directionalSlot != -1) {
									ItemStack directionalStack = inv.getStackInSlot(directionalSlot);
									if(directionalStack.hasTag() && directionalStack.getTag() != null) {
										CompoundNBT dirTag = directionalStack.getTag();
										if(CurseHelper.hasCurse(dirTag)) {
											if(dirTag.getBoolean(CurseTags.REMAIN_HIDDEN.getCurseTag())) {
												List<ItemStack> revealedStacks = CurseHelper.revealStacks(directionalStack, dirTag);
												if(!revealedStacks.isEmpty()) {
													for(int s = 0; s < revealedStacks.size(); s++) {
														if(s == 0) {
															inv.setInventorySlotContents(directionalSlot, revealedStacks.get(s));
														} else {
															if(!inv.addItemStackToInventory(revealedStacks.get(s))) {
																player.dropItem(revealedStacks.get(s), false);
															}
														}
													}
												}
											} else {
												CompoundNBT curseLessTag = CurseHelper.removeCurse(dirTag);
												
												directionalStack.setTag(curseLessTag);
											}
											tag.putBoolean(CurseTags.used_destroy_curse, true);
										}
									}
								}
							}
						}
						if(stack.getItem() == Items.ROTTEN_FLESH) {
							System.out.println(stack.getTag());
						}
						if(tag.getBoolean(CurseTags.ITEM_TO_SHOP.getCurseTag())) {
							if(tag.getBoolean(CurseTags.USED_TO_SHOP_TAG)) {
								CompoundNBT tag2 = tag.copy();
								tag2 = CurseHelper.removeCurse(tag2);
								ItemStack stack2 = stack.copy();

								stack2.setTag(tag2);
								inv.setInventorySlotContents(i, stack2);
							} else {
								int directionalSlot = InvHelper.getDirectionalSlotNumber(stack, i);
								if(directionalSlot != -1) {
									ItemStack directionalStack = inv.getStackInSlot(directionalSlot);
									if(!directionalStack.isEmpty()) {
										BaseChestInventory inventory = InvHelper.getChestInventory(player, world);
										if(directionalStack.hasTag() && directionalStack.getTag() != null) {
											CompoundNBT dirTag = directionalStack.getTag();
											if(CurseHelper.hasCurse(dirTag)) {
												if(dirTag.getBoolean(CurseTags.REMAIN_HIDDEN.getCurseTag())) {
													List<ItemStack> revealedStacks = CurseHelper.revealStacks(directionalStack, dirTag);
													if(!revealedStacks.isEmpty()) {
														for(ItemStack revealedStack : revealedStacks) {
															inventory.addItemStackToInventory(revealedStack);
														}
													}
												} else {
													CompoundNBT curseLessTag = CurseHelper.removeCurse(dirTag);
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
										CursedWorldData.get(world).markDirty();
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
		if(event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getEntityLiving();

			if(event.getSource() != null) {
				PlayerInventory inv = player.inventory;
				for(int i = 0; i < inv.mainInventory.size(); i++) {
					if(!inv.getStackInSlot(i).isEmpty()) {
						ItemStack stack = inv.getStackInSlot(i);
						if(stack.hasTag() && stack.getTag() != null) {
							CompoundNBT tag = stack.getTag();
							if(tag.getBoolean(CurseTags.HITS_BREAK_ITEM.getCurseTag())) {
								int hits = tag.getInt(CurseTags.HITS_TAG);
								if(hits > 5) {
									player.sendStatusMessage(new TranslationTextComponent("cursedloot:hits.broken.item").appendSibling(stack.getDisplayName()), true);
									inv.setInventorySlotContents(i, ItemStack.EMPTY);
								} else {
									tag.putInt(CurseTags.HITS_TAG, hits + 1);
									stack.setTag(tag);
									
									inv.setInventorySlotContents(i, stack);
								}
							}
						}
					}
				}
			}
		}
	}
}
