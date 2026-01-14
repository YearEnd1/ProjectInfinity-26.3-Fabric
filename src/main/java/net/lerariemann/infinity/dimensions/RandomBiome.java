package net.lerariemann.infinity.dimensions;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.*;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.nbt.*;

import java.util.*;

public class RandomBiome {
    public RandomDimension parent;
    public final RandomProvider PROVIDER;
    public long id;
    public String name;
    public String fullname;
    public final Random random;
    public Set<String> mobs;
    public CompoundTag data;
    public CompoundTag colors;

    RandomBiome(long i, RandomDimension dim) {
        id = i;
        parent = dim;
        random = new Random(i);
        PROVIDER = dim.PROVIDER;
        name = "biome_" +i;
        fullname = InfinityMod.MOD_ID + ":" + name;
        mobs = new HashSet<>();
        data = new CompoundTag();
        data.putDouble("temperature", -1 + random.nextFloat()*3);
        data.putBoolean("has_precipitation", PROVIDER.roll(random, "has_precipitation"));
        data.putString("temperature_modifier", roll("temperature_modifier_frozen") ? "frozen" : "none");
        data.putDouble("downfall", random.nextDouble());
        data.put("effects", randomEffects());
        data.put("spawners", (new RandomMobsList(this)).asData());
        data.put("spawn_costs", spawnCosts());
        data.put("features", (new RandomFeaturesList(this)).data);
        data.put("carvers", carvers());
        CommonIO.write(data, dim.getStoragePath() + "/worldgen/biome", name + ".json");
    }

    boolean roll(String key) {
        return PROVIDER.roll(random, key);
    }

    void rollAndPutSafe(CompoundTag res, String key, Tag randomSound) {
        if (roll(key)) {
            if (randomSound != null && !NbtUtils.getAsString(randomSound).isBlank())
                res.put(key, randomSound);
        }
    }

    public IntTag randomColor() {
        return IntTag.valueOf(random.nextInt(16777216));
    }
    public ListTag randomDustColor() {
        ListTag res = new ListTag();
        res.add(DoubleTag.valueOf(random.nextDouble()));
        res.add(DoubleTag.valueOf(random.nextDouble()));
        res.add(DoubleTag.valueOf(random.nextDouble()));
        return res;
    }
    public ListTag randomEntityEffectColor() {
        ListTag res = randomDustColor();
        res.add(DoubleTag.valueOf(random.nextDouble()));
        return res;
    }

    private ListTag zeroBlockPos() {
        ListTag nbtElements = new ListTag();
        nbtElements.add(FloatTag.valueOf(0));
        nbtElements.add(FloatTag.valueOf(0));
        nbtElements.add(FloatTag.valueOf(0));
        return nbtElements;
    }

    private Tag randomBlockState() {
        return InfinityMod.provider.randomBlockProvider(new Random(), ConfigType.ALL_BLOCKS);
    }

    StringTag randomSound(){
        return StringTag.valueOf(PROVIDER.randomName(random, ConfigType.SOUNDS));
    }

    CompoundTag randomEffects() {
        CompoundTag res = new CompoundTag();
        res.put("fog_color", randomColor());
        res.put("sky_color", randomColor());
        res.put("water_color", randomColor());
        res.put("water_fog_color", randomColor());
        if (random.nextBoolean()) res.put("foliage_color", randomColor());
        if (random.nextBoolean()) res.put("grass_color", randomColor());
        colors = res.copy();
        if (random.nextBoolean()) res.put("grass_color_modifier", StringTag.valueOf(random.nextBoolean() ? "dark_forest" : "swamp"));
        if (roll("use_particles")) res.put("particle", randomParticle());
        rollAndPutSafe(res, "ambient_sound", randomSound());
        rollAndPutSafe(res, "mood_sound", randomMoodSound());
        rollAndPutSafe(res, "additions_sound", randomAdditionSound());
        if (roll("music")) res.put("music", randomMusic());
        return res;
    }

    CompoundTag randomMoodSound() {
        CompoundTag res = new CompoundTag();
        var sound = randomSound();
        if (NbtUtils.getAsString(sound).isEmpty()) {
            return null;
        }
        res.put("sound", sound);
        res.putInt("tick_delay", random.nextInt(6000));
        res.putInt("block_search_extent", random.nextInt(32));
        res.putDouble("offset", random.nextDouble()*8);
        return res;
    }

    CompoundTag randomAdditionSound(){
        CompoundTag res = new CompoundTag();
        var sound = randomSound();
        if (NbtUtils.getAsString(sound).isEmpty()) {
            return null;
        }
        res.put("sound", sound);
        res.putDouble("tick_chance", random.nextExponential()*0.01);
        return res;
    }

    Tag randomMusic(){
        CompoundTag data = new CompoundTag();
        data.put("sound", StringTag.valueOf(PROVIDER.randomName(random, ConfigType.MUSIC)));
        int a = random.nextInt(0, 12000);
        int b = random.nextInt(0, 24000);
        data.putInt("min_delay", Math.min(a,b));
        data.putInt("max_delay", Math.max(a,b));
        data.putBoolean("replace_current_music", random.nextBoolean());
        // todo - multiple music entries per biome?
        //? if >1.21.6 {
        ListTag musicList = new ListTag();
        CompoundTag res = new CompoundTag();
        res.putInt("weight", 1);
        res.put("data", data);
        musicList.add(res);
        return musicList;
        //?} else {
        /*return data;
        *///?}
    }

