package com.mrbysco.cursedloot.handlers;

import com.mrbysco.cursedloot.util.CurseHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LootTableHandler {

	@SubscribeEvent
	public void onLootTableLoad(PlayerInteractEvent.RightClickBlock event) {
		final Level level = event.getLevel();
		final BlockPos pos = event.getPos();
		if (!level.isClientSide && event.getHand() == InteractionHand.MAIN_HAND) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof RandomizableContainerBlockEntity chest) {
				if (chest.lootTable != null) {
					for (int i = 0; i < chest.getContainerSize(); i++) {
						ItemStack stack = chest.getItem(i);
						if (!stack.isEmpty()) {
							if (level.random.nextInt(100) < 75) {
								ItemStack cursedStack = CurseHelper.applyRandomCurse(stack);
								chest.setItem(i, cursedStack);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onLootTableLoad(PlayerInteractEvent.EntityInteract event) {
		final Level level = event.getLevel();
		if (!level.isClientSide && event.getTarget() instanceof AbstractMinecartContainer minecartChest) {
			if (minecartChest.lootTable != null) {
				for (int i = 0; i < minecartChest.getContainerSize(); i++) {
					ItemStack stack = minecartChest.getItem(i);
					if (!stack.isEmpty()) {
						if (level.random.nextInt(100) < 75) {
							ItemStack cursedStack = CurseHelper.applyRandomCurse(stack);
							minecartChest.setItem(i, cursedStack);
						}
					}
				}
			}
		}
	}
}
