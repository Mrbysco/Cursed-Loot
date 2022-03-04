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
		Level world = event.getWorld();
		BlockPos pos = event.getPos();
		if (!world.isClientSide && event.getHand() == InteractionHand.MAIN_HAND) {
			BlockEntity te = world.getBlockEntity(pos);
			if (te instanceof RandomizableContainerBlockEntity chest) {
				if (chest.lootTable != null) {
					for (int i = 0; i < chest.getContainerSize(); i++) {
						ItemStack stack = chest.getItem(i);
						if (!stack.isEmpty()) {
							if (world.random.nextInt(100) < 75) {
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
		Level world = event.getWorld();
		if (!world.isClientSide && event.getTarget() instanceof AbstractMinecartContainer minecartChest) {
			if (minecartChest.lootTable != null) {
				for (int i = 0; i < minecartChest.getContainerSize(); i++) {
					ItemStack stack = minecartChest.getItem(i);
					if (!stack.isEmpty()) {
						if (world.random.nextInt(100) < 75) {
							ItemStack cursedStack = CurseHelper.applyRandomCurse(stack);
							minecartChest.setItem(i, cursedStack);
						}
					}
				}
			}
		}
	}
}
