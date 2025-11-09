package net.lerariemann.infinity.dimensions.features.surface_structures;

import net.lerariemann.infinity.dimensions.RandomFeaturesList;
import net.lerariemann.infinity.dimensions.features.Placement;
import net.lerariemann.infinity.dimensions.features.RandomisedFeature;
import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.util.core.NbtUtils;
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
        config.put("size", NbtUtils.randomIntProvider(random, 17, true));
        config.put("rim_size", NbtUtils.randomIntProvider(random, 17, true));
        return feature(config);
    }
}
