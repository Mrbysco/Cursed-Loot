package com.mrbysco.cursedloot;

import com.mojang.logging.LogUtils;
import com.mrbysco.cursedloot.client.ClientEvents;
import com.mrbysco.cursedloot.client.ClientHandler;
import com.mrbysco.cursedloot.commands.CursedCommands;
import com.mrbysco.cursedloot.handlers.ChestHandler;
import com.mrbysco.cursedloot.handlers.ItemHandler;
import com.mrbysco.cursedloot.handlers.LootTableHandler;
import com.mrbysco.cursedloot.init.CursedRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

@Mod(Reference.MOD_ID)
public class CursedLoot {
	public static final Logger LOGGER = LogUtils.getLogger();

	public CursedLoot() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		CursedRegistry.BLOCKS.register(eventBus);
		CursedRegistry.ITEMS.register(eventBus);
		CursedRegistry.BLOCK_ENTITY_TYPES.register(eventBus);

		NeoForge.EVENT_BUS.register(new LootTableHandler());
		NeoForge.EVENT_BUS.register(new ItemHandler());
		NeoForge.EVENT_BUS.register(new ChestHandler());

		NeoForge.EVENT_BUS.addListener(this::onCommandRegister);

		if (FMLEnvironment.dist == Dist.CLIENT) {
			eventBus.addListener(ClientHandler::registerClientTooltip);
			eventBus.addListener(ClientHandler::registerRenders);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
			NeoForge.EVENT_BUS.register(new ClientEvents());
		}
	}

	public void onCommandRegister(RegisterCommandsEvent event) {
		CursedCommands.initializeCommands(event.getDispatcher());
	}
}
