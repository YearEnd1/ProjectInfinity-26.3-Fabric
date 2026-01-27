package net.lerariemann.infinity.mixin.options;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.access.InfinityOptionsAccess;
import net.lerariemann.infinity.access.Timebombable;
import net.lerariemann.infinity.options.InfinityOptions;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
//? if <1.21.9 {
import net.minecraft.server.level.progress.ChunkProgressListener;
//?}
//? if <1.21.2
/*import net.minecraft.util.profiling.ProfilerFiller;
*/
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin extends Level implements WorldGenLevel, InfinityOptionsAccess, Timebombable, SignalGetter {
    @Shadow public abstract void updateNeighborsAt(BlockPos pos, Block block);

    @Unique
    public InfinityOptions infinity$options;
    @Unique
    public int infinity$timebombProgress;


    //? if >1.21.2 {
    protected ServerWorldMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }
    //?} else {
    /*protected ServerWorldMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, Supplier<ProfilerFiller> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }
    *///?}

    @Inject(method = "<init>", at = @At("TAIL"))
    //? if >1.21.9 {
    /*private void timebomb(MinecraftServer server, Executor dispatcher, LevelStorageSource.LevelStorageAccess storageSource, ServerLevelData levelData, ResourceKey<Level> worldKey, LevelStem levelStem, boolean isDebug, long biomeZoomSeed, List customSpawners, boolean tickTime, RandomSequences randomSequences, CallbackInfo ci) {
    *///?} else {
    private void timebomb(MinecraftServer server, Executor workerExecutor, LevelStorageSource.LevelStorageAccess session, ServerLevelData properties, ResourceKey<Level> worldKey, LevelStem dimensionOptions, ChunkProgressListener worldGenerationProgressListener, boolean debugWorld, long seed, List<CustomSpawner> spawners, boolean shouldTickTime, RandomSequences randomSequencesState, CallbackInfo ci) {
    //?}
        infinity$options = InfinityOptions.generate(server, worldKey.location());
        DimensionType t = dimensionType();
        ((InfinityOptionsAccess)(Object)t).infinity$setOptions(infinity$options);
        infinity$timebombProgress = 0;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void addTimebombProgress(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (InfinityMethods.isInfinity(dimension()) && infinity$timebombProgress > 0) infinity$timebombProgress++;
    }

    /**
     * InfDims share the weather with the Overworld, but if we don't do this, they will also tick it, causing the weather cycle to be very rapid
     */
    @Inject(method="advanceWeatherCycle", at=@At(value= "HEAD"), cancellable = true)
    private void weather_inj(CallbackInfo ci) {
        if (InfinityMethods.isInfinity(dimension())) ci.cancel();
    }

    @ModifyExpressionValue(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;isRandomlyTicking()Z"))
    boolean mev2(boolean original, @Local LevelChunkSection cs) {
        return original || (infinity$options.isHaunted() && !cs.hasOnlyAir());
    }

    @Inject(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;isRandomlyTicking()Z"))
    void inj(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci, @Local BlockState bs, @Local BlockPos blockPos2, @Local FluidState fs) {
        if (infinity$options.isHaunted() && random.nextInt(200) < (int)(Math.floor(getMoonBrightness() * 4))) {
            updateNeighborsAt(blockPos2, bs.getBlock());
        }
    }

    @Override
    public int getSignal(BlockPos pos, Direction direction) {
        if (infinity$options.isHaunted() && InfinityMod.random.nextFloat() > 0.9f) return InfinityMod.random.nextInt(16);
        BlockState blockState = this.getBlockState(pos);
        int i = blockState.getSignal(this, pos, direction);
        return blockState.isRedstoneConductor(this, pos) ? Math.max(i, this.getDirectSignalTo(pos)) : i;
    }

    @Override
    public void infinity$timebomb() {
        if(InfinityMethods.isInfinity(dimension())) {
            infinity$timebombProgress = 1;
        }
    }
    @Override
    public boolean infinity$tryRestore() {
        if(InfinityMethods.isInfinity(dimension()) && (infinity$timebombProgress > Timebombable.getCooldownTicks())) {
            infinity$timebombProgress = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean infinity$isTimebombed() {
        return infinity$timebombProgress > 0;
    }
    @Override
    public int infinity$getTimebombProgress() {
        return infinity$timebombProgress;
    }

    @Override
    public InfinityOptions infinity$getOptions() {
        return InfinityOptions.nullSafe(infinity$options);
    }
    @Override
    public void infinity$setOptions(InfinityOptions options) {
        infinity$options = options;
    }
}
