package net.lerariemann.infinity.dimensions.features.underground_ores;

import net.lerariemann.infinity.dimensions.RandomFeaturesList;
import net.lerariemann.infinity.dimensions.features.Placement;
import net.lerariemann.infinity.dimensions.features.RandomisedFeature;
import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import static net.lerariemann.infinity.dimensions.features.Placement.heightRange;

public class RandomOre extends RandomisedFeature {

    public RandomOre(RandomFeaturesList parent) {
        super(parent, "ore");
        id = (parent.PROVIDER.roll(random, "scatter_ores")) ? "scattered_ore" : "ore";
        savePlacement();
    }

    public ListTag placement() {
        Placement res = new Placement();
        int center = random.nextInt(daddy.min_y, daddy.min_y + daddy.height);
        int sigma = random.nextInt(daddy.height);
        res.addCount(1 + random.nextInt(128));
        res.addInSquare();
        res.addHeightRange(heightRange(center - sigma,center + sigma, "trapezoid"));
        res.addBiome();
        return res.data;
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        config.putInt("size", 1 + Math.min(63, (int)Math.floor(random.nextExponential()*4)));
        config.putFloat("discard_chance_on_air_exposure", Math.max(0.0f, Math.min(1.0f, (float)(random.nextExponential()*0.2))));
        CompoundTag block = PROVIDER.randomElement(random, ConfigType.FULL_BLOCKS_WG);
        ListTag targets = new ListTag();
        int j = daddy.additional_blocks.size();
        for (int i = 0; i < j + 1; i++) {
            CompoundTag block2 = (i < j) ? daddy.additional_blocks.get(i) : daddy.default_block;
            CompoundTag target = new CompoundTag();
            target.put("state", block);
            target.put("target", target(block2, PROVIDER.roll(random, "ores_spawn_everywhere")));
            targets.add(target);
        }
        config.put("targets", targets);
        return feature(config);
    }

    static CompoundTag target(CompoundTag block, boolean awt) {
        CompoundTag res = new CompoundTag();
        res.putString("predicate_type", awt ? "always_true" : "block_match");
        if (!awt) res.putString("block", NbtUtils.getString(block, "Name"));
        return res;
    }
}
