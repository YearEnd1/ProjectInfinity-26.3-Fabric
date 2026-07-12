package net.codexarchonic.infinity.dimensions.features.vegetation;

import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import static net.codexarchonic.infinity.dimensions.features.Placement.matchingBlocks;
import static net.codexarchonic.infinity.dimensions.features.Placement.singleRule;

public class RandomFlowerPatch extends RandomisedFeature {
    public RandomFlowerPatch(RandomFeaturesList parent) {
        super(parent, "flowers");
        id = "flower";
        savePlacement();
    }

    public ListTag placement() {
        Placement res = new Placement();
        int a = random.nextInt(1, 9);
        if (a>1) res.addCount(a);
        res.addRarityFilter(1 + random.nextInt(32));
        res.addInSquare();
        res.addHeightmap("MOTION_BLOCKING");
        res.addBiome();
        return res.data;
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        config.putInt("tries", 1 + random.nextInt( 100));
        config.putInt("xz_spread", random.nextInt(2, 13));
        config.putInt("y_spread", random.nextInt(1, 6));

        ListTag flowerlist = new ListTag();
        int i = 1;
        while (i > 0) {
            CompoundTag entry = new CompoundTag();
            addRandomBlock(entry, "data", ConfigType.FLOWERS);
            entry.putInt("weight", 1 + random.nextInt(20));
            flowerlist.add(entry);
            i = random.nextDouble() > 0.5 ? 1 : 0;
        }
        ListTag placement_inner = new ListTag();
        placement_inner.add(singleRule("block_predicate_filter", "predicate", matchingBlocks("minecraft:air")));
        CompoundTag config_inner = new CompoundTag();
        config_inner.put("to_place", singleRule("weighted_state_provider", "entries", flowerlist));
        CompoundTag feature_inner = singleRule("simple_block", "config", config_inner);
        CompoundTag feature_outer = new CompoundTag();
        feature_outer.put("feature", feature_inner);
        feature_outer.put("placement", placement_inner);
        config.put("feature", feature_outer);
        return feature(config);
    }
}
