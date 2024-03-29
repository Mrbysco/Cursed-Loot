package com.mrbysco.cursedloot.client;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.client.ClientEvents.CurseClientTooltip;
import com.mrbysco.cursedloot.client.ClientEvents.CurseTooltip;
import com.mrbysco.cursedloot.client.renderer.BaseChestRenderer;
import com.mrbysco.cursedloot.init.CursedRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

public class ClientHandler {
	public static final ModelLayerLocation BASE_CHEST = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "base_chest"), "base_chest");

	public static void registerClientTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(CurseTooltip.class, CurseClientTooltip::new);
	}

	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(BASE_CHEST, BaseChestRenderer::createSingleBodyLayer);
	}

	public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(CursedRegistry.BASE_CHEST_BLOCK_ENTITY.get(), BaseChestRenderer::new);
	}

	public static final ResourceLocation BASE_CHEST_LOCATION = new ResourceLocation(Reference.MOD_ID, "entity/base_chest");
}
