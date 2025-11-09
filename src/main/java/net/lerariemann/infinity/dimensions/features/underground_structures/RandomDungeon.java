package net.lerariemann.infinity.dimensions.features.underground_structures;

import net.lerariemann.infinity.dimensions.RandomFeaturesList;
import net.lerariemann.infinity.dimensions.features.Placement;
import net.lerariemann.infinity.dimensions.features.RandomisedFeature;
import net.lerariemann.infinity.util.core.ConfigType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class RandomDungeon extends RandomisedFeature {
    public RandomDungeon(RandomFeaturesList parent) {
        super(parent, "dungeon");
        id = "infinity:random_dungeon";
        savePlacement();
    }

    public ListTag placement() {
        return Placement.uniform(10 + random.nextInt(200));
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        addRandomBlock(config, "main_state", ConfigType.FULL_BLOCKS);
        addRandomBlock(config, "decor_state", ConfigType.FULL_BLOCKS_WG);
        config.putString("mob", PROVIDER.randomName(random, ConfigType.MOBS));
        config.putInt("size", 1 + random.nextInt(6));
        return feature(config);
    }
}
