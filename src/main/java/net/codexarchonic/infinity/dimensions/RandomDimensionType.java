package net.codexarchonic.infinity.dimensions;

import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.util.core.CommonIO;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.codexarchonic.infinity.util.core.RandomProvider;
import net.minecraft.nbt.*;

import java.util.Random;

public class RandomDimensionType {
    public String name;
    public String fullname;
    private final Random random;
    public RandomDimension parent;
    public CompoundTag data;
    public boolean ultrawarm;
    public static int scaleCap = 100000;

    RandomDimensionType(RandomDimension dim) {
        parent = dim;
        random = dim.random;
        name = "generated_" +dim.numericId;
        fullname = InfinityMod.MOD_ID + ":" + name;
        data = new CompoundTag();
        add_roll("ultrawarm");
        add_roll("natural");
        add_roll("piglin_safe");
        add_roll("bed_works");
        add_roll("respawn_anchor_works");
        add_roll("has_raids");
        data.putBoolean("has_ceiling", dim.hasCeiling());
        data.putDouble("coordinate_scale", coordinateScale());
        data.putFloat("ambient_light", random.nextFloat());
        if (roll("fixed_time")) {
            data.putInt("fixed_time", random.nextInt(24000));
        }
        data.putInt("min_y", parent.min_y);
        data.putInt("height", parent.height);
        data.putInt("logical_height", parent.height);
        data.putInt("monster_spawn_block_light_limit", random.nextInt(16));
        CompoundTag lightLevel = new CompoundTag();
        lightLevel.putString("type", "uniform");
        NbtUtils.addBounds(lightLevel, 0, random.nextInt(16));
        data.put("monster_spawn_light_level", lightLevel);
        data.putString("infiniburn", "#" + dim.PROVIDER.randomName(random, ConfigType.TAGS));
        String s = dim.PROVIDER.randomName(random, ConfigType.DIMENSION_EFFECTS);
        data.putString("effects", s);
        data.putBoolean("has_skylight", s.equals("minecraft:overworld"));
        CommonIO.write(data, dim.getStoragePath() + "/dimension_type", name + ".json");
    }

    boolean roll(String key) {
        return parent.PROVIDER.roll(random, key);
    }

    void add_roll(String key) {
        data.putBoolean(key, roll(key));
    }

    double coordinateScale() {
        int scaleMax = RandomProvider.ruleInt("maxDimensionScale");
        if (scaleMax <= 0 || scaleMax > scaleCap) scaleMax = scaleCap;
        int distribParam = Math.min(scaleMax, 8);
        double scaleMin = 1.0 / scaleMax;
        double random1 = random.nextBoolean() ? 1.0 : scaleMin + (Math.sqrt(distribParam) - scaleMin)*random.nextDouble();
        double random2 = distribParam / random1;
        return random.nextBoolean() ? random1 : random2;
    }
}
