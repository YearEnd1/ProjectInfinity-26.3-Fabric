package net.lerariemann.infinity.util.config;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.access.BlockEntityTypeAccess;
import net.lerariemann.infinity.block.entity.CosmicAltarBlockEntity;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.util.var.ColorLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
//? if >1.21.2 {
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
//?} else {
/*import net.minecraft.util.random.WeightedRandomList;
*///?}
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Methods for generating modular config files (scanning all registries for usable content) and writing them to the disk.</p>
 * <p>Generating most content types is done via {@link ConfigFactory}.</p>
 * <p>Used by the {@link CosmicAltarBlockEntity} performing an invocation.</p>
 * @author LeraRiemann
 */
public interface ConfigGenerator {
    static void generateAll(MinecraftServer server) {
        ConfigFactory.of(BuiltInRegistries.ITEM).generate(ConfigType.ITEMS);
        ConfigFactory.of(BuiltInRegistries.PARTICLE_TYPE, ConfigGenerator::extractParticle).generate(ConfigType.PARTICLES);
        ConfigFactory.of(BuiltInRegistries.ENTITY_TYPE, ConfigGenerator::extractMob).generate(ConfigType.MOBS);
        ConfigFactory.of(BuiltInRegistries.MOB_EFFECT, ConfigGenerator::extractEffect).generate(ConfigType.EFFECTS);
        generateSounds();
        generateBlockTags();
        generateBEs();
        SurfaceRuleScanner.scan(server);
        RegistryAccess manager = server.registryAccess();
        //? if >1.21 {
        ConfigFactory.of(VersionMethods.getRegistry(manager, Registries.JUKEBOX_SONG)).generate(ConfigType.JUKEBOXES);
        //?}
        ConfigFactory.of(VersionMethods.getRegistry(manager, Registries.BIOME), ConfigGenerator::extractBiome).generate(ConfigType.BIOMES);
        ConfigFactory.of(VersionMethods.getRegistry(manager, Registries.STRUCTURE), ConfigGenerator::extractStructure).generate(ConfigType.STRUCTURES);
        ConfigFactory.of(VersionMethods.getRegistry(manager, Registries.CONFIGURED_FEATURE), ConfigGenerator::extractFeature).generate(ConfigType.TREES);
        generateLootTables(server);
    }

    static void generateSounds() {
        Registry<SoundEvent> r = BuiltInRegistries.SOUND_EVENT;
        DataCollection music = new DataCollection.Logged(ConfigType.MUSIC, "music tracks");
        DataCollection sounds = new DataCollection.Logged(ConfigType.SOUNDS);
        r.registryKeySet().forEach(a -> {
            ResourceLocation id = a.location();
            if (id.toString().contains("music")) music.addIdentifier(id);
            else sounds.addIdentifier(id);
        });
        sounds.save();
        music.save();
    }

    static void generateBlockTags() {
        DataCollection tagMap = new DataCollection.Logged(ConfigType.TAGS, "block tags");
        //? if >1.21.2 {
        BuiltInRegistries.BLOCK.listTagIds().forEach(tagKey -> tagMap.addIdentifier(tagKey.location()));
        //?} else {
        /*BuiltInRegistries.BLOCK.getTagNames().forEach(tagKey -> tagMap.addIdentifier(tagKey.location()));
        *///?}
        tagMap.save();
    }

    static void generateLootTables(MinecraftServer server) {
        //? if >1.21.7 {
        var r = VersionMethods.getRegistry(server.reloadableRegistries().lookup(), Registries.LOOT_TABLE);
        DataCollection lootTables = new DataCollection.Logged(ConfigType.LOOT_TABLES, "loot tables");
        r.listElementIds().forEach(id -> lootTables.addIdentifier(id.location()));
        lootTables.save();
        //?} else if =1.21.1 {
        /*ConfigFactory.of(server.reloadableRegistries().get().registryOrThrow(Registries.LOOT_TABLE), ConfigGenerator::extractLootTable)
                .generate(ConfigType.LOOT_TABLES);
        *///?} else {
        /*DataCollection lootTables = new DataCollection.Logged(ConfigType.LOOT_TABLES, "loot tables");
        server.getLootData().getKeys(net.minecraft.world.level.storage.loot.LootDataType.TABLE).forEach((key)->{
            LootTable table = server.getLootData().getLootTable(key);
            //? if >1.21 {
            ResourceLocation type = LootContextParamSets.REGISTRY.inverse().get(table.getParamSet());
            //?} else {
            /^ResourceLocation type = LootContextParamSets.getKey(table.getParamSet());
            ^///?}
            if (type == null) return;
            CompoundTag res = new CompoundTag();
            res.putString("Type", type.getPath()); //this data is unused for now but might be important later
            lootTables.add(key.getNamespace(), key.getPath(), res);
        });
        lootTables.save();
        *///?}

    }

