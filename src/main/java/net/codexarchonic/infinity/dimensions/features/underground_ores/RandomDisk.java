package net.codexarchonic.infinity.dimensions.features.underground_ores;

import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

import static net.codexarchonic.infinity.dimensions.features.Placement.matchingFluids;

public class RandomDisk extends RandomisedFeature {
    String target;
    public RandomDisk(RandomFeaturesList parent) {
        super(parent, "disk");
        target = NbtUtils.getString(daddy.underwater.get(parent.parent.fullname), "Name");
        id = "disk";
        savePlacement();
    }

    public ListTag placement() {
        Placement res = new Placement();
        res.addInSquare();
        res.addHeightmap("OCEAN_FLOOR_WG");
        String s = NbtUtils.getString(daddy.default_fluid, "fluidName");
        if (!s.equals("minecraft:air")) res.addBlockPredicateFilter(matchingFluids(s));
        res.addBiome();
        return res.data;
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        CompoundTag blockProvider = PROVIDER.randomBlockProvider(random, ConfigType.FULL_BLOCKS_WG);
        config.putInt("half_height", random.nextInt(5));
        addRandomIntProvider(config, "radius", 0, 9);
        CompoundTag targets = new CompoundTag();
        boolean awt = PROVIDER.roll(random, "ores_spawn_everywhere");
        targets.putString("type", awt ? "true" : "matching_blocks");
        if (!awt) {
            ListTag blocks = new ListTag();
            blocks.add(StringTag.valueOf(target));
            blocks.add(StringTag.valueOf(NbtUtils.getString(NbtUtils.getCompound(blockProvider, "state"), "Name")));
            targets.put("blocks", blocks);
        }
        config.put("target", targets);
        //? if >26 {
        /*config.put("state_provider", blockProvider);
        *///?} else {
        CompoundTag state_provider = new CompoundTag();
        state_provider.put("fallback", blockProvider);
        state_provider.put("rules", new ListTag());
        config.put("state_provider", state_provider);
        //?}
        return feature(config);
    }
}
