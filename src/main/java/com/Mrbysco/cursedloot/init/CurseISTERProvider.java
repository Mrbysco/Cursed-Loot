package com.mrbysco.cursedloot.init;

import com.mrbysco.cursedloot.client.renderer.BaseChestItemStackRenderer;
import com.mrbysco.cursedloot.tileentity.BaseChestTile;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;

import java.util.concurrent.Callable;

public class CurseISTERProvider {
	public static Callable<ItemStackTileEntityRenderer> chest() {
		return () -> new BaseChestItemStackRenderer<>(BaseChestTile::new);
	}
}
