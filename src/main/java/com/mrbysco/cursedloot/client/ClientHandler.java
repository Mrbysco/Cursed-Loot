package com.mrbysco.cursedloot.client;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.client.ClientEvents.CurseClientTooltip;
import com.mrbysco.cursedloot.client.ClientEvents.CurseTooltip;
import com.mrbysco.cursedloot.client.renderer.BaseChestRenderer;
import com.mrbysco.cursedloot.init.CursedRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
	public static final ModelLayerLocation BASE_CHEST = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "base_chest"), "base_chest");

	public static void onClientSetup(FMLClientSetupEvent event) {
		MinecraftForgeClient.registerTooltipComponentFactory(CurseTooltip.class, CurseClientTooltip::new);
	}

	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(BASE_CHEST, BaseChestRenderer::createSingleBodyLayer);
	}

	public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(CursedRegistry.BASE_CHEST_BLOCK_ENTITY.get(), BaseChestRenderer::new);
	}

	public static final ResourceLocation BASE_CHEST_LOCATION = new ResourceLocation(Reference.MOD_ID, "entity/base_chest");

	public static void preStitchEvent(TextureStitchEvent.Pre event) {
		if (event.getAtlas().location().toString().equals("minecraft:textures/atlas/chest.png")) {
			event.addSprite(BASE_CHEST_LOCATION);
		}
	}
}
