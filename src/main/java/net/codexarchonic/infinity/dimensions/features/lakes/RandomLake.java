package net.codexarchonic.infinity.dimensions.features.lakes;

import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import java.util.Arrays;

import static net.codexarchonic.infinity.dimensions.features.Placement.*;

public class RandomLake extends RandomisedFeature {
    public RandomLake(RandomFeaturesList parent) {
        super(parent, "lake");
        savePlacement();
    }

    void env_scan(Placement placement) {
        ListTag predicates = new ListTag();
        predicates.add(not(matchingBlocks("minecraft:air")));
        predicates.add(singleRule("inside_world_bounds", "offset", offsetToNbt(Arrays.asList(0, -5, 0))));
        CompoundTag res = singleRule("environment_scan", "direction_of_search", StringTag.valueOf("down"));
        res.putInt("max_steps", 32);
        res.put("target_condition", singleRule("all_of", "predicates", predicates));
        placement.data.add(res);
    }

    @Override
    public ListTag placement() {
        Placement res = new Placement();
        boolean surface = random.nextBoolean();
        if (surface) {
            res.addRarityFilter(1 + random.nextInt(50));
            res.addInSquare();
            res.addHeightmap("WORLD_SURFACE_WG");
            res.addBiome();
        }
        else {
            res.addRarityFilter(1 + random.nextInt(9));
            res.addInSquare();
            res.addHeightRange(fullHeightRange());
            env_scan(res);
            CompoundTag a = singleRule("surface_relative_threshold_filter", "heightmap", StringTag.valueOf("OCEAN_FLOOR_WG"));
            a.putInt("max_inclusive", -5);
            res.data.add(a);
            res.addBiome();
        }
        return res.data;
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        CompoundTag block = genBlockOrFluid();
        config.put("fluid", PROVIDER.blockToProvider(block, random));
        addRandomBlockProvider(config, "barrier", ConfigType.FULL_BLOCKS_WG);
        return feature(config);
    }
}