    static Set<String> generateFluids() {
        DataCollection fluidMap = new DataCollection.Logged(ConfigType.FLUIDS);
        Registry<Fluid> r = BuiltInRegistries.FLUID;
        Set<String> fluidBlockNames = new HashSet<>();
        r.registryKeySet().forEach(key -> {
            Fluid fluid = VersionMethods.getFromRegistry(BuiltInRegistries.FLUID, key);
            assert fluid != null;
            Block block = fluid.defaultFluidState().createLegacyBlock().getBlock();
            String blockName = BuiltInRegistries.BLOCK.getKey(block).toString();
            CompoundTag data = new CompoundTag();
            data.putString("fluidName", key.location().toString());
            fluidBlockNames.add(blockName);
            if (fluid instanceof FlowingFluid fl
                    && fl.equals(fl.getSource())
                    && block.defaultBlockState().getFluidState().is(fl)) {
                String modId = key.location().getNamespace();
                fluidMap.add(modId, blockName, data);
            }
        });
        fluidMap.save();
        return fluidBlockNames;
    }

    static void generateBlocks(ServerLevel serverWorld, BlockPos inAir, BlockPos onAltar, Set<String> excludedBlockNames) {
        DataCollection blockMap = new DataCollection.Logged(ConfigType.BLOCKS);
        DataCollection colorPresetMap = new DataCollection(ConfigType.COLOR_PRESETS);
        DataCollection flowerMap = new DataCollection(ConfigType.FLOWERS);
        Registry<Block> r = BuiltInRegistries.BLOCK;
        r.registryKeySet().forEach(key -> {
            String blockName = key.location().toString();
            if(excludedBlockNames.contains(blockName)) return;
            Block block = VersionMethods.getBlock(key);
            assert block != null;
            String modId = key.location().getNamespace();
            blockMap.add(modId, blockName, extractBlock(key, serverWorld, inAir, onAltar));
            if (blockName.contains("magenta") && !isLaggy(block) && isFloat(block.defaultBlockState(), serverWorld, inAir)) {
                if (checkColorSet(blockName)) colorPresetMap.add(modId, blockName.replace("magenta", "$"));
            }
            if (block.defaultBlockState().is(BlockTags.SMALL_FLOWERS)) flowerMap.addIdentifier(key.location());
        });
        blockMap.save();
        colorPresetMap.save();
        flowerMap.save();
    }

    static void generateBEs() {
        Registry<BlockEntityType<?>> r = BuiltInRegistries.BLOCK_ENTITY_TYPE;
        DataCollection chests = new DataCollection.Logged(ConfigType.CHESTS);
        Set<ResourceLocation> allBlocks = new HashSet<>();
        r.registryKeySet().forEach(key -> {
            BlockEntityTypeAccess<? extends BlockEntity> bet = (BlockEntityTypeAccess<?>)(VersionMethods.getFromRegistry(r, key));
            assert bet != null;
            Set<Block> blocks = bet.infinity$getBlocks();
            if (!blocks.isEmpty()) {
                try {
                    BlockEntity be = bet.infinity$getFactory().create(BlockPos.ZERO, ((Block)blocks.toArray()[0]).defaultBlockState());
                    if (be instanceof RandomizableContainerBlockEntity && !(be instanceof ShulkerBoxBlockEntity)) {
                        for (Block b : blocks) allBlocks.add(BuiltInRegistries.BLOCK.getKey(b));
                    }
                } catch (Exception ignored) {
                    // do not try and use block entities that do not set valid blocks or have specific requirements.
                }
            }
        });
        allBlocks.forEach(chests::addIdentifier);
        chests.save();
    }

    static boolean checkColorSet(String block) {
        AtomicInteger successCounter = new AtomicInteger();
        Arrays.stream(ColorLogic.vanillaColors).forEach(color -> {
            int i = block.lastIndexOf("magenta");
            String blockColored = block.substring(0, i) + color + block.substring(i+7);
            if (BuiltInRegistries.BLOCK.containsKey(VersionMethods.id(blockColored))) {
                successCounter.addAndGet(1);
            }
        });
        return (successCounter.get() == ColorLogic.vanillaColors.length);
    }

