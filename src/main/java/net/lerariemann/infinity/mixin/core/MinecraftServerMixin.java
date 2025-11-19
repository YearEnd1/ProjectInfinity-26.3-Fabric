package net.lerariemann.infinity.mixin.core;

import com.google.common.collect.ImmutableList;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.util.platform.PlatformMethods;
import net.lerariemann.infinity.access.MinecraftServerAccess;
import net.lerariemann.infinity.util.config.ConfigManager;
import net.minecraft.commands.CommandSource;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
//? if >1.21 {
import net.minecraft.server.ServerInfo;
import net.minecraft.world.level.chunk.storage.ChunkIOErrorReporter;
//?}
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantBlockableEventLoop<TickTask> implements
        //? if >1.21
        ServerInfo, ChunkIOErrorReporter,
        CommandSource, AutoCloseable, MinecraftServerAccess {
    @Final @Shadow
    private Map<ResourceKey<Level>, ServerLevel> levels;
    @Final @Shadow
    private Executor executor;
    @Final @Shadow
    protected LevelStorageSource.LevelStorageAccess storageSource;
    @Final @Shadow
    protected WorldData worldData;
    @Final @Shadow
    private ChunkProgressListenerFactory progressListenerFactory;

    public MinecraftServerMixin(String string) {
        super(string);
    }

    @Shadow
    public ServerLevel getLevel(ResourceKey<Level> key) {
        return null;
    }
    @Shadow
    protected TickTask wrapRunnable(Runnable runnable) {
        return null;
    }

    @Shadow public abstract RegistryAccess.Frozen registryAccess();

    @Unique
    public Map<ResourceKey<Level>, ServerLevel> infinity$worldsToAdd;
    @Unique
    public boolean infinity$needsInvocation;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injected(CallbackInfo info) {
        infinity$worldsToAdd = new HashMap<>();
        infinity$needsInvocation = !Files.exists(InfinityMod.invocationLock);
        InfinityMod.LOGGER.info("Invocation {}", infinity$needsInvocation ? "needed..." : "not needed");
        if (!infinity$needsInvocation) infinity$setDimensionProvider();
    }
    @Override
    public boolean infinity$needsInvocation() {return infinity$needsInvocation;}
    @Override
    public void infinity$onInvocation() {
        infinity$needsInvocation = false;
        try {
            ConfigManager.updateInvocationLock();
            infinity$setDimensionProvider();
            InfinityMod.LOGGER.info("Invocation complete");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void infinity$setDimensionProvider() {
        InfinityMod.updateProvider((MinecraftServer)(Object)this);
    }

    /* Ensure newly created dimensions are accessible, will tick, etc. */
    @Override
    public void infinity$addWorld(ResourceKey<Level> key, LevelStem options) {
        ServerLevelData serverWorldProperties = worldData.overworldData();
        ServerLevel world = new ServerLevel(
                ((MinecraftServer) (Object) this),
                executor,
                storageSource,
                serverWorldProperties,
                key,
                options,
                progressListenerFactory.create(
                        //? if >1.21 {
                        worldData.getGameRules().getInt(GameRules.RULE_SPAWN_CHUNK_RADIUS)
                        //?} else {
                        /*2
                        *///?}
                ),
                worldData.isDebugWorld(),
                BiomeManager.obfuscateSeed(worldData.worldGenOptions().seed()),
                ImmutableList.of(),
                false,
                getLevel(Level.OVERWORLD).getRandomSequences());
        getLevel(Level.OVERWORLD).getWorldBorder().addListener(new BorderChangeListener.DelegateBorderChangeListener(world.getWorldBorder()));
        infinity$worldsToAdd.put(key, world);
        tell(wrapRunnable(() -> {
            levels.put(key, world);
            infinity$worldsToAdd.clear();
            PlatformMethods.onWorldLoad(this, world);
        }));
    }

    @Override
    public boolean infinity$hasToAdd(ResourceKey<Level> key) {
        return (infinity$worldsToAdd.containsKey(key));
    }

    /* Makes weather properly work in infdims. */
    @Redirect(method="createLevels", at=@At(value="NEW", target="(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/world/level/storage/ServerLevelData;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/dimension/LevelStem;Lnet/minecraft/server/level/progress/ChunkProgressListener;ZJLjava/util/List;ZLnet/minecraft/world/RandomSequences;)Lnet/minecraft/server/level/ServerLevel;"))
    public ServerLevel create(MinecraftServer server, Executor workerExecutor, LevelStorageSource.LevelStorageAccess session, ServerLevelData properties, ResourceKey<Level> worldKey, LevelStem dimensionOptions, ChunkProgressListener worldGenerationProgressListener, boolean debugWorld, long seed, List<CustomSpawner> spawners, boolean shouldTickTime, RandomSequences randomSequencesState) {
        ServerLevelData prop = (worldKey.location().toString().contains("infinity")) ? worldData.overworldData() : properties;
        return new ServerLevel(server, workerExecutor, session, prop, worldKey, dimensionOptions, worldGenerationProgressListener, debugWorld, seed, spawners, shouldTickTime, randomSequencesState);
    }
}