    CompoundTag randomParticle(){
        CompoundTag res = new CompoundTag();
        res.putFloat("probability", (float)(random.nextExponential()*0.01));
        res.put("options", particleOptions());
        return res;
    }

    float randomDustScale() {
        return random.nextBoolean() ? random.nextFloat() * 0.99f + 0.01f : random.nextFloat() * 3f + 1f;
    }

    CompoundTag particleOptions() {
        CompoundTag res = new CompoundTag();
        String particle = PROVIDER.randomName(random, ConfigType.PARTICLES);
        res.putString("type", particle);
        switch(particle) {
            case "minecraft:block", "minecraft:block_marker", "minecraft:dust_pillar", "minecraft:falling_dust" -> {
                CompoundTag value = new CompoundTag();
                value.putString("Name", PROVIDER.randomName(random, ConfigType.ALL_BLOCKS));
                res.put("block_state", value);
                return res;
            }
            case "minecraft:item" -> {
                CompoundTag value = new CompoundTag();
                value.putString("id", PROVIDER.randomName(random, ConfigType.ITEMS));
                res.put("item", value);
                return res;
            }
            case "minecraft:dust" -> {
                res.put("color", randomDustColor());
                res.putFloat("scale", randomDustScale());
                return res;
            }
            case "minecraft:dust_color_transition" -> {
                res.put("from_color", randomDustColor());
                res.put("to_color", randomDustColor());
                res.putFloat("scale", randomDustScale());
                return res;
            }
            case "minecraft:entity_effect" -> {
                res.put("color", randomEntityEffectColor());
                return res;
            }
            case "minecraft:sculk_charge" -> {
                res.putFloat("roll", (float)(random.nextFloat()*Math.PI));
                return res;
            }
            case "minecraft:shriek" -> {
                res.putInt("delay", random.nextInt(500));
                return res;
            }
            case "minecraft:vibration" -> {
                CompoundTag destination = new CompoundTag();
                destination.putString("type", "block");
                ListTag pos = new ListTag();
                for (int ii = 0; ii < 3; ii+=1) pos.add(IntTag.valueOf(random.nextInt(20000)));
                destination.put("pos", pos);
                res.put("destination", destination);
                res.putInt("arrival_in_ticks", random.nextInt(20000));
                return res;
            }
            //? if >1.21.4 {
            case "minecraft:trail" -> {
                res.put("color", randomColor());
                res.put("target", zeroBlockPos());
                res.putInt("duration", random.nextInt(500));
                return res;
            }
            case "minecraft:block_crumble" -> {
                res.put("block_state", randomBlockState());
                return res;
            }
            //?}
        }
        return res;
    }

    CompoundTag addMob(String category, boolean add) {
        CompoundTag mob = new CompoundTag();
        String mobname = PROVIDER.randomName(random, ConfigType.byName(category));
        mob.putString("type", mobname);
        if (add) mobs.add(mobname);
        mob.putInt("weight", 1 + random.nextInt(20));
        int a = 1 + random.nextInt(6);
        int b = 1 + random.nextInt(6);
        mob.putInt("minCount", Math.min(a, b));
        mob.putInt("maxCount", Math.max(a, b));
        return mob;
    }

    CompoundTag spawnCosts() {
        CompoundTag res = new CompoundTag();
        for (String mob: mobs) {
            CompoundTag mobData = new CompoundTag();
            mobData.putDouble("energy_budget", random.nextDouble()*0.6);
            mobData.putDouble("charge", 0.5 + random.nextDouble()*0.4);
            res.put(mob, mobData);
        }
        return res;
    }

    //? if <1.21.5 {
    /*CompoundTag carvers() {
        CompoundTag res = new CompoundTag();
        ListTag air = new ListTag();
        if (PROVIDER.roll(random, "use_random_cave")) air.add(StringTag.valueOf((new RandomCarver(this, true)).fullname));
        if (PROVIDER.roll(random, "use_random_canyon")) air.add(StringTag.valueOf((new RandomCarver(this, false)).fullname));
        PROVIDER.registry.get(ConfigType.CARVERS).getAllNames(random::nextDouble).forEach(s -> air.add(StringTag.valueOf(s)));
        res.put("air", air);
        return res;
    }
    *///?} else {
    ListTag carvers() {
        ListTag air = new ListTag();
        if (PROVIDER.roll(random, "use_random_cave")) air.add(StringTag.valueOf((new RandomCarver(this, true)).fullname));
        if (PROVIDER.roll(random, "use_random_canyon")) air.add(StringTag.valueOf((new RandomCarver(this, false)).fullname));
        PROVIDER.registry.get(ConfigType.CARVERS).getAllNames(random::nextDouble).forEach(s -> air.add(StringTag.valueOf(s)));
        return air;
    }
    //?}

}
