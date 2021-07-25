package com.mrbysco.cursedloot.client;

import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.client.renderer.BaseChestTESR;
import com.mrbysco.cursedloot.init.CursedRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
    public static void onClientSetup(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(CursedRegistry.BASE_CHEST_TILE.get(), BaseChestTESR::new);
    }

    public static final ResourceLocation BASE_CHEST_LOCATION = new ResourceLocation(Reference.MOD_ID, "entity/base_chest");
    public static void preStitchEvent(TextureStitchEvent.Pre event) {
        if(event.getMap().location().toString().equals("minecraft:textures/atlas/chest.png")) {
            event.addSprite(BASE_CHEST_LOCATION);
        }
    }
}
