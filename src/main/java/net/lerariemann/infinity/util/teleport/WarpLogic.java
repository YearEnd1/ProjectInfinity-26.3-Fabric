package net.lerariemann.infinity.util.teleport;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.lerariemann.infinity.access.MinecraftServerAccess;
import net.lerariemann.infinity.access.ServerPlayerEntityAccess;
import net.lerariemann.infinity.registry.var.ModCommands;
import net.lerariemann.infinity.registry.var.ModStats;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
//? if >1.21 {
import net.minecraft.world.level.portal.TeleportTransition;
//?}
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;

public interface WarpLogic {
    /**
     * Handles the /warp id command, converting the provided numeric ID to an Identifier.
     */
    static void requestWarpById(CommandContext<CommandSourceStack> context, long value) {
        requestWarp(context.getSource().getPlayer(), InfinityMethods.getDimId(value), true);
    }
    /**
     * Handles the /warp existing command, warping the player to a specified dimension. This is the same as writing the input into a book.
     */
    static void requestWarpToExisting(CommandContext<CommandSourceStack> context, ResourceLocation value) throws CommandSyntaxException {
        ServerLevel w = context.getSource().getServer().getLevel(ResourceKey.create(Registries.DIMENSION, value));
        if (w == null)
            throw ModCommands.MALFORM_IDENTIFIER_EXCEPTION.create(value);
        else if (InfinityMethods.isTimebombed(w))
            throw ModCommands.TIMEBOMBED_EXCEPTION.create(value);
        else requestWarp(context.getSource().getPlayer(), value, true);
    }
    /**
     * Handles the /warp text command, warping the player to a specified dimension. This is the same as writing the input into a book.
     */
    static void requestWarpByText(CommandContext<CommandSourceStack> context, String value) {
        value = InfinityMethods.dimTextPreprocess(value);
        ResourceLocation id = InfinityMethods.dimTextToId(value);
        requestWarp(context.getSource().getPlayer(), id, true);
        PortalCreator.recordIdTranslation(context.getSource().getServer(), id, value);
    }

    /**
     * Handles moving a specified player to a specified dimension which may or may not yet exist.
     */
    static void requestWarp(@Nullable ServerPlayer player, ResourceLocation value, boolean increaseStats) {
        if (player == null) return;
        MinecraftServer s = player.getServer();
        if (s == null) return;
        if (((MinecraftServerAccess)s).infinity$needsInvocation()) {
            PortalCreator.onInvocationNeedDetected(player);
            return;
        }
        boolean isThisANewDimension = PortalCreator.tryAddInfinityDimension(s, value);
        if (isThisANewDimension) { //the server needs a bit of time to process the dim-adding request, hence the 2-tick wait
            if (increaseStats) player.awardStat(ModStats.DIMS_OPENED_STAT, 1);
            ((ServerPlayerEntityAccess)(player)).infinity$setWarpTimer(2, value);
        }
        else performWarp(player, value);
    }

    /**
     * Moves the player to a specified existing dimension.
     */
    static void performWarp(ServerPlayer player, ResourceLocation idForWarp) {
        MinecraftServer s = player.serverLevel().getServer();
        ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, idForWarp);
        ServerLevel w = s.getLevel(key);
        if (w==null) {
            InfinityMethods.sendUnexpectedError(player, "warp");
            return;
        }

        double d = DimensionType.getTeleportationScale(player.serverLevel().dimensionType(), w.dimensionType());
        double y = Mth.clamp(player.getY(), w.getMinBuildHeight(), w.getMaxBuildHeight());

        BlockPos blockPos2 = getPosForWarp(w.getWorldBorder().clampToBounds(player.getX() * d, y, player.getZ() * d), w);

