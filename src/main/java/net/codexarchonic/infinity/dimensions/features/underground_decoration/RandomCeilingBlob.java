package net.codexarchonic.infinity.dimensions.features.underground_decoration;

import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import static net.codexarchonic.infinity.dimensions.features.Placement.fullHeightRange;

public class RandomCeilingBlob extends RandomisedFeature {
    public RandomCeilingBlob(RandomFeaturesList parent) {
        super(parent, "ceilingblob");
        id = "infinity:random_ceiling_blob";
        savePlacement();
    }

    public ListTag placement() {
        Placement res = new Placement();
        res.addCount(random.nextInt(1, daddy.height/8));
        res.addInSquare();
        res.addHeightRange(fullHeightRange());
        res.addBiome();
        return res.data;
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        ListTag targets = new ListTag();
        targets.add(daddy.default_block);
        targets.addAll(daddy.additional_blocks);
        config.put("targets", targets);
        addRandomBlockProvider(config, "block", ConfigType.FULL_BLOCKS);
        config.putInt("size_xz", random.nextInt(5, 12));
        config.putInt("size_y", random.nextInt(8, 16));
        return feature(config);
    }
}
