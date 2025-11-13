package net.lerariemann.infinity.registry.var;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.platform.Platform;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.teleport.WarpLogic;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
                        /*translatableEscape
                        *///?} else {
                        translatable
                //?}
                ("error.infinity.warp.malformed_identifier", id)
        );
    }

    public static void registerCommands() {
        String warp;
        if (Platform.isModLoaded("ftbessentials") || Platform.isModLoaded("fabric-essentials")) // FTB/Fabric Essentials add their own warp command that plays havoc with ours.
            warp = "dimwarp";
        else {
            warp = "warp";
        }
        CommandRegistrationEvent.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal(warp)
                .requires(source -> source.hasPermission(2))
                .then(
                        Commands.literal("random").executes((context -> {
                            final long text = InfinityMethods.getRandomSeed(new Random());
                            WarpLogic.requestWarpById(context, text);
                            return 1;
                        }))
                )
                .then(
                        Commands.literal("existing").then(
                                argument("existing", DimensionArgument.dimension()).executes(context -> {
                                    final ResourceLocation identifier = context.getArgument("existing", ResourceLocation.class);
                                    WarpLogic.requestWarpToExisting(context, identifier);
                                    return 1;
                                })
                        )
                )
                .then(
                        Commands.literal("id").then(
                                argument("id", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            final int value = IntegerArgumentType.getInteger(context, "id");
                                            WarpLogic.requestWarpById(context, value);
                                            return 1;
                                        })
                        )
                )
                .then(
                        argument("text", StringArgumentType.string())
                                .executes(context -> {
                                    final String text = StringArgumentType.getString( context, "text");
                                    WarpLogic.requestWarpByText(context, text);
                                    return 1;
                                })
                )
        ));
        CommandRegistrationEvent.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("respawn")
                .requires(source -> source.hasPermission(2)).executes(context -> {
                    WarpLogic.respawnAlive(context.getSource().getPlayer());
                    return 1;
                })));
    }
}
