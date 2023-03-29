package com.mrbysco.cursedloot.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.cursedloot.blockentity.BaseChestBlockEntity;
import com.mrbysco.cursedloot.init.CursedRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BaseChestItemStackRenderer extends BlockEntityWithoutLevelRenderer {
	private BaseChestBlockEntity baseChest = null;
	private BlockEntityRenderDispatcher blockEntityRenderDispatcher = null;

	public BaseChestItemStackRenderer() {
		super(null, null);
	}

	@Override
	public void renderByItem(ItemStack itemStackIn, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
		if (baseChest == null) {
			this.baseChest = new BaseChestBlockEntity(BlockPos.ZERO, CursedRegistry.BASE_CHEST.get().defaultBlockState());
		}
		if (blockEntityRenderDispatcher == null) {
			final Minecraft minecraft = Minecraft.getInstance();
			blockEntityRenderDispatcher = minecraft.getBlockEntityRenderDispatcher();
		}
		if (blockEntityRenderDispatcher != null) {
			blockEntityRenderDispatcher.renderItem((BlockEntity) baseChest, poseStack, bufferSource, combinedLightIn, combinedOverlayIn);
		}
	}
}
