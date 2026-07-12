package net.codexarchonic.infinity.dimensions.features.vegetation;

import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.codexarchonic.infinity.util.core.CommonIO;
import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class RandomSurfacePatch extends RandomisedFeature {
    public RandomSurfacePatch(RandomFeaturesList parent) {
        super(parent, "patch");
        id = "random_patch";
        savePlacement();
    }

    public ListTag placement() {
        Placement res = new Placement();
        res.addRarityFilter(1 + random.nextInt(64));
        res.addInSquare();
        res.addBiome();
        return res.data;
    }

    public CompoundTag feature() {
        int xz_spread = 2 + random.nextInt(14);
        int y_spread = 1 + random.nextInt(7);
        int tries_max = (xz_spread+1)*(xz_spread+1);
        CompoundTag config = CommonIO.readAndFormat(InfinityMod.utilPath + "/preplacements/surfacepatch.json",
                CommonIO.compoundToString(PROVIDER.randomElement(random, ConfigType.BLOCKS_FEATURES)),
                NbtUtils.getString(parent.surface_block, "Name"), Math.min(256, random.nextInt(tries_max)), xz_spread, y_spread);
        return feature(config);
    }
}
