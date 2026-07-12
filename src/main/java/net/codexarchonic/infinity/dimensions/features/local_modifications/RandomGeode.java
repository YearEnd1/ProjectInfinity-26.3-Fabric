package net.codexarchonic.infinity.dimensions.features.local_modifications;

import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import static java.lang.Math.max;
import static net.codexarchonic.infinity.dimensions.features.Placement.uniformHeightRange;

public class RandomGeode extends RandomisedFeature {
    public RandomGeode(RandomFeaturesList parent) {
        super(parent, "geode");
        savePlacement();
    }

    public ListTag placement() {
        Placement res = new Placement();
        int sea = max(daddy.sea_level, daddy.min_y + daddy.height / 8);
        int halfsea = (sea + daddy.min_y) / 2;
        int minbound = random.nextInt(daddy.min_y, halfsea);
        int maxbound = random.nextInt(halfsea, sea);
        res.addRarityFilter(1 + random.nextInt(32));
        res.addInSquare();
        res.addHeightRange(uniformHeightRange(minbound, maxbound));
        res.addBiome();
        return res.data;
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        CompoundTag blocks = new CompoundTag();
        blocks.put("filling_provider", parent.roll("flood_geodes") ? PROVIDER.randomBlockProvider(random, ConfigType.FLUIDS) :
                NbtUtils.blockToSimpleStateProvider(NbtUtils.nameToElement("minecraft:air")));
        blocks.put("inner_layer_provider", PROVIDER.randomBlockProvider(random, ConfigType.FULL_BLOCKS_WG));
        blocks.put("alternate_inner_layer_provider", PROVIDER.randomBlockProvider(random, ConfigType.FULL_BLOCKS_WG));
        blocks.put("middle_layer_provider", PROVIDER.randomBlockProvider(random, ConfigType.FULL_BLOCKS_WG));
        blocks.put("outer_layer_provider", PROVIDER.randomBlockProvider(random, ConfigType.FULL_BLOCKS_WG));
        ListTag inner_placements = new ListTag();
        inner_placements.add(PROVIDER.randomElement(random, ConfigType.ALL_BLOCKS));
        blocks.put("inner_placements", inner_placements);
        blocks.putString("cannot_replace", "#" + PROVIDER.randomName(random, ConfigType.TAGS));
        blocks.putString("invalid_blocks", "#" + PROVIDER.randomName(random, ConfigType.TAGS));
        config.put("blocks", blocks);
        CompoundTag layers = new CompoundTag();
        double r = 1.0;
        for (String str: new String[]{"filling", "inner_layer", "middle_layer", "outer_layer"}) {
            r += random.nextExponential();
            layers.putDouble(str, r);
        }
        config.put("layers", layers);
        CompoundTag crack = new CompoundTag();
        crack.putDouble("generate_crack_chance", random.nextDouble());
        crack.putDouble("base_crack_size", random.nextDouble()*5);
        crack.putInt("crack_point_offset", random.nextInt(11));
        config.put("crack", crack);
        config.putDouble("noise_multiplier", Math.min(1.0, random.nextExponential()*0.1));
        config.putDouble("use_potential_placements_chance", random.nextDouble());
        config.putDouble("use_alternate_layer0_chance:", random.nextDouble());
        config.putBoolean("placements_require_layer0_alternate", random.nextBoolean());
        config.putInt("invalid_blocks_threshold", 1 + random.nextInt(16));
        return feature(config);
    }
}
