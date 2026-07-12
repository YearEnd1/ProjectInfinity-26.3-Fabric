package net.codexarchonic.infinity.dimensions.features.vegetation;

import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.codexarchonic.infinity.util.core.CommonIO;
import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class RandomFloatingPatch extends RandomisedFeature {
    public RandomFloatingPatch(RandomFeaturesList parent) {
        super(parent, "patch_floating");
        id = "random_patch";
        savePlacement();
    }

    public ListTag placement() {
        int a = daddy.min_y + random.nextInt(daddy.height);
        int b = daddy.min_y + random.nextInt(daddy.height);
        return Placement.floating(random.nextInt(1,8), a, b);
    }

    public CompoundTag feature() {
        int xz_spread = 2 + random.nextInt(14);
        int y_spread = 1 + random.nextInt(7);
        int tries_max = (xz_spread+1)*(xz_spread+1);
        CompoundTag config = CommonIO.readAndFormat(InfinityMod.utilPath + "/preplacements/floatingpatch.json",
                CommonIO.compoundToString(PROVIDER.randomElement(random, ConfigType.BLOCKS_FEATURES)),
                Math.min(256, random.nextInt(tries_max)), xz_spread, y_spread);
        return feature(config);
    }
}
