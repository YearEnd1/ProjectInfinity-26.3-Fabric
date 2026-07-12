package net.codexarchonic.infinity.dimensions.features.vegetation;

import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class RandomMushroom extends RandomisedFeature {
    public RandomMushroom(RandomFeaturesList parent) {
        super(parent, "mushroom");
        id = random.nextBoolean() ? "infinity:random_flat_mushroom" : "infinity:random_round_mushroom";
        savePlacement();
    }

    public ListTag placement() {
        Placement res = new Placement();
        res.addCountEveryLayer(1);
        return res.data;
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        addRandomBlockProvider(config, "cap_provider", ConfigType.FULL_BLOCKS_WG);
        addRandomBlockProvider(config, "stem_provider", ConfigType.FULL_BLOCKS_WG);
        config.put("valid_base_block", parent.surface_block);
        config.putInt("foliage_radius", random.nextBoolean() ? 2 : 1 + (int)Math.floor(random.nextExponential()*2));
        config.putInt("height", random.nextBoolean() ? 5 : random.nextInt(3, 7));
        return feature(config);
    }
}
