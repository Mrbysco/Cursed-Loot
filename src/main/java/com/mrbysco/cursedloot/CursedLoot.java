package com.mrbysco.cursedloot;

import com.mrbysco.cursedloot.client.ClientEvents;
import com.mrbysco.cursedloot.client.ClientHandler;
import com.mrbysco.cursedloot.handlers.ChestHandler;
import com.mrbysco.cursedloot.handlers.ItemHandler;
import com.mrbysco.cursedloot.handlers.LootTableHandler;
import com.mrbysco.cursedloot.init.CursedRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class CursedLoot {
	public static final Logger logger = LogManager.getLogger(Reference.MOD_ID);

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(Reference.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	public CursedLoot() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		CursedRegistry.BLOCKS.register(eventBus);
		CursedRegistry.ITEMS.register(eventBus);
		CursedRegistry.TILE_ENTITIES.register(eventBus);

		MinecraftForge.EVENT_BUS.register(new LootTableHandler());
		MinecraftForge.EVENT_BUS.register(new ItemHandler());
		MinecraftForge.EVENT_BUS.register(new ChestHandler());

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			eventBus.addListener(ClientHandler::onClientSetup);
			eventBus.addListener(ClientHandler::preStitchEvent);
			MinecraftForge.EVENT_BUS.register(new ClientEvents());
		});
	}
}
