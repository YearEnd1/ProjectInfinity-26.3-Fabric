package net.lerariemann.infinity.dimensions.features.local_modifications;

import net.lerariemann.infinity.dimensions.RandomFeaturesList;
import net.lerariemann.infinity.dimensions.features.Placement;
import net.lerariemann.infinity.dimensions.features.RandomisedFeature;
import net.lerariemann.infinity.util.core.ConfigType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class RandomRock extends RandomisedFeature {
    public RandomRock(RandomFeaturesList parent) {
        super(parent, "rock");
        id = "forest_rock";
        savePlacement();
    }

    public ListTag placement() {
        Placement res = new Placement();
        res.addCount(1 + random.nextInt(8));
        res.addInSquare();
        res.addHeightmap("MOTION_BLOCKING");
        res.addBiome();
        return res.data;
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        addRandomBlock(config, "state", ConfigType.FULL_BLOCKS);
        return feature(config);
    }
}
