package net.codexarchonic.infinity.dimensions.features.underground_decoration;

import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class RandomBlobs extends RandomisedFeature {
    CompoundTag block;

    public RandomBlobs(RandomFeaturesList parent) {
        super(parent, "blob");
        block = parent.PROVIDER.randomElement(random, ConfigType.FULL_BLOCKS_WG);
        daddy.additional_blocks.add(block);
        id = "netherrack_replace_blobs";
        savePlacement();
    }

    public ListTag placement() {
        return Placement.uniform(1 + random.nextInt(32));
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        addRandomIntProvider(config, "radius", 0, 12);
        config.put("state", block);
        config.put("target", daddy.default_block);
        return feature(config);
    }
}
