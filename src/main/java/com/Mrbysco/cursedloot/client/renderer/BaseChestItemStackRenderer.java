package com.mrbysco.cursedloot.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.cursedloot.init.CursedRegistry;
import com.mrbysco.cursedloot.blockentity.BaseChestBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BaseChestItemStackRenderer extends BlockEntityWithoutLevelRenderer{
	private final BaseChestBlockEntity baseChest;

	public BaseChestItemStackRenderer() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
		this.baseChest = new BaseChestBlockEntity(BlockPos.ZERO, CursedRegistry.BASE_CHEST.get().defaultBlockState());
	}

	@Override
	public void renderByItem(ItemStack itemStackIn, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
		Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem((BlockEntity)baseChest, poseStack, bufferSource, combinedLightIn, combinedOverlayIn);
	}
}