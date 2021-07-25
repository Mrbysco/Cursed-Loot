package com.mrbysco.cursedloot.handlers;

import com.mrbysco.cursedloot.init.CursedRegistry;
import com.mrbysco.cursedloot.util.CurseHelper;
import com.mrbysco.cursedloot.util.CurseTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LootTableHandler {
	
	@SubscribeEvent
    public void onLootTableLoad(PlayerInteractEvent.RightClickBlock event) {
		Level world = event.getWorld();
		BlockPos pos = event.getPos();
		if(!world.isClientSide && event.getHand() == InteractionHand.MAIN_HAND) {
			BlockEntity te = world.getBlockEntity(pos);
			if(te instanceof RandomizableContainerBlockEntity) {
				RandomizableContainerBlockEntity chest = (RandomizableContainerBlockEntity)te;
				if(chest.lootTable != null) {
					for(int i = 0; i < chest.getContainerSize(); i++) {
						ItemStack stack = chest.getItem(i);
						if(!stack.isEmpty()) {
							if(world.random.nextInt(100) < 75) {
								CompoundTag tag = new CompoundTag();

								CurseTags curseTag = CurseHelper.getRandomTag();
								if(curseTag != null) {
									if(curseTag == CurseTags.REMAIN_HIDDEN) {
										ItemStack hiddenStack = new ItemStack(CursedRegistry.HIDDEN_ITEM.get());

										CompoundTag hiddenTag = new CompoundTag();
										hiddenTag.put(CurseTags.HIDDEN_TAG, stack.save(new CompoundTag()));
										hiddenTag.putBoolean(curseTag.getCurseTag(), true);

										hiddenStack.setTag(hiddenTag);
										chest.setItem(i, hiddenStack);
									} else {
										tag.putBoolean(curseTag.getCurseTag(), true);

										if(curseTag == CurseTags.DESTROY_CURSE) {
											tag.putBoolean(CurseTags.used_destroy_curse, false);
										}
										if(curseTag.isDirectional()) {
											String locationTag = CurseHelper.getRandomLocation().getDirectionTag();
											if(locationTag != null)
												tag.putBoolean(locationTag, true);
										}
										tag.putBoolean("cursedLoot", true);
										stack.setTag(tag);
										chest.setItem(i, stack);
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
