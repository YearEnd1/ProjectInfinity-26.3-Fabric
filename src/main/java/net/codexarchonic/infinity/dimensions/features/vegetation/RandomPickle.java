package net.codexarchonic.infinity.dimensions.features.vegetation;

import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class RandomPickle extends RandomisedFeature {
    public RandomPickle(RandomFeaturesList lst) {
        super(lst.parent.id, lst, "pickle");
        id = "minecraft:sea_pickle";
        savePlacement();
    }
    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        config.putInt("count", 1 + random.nextInt(30));
        return feature(config);
    }
    public ListTag placement() {
        Placement res = new Placement();
        res.addRarityFilter(4 + random.nextInt(20));
        res.addInSquare();
        res.addHeightmap("OCEAN_FLOOR_WG");
        res.addBiome();
        return res.data;
    }
}
