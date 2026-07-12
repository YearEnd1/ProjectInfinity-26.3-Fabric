package net.codexarchonic.infinity.dimensions.features.vegetation;

import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class RandomFungus extends RandomisedFeature {
    CompoundTag mainsurfaceblock;

    public RandomFungus(RandomFeaturesList parent) {
        super(parent, "fungus");
        id = "huge_fungus";
        mainsurfaceblock = parent.surface_block;
        savePlacement();
    }

    public ListTag placement() {
        Placement res = new Placement();
        res.addCountEveryLayer(1);
        return res.data;
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        addRandomBlock(config, "hat_state", ConfigType.FULL_BLOCKS_WG);
        addRandomBlock(config, "decor_state", ConfigType.BLOCKS_FEATURES);
        addRandomBlock(config, "stem_state", ConfigType.FULL_BLOCKS_WG);
        CompoundTag replaceableBlocks = new CompoundTag();
        replaceableBlocks.putString("type", "minecraft:matching_block_tag");
        replaceableBlocks.putString("tag", PROVIDER.randomName(random, ConfigType.TAGS));
        config.put("replaceable_blocks", replaceableBlocks);
        config.put("valid_base_block", mainsurfaceblock);
        return feature(config);
    }
}
