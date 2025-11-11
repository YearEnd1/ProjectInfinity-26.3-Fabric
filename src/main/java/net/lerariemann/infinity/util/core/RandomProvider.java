package net.lerariemann.infinity.util.core;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.var.ColorLogic;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;

import static net.lerariemann.infinity.InfinityMod.configPath;

public class RandomProvider {
    public Map<ConfigType, WeighedStructure> registry = new HashMap<>();
    private final Map<String, Double> rootChances = new HashMap<>();
    private final Map<String, Boolean> gameRules = new HashMap<>();
    private final Map<String, Integer> gameRulesInt = new HashMap<>();
    private final Map<String, Double> gameRulesDouble = new HashMap<>();
    public ArrayList<String> disabledDimensions = new ArrayList<>();
    public Path savingPath;
    private String portalKey;
    public String salt;
    public Easterizer easterizer;

    public RandomProvider() {
        registerAll();
        easterizer = new Easterizer();
    }

    public RandomProvider(Path savingPath) {
        this();
        this.savingPath = savingPath;
        (new CorePack(this, savingPath)).generate();
    }

    public Optional<Item> getPortalKeyAsItem() {
        if (portalKey.isBlank()) return Optional.empty();
        return BuiltInRegistries.ITEM.getOptional(VersionMethods.id(portalKey));
    }
    public boolean isPortalKeyBlank() {
        return getPortalKeyAsItem().isEmpty();
    }
    public boolean roll(Random random, String key) {
        return (random.nextDouble() < rootChances.getOrDefault(key, 0.0));
    }

    private static <T> T getStaticRule(BiFunction<RandomProvider, String, T> applier,
                                      BiFunction<CompoundTag, String, T> applier2,
                                      String key, T def) {
        if (InfinityMod.provider != null) {
            return applier.apply(InfinityMod.provider, key);
        }
        Path root = configPath.resolve("infinity.json");
        if (!root.toFile().exists()) return def;
        CompoundTag rules = NbtUtils.getCompound(CommonIO.read(configPath.resolve("infinity.json")), "gameRules");
        if (!rules.contains(key)) return def;
        return applier2.apply(rules, key);
    }
    public static boolean rule(String key) {
        return getStaticRule((p, k) -> p.gameRules.getOrDefault(k, false), CompoundTag::getBoolean, key, false);
    }
    public static int ruleInt(String key) {
        return getStaticRule(RandomProvider::_ruleInt, (p, k) -> ((NumericTag) Objects.requireNonNull(p.get(k))).getAsInt(), key, -1);
    }
    private int _ruleInt(String key) {
        if (gameRulesInt.containsKey(key)) return gameRulesInt.get(key);
        return (gameRulesDouble.get(key)).intValue();
    }

    public String randomName(Random random, ConfigType key) {
        return randomName(random.nextDouble(), key);
    }
    public String randomName(net.minecraft.util.RandomSource random, ConfigType key) {
        return randomName(random.nextDouble(), key);
    }
    public String randomName(double d, ConfigType key) {
        if (registry.containsKey(key))
            return registry.get(key).getName(d);
        return key.getDef();
    }

    public CompoundTag randomElement(net.minecraft.util.RandomSource random, ConfigType key) {
        return randomElement(random.nextDouble(), key);
    }
    public CompoundTag randomElement(Random random, ConfigType key) {
        return randomElement(random.nextDouble(), key);
    }
    public CompoundTag randomElement(double d, ConfigType key) {
        if (registry.containsKey(key)) {
            WeighedStructure ws = registry.get(key);
            return ws.getElement(ws.getName(d), key.getConverter());
        }
        return key.fromName(key.getDef());
    }

    void registerAll() {
        readRootConfig();
        registerHardcoded();
        for (ConfigType type: ConfigType.normalModular) registerCategory(type);
        extractBlocks();
        extractMobs();
    }
    void registerCategory(ConfigType type) {
        registerCategory(type, CommonIO.readCategory(type));
    }
    void registerCategory(ConfigType type, List<CompoundTag> data) {
        registry.put(type, new WeighedStructure.Recursor(data, type));
    }

    void readRootConfig() {
        CompoundTag rootConfig = CommonIO.read(configPath.resolve("infinity.json"));
        portalKey = NbtUtils.getString(rootConfig, "portalKey");
        salt = NbtUtils.getString(rootConfig, "salt");
        CompoundTag gameRules = NbtUtils.getCompound(rootConfig, "gameRules");
        for (String s: gameRules.getAllKeys()) {
            Tag elem = gameRules.get(s);
            if (elem!=null) {
                if (elem.getId() == Tag.TAG_INT) gameRulesInt.put(s, NbtUtils.getInt(gameRules, s));
                if (elem.getId() == Tag.TAG_DOUBLE) gameRulesDouble.put(s, NbtUtils.getDouble(gameRules, s));
                else this.gameRules.put(s, NbtUtils.getBoolean(gameRules, s));
            }
        }
        CompoundTag rootChances = NbtUtils.getCompound(rootConfig, "rootChances");
        for (String c: rootChances.getAllKeys()) {
            var compound = NbtUtils.getCompound(rootChances, c);
            for (String s: compound.getAllKeys()) {
                this.rootChances.put(s, NbtUtils.getDouble(compound, s));
            }
        }

        ListTag disabledDimensions = NbtUtils.getList(rootConfig, "disabledDimensions", Tag.TAG_STRING);
        for (Tag jsonElement : disabledDimensions) {
            this.disabledDimensions.add(String.valueOf(jsonElement));
        }
    }

