package net.lerariemann.infinity.mixin.fixes;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Codec;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiSection;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.storage.SectionStorage;
//? if >1.21 {

import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
import net.minecraft.world.level.chunk.storage.ChunkIOErrorReporter;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?}

@Mixin(PoiManager.class)
public abstract class PointOfInterestStorageMixin extends SectionStorage<PoiSection
        //? if >1.21.2
        , PoiSection.Packed
        > {

    //? if >1.21.2 {
    public PointOfInterestStorageMixin(SimpleRegionStorage simpleRegionStorage, Codec<PoiSection.Packed> codec, Function<PoiSection, PoiSection.Packed> packer, BiFunction<PoiSection.Packed, Runnable, PoiSection> unpacker, Function<Runnable, PoiSection> factory, RegistryAccess registryAccess, ChunkIOErrorReporter errorReporter, LevelHeightAccessor levelHeightAccessor) {
        super(simpleRegionStorage, codec, packer, unpacker, factory, registryAccess, errorReporter, levelHeightAccessor);
    }
    //?} else if >1.21 {
    /*public PointOfInterestStorageMixin(SimpleRegionStorage storageAccess, Function<Runnable, Codec<PoiSection>> codecFactory, Function<Runnable, PoiSection> factory, RegistryAccess registryManager, ChunkIOErrorReporter errorHandler, LevelHeightAccessor world) {
        super(storageAccess, codecFactory, factory, registryManager, errorHandler, world);
    }
    *///?} else {
    /*public PointOfInterestStorageMixin(Path folder, Function<Runnable, Codec<PoiSection>> codec, Function<Runnable, PoiSection> factory, DataFixer fixerUpper, DataFixTypes type, boolean sync, RegistryAccess registryAccess, LevelHeightAccessor levelHeightAccessor) {
        super(folder, codec, factory, fixerUpper, type, sync, registryAccess, levelHeightAccessor);
    }
    *///?}

    /* There's null-unsafe inner-minecraft code that can crash if we don't do this */
    //? if >1.21.9 {
    /*@Inject(method = "add", at = @At("HEAD"), cancellable = true)
    void makePoiNullSafe(BlockPos pos, Holder<PoiType> type, CallbackInfoReturnable<PoiRecord> cir) {
        if (levelHeightAccessor.isOutsideBuildHeight(pos.getY())) cir.cancel();
    }
    *///?} else {
    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    void makePoiNullSafe(BlockPos pos, Holder<PoiType> type, CallbackInfo ci) {
        if (levelHeightAccessor.isOutsideBuildHeight(pos.getY())) ci.cancel();
    }
    //?}
}
