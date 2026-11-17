package net.codexarchonic.infinity.dimensions;

import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.util.core.CommonIO;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import java.util.Random;

public class RandomCarver {
    public String name;
    public String fullname;
    private final Random random;
    public RandomBiome parent;

    RandomCarver(RandomBiome b, boolean cave) {
        parent = b;
        String s = cave ? "cave" : "canyon";
        name = s + "_" + b.id;
        fullname = InfinityMod.MOD_ID + ":" + name;
        random = b.random;
        CompoundTag config = new CompoundTag();
        config.putString("type", "minecraft:" + s);
        config.putFloat("probability", (float)Math.min(1.0, random.nextExponential()*(cave ? 0.1 : 0.02)));
        config.put("y", y());
        if (cave) {
            CompoundTag count = new CompoundTag();
            count.putString("type", "minecraft:very_biased_to_bottom");
            count.putInt("min_inclusive", 0);
            count.putInt("max_inclusive", 14);
            config.put("count", count);
            putScale(config, "room_vertical_radius_multiplier", 3.0f);
            putScale(config, "thickness", 3.0f);
            config.putBoolean("weird_thickness_bias", true);
            putScale(config, "horizontal_radius_multiplier", 2.5f);
            putScale(config, "vertical_radius_multiplier", 2.5f);
            config.put("floor_level", NbtUtils.randomFloatProvider(random, -1.0f, 1.0f));
        }
        else {
            config.put("vertical_rotation", NbtUtils.randomFloatProvider(random, -1.0f, 1.0f));
            CompoundTag shape = new CompoundTag();
            putScale(shape, "y_scale", 3.0f);
            putScale(shape, "distance_factor", 2.0f);
            putScale(shape, "thickness", 3.0f);
            putScale(shape, "horizontal_radius_factor", 2.0f);
            shape.putFloat("vertical_radius_default_factor", random.nextFloat(0.5f, 2.0f));
            shape.putFloat("vertical_radius_center_factor", random.nextFloat(0.5f, 2.0f));
            shape.putInt("width_smoothness", 1 + random.nextInt(8));
            config.put("shape", shape);
        }
        CommonIO.write(config, parent.parent.getStoragePath() + "/worldgen/carver", name + ".json");
    }

    void putScale(CompoundTag config, String key, float bound) {
        config.put(key, NbtUtils.randomFloatProvider(random, 1/bound, bound));
    }

    CompoundTag y() {
        int min_y = parent.parent.min_y;
        return NbtUtils.randomHeightProvider(random, min_y, min_y + parent.parent.height, true, false);
    }
}
