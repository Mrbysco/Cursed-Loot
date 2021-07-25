package com.mrbysco.cursedloot.client;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.client.renderer.BaseChestRenderer;
import com.mrbysco.cursedloot.init.CursedRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.registry.RenderingRegistry;

public class ClientHandler {
    public static final ModelLayerLocation BASE_CHEST = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "base_chest"), "base_chest");

    public static void onClientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerLayerDefinition(BASE_CHEST, () -> BaseChestRenderer.createSingleBodyLayer());

        BlockEntityRenderers.register(CursedRegistry.BASE_CHEST_TILE.get(), BaseChestRenderer::new);
    }

    public static final ResourceLocation BASE_CHEST_LOCATION = new ResourceLocation(Reference.MOD_ID, "entity/base_chest");
    public static void preStitchEvent(TextureStitchEvent.Pre event) {
        if(event.getMap().location().toString().equals("minecraft:textures/atlas/chest.png")) {
            event.addSprite(BASE_CHEST_LOCATION);
        }
    }
}
