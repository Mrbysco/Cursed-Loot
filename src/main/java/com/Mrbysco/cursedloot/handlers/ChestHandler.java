package com.mrbysco.cursedloot.handlers;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.init.CursedRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChestHandler {	
	
	public static final String baseChestTag = Reference.PREFIX + "gotBaseChest";
	
	@SubscribeEvent
	public void firstJoin(PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		
		if(!player.world.isRemote) {
			CompoundNBT playerData = player.getPersistentData();

			if(!playerData.getBoolean(baseChestTag)) {
				player.inventory.addItemStackToInventory(new ItemStack(CursedRegistry.BASE_CHEST.get()));
				playerData.putBoolean(baseChestTag, true);
			}
		}
	}
}
