package net.lerariemann.infinity.dimensions;

import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.util.core.RandomProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomMobsList {
    public RandomBiome parent;
    public Random random;
    public final RandomProvider PROVIDER;
    public Map<String, ListTag> data;

    RandomMobsList(RandomBiome biome) {
        parent = biome;
        random = biome.random;
        PROVIDER = biome.PROVIDER;
        data = new HashMap<>();
        for (String i : PROVIDER.getMobCategories()) {
            data.put(i, new ListTag());
        }
        int mobCount = random.nextInt(20);
        for(int i = 0; i < mobCount; i++) {
            String category = PROVIDER.randomName(random, ConfigType.MOB_CATEGORIES);
            CompoundTag mob = biome.addMob(category, true);
            data.get(category).add(mob);
        }
    }

    CompoundTag asData() {
        CompoundTag res = new CompoundTag();
        for (String i : PROVIDER.getMobCategories()) res.put(i, data.get(i));
        return res;
    }
}
