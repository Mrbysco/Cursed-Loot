package com.mrbysco.cursedloot.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mrbysco.cursedloot.Reference;
import com.mrbysco.cursedloot.util.CurseHelper;
import com.mrbysco.cursedloot.util.CurseTags;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CursedCommands {
	public static void initializeCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(Reference.MOD_ID);
		root.requires((sourceStack) -> sourceStack.hasPermission(2))
				.then(Commands.literal("randomCurse")
						.then(Commands.argument("player", EntityArgument.players())
								.then(Commands.argument("slot", IntegerArgumentType.integer(0)).executes(CursedCommands::randomCurse))))
				.then(Commands.literal("curse")
						.then(Commands.argument("curseID", ResourceLocationArgument.id()).suggests((cs, builder) -> {
							List<String> curseIDS = new ArrayList<>();
							for (CurseTags tag : CurseTags.values()) {
								curseIDS.add(tag.getRegistryLocation().toString());
							}
							return SharedSuggestionProvider.suggest(curseIDS, builder);
						}).then(Commands.argument("player", EntityArgument.players())
								.then(Commands.argument("slot", IntegerArgumentType.integer(0)).executes(CursedCommands::curseSlot)))));
		dispatcher.register(root);
	}

	private static int randomCurse(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		final int slot = IntegerArgumentType.getInteger(ctx, "slot");
		for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
			Inventory inventory = player.getInventory();
			if (slot >= 0 && slot <= inventory.getContainerSize()) {
				ItemStack stack = inventory.getItem(slot);
				if (!stack.isEmpty()) {
					ItemStack cursedStack = CurseHelper.applyRandomCurse(stack);
					inventory.setItem(slot, cursedStack);
				}
			}
		}

		return 0;
	}

	private static int curseSlot(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		final int slot = IntegerArgumentType.getInteger(ctx, "slot");
		final ResourceLocation curse = ResourceLocationArgument.getId(ctx, "curseID");
		for (ServerPlayer player : EntityArgument.getPlayers(ctx, "player")) {
			Inventory inventory = player.getInventory();
			if (slot >= 0) {
				ItemStack stack = inventory.getItem(slot);
				if (!stack.isEmpty()) {
					CurseTags curseTag = CurseTags.getByRegistryName(curse);
					ItemStack cursedStack = CurseHelper.applyCurse(stack, curseTag);
					inventory.setItem(slot, cursedStack);
				}
			}
		}

		return 0;
	}
}