    static CompoundTag extractBlock(ResourceKey<Block> key, LevelReader w, BlockPos inAir, BlockPos onStone) {
        Block b = VersionMethods.getBlock(key);
        assert b!= null;
        BlockState bs = b.defaultBlockState();
        CompoundTag res = new CompoundTag();
        res.putBoolean("laggy", isLaggy(b));
        res.putBoolean("full", isFull(bs, w, inAir));
        res.putBoolean("float", isFloat(bs, w, inAir));
        CompoundTag properties = new CompoundTag();
        if (bs.hasProperty(BlockStateProperties.PERSISTENT)) properties.putString("persistent", "true");
        if (bs.hasProperty(BlockStateProperties.LIT)) properties.putString("lit", "false");
        if (bs.hasProperty(BlockStateProperties.ATTACH_FACE)) {
            properties.putString("face", "floor");
            bs = bs.setValue(BlockStateProperties.ATTACH_FACE, AttachFace.FLOOR);
        }
        res.putBoolean("top", isTop(bs, w, onStone));
        if (!properties.isEmpty()) res.put("Properties", properties);
        return res;
    }

    static boolean isLaggy(Block b) {
        return (b.defaultBlockState().hasBlockEntity());
    }

    static boolean isTop(BlockState bs, LevelReader w, BlockPos onStone) {
        try {
            return bs.canSurvive(w, onStone);
        }
        catch (Exception e) {
            return false;
        }
    }
    static boolean isFloat(BlockState bs, LevelReader w, BlockPos inAir) {
        try {
            return bs.canSurvive(w, inAir) && !(bs.getBlock() instanceof FallingBlock);
        }
        catch (Exception e) {
            return false;
        }
    }
    static boolean isFull(BlockState bs, LevelReader w, BlockPos inAir) {
        return Block.isShapeFullBlock(bs.getCollisionShape(w, inAir)) && Block.isShapeFullBlock(bs.getShape(w, inAir));
    }

    static CompoundTag extractParticle(ResourceKey<ParticleType<?>> key) {
        ResourceLocation id = key.location();
        if (!id.getNamespace().equals("minecraft") && !(VersionMethods.getFromRegistry(BuiltInRegistries.PARTICLE_TYPE, id) instanceof SimpleParticleType)) return null;
        return new CompoundTag();
    }

    static CompoundTag extractMob(ResourceKey<EntityType<?>> key) {
        MobCategory sg = VersionMethods.getFromRegistry(BuiltInRegistries.ENTITY_TYPE, key.location()).getCategory();
        if (sg == MobCategory.MISC) return null; //minecarts n stuff
        CompoundTag mob = new CompoundTag();
        mob.putString("Category", sg.getSerializedName());
        return mob;
    }

    static CompoundTag extractEffect(ResourceKey<MobEffect> key) {
        CompoundTag res = new CompoundTag();
        Optional<MobEffect> o = BuiltInRegistries.MOB_EFFECT.getOptional(key);
        if (o.isEmpty()) return null;
        MobEffect effect = o.get();
        res.putString("Category", switch (effect.getCategory()) { //this data is unused for now but might be important later
            case HARMFUL -> "harmful";
            case BENEFICIAL -> "beneficial";
            case NEUTRAL -> "neutral";
        });
        res.putInt("Color", effect.getColor());
        res.putBoolean("Instant", effect.isInstantenous());
        return res;
    }

    static CompoundTag extractStructure(Registry<Structure> registry, ResourceKey<Structure> key) {
        ResourceLocation id = key.location();
        if (id.getNamespace().equals(InfinityMod.MOD_ID) && id.getPath().contains("_")) return null;
        Optional<Structure> o = registry.getOptional(key);
        if (o.isEmpty()) return null;
        Structure structure = o.get();
        String step = structure.step().name().toLowerCase();
        String adaptation = structure.terrainAdaptation().name().toLowerCase();
        CompoundTag overrides = genOverrides(structure.spawnOverrides());
        CompoundTag res = new CompoundTag();
        res.putString("id", id.toString());
        res.putString("step", step);
        res.put("spawn_overrides", overrides);
        res.putString("terrain_adaptation", adaptation);
        return res;
    }

