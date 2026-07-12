package net.codexarchonic.infinity.dimensions;

import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.codexarchonic.infinity.dimensions.features.surface_structures.*;
import net.codexarchonic.infinity.dimensions.features.vegetation.*;
import net.codexarchonic.infinity.dimensions.features.*;
import net.codexarchonic.infinity.dimensions.features.fluid_springs.*;
import net.codexarchonic.infinity.dimensions.features.lakes.*;
import net.codexarchonic.infinity.dimensions.features.local_modifications.*;
import net.codexarchonic.infinity.dimensions.features.top_layer.*;
import net.codexarchonic.infinity.dimensions.features.raw_generation.*;
import net.codexarchonic.infinity.dimensions.features.underground_decoration.*;
import net.codexarchonic.infinity.dimensions.features.underground_ores.*;
import net.codexarchonic.infinity.dimensions.features.underground_structures.*;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.codexarchonic.infinity.util.core.RandomProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import java.util.*;

public class RandomFeaturesList {
    public ListTag data;
    public final RandomProvider PROVIDER;
    public String storagePath;

    public Random random;
    public CompoundTag surface_block;
    public RandomBiome parent;
    public List<String> blocks;
    public boolean useVanillaFeatures;

    RandomFeaturesList(RandomBiome biome) {
        parent = biome;
        random = biome.random;
        PROVIDER = biome.PROVIDER;
        surface_block = parent.parent.top_blocks.get(parent.fullname);
        storagePath = biome.parent.getStoragePath();
        blocks = new ArrayList<>();
        useVanillaFeatures = roll("generate_vanilla_features");
        data = new ListTag();
        data.add(rawGeneration());
        data.add(lakes());
        data.add(localModifications());
        data.add(undergroundStructures());
        data.add(surfaceStructures());
        data.add(undergroundOres());
        data.add(undergroundDecoration());
        data.add(fluidSprings());
        data.add(vegetation());
        data.add(topLayer());
    }

    ListTag getAllElements(ConfigType name) {
        if (!useVanillaFeatures) return new ListTag();
        List<String> lst = PROVIDER.registry.get(name).getAllNames(random::nextDouble);
        ListTag res = new ListTag();
        for (String s : lst) res.add(StringTag.valueOf(s));
        return res;
    }

    void addRandomFeature(ListTag res, RandomisedFeature feature) {
        res.add(StringTag.valueOf(feature.fullName()));
    }
    void addRandomFeature(String key, ListTag res, FeatureRegistrar registrar) {
        if (roll(key)) addRandomFeature(res, registrar);
    }
    void addRandomFeature(ListTag res, FeatureRegistrar registrar) {
        res.add(StringTag.valueOf(registrar.op(this).fullName()));
    }

    public boolean roll(String key) {
        return PROVIDER.roll(random, key);
    }

    ListTag rawGeneration() {
        ListTag res = new ListTag();
        addRandomFeature("end_island", res, RandomEndIsland::new);
        if (roll("shape")) addRandomFeature(res, new RandomShape(this, PROVIDER.randomName(random, ConfigType.SHAPE_TYPES)));
        return res;
    }

    ListTag lakes() {
        ListTag res = new ListTag();
        addRandomFeature("lake", res, RandomLake::new);
        return res;
    }

    ListTag localModifications() {
        ListTag res = getAllElements(ConfigType.LOCAL_MOD);
        addRandomFeature("iceberg", res, RandomIceberg::new);
        addRandomFeature("geode", res, RandomGeode::new);
        addRandomFeature("rock", res, RandomRock::new);
        return res;
    }

    ListTag undergroundStructures() {
        ListTag res = new ListTag();
        addRandomFeature("dungeon", res, RandomDungeon::new);
        addRandomFeature("fossil", res, RandomFossil::new);
        return res;
    }

    ListTag surfaceStructures() {
        ListTag res = new ListTag();
        addRandomFeature("end_spikes", res, RandomEndSpikes::new);
        addRandomFeature("end_gateway", res, RandomEndGateway::new);
        addRandomFeature("delta", res, RandomDelta::new);
        addRandomFeature("columns", res, RandomColumns::new);
        addRandomFeature("well", res, RandomWell::new);
        return res;
    }

    ListTag undergroundOres() {
        ListTag res = getAllElements(ConfigType.UNDERGROUND_ORES);
        int num_ores = random.nextInt( 4);
        int num_disks = Math.max(3, (int) Math.floor(random.nextExponential()));
        for (int i = 0; i < num_ores; i++) {
            addRandomFeature(res, new RandomOre(this));
        }
        for (int i = 0; i < num_disks; i++) {
            addRandomFeature(res, new RandomDisk(this));
        }
        return res;
    }

    ListTag undergroundDecoration() {
        ListTag res = getAllElements(ConfigType.UNDERGROUND_DEC);
        addRandomFeature("blobs", res, RandomBlobs::new);
        addRandomFeature("ceiling_blobs", res, RandomCeilingBlob::new);
        return res;
    }

    ListTag fluidSprings() {
        ListTag res = new ListTag();
        addRandomFeature("springs", res, RandomSpring::new);
        return res;
    }

    ListTag vegetation() {
        ListTag res = new ListTag();
        addRandomFeature("vegetation", res, RandomVegetation::new);
        res.addAll(getAllElements(ConfigType.VEG1));
        addRandomFeature(res, RandomFlowerPatch::new);
        res.add(StringTag.valueOf(PROVIDER.randomName(random, ConfigType.GRASS)));
        res.addAll(getAllElements(ConfigType.VEG2));
        addRandomFeature("surface_patch", res, RandomSurfacePatch::new);
        addRandomFeature("floating_patch", res, RandomFloatingPatch::new);
        if (NbtUtils.getString(parent.parent.default_fluid, "Name").contains("water")) {
            addRandomFeature("water_plants", res, RandomSeagrass::new);
            addRandomFeature("water_plants", res, RandomPickle::new);
            addRandomFeature("water_plants", res, RandomKelp::new);
        }
        return res;
    }

    ListTag topLayer() {
        ListTag res = getAllElements(ConfigType.TOP_LAYER);
        addRandomFeature("bonus_chest", res, RandomBonusChest::new);
        return res;
    }

    interface FeatureRegistrar {
        RandomisedFeature op(RandomFeaturesList parent);
    }
}
