package net.codexarchonic.infinity.dimensions;

import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.util.core.CommonIO;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
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
        CompoundTag res = new CompoundTag();
        res.putString("type", "minecraft:" + s);
        CompoundTag config = new CompoundTag();
        config.putFloat("probability", (float)Math.min(1.0, random.nextExponential()*(cave ? 0.1 : 0.02)));
        CompoundTag lava_level = new CompoundTag();
        lava_level.putInt("above_bottom", 0);
        config.put("lava_level", lava_level);
        config.put("y", y());
        config.put("replaceable", replaceable());
        if (cave) {
            putScale(config, "yScale", 3.0f);
            putScale(config, "horizontal_radius_multiplier", 2.5f);
            putScale(config, "vertical_radius_multiplier", 2.5f);
            config.put("floor_level", NbtUtils.randomFloatProvider(random, -1.0f, 1.0f));
        }
        else {
            putScale(config, "yScale", 3.0f);
            config.put("vertical_rotation", NbtUtils.randomFloatProvider(random, -1.0f, 1.0f));
            CompoundTag shape = new CompoundTag();
            putScale(shape, "distance_factor", 2.0f);
            putScale(shape, "thickness", 3.0f);
            putScale(shape, "horizontal_radius_factor", 2.0f);
            shape.putFloat("vertical_radius_default_factor", random.nextFloat(0.5f, 2.0f));
            shape.putFloat("vertical_radius_center_factor", random.nextFloat(0.5f, 2.0f));
            shape.putInt("width_smoothness", 1 + random.nextInt(8));
            config.put("shape", shape);
        }
        res.put("config", config);
        CommonIO.write(res, parent.parent.getStoragePath() + "/worldgen/configured_carver", name + ".json");
    }

    void putScale(CompoundTag config, String key, float bound) {
        config.put(key, NbtUtils.randomFloatProvider(random, 1/bound, bound));
    }

    CompoundTag y() {
        int min_y = parent.parent.min_y;
        return NbtUtils.randomHeightProvider(random, min_y, min_y + parent.parent.height, true, false);
    }

    ListTag replaceable() {
        ListTag res = new ListTag();
        res.add(parent.parent.default_block.get("Name"));
        for (CompoundTag a : parent.parent.additional_blocks) res.add(a.get("Name"));
        res.add(parent.parent.default_fluid.get("Name"));
        res.add(StringTag.valueOf(NbtUtils.getString(parent.parent.underwater.get(parent.fullname), "Name")));
        return res;
    }
}