    static CompoundTag genOverrides(Map<MobCategory, StructureSpawnOverride> overrides) {
        CompoundTag res = new CompoundTag();
        overrides.forEach((key, value) -> {
            if (key != null && value != null) res.put(key.name().toLowerCase(), genOverride(value));
        });
        return res;
    }

    static CompoundTag genOverride(StructureSpawnOverride spawns) {
        CompoundTag res = new CompoundTag();
        StructureSpawnOverride.BoundingBoxType box = spawns.boundingBox();
        res.putString("bounding_box", box == StructureSpawnOverride.BoundingBoxType.PIECE ? "piece" : "full");
        res.put("spawns", genSpawns(spawns.spawns()));
        return res;
    }

    static ListTag genSpawns(
            //? if >1.21.2 {
            WeightedList
            //?} else {
            /*WeightedRandomList
            *///?}
            <MobSpawnSettings.SpawnerData> entries) {
        ListTag lst = new ListTag();
        entries.unwrap().forEach((entry) -> lst.add(genEntry(entry)));
        return lst;
    }

    static CompoundTag genEntry(
            //? if >1.21.2 {
            Weighted<MobSpawnSettings.SpawnerData> spawnerDataWeighted
            //?} else {
            /*MobSpawnSettings.SpawnerData entry
            *///?}
             ) {
        //? if >1.21.2 {
        MobSpawnSettings.SpawnerData entry = spawnerDataWeighted.value();
        var weight = spawnerDataWeighted.weight();
        //?} else {
        /*var weight = entry.getWeight().asInt();
        *///?}
        CompoundTag res = new CompoundTag();
        res.putString("type", BuiltInRegistries.ENTITY_TYPE.getKey(entry.type
                //? if >1.21.7
                ()
        ).toString());
        res.putInt("maxCount", entry.maxCount
                //? if >1.21.7
                        ()
        );
        res.putInt("minCount", entry.minCount
                //? if >1.21.7
                        ()
        );
        res.putInt("weight", weight);
        return res;
    }

    static CompoundTag extractFeature(Registry<ConfiguredFeature<?,?>> registry, ResourceKey<ConfiguredFeature<?,?>> key) {
        ResourceLocation id = key.location();
        if (id.getNamespace().equals(InfinityMod.MOD_ID)) return null; //our mod's custom trees
        Optional<ConfiguredFeature<?,? extends Feature<?>>> o = registry.getOptional(key);
        if (o.isEmpty()) return null;
        ConfiguredFeature<?,? extends Feature<?>> feature = o.get();
        String type = getFeatureType(feature.feature());
        if (type.isEmpty()) return null;
        CompoundTag res = new CompoundTag();
        res.putString("Type", type); //this data is unused for now but might be important later
        return res;
    }

    static String getFeatureType(Feature<?> type) {
        if (type.equals(Feature.TREE)) return "tree";
        if (type.equals(Feature.HUGE_FUNGUS)) return "huge_fungus";
        if (type.equals(Feature.HUGE_BROWN_MUSHROOM)) return "huge_brown_mushroom";
        if (type.equals(Feature.HUGE_RED_MUSHROOM)) return "huge_red_mushroom";
        return "";
    }

    static CompoundTag extractLootTable(Registry<LootTable> registry, ResourceKey<LootTable> key) {
        Optional<LootTable> o = registry.getOptional(key);
        if (o.isEmpty()) return null;
        LootTable table = o.get();
        //? if >1.21 {
        ResourceLocation type = LootContextParamSets.REGISTRY.inverse().get(table.getParamSet());
        //?} else {
        /*ResourceLocation type = LootContextParamSets.getKey(table.getParamSet());
        *///?}
        if (type == null) return null;
        CompoundTag res = new CompoundTag();
        res.putString("Type", type.getPath()); //this data is unused for now but might be important later
        return res;
    }

    static CompoundTag extractBiome(Registry<Biome> registry, ResourceKey<Biome> key) {
        ResourceLocation id = key.location();
        if (id.getNamespace().equals(InfinityMod.MOD_ID)) return null; //cull our generated biomes
        Optional<Biome> o = registry.getOptional(key);
        if (o.isEmpty()) return null;
        Biome biome = o.get();
        CompoundTag res = new CompoundTag();
        res.putInt("Color", biome.getFoliageColor());
        return res;
    }
}
