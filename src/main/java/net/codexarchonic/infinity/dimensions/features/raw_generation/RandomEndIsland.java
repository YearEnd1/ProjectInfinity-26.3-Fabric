package net.codexarchonic.infinity.dimensions.features.raw_generation;

import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import static net.codexarchonic.infinity.dimensions.features.Placement.*;

public class RandomEndIsland extends RandomisedFeature {
    public RandomEndIsland(RandomFeaturesList parent) {
        super(parent, "island");
        id = "infinity:random_end_island";
        savePlacement();
    }

    CompoundTag element(int data, int weight) {
        CompoundTag res = new CompoundTag();
        res.putInt("data", data);
        res.putInt("weight", weight);
        return res;
    }

    CompoundTag count() {
        ListTag distribution = new ListTag();
        int c = random.nextInt(2);
        distribution.add(element(1, 1 + random.nextInt(6)));
        distribution.add(element(2, 1 + random.nextInt(4)));
        if (c!=0) {
            distribution.add(element(3, c));
        }
        return singleRule("weighted_list", "distribution", distribution);
    }

    public ListTag placement() {
        Placement res = new Placement();
        int a = (int)random.nextGaussian(daddy.sea_level, 16);
        int b = random.nextInt(daddy.sea_level, daddy.height + daddy.min_y);
        res.addRarityFilter(1 + random.nextInt(16));
        res.addSingleRule("count", "count", count());
        res.addInSquare();
        res.addHeightRange(uniformHeightRange(Math.min(b, Math.max(a, daddy.min_y)), Math.max(a, b)));
        res.addBiome();
        return res.data;
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        addRandomBlock(config, "state", ConfigType.BLOCKS_FEATURES);
        return feature(config);
    }
}
