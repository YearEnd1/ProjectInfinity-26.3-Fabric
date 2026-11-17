package net.codexarchonic.infinity.dimensions.features.surface_structures;

import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class RandomDelta extends RandomisedFeature {
    public RandomDelta(RandomFeaturesList parent) {
        super(parent, "delta");
        id = "delta_feature";
        savePlacement();
    }

    public ListTag placement() {
        return Placement.everylayerBiome(1 + random.nextInt(50));
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        CompoundTag block = genBlockOrFluid();
        config.put("contents", block);
        addRandomBlock(config, "rim", ConfigType.FULL_BLOCKS);
        config.put("size", NbtUtils.randomIntProvider(random, 9, true));
        config.put("rim_size", NbtUtils.randomIntProvider(random, 9, true));
        return feature(config);
    }
}
