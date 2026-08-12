package net.codexarchonic.infinity.mixin.options;

import net.codexarchonic.infinity.access.InfinityOptionsAccess;
import net.codexarchonic.infinity.options.InfinityOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientWorldMixin extends Level implements InfinityOptionsAccess {
    @Unique
    public InfinityOptions infinity$options;

    //? if >1.21.2 {
    protected ClientWorldMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }
    //?} else {
    /*protected ClientWorldMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, Supplier<ProfilerFiller> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }
    *///?}

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setClientInfinityOptions(ClientPacketListener connection, ClientLevel.ClientLevelData levelData, ResourceKey dimension, Holder dimensionTypeRegistration, int viewDistance, int simulationDistance,
    //? if >1.21.2 {
    LevelRenderer levelRenderer, boolean isDebug, long biomeZoomSeed, int seaLevel, CallbackInfo ci) {
    //?} else {
    /*Supplier<ProfilerFiller> profiler, LevelRenderer worldRenderer, boolean debugWorld, long seed, CallbackInfo ci) {
    *///?}
        infinity$setOptions(InfinityOptions.ofClient());
    }

    @Override
    public InfinityOptions infinity$getOptions() {
        return InfinityOptions.nullSafe(infinity$options);
    }
    @Override
    public void infinity$setOptions(InfinityOptions options) {
        infinity$options = options;
        ((InfinityOptionsAccess)(Object) dimensionType()).infinity$setOptions(options);
    }
}