    void extractBlocks() {
        List<CompoundTag> blocksSettings = CommonIO.readCategory(ConfigType.BLOCKS);
        List<CompoundTag> allBlocks = new ArrayList<>();
        List<CompoundTag> fullBlocks = new ArrayList<>();
        List<CompoundTag> topBlocks = new ArrayList<>();
        List<CompoundTag> blocksFeatures = new ArrayList<>();
        List<CompoundTag> fullBlocksWG = new ArrayList<>();
        for (CompoundTag block : blocksSettings) {
            CompoundTag data = NbtUtils.getCompound(block, "data", new CompoundTag());
            boolean isfull, istop, isfloat, islaggy;

            islaggy = popBlockData(data, "laggy", false);
            if (islaggy && RandomProvider.rule("safeMode")) continue;

            isfull = popBlockData(data, "full", false);
            isfloat = popBlockData(data, "float", isfull);
            istop = popBlockData(data, "top", isfull);
            istop = istop || isfloat;

            block.put("data", data);
            allBlocks.add(block);
            if (isfull) fullBlocks.add(block);
            if (istop && !islaggy) topBlocks.add(block);
            if (isfloat) blocksFeatures.add(block);
            if (isfull && isfloat && !islaggy) fullBlocksWG.add(block);
        }
        registerCategory(ConfigType.ALL_BLOCKS, allBlocks);
        registerCategory(ConfigType.BLOCKS_FEATURES, blocksFeatures);
        registerCategory(ConfigType.FULL_BLOCKS, fullBlocks);
        registerCategory(ConfigType.FULL_BLOCKS_WG, fullBlocksWG);
        registerCategory(ConfigType.TOP_BLOCKS, topBlocks);
    }
    static boolean popBlockData(CompoundTag e, String key, boolean def) {
        boolean res = NbtUtils.getBoolean(e, key, def);
        e.remove(key);
        return res;
    }

    void extractMobs() {
        List<CompoundTag> allmobs = CommonIO.readCategory(ConfigType.MOBS);
        Map<ConfigType, List<CompoundTag>> byCategory = new HashMap<>();
        List<CompoundTag> cleanMobs = new ArrayList<>();
        for (ConfigType type : ConfigType.mobCategories) byCategory.put(type, new ArrayList<>());
        for (CompoundTag mob : allmobs) {
            String group = NbtUtils.getString(NbtUtils.getCompound(mob, "data"), "Category");
            mob.remove("data");
            ConfigType type = ConfigType.byName(group);
            if (type != null) {
                byCategory.get(type).add(mob);
                cleanMobs.add(mob);
            }
        }
        registerCategory(ConfigType.MOBS, cleanMobs);
        for (ConfigType type : byCategory.keySet())
            registerCategory(type, byCategory.get(type));
    }

    void registerHardcoded() {
        Path root = configPath.resolve("hardcoded");
        registerHardcoded(ConfigType.hardcoded, root);
        registerHardcoded(ConfigType.vegetation, root.resolve("vegetation"));
        registerHardcoded(ConfigType.features, root.resolve("features"));
    }

    void registerHardcoded(ConfigType[] types, Path dir) {
        for (ConfigType type: types) {
            Path file = dir.resolve(type.getKey() + ".json");
            if (file.toFile().exists()) {
                CompoundTag base = CommonIO.read(file);
                List<CompoundTag> list = NbtUtils.getList(base, "elements", Tag.TAG_COMPOUND).stream().map(e -> (CompoundTag)e).toList();
                registry.put(type, new WeighedStructure.Simple(list, type.getDef()));
            }
        }
    }

    public List<String> getMobCategories() {
        return Arrays.stream(ConfigType.mobCategories).map(ConfigType::getKey).toList();
    }

    public CompoundTag blockToProvider(CompoundTag block, Random random) {
        CompoundTag res = new CompoundTag();
        boolean isRotatable = BuiltInRegistries.BLOCK.get(VersionMethods.id(NbtUtils.getString(block, "Name"))).defaultBlockState().getProperties().contains(BlockStateProperties.AXIS);
        res.putString("type", isRotatable && roll(random, "rotate_blocks") ?
                "minecraft:rotated_block_provider" : "minecraft:simple_state_provider");
        res.put("state", block);
        return res;
    }
    public CompoundTag randomBlockProvider(Random random, ConfigType key) {
        return blockToProvider(randomElement(random, key), random);
    }

    public CompoundTag randomPreset(Random random, String key) {
        String preset = randomName(random, ConfigType.COLOR_PRESETS);
        ListTag lst = new ListTag();
        Arrays.stream(ColorLogic.vanillaColors).forEach(color -> lst.add(NbtUtils.nameToElement(preset.replace("$", color))));
        CompoundTag res = new CompoundTag();
        res.putString("type", key);
        if (key.equals("noise_provider")) {
            res.putInt("seed", 0);
            res.putDouble("scale", 4.0);
            res.put("states", lst);
            res.put("noise", CommonIO.read(InfinityMod.utilPath + "/noise.json"));
        }
        if (key.equals("weighted_state_provider")) {
            ListTag entries = new ListTag();
            for (Tag block : lst) {
                CompoundTag entry = new CompoundTag();
                entry.put("data", block);
                entry.putDouble("weight", 1.0);
                entries.add(entry);
            }
            res.put("entries", entries);
        }
        return res;
    }

    public void kickGhostsOut(RegistryAccess s) {
        registry.remove(ConfigType.BIOMES);
        List<CompoundTag> biomes = CommonIO.readCategory(ConfigType.BIOMES);
        Registry<Biome> reg = s.registryOrThrow(Registries.BIOME);
        registerCategory(ConfigType.BIOMES, biomes.stream().filter(comp -> reg.containsKey(VersionMethods.id(NbtUtils.getString(comp,"key")))).toList());
    }
}
