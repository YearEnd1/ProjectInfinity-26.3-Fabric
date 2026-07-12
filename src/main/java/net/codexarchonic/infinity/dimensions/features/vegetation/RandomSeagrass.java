package net.codexarchonic.infinity.dimensions.features.vegetation;

import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class RandomSeagrass extends RandomisedFeature {
    public RandomSeagrass(RandomFeaturesList lst) {
        super(lst.parent.id, lst, "seagrass");
        id = "minecraft:seagrass";
        savePlacement();
    }
    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        config.putDouble("probability", random.nextDouble());
        return feature(config);
    }
    public ListTag placement() {
        Placement res = new Placement();
        res.addInSquare();
        res.addHeightmap("OCEAN_FLOOR_WG");
        res.addCount(random.nextInt(10, 100));
        res.addBiome();
        return res.data;
    }
}
