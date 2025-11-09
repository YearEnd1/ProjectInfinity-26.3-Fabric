package net.lerariemann.infinity.dimensions.features.underground_structures;

import net.lerariemann.infinity.dimensions.RandomFeaturesList;
import net.lerariemann.infinity.dimensions.features.Placement;
import net.lerariemann.infinity.dimensions.features.RandomisedFeature;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class RandomFossil extends RandomisedFeature {
    public RandomFossil(RandomFeaturesList lst) {
        super(lst, "well");
        savePlacement("minecraft:fossil_coal");
    }

    public CompoundTag feature() {
        return null;
    }
    public ListTag placement() {
        Placement res = new Placement();
        res.addRarityFilter(random.nextInt(1, 128));
        res.addInSquare();
        res.addHeightRange(NbtUtils.randomHeightProvider(random, daddy.min_y,
                daddy.min_y + daddy.height, false, true));
        res.addBiome();
        return res.data;
    }
}
