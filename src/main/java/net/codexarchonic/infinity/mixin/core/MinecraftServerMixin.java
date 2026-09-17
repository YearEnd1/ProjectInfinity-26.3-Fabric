package net.codexarchonic.infinity.mixin.core;

import com.google.common.collect.ImmutableList;
import net.codexarchonic.infinity.InfinityMod;
//? if <1.21.2 && fabric {
/*import net.codexarchonic.infinity.compat.GravityChangerCompat;
import net.codexarchonic.infinity.compat.ModCompat;
*///?}
//? if <1.21.2 {
/*import net.minecraft.world.level.border.BorderChangeListener;
*///?}
import net.codexarchonic.infinity.util.platform.InfinityPlatform;
import net.codexarchonic.infinity.access.MinecraftServerAccess;
import net.codexarchonic.infinity.util.config.ConfigManager;
import net.minecraft.commands.CommandSource;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
//? if >1.21 {
import net.minecraft.server.ServerInfo;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.chunk.storage.ChunkIOErrorReporter;
//?} else {
/*import net.codexarchonic.infinity.item.DiscItem;
import net.minecraft.nbt.CompoundTag;
import com.mojang.datafixers.util.Pair;
import net.codexarchonic.infinity.util.core.CommonIO;
*///?}
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
//? if <1.21.9 {
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
//?}
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelResource;
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
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

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
    //? if <1.21.9 {
    @Final @Shadow
    private ChunkProgressListenerFactory progressListenerFactory;
    //?}

    public MinecraftServerMixin(String string) {
        super(string);
    }

    @Shadow
    public ServerLevel getLevel(ResourceKey<Level> key) {
        return null;
    }

    @Shadow public abstract RegistryAccess.Frozen registryAccess();

    @Shadow public abstract Path getWorldPath(LevelResource levelResource);

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
        //? if <1.21 {
        /*Path jukeboxesPath = getWorldPath(LevelResource.DATAPACK_DIR).resolve("server_sound_pack_data.json");
        if (Files.exists(jukeboxesPath)) {
            infinity$setJukeboxDataMap(CommonIO.read(jukeboxesPath));
        }
        else InfinityMod.LOGGER.info("Jukeboxes not found");
        *///?}
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

    /** Creates the body of the newly added dimension and schedules it to be loaded
     * (ensuring it will be accessible, tick, etc.) */
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
                //? if <1.21.9 {
                progressListenerFactory.create(
                        //? if >1.21 {
                        worldData.getGameRules().getInt(GameRules.RULE_SPAWN_CHUNK_RADIUS)
                        //?} else {
                        /*2
                        *///?}
                ),
                //?}
                worldData.isDebugWorld(),
                BiomeManager.obfuscateSeed(worldData.worldGenOptions().seed()),
                ImmutableList.of(),
                false,
                getLevel(Level.OVERWORLD).getRandomSequences());
        //? if <1.21.2 {
        /*getLevel(Level.OVERWORLD).getWorldBorder().addListener(new BorderChangeListener.DelegateBorderChangeListener(world.getWorldBorder()));
        *///?}
        infinity$worldsToAdd.put(key, world);
    }

    @Override
    public void infinity$loadDimensions() {
        if (!infinity$worldsToAdd.isEmpty()) {
            for (ResourceKey<Level> key: infinity$worldsToAdd.keySet()) {
                ServerLevel world = infinity$worldsToAdd.get(key);
                levels.put(key, world);
                //? if <1.21.2 && fabric {
                /*if (ModCompat.GRAVITY_CHANGER)
                GravityChangerCompat.changeMavity(world);
                *///?}
                InfinityPlatform.INSTANCE.onWorldLoad(this, world);
            }
            infinity$worldsToAdd.clear();
        }
    }

    /** Loads the scheduled dimensions. */
    @Inject(method="tickServer", at=@At("HEAD"))
    void loadDimensions(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        infinity$loadDimensions();
    }

    @Override
    public boolean infinity$hasToAdd(ResourceKey<Level> key) {
        return (infinity$worldsToAdd.containsKey(key));
    }

    /** Makes weather properly work in infdims. */
    //? if >1.21.9 {
    /*@Redirect(method="createLevels", at=@At(value="NEW", target="(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/world/level/storage/ServerLevelData;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/dimension/LevelStem;ZJLjava/util/List;Z)Lnet/minecraft/server/level/ServerLevel;"))
    *///?} else {
    @Redirect(method="createLevels", at=@At(value="NEW", target="(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lnet/minecraft/world/level/storage/ServerLevelData;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/dimension/LevelStem;Lnet/minecraft/server/level/progress/ChunkProgressListener;ZJLjava/util/List;ZLnet/minecraft/world/RandomSequences;)Lnet/minecraft/server/level/ServerLevel;"))
    //?}
    public ServerLevel fixWeatherCreation(MinecraftServer server, Executor workerExecutor, LevelStorageSource.LevelStorageAccess session, ServerLevelData properties, ResourceKey<Level> worldKey, LevelStem dimensionOptions,
                              //? if <1.21.9
                              ChunkProgressListener worldGenerationProgressListener,
                              boolean debugWorld, long seed, List<CustomSpawner> spawners, boolean shouldTickTime, RandomSequences randomSequencesState) {
        //~ if >26 'location'->'identifier'
        ServerLevelData prop = (worldKey.location().toString().contains("infinity")) ? worldData.overworldData() : properties;
        return new ServerLevel(server, workerExecutor, session, prop, worldKey, dimensionOptions
                //? if <1.21.9
                , worldGenerationProgressListener
                , debugWorld, seed, spawners, shouldTickTime, randomSequencesState);
    }

    //? if <1.21 {
    /*@Unique Map<String, Pair<String, Integer>> subtitleDurationMap = new HashMap<>();

    public void infinity$setJukeboxDataMap(CompoundTag data) {
        subtitleDurationMap.clear();
        int i = 0;
        for (String key : data.getAllKeys()) {
            CompoundTag jukebox = data.getCompound(key);
            subtitleDurationMap.put(jukebox.getString("sound_id"), Pair.of(jukebox.getString("sound_sub"), jukebox.getInt("sound_duration")));
            i++;
        }
        InfinityMod.LOGGER.info("Loaded {} jukeboxes", i);
    }
    public boolean infinity$hasJukeboxes() {
        return !subtitleDurationMap.isEmpty();
    }
    public String infinity$getRandomJukeboxId() {
        if (subtitleDurationMap.isEmpty()) return "empty";
        List<String> lst = subtitleDurationMap.keySet().stream().toList();
        return lst.get(InfinityMod.random.nextInt(lst.size()));
    }
    public String infinity$getJukeboxSubtitle(String id) {
        var v = subtitleDurationMap.get(id);
        if (v != null) return v.getFirst();
        return DiscItem.emptySubtitle;
    }
    public int infinity$getJukeboxDuration(String id) {
        var v = subtitleDurationMap.get(id);
        if (v != null) return v.getSecond();
        return DiscItem.defaultDuration;
    }
    *///?}
}
