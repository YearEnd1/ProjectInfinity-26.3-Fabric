package net.codexarchonic.infinity.dimensions;

import net.codexarchonic.infinity.util.core.CommonIO;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

public class DimensionData {
    CompoundTag data;
    String path;
    DimensionData(RandomDimension parent) {
        data = new CompoundTag();
        path = parent.getStoragePath();
        data.put("stats", stats(parent));
        data.put("blocks", blocks(parent));
        data.put("biomes", biomes(parent));
        data.put("structures", structures(parent));
    }

    void save() {
        CommonIO.write(data, path, "data.json");
    }

    CompoundTag stats(RandomDimension parent) {
        CompoundTag res = new CompoundTag();
        res.putString("type", parent.type_alike);
        res.putInt("height", parent.height);
        res.putInt("min_y", parent.min_y);
        res.putInt("sea_level", parent.sea_level);
        return res;
    }

    CompoundTag blocks(RandomDimension parent) {
        CompoundTag res = new CompoundTag();
        res.put("default", parent.default_block);
        res.put("deepslate", parent.deepslate);
        res.put("fluid", parent.default_fluid);
        return res;
    }

    CompoundTag biomes(RandomDimension parent) {
        CompoundTag res = new CompoundTag();
        ListTag vanilla = new ListTag();
        parent.vanilla_biomes.forEach(s -> vanilla.add(StringTag.valueOf(s)));
        res.put("vanilla", vanilla);
        ListTag random = new ListTag();
        parent.random_biomes.forEach(biome -> {
            CompoundTag b = new CompoundTag();
            String name = biome.fullname;
            b.putString("name", name);
            b.put("grass", parent.top_blocks.get(name));
            b.put("dirt", parent.underwater.get(name));
            b.put("colors", biome.colors);
            ListTag mobs = new ListTag();
            biome.mobs.forEach(mob -> mobs.add(StringTag.valueOf(mob)));
            b.put("mobs", mobs);
            random.add(b);
        });
        res.put("random", random);
        return res;
    }

    CompoundTag structures(RandomDimension parent) {
        CompoundTag res = new CompoundTag();
        parent.structure_ids.forEach((type, list) -> {
            ListTag ids = new ListTag();
            list.forEach(id -> ids.add(StringTag.valueOf(id)));
            res.put(type, ids);
        });
        return res;
    }
}
