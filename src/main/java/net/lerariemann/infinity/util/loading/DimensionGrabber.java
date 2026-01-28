package net.lerariemann.infinity.util.loading;

import com.mojang.serialization.Codec;
import net.lerariemann.infinity.access.RegistryAccess;
import net.lerariemann.infinity.dimensions.RandomDimension;
import net.lerariemann.infinity.registry.payload.ModPayloads;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Prepares Minecraft's registries for runtime injection of generated content.
 * Delegates the act of injection itself to per-registry {@link JsonGrabber}s.
 * @see net.lerariemann.infinity.mixin.core.SimpleRegistryMixin
 */
public class DimensionGrabber {
    net.minecraft.core.RegistryAccess baseRegistryManager;
    Set<WritableRegistry<?>> mutableRegistries = new HashSet<>();
    RegistryOps.RegistryInfoLookup registryInfoGetter;

    public DimensionGrabber(net.minecraft.core.RegistryAccess brm) {
        this(brm, Set.of(Registries.CONFIGURED_FEATURE,
                Registries.PLACED_FEATURE,
                Registries.CONFIGURED_CARVER,
                Registries.BIOME,
                Registries.STRUCTURE,
                Registries.STRUCTURE_SET,
                Registries.NOISE_SETTINGS,
                Registries.DIMENSION_TYPE,
                Registries.LEVEL_STEM));
    }

    /**
     * @param unfrozenKeys a set of keys for all registries we want to modify.
     * unfortunately, unfreezing and refreezing literally everything leads to problems on neoforge servers
     */
    public DimensionGrabber(net.minecraft.core.RegistryAccess brm, Set<ResourceKey<? extends Registry<?>>> unfrozenKeys) {
        baseRegistryManager = brm;
        baseRegistryManager.registries().forEach((entry) -> {
            if (unfrozenKeys.contains(entry.key())) {
                ((RegistryAccess)entry.value()).infinity$unfreeze();
                mutableRegistries.add((WritableRegistry<?>)entry.value());
            }
        });
        registryInfoGetter = getGetter();
    }

    /**
     * Grabs everything within a dimension's datapack.
     * @return a {@link LevelStem} object containing all necessary headers for creating the actual playable {@link net.minecraft.world.level.Level}.
     */
    public static LevelStem readDimensionFromDisk(RandomDimension d) {
        DimensionGrabber grabber = new DimensionGrabber(d.server.registryAccess());
        Path rootdir = Paths.get(d.getStoragePath());
        grabber.grabAllRelatedData(rootdir);
        LevelStem options = grabber.grabDimension(rootdir, d.getName());
        grabber.close();
        return options;
    }

    /**
     * Grabs contents of all files in a directory. Used by {@link net.lerariemann.infinity.util.config.SoundScanner} to runtime-inject jukebox song definition data.
     */
    public static <T> void readCategoryFromDisk(MinecraftServer server, Codec<T> codec, ResourceKey<Registry<T>> registryKey, Path path) {
        DimensionGrabber grabber = new DimensionGrabber(server.registryAccess(), Set.of(registryKey));
        grabber.buildGrabber(codec, registryKey).grabAll(path);
        grabber.close();
    }

    /**
     * Inserts one object into one registry. Used on the client to insert individual dimension types and biomes when the server asks it to, since the client
     * does not know or care about everything else a dimension datapack holds.
     * @see ModPayloads
     */
    public static <T> void grabObjectForClient(Minecraft client, Codec<T> codec, ResourceKey<Registry<T>> registryKey,
                                           ResourceLocation id, CompoundTag data) {
        if (data.isEmpty()) return;
        DimensionGrabber grabber = new DimensionGrabber(
                Objects.requireNonNull(client.getConnection()).registryAccess(),
                Set.of(registryKey)
        );
        grabber.grabObjectForClient(codec, registryKey, id, data);
        grabber.close();
    }