        ensureSafety(w, blockPos2.below(), Blocks.OBSIDIAN);
        ensureSafety(w, blockPos2, Blocks.AIR);
        ensureSafety(w, blockPos2.above(), Blocks.AIR);
        player.teleportTo(w, blockPos2.getX() + 0.5, blockPos2.getY(), blockPos2.getZ() + 0.5,
                new HashSet<>(), player.getYRot(), player.getXRot());
    }

    /**
     * Teleport a player to their respawn point.
     */
    static void respawnAlive(@Nullable ServerPlayer player) {
        if (player == null) return;
        //? if >1.21 {
        TeleportTransition targ = player.findRespawnPositionAndUseSpawnBlock(true, TeleportTransition.DO_NOTHING);

        //? if <1.21.4 {
        /*var pos = targ.pos();
        *///?} else {
        var pos = targ.position();
        //?}

        VersionMethods.teleportTo(player, targ.newLevel(), pos, targ.yRot(), targ.xRot());
        //?} else {
        /*BlockPos targ = player.getRespawnPosition();
        var serverWorld = player.server.getLevel(player.getRespawnDimension());
        if (targ == null || serverWorld == null) {
            serverWorld = player.server.overworld();
            targ = serverWorld.getSharedSpawnPos();
        }
        player.teleportTo(serverWorld, targ.getX() + 0.5, targ.getY(), targ.getZ() + 0.5,
                new HashSet<>(), player.getYRot(), player.getXRot());
        *///?}

    }
    /* will implement a proper end-of-time dimension later */
    static void sendToMissingno(ServerPlayer player) {
        requestWarp(player, InfinityMethods.getId("missingno"), false);
    }

    /**
     * Root method for finding a safe position to warp to.
     */
    static BlockPos getPosForWarp(@NotNull BlockPos orig, @NotNull ServerLevel world) {
        BlockPos iter = orig;
        int counter = 0;
        BlockPos pos = scanColumn(orig, world, true);
        while (pos == null) { // do eight attempts on strong rules and one on weak rules
            iter = iter.offset(world.random.nextInt(16) - 8, 0, world.random.nextInt(16) - 8);
            pos = scanColumn(iter, world, true);
            if (++counter > 7) {
                pos = Objects.requireNonNullElse(scanColumn(orig, world, false), orig);
                break;
            }
        }
        return pos;
    }

    /**
     * Tries to find a safe position with fixed x and z coordinates.
     */
    @Nullable
    static BlockPos scanColumn(BlockPos orig, ServerLevel world, boolean strong) {
        int x = orig.getX();
        int y1 = orig.getY();
        int z = orig.getZ();
        if (isPosViable(x, y1, z, world, strong)) return orig;
        int y2 = y1;
        while (y1 > world.getMinBuildHeight() || y2 < world.getMaxBuildHeight()) {
            y1-=1;
            if (isPosViable(x, y1, z, world, strong)) return new BlockPos(x, y1, z);
            y2+=1;
            if (isPosViable(x, y2, z, world, strong)) return new BlockPos(x, y2, z);
        }
        return null;
    }

    static boolean isPosViable(int x, int y, int z, BlockGetter w, boolean strong) {
        return strong ? isPosViableStrong(new BlockPos(x, y, z), w) : isPosViableWeak(new BlockPos(x, y, z), w);
    }
    /** "Place me on ground surface" where ground is equivalent to "not air". */
    static boolean isPosViableWeak(BlockPos pos, BlockGetter w) {
        if (w.isOutsideBuildHeight(pos.getY())) return false;
        boolean bl = w.getBlockState(pos.above()).isAir();
        boolean bl2 = w.getBlockState(pos).isAir();
        boolean bl3 = w.getBlockState(pos.below()).isAir();
        return (!bl3 && bl2 && bl);
    }
    /** Same as above except now fluids (except water) are unacceptable ground. */
    static boolean isPosViableStrong(BlockPos pos, BlockGetter w) {
        if (w.isOutsideBuildHeight(pos.getY())) return false;
        boolean bl = isSafe(w, pos.above(), true);
        boolean bl2 = isSafe(w, pos, true);
        boolean bl3 = isSafe(w, pos.below(), false);
        return (bl3 && bl2 && bl);
    }

    static boolean isSafe(BlockGetter world, BlockPos pos, boolean asAir) {
        BlockState state = world.getBlockState(pos);
        if (state.isAir()) return asAir;
        if (asAir) return false;
        FluidState fluidState = state.getFluidState();
        return (fluidState.is(Fluids.EMPTY) || fluidState.is(FluidTags.WATER));
    }

    /** Replaces blocks at the target position in case all above measures failed. */
    static void ensureSafety(ServerLevel world, BlockPos pos, Block fallback) {
        BlockState fb = fallback.defaultBlockState();
        if (!isSafe(world, pos, fb.isAir()))
            world.setBlockAndUpdate(pos, fb);
    }
}
