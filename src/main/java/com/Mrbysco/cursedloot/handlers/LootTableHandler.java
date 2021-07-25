package com.mrbysco.cursedloot.handlers;

import com.mrbysco.cursedloot.init.CursedRegistry;
import com.mrbysco.cursedloot.util.CurseHelper;
import com.mrbysco.cursedloot.util.CurseTags;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LootTableHandler {
	
	@SubscribeEvent
    public void onLootTableLoad(PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		if(!world.isClientSide && event.getHand() == Hand.MAIN_HAND) {
			TileEntity te = world.getBlockEntity(pos);
			if(te instanceof LockableLootTileEntity) {
				LockableLootTileEntity chest = (LockableLootTileEntity)te;
				if(chest.lootTable != null) {
					for(int i = 0; i < chest.getContainerSize(); i++) {
						ItemStack stack = chest.getItem(i);
						if(!stack.isEmpty()) {
							if(world.random.nextInt(100) < 75) {
								CompoundNBT tag = new CompoundNBT();

								CurseTags curseTag = CurseHelper.getRandomTag();
								if(curseTag != null) {
									if(curseTag == CurseTags.REMAIN_HIDDEN) {
										ItemStack hiddenStack = new ItemStack(CursedRegistry.HIDDEN_ITEM.get());

										CompoundNBT hiddenTag = new CompoundNBT();
										hiddenTag.put(CurseTags.HIDDEN_TAG, stack.save(new CompoundNBT()));
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
