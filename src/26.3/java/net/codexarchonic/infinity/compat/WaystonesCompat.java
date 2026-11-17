package net.codexarchonic.infinity.compat;

import com.mojang.serialization.Codec;
import net.codexarchonic.infinity.InfinityMod;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.concurrent.ConcurrentLinkedQueue;

/** Uses Waystones' registered feature, including its native naming and saved-waystone registration. */
public final class WaystonesCompat {
    private static final Identifier FEATURE = Identifier.parse("waystones:waystone");
    private static final int SPACING = 18;
    private record Pending(ServerLevel level, ChunkPos chunk) { }
    private static final ConcurrentLinkedQueue<Pending> PENDING = new ConcurrentLinkedQueue<>();

    public static void register() {
        if (!FabricLoader.getInstance().isModLoaded("waystones")) return;
        ServerChunkEvents.CHUNK_GENERATE.register((level, chunk) -> {
            if (isInfinity(level) && isCandidate(level, chunk.getPos()))
                PENDING.add(new Pending(level, chunk.getPos()));
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            // Run after chunk completion on the server thread, never inside the generation callback.
            for (int i = 0, count = Math.min(32, PENDING.size()); i < count; i++) {
                Pending pending = PENDING.poll();
                if (pending != null) generateNatural(pending.level, pending.chunk);
            }
            if (server.getTickCount() % 20 == 0) {
                for (var player : server.getPlayerList().getPlayers()) {
                    ServerLevel level = (ServerLevel) player.level();
                    if (isInfinity(level)) ensureEntry(level, player.blockPosition());
                }
            }
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> PENDING.clear());
        InfinityMod.LOGGER.info("Waystones integration enabled: wilderness generation and one entry waystone per Infinity dimension");
    }

    private static boolean isInfinity(ServerLevel level) {
        return level.dimension().identifier().getNamespace().equals(InfinityMod.MOD_ID);
    }

    public static boolean isCandidate(ServerLevel level, ChunkPos chunk) {
        int gx = Math.floorDiv(chunk.x(), SPACING), gz = Math.floorDiv(chunk.z(), SPACING);
        RandomSource random = RandomSource.create(level.getSeed() ^ level.dimension().identifier().hashCode()
                ^ gx * 341873128712L ^ gz * 132897987541L);
        return chunk.x() == gx * SPACING + random.nextInt(SPACING)
                && chunk.z() == gz * SPACING + random.nextInt(SPACING);
    }

    public static boolean generateNatural(ServerLevel level, ChunkPos chunk) {
        if (!isInfinity(level) || level.getChunkSource().getChunkNow(chunk.x(), chunk.z()) == null) return false;
        int x = chunk.getMinBlockX() + 8, z = chunk.getMinBlockZ() + 8;
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        BlockPos pos = new BlockPos(x, y, z);
        return safeSite(level, pos, false) && place(level, pos);
    }

    /** Saved independently of the block so mining it or revisiting the dimension cannot duplicate it. */
    public static boolean ensureEntry(ServerLevel level, BlockPos arrival) {
        if (!isInfinity(level)) return false;
        EntryData data = level.getDataStorage().computeIfAbsent(EntryData.TYPE);
        if (data.created) return true;
        for (boolean platform : new boolean[]{false, true}) {
            for (int radius = 4; radius <= 12; radius += 2) {
                for (int dx = -radius; dx <= radius; dx += 2) {
                    for (int dz = -radius; dz <= radius; dz += 2) {
                        if (Math.abs(dx) != radius && Math.abs(dz) != radius) continue;
                        for (int dy : new int[]{0, -1, 1, -2, 2, -3, 3}) {
                            BlockPos pos = arrival.offset(dx, dy, dz);
                            if (!safeSite(level, pos, platform)) continue;
                            if (platform) {
                                for (int x = -1; x <= 1; x++) for (int z = -1; z <= 1; z++) {
                                    BlockPos floor = pos.offset(x, -1, z);
                                    if (!level.getBlockState(floor).isFaceSturdy(level, floor, Direction.UP))
                                        level.setBlockAndUpdate(floor, Blocks.COBBLESTONE.defaultBlockState());
                                }
                            }
                            if (place(level, pos)) {
                                data.created = true;
                                data.setDirty();
                                InfinityMod.LOGGER.info("Created entry waystone in {} at {}", level.dimension().identifier(), pos);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false; // Retry on a later tick if the entrance is obstructed or its chunks are not ready.
    }

    private static boolean safeSite(ServerLevel level, BlockPos pos, boolean platform) {
        if (pos.getY() <= level.getMinY() || pos.getY() + 2 >= level.getMaxY()) return false;
        for (int x = -1; x <= 1; x++) for (int z = -1; z <= 1; z++) {
            BlockPos feet = pos.offset(x, 0, z), floor = feet.below();
            if (!level.getWorldBorder().isWithinBounds(feet)
                    || level.getChunkSource().getChunkNow(feet.getX() >> 4, feet.getZ() >> 4) == null) return false;
            if (!level.getBlockState(feet).isAir() || !level.getBlockState(feet.above()).isAir()) return false;
            var state = level.getBlockState(floor);
            if (!state.getFluidState().isEmpty() || state.is(Blocks.MAGMA_BLOCK) || state.is(Blocks.CACTUS)
                    || state.is(Blocks.CAMPFIRE) || state.is(Blocks.SOUL_CAMPFIRE)) return false;
            if (!state.isFaceSturdy(level, floor, Direction.UP) && !(platform && state.isAir())) return false;
        }
        return true;
    }

    private static boolean place(ServerLevel level, BlockPos pos) {
        var feature = level.registryAccess().lookupOrThrow(Registries.FEATURE).getValue(FEATURE);
        return feature != null && feature.place(level, level.getChunkSource().getGenerator(),
                RandomSource.create(level.getSeed() ^ pos.asLong()), pos);
    }

    public static final class EntryData extends SavedData {
        private boolean created;
        private static final Codec<EntryData> CODEC = Codec.BOOL.fieldOf("created").codec().xmap(value -> {
            EntryData data = new EntryData();
            data.created = value;
            return data;
        }, data -> data.created);
        private static final SavedDataType<EntryData> TYPE = new SavedDataType<>(
                Identifier.parse("infinity:entry_waystone"), EntryData::new, CODEC, null);
    }
}
