package net.lerariemann.infinity.dimensions;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.util.Methods;
import net.lerariemann.infinity.util.core.CommonIO;
import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomStructure {
    public RandomBiome parent;
    public int id;
    public String type;
    public String name;
    public String fullname;
    public Random random;
    public CompoundTag data;

    RandomStructure(int i, RandomBiome b) {
        id = i;
        parent = b;
        random = new Random(i);
        addData();
        fullname = InfinityMod.MOD_ID + ":" + name;
    }

    void addData() {
        data = parent.PROVIDER.randomElement(random, ConfigType.STRUCTURES);
        assert data.contains("id");
        type = NbtUtils.getString(data,"id");
        name = Methods.id(type).getPath().replace("/", "_").replace("\\", "_") + "_" + id;

        data.putString("type", "infinity:setupper");
        data.putString("biomes", parent.fullname);
        if (roll("spawn_override")) {
            data.remove("spawn_overrides");
            data.put("spawn_overrides", spawnOverrides());
        }
    }

    void save() {
        CommonIO.write(data, parent.parent.getStoragePath() + "/worldgen/structure", name + ".json");
        (new RandomStructureSet(this)).save();
    }

    boolean roll(String key) {
        return parent.PROVIDER.roll(random, key);
    }

    CompoundTag spawnOverrides() {
        CompoundTag res = new CompoundTag();
        Map<String, ListTag> moblist = new HashMap<>();
        int nummobs = random.nextInt(1, 7);
        for (int i = 0; i < nummobs; i++) {
            String mobcategory = parent.PROVIDER.randomName(random, ConfigType.MOB_CATEGORIES);
            CompoundTag mob = parent.addMob(mobcategory, false);
            if (!moblist.containsKey(mobcategory)) moblist.put(mobcategory, new ListTag());
            moblist.get(mobcategory).add(mob);
        }
        for (String i : moblist.keySet()) {
            CompoundTag category = new CompoundTag();
            category.putString("bounding_box", roll("full_box") ? "full" : "piece");
            category.put("spawns", new ListTag());
            res.put(i, category);
        }
        return res;
    }
}
