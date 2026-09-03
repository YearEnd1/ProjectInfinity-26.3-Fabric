package net.codexarchonic.infinity.registry.var;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.codexarchonic.infinity.compat.ModCompat;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.codexarchonic.infinity.util.teleport.WarpLogic;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ModCommands {
    public static final DynamicCommandExceptionType MALFORM_IDENTIFIER_EXCEPTION = makeException("error.infinity.warp.malformed_identifier");
    public static final DynamicCommandExceptionType TIMEBOMBED_EXCEPTION = makeException("error.infinity.warp.timebombed");

    public static DynamicCommandExceptionType makeException(String key) {
        return new DynamicCommandExceptionType(
                id -> Component.
                        //? if >1.21 {
                        translatableEscape
                        //?} else {
                        /*translatable
                *///?}
                ("error.infinity.warp.malformed_identifier", id)
        );
    }

    public static void registerCommands() {
        String warp;
        if (ModCompat.ESSENTIALS) // FTB/Fabric Essentials add their own warp command that plays havoc with ours.
            warp = "dimwarp";
        else {
            warp = "warp";
        }
        CommandRegistrationEvent.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal(warp)
                .requires(source -> source.hasPermission(2))
                .then(
                        Commands.argument("targets", EntityArgument.players())
                                .then(randomArgument(true))
                                .then(existingArgument(true))
                                .then(idArgument(true))
                                .then(textArgument(true))
                )
                .then(randomArgument(false))
                .then(existingArgument(false))
                .then(idArgument(false))
                .then(textArgument(false))
        ));
        CommandRegistrationEvent.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("respawn")
                .requires(source -> source.hasPermission(2)).executes(context -> {
                    WarpLogic.respawnAlive(context.getSource().getPlayer());
                    return 1;
                })));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> textArgument(boolean b) {
        return Commands.literal("text").then(
                argument("text", StringArgumentType.string())
                .executes(context -> {
                    final String text = StringArgumentType.getString(context, "text");
                    WarpLogic.requestWarpByText(context, players(b, context), text);
                    return 1;
                })
        );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> idArgument(boolean b) {
        return Commands.literal("id").then(
                argument("id", IntegerArgumentType.integer())
                        .executes(context -> {
                            final int value = IntegerArgumentType.getInteger(context, "id");
                            WarpLogic.requestWarpById(players(b, context), value);
                            return 1;
                        })
        );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> existingArgument(boolean b) {
        return Commands.literal("existing").then(
                argument("existing", DimensionArgument.dimension()).executes(context -> {
                    final ResourceLocation identifier = context.getArgument("existing", ResourceLocation.class);
                    WarpLogic.requestWarpToExisting(context, players(b, context), identifier);
                    return 1;
                })
        );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> randomArgument(boolean b) {
        return Commands.literal("random").executes((context -> {
            final long text = InfinityMethods.getRandomSeed(new Random());
            Collection<ServerPlayer> players = players(b, context);
            WarpLogic.requestWarpById(players, text);
            return 1;
        }));
    }

	private static Collection<ServerPlayer> players(boolean hasPlayerArgument, CommandContext<CommandSourceStack> context) {
		if (hasPlayerArgument) {
            return players(context);
        } else {
            return player(context);
        }
	}

    private static Collection<ServerPlayer> player(CommandContext<CommandSourceStack> context) {
        return Collections.singleton(context.getSource().getPlayer());
    }

    private static Collection<ServerPlayer> players(CommandContext<CommandSourceStack> context) {
		try {
			return EntityArgument.getPlayers(context, "targets");
		} catch (CommandSyntaxException e) {
            return Collections.singleton(context.getSource().getPlayer());
		}
	}
}
