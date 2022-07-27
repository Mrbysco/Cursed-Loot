package com.mrbysco.cursedloot;

import com.mojang.logging.LogUtils;
import com.mrbysco.cursedloot.client.ClientEvents;
import com.mrbysco.cursedloot.client.ClientHandler;
import com.mrbysco.cursedloot.commands.CursedCommands;
import com.mrbysco.cursedloot.handlers.ChestHandler;
import com.mrbysco.cursedloot.handlers.ItemHandler;
import com.mrbysco.cursedloot.handlers.LootTableHandler;
import com.mrbysco.cursedloot.init.CursedRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Reference.MOD_ID)
public class CursedLoot {
	public static final Logger LOGGER = LogUtils.getLogger();

	public CursedLoot() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		CursedRegistry.BLOCKS.register(eventBus);
		CursedRegistry.ITEMS.register(eventBus);
		CursedRegistry.BLOCK_ENTITY_TYPES.register(eventBus);

		MinecraftForge.EVENT_BUS.register(new LootTableHandler());
		MinecraftForge.EVENT_BUS.register(new ItemHandler());
		MinecraftForge.EVENT_BUS.register(new ChestHandler());

		MinecraftForge.EVENT_BUS.addListener(this::onCommandRegister);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			eventBus.addListener(ClientHandler::registerClientTooltip);
			eventBus.addListener(ClientHandler::registerRenders);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
			eventBus.addListener(ClientHandler::preStitchEvent);
			MinecraftForge.EVENT_BUS.register(new ClientEvents());
		});
	}

	public void onCommandRegister(RegisterCommandsEvent event) {
		CursedCommands.initializeCommands(event.getDispatcher());
	}
}
