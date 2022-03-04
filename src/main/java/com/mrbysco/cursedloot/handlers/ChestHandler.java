package com.mrbysco.cursedloot.handlers;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.init.CursedRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChestHandler {

	public static final String baseChestTag = Reference.MOD_PREFIX + "gotBaseChest";

	@SubscribeEvent
	public void firstJoin(PlayerLoggedInEvent event) {
		Player player = event.getPlayer();

		if (!player.level.isClientSide) {
			CompoundTag playerData = player.getPersistentData();

			if (!playerData.getBoolean(baseChestTag)) {
				player.getInventory().add(new ItemStack(CursedRegistry.BASE_CHEST.get()));
				playerData.putBoolean(baseChestTag, true);
			}
		}
	}
}
