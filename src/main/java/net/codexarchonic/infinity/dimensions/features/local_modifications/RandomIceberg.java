package net.codexarchonic.infinity.dimensions.features.local_modifications;

import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class RandomIceberg extends RandomisedFeature {
    public RandomIceberg(RandomFeaturesList parent) {
        super(parent, "iceberg");
        savePlacement();
    }

    public ListTag placement() {
        Placement res = new Placement();
        res.addRarityFilter(1 + random.nextInt(16));
        res.addInSquare();
        res.addBiome();
        return res.data;
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        addRandomBlock(config, "state", ConfigType.FULL_BLOCKS_WG);
        return feature(config);
    }
}