    private void grabAllRelatedData(Path rootdir) {
        buildGrabber(ConfiguredFeature.DIRECT_CODEC, Registries.CONFIGURED_FEATURE).grabAll(rootdir.resolve("worldgen/configured_feature"));
        buildGrabber(PlacedFeature.DIRECT_CODEC, Registries.PLACED_FEATURE).grabAll(rootdir.resolve("worldgen/placed_feature"), true);
        buildGrabber(ConfiguredWorldCarver.DIRECT_CODEC, Registries.CONFIGURED_CARVER).grabAll(rootdir.resolve("worldgen/configured_carver"));
        buildGrabber(Biome.DIRECT_CODEC, Registries.BIOME).grabAll(rootdir.resolve("worldgen/biome"));
        buildGrabber(Structure.DIRECT_CODEC, Registries.STRUCTURE).grabAll(rootdir.resolve("worldgen/structure"));
        buildGrabber(StructureSet.DIRECT_CODEC, Registries.STRUCTURE_SET).grabAll(rootdir.resolve("worldgen/structure_set"));
        buildGrabber(NoiseGeneratorSettings.DIRECT_CODEC, Registries.NOISE_SETTINGS).grabAll(rootdir.resolve("worldgen/noise_settings"));
        buildGrabber(DimensionType.DIRECT_CODEC, Registries.DIMENSION_TYPE).grabAll(rootdir.resolve("dimension_type"));
    }

    private LevelStem grabDimension(Path rootdir, String i) {
        LevelStem ret = buildGrabber(LevelStem.CODEC, Registries.LEVEL_STEM).grabWithReturn(rootdir.toString()+"/dimension", i, false);
        close();
        return ret;
    }

    private <T> JsonGrabber<T> buildGrabber(Codec<T> codec, ResourceKey<Registry<T>> key) {
        return (new JsonGrabber<>(registryInfoGetter, codec, (WritableRegistry<T>) (VersionMethods.getRegistry(baseRegistryManager, key))));
    }

    private <T> void grabObjectForClient(Codec<T> codec, ResourceKey<Registry<T>> key, ResourceLocation id, CompoundTag optiondata) {
        if (!(VersionMethods.getRegistry(baseRegistryManager, key).containsKey(ResourceKey.create(key, id)))) buildGrabber(codec, key).grab(id, optiondata, false);
    }

    /** Freezes all the registries after the grabbing is done, since registries can't be left unfrozen when the game's running. */
    private void close() {
        baseRegistryManager.registries().forEach((entry) -> entry.value().freeze());
    }

    /**
     * Creates and returns a {@link RegistryOps.RegistryInfoLookup}, which will be used by {@link JsonGrabber}s to translate raw json into all sorts of objects.
     */
    private RegistryOps.RegistryInfoLookup getGetter() {
        final Map<ResourceKey<? extends Registry<?>>, RegistryOps.RegistryInfo<?>> map = new HashMap<>();
        baseRegistryManager.registries().forEach((entry) -> map.put(entry.key(), createInfo(entry.value())));
        mutableRegistries.forEach(registry -> map.put(registry.key(), createMutableInfo(registry)));
        return new RegistryOps.RegistryInfoLookup() {
            public <T> @NotNull Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> registryRef) {
                return Optional.ofNullable((RegistryOps.RegistryInfo<T>)map.get(registryRef));
            }
        };
    }

    public static <T> RegistryOps.RegistryInfo<T> createMutableInfo(WritableRegistry<T> registry) {
        //? if >1.21.2 {
        return new RegistryOps.RegistryInfo<>(registry, registry.createRegistrationLookup(), registry.registryLifecycle());
        //?} else {
        /*return new RegistryOps.RegistryInfo<>(registry.asLookup(), registry.createRegistrationLookup(), registry.registryLifecycle());
        *///?}
    }
    public static <T> RegistryOps.RegistryInfo<T> createInfo(Registry<T> registry) {
        //? if >1.21.2 {
        return new RegistryOps.RegistryInfo<>(registry, registry, registry.registryLifecycle());
        //?} else {
        /*return new RegistryOps.RegistryInfo<>(registry.asLookup(), registry.asTagAddingLookup(), registry.registryLifecycle());
        *///?}

    }
}