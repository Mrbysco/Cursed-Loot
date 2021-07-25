package com.mrbysco.cursedloot.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.cursedloot.blockentity.BaseChestBlockEntity;
import com.mrbysco.cursedloot.init.CursedRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BaseChestItemStackRenderer extends BlockEntityWithoutLevelRenderer{
	private BaseChestBlockEntity baseChest = null;

	public BaseChestItemStackRenderer() {
		super(null, null);
		Minecraft minecraft = Minecraft.getInstance();
		if(minecraft != null) {
			ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
			if (resourceManager instanceof ReloadableResourceManager) {
				((ReloadableResourceManager) resourceManager).registerReloadListener(this);
			}
		}
	}

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		this.baseChest = new BaseChestBlockEntity(BlockPos.ZERO, CursedRegistry.BASE_CHEST.get().defaultBlockState());
	}

	@Override
	public void renderByItem(ItemStack itemStackIn, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
		if(baseChest != null) {
			final Minecraft minecraft = Minecraft.getInstance();
			BlockEntityRenderDispatcher blockEntityRenderDispatcher = minecraft.getBlockEntityRenderDispatcher();
			if(blockEntityRenderDispatcher != null) {
				blockEntityRenderDispatcher.renderItem((BlockEntity)baseChest, poseStack, bufferSource, combinedLightIn, combinedOverlayIn);
			}
		}
	}
}