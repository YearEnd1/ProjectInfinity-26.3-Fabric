package net.codexarchonic.infinity.util.core;

import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.util.loading.DimensionGrabber;
import net.codexarchonic.infinity.util.platform.InfinityPlatform;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.nio.file.Path;
import java.util.List;

public record CorePack(RandomProvider provider, Path savingPath) {
    void generate() {
//        saveVersioned();
        saveTrees();
        saveEndOfTime();
        if (!savingPath.resolve("pack.mcmeta").toFile().exists()) {
            CompoundTag mcmeta = InfinityPlatform.dataPackMcmeta("Common content providers for Infinite Dimensions");
            CommonIO.write(mcmeta, savingPath, "pack.mcmeta");
        }
    }

    void grab(MinecraftServer server) {
        DimensionGrabber.readCategoryFromDisk(server, DimensionType.DIRECT_CODEC, Registries.DIMENSION_TYPE,
                savingPath.resolve("data/infinity/dimension_type"));
        DimensionGrabber.readCategoryFromDisk(server, Biome.DIRECT_CODEC, Registries.BIOME,
                savingPath.resolve("data/infinity/worldgen/biome"));
        DimensionGrabber.readCategoryFromDisk(server, ConfiguredFeature.DIRECT_CODEC, Registries.CONFIGURED_FEATURE,
                savingPath.resolve("data/infinity/worldgen/configured_feature"));
    }

    public static CompoundTag treePlacement(String tree, String block) {
        return CommonIO.readAndFormat(InfinityMod.utilPath.resolve("placements/tree_vanilla.json").toString(), tree, block);
    }

//    void saveVersioned() {
//        String dirName = "util" + (InfinityPlatform.version);
//        try (Stream<Path> files = Files.walk(InfinityPlatform.INSTANCE.getRootPathInJar(dirName))) {
//            files.filter(p -> p.toString().endsWith(".json")).forEach(p -> ConfigManager.registerConfig(p, savingPath, dirName));
//            Files.deleteIfExists(tempFile); //being a good method cleaning up after itself :3
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    void saveTrees() {
        List<String> trees = provider.registry.get(ConfigType.TREES).getAllNames();
        double size = trees.size();
        CompoundTag c = new CompoundTag();
        ListTag l = new ListTag();
        c.put("default", treePlacement(trees.get(0), "minecraft:grass_block"));
        for (int i = 1; i < size; i++) {
            CompoundTag c1 = new CompoundTag();
            c1.put("feature", treePlacement(trees.get(i), "minecraft:grass_block"));
            c1.putDouble("chance", 1 / (size - i + 1));
            l.add(c1);
        }
        c.put("features", l);
        CompoundTag c2 = new CompoundTag();
        c2.putString("type", "minecraft:random_selector");
        c2.put("config", c);
        CommonIO.write(c2, savingPath.resolve("data/" + InfinityMod.MOD_ID + "/worldgen/configured_feature"), "all_trees.json");
    }

    void saveEndOfTime() {
        CompoundTag res = new CompoundTag();
        for (ConfigType category : ConfigType.mobCategories) {
            ListTag entries = new ListTag();
            for (String mob: provider.registry.get(category).getAllNames()) {
                CompoundTag entry = new CompoundTag();
                entry.putString("type", mob);
                entry.putInt("minCount", 1);
                entry.putInt("maxCount", 1);
                entry.putInt("weight", 1);
                entries.add(entry);
            }
            res.put(category.getKey(), entries);
        }
        CommonIO.write(CommonIO.readAndAddCompound(InfinityMod.utilPath.resolve("end_of_time.json").toString(), res),
                savingPath.resolve("data/" + InfinityMod.MOD_ID + "/worldgen/biome"), "end.json");
    }
}
