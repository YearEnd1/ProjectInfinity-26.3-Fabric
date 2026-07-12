package net.codexarchonic.infinity.dimensions.features.surface_structures;

import net.codexarchonic.infinity.dimensions.RandomFeaturesList;
import net.codexarchonic.infinity.dimensions.features.Placement;
import net.codexarchonic.infinity.dimensions.features.RandomisedFeature;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;

public class RandomEndSpikes extends RandomisedFeature {
    public RandomEndSpikes(RandomFeaturesList parent) {
        super(parent, "spikes");
        id = "end_spike";
        savePlacement();
    }

    public ListTag placement() {
        Placement res = new Placement();
        res.addBiome();
        return res.data;
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        if (random.nextBoolean()) config.putBoolean("crystal_invulnerable", true);
        if (random.nextBoolean()) {
            ListTag crystal_beam_target = new ListTag();
            crystal_beam_target.add(IntTag.valueOf(random.nextInt(-128, 128)));
            crystal_beam_target.add(IntTag.valueOf(daddy.min_y + random.nextInt(daddy.height)));
            crystal_beam_target.add(IntTag.valueOf(random.nextInt(-128, 128)));
            config.put("crystal_beam_target", crystal_beam_target);
        }
        ListTag spikes = new ListTag();
        if (PROVIDER.roll(random, "scatter_end_spikes")) {
            int i = random.nextInt(6, 24);
            for (int j = 0; j < i; j++) {
                CompoundTag spike = new CompoundTag();
                spike.putInt("centerX", random.nextInt(-128, 128));
                spike.putInt("centerZ", random.nextInt(-128, 128));
                spike.putInt("radius", random.nextInt(1, 8));
                int max = daddy.min_y + daddy.height;
                int min = Math.max(max - daddy.height / 2, daddy.sea_level);
                int mean = random.nextInt(min, max);
                int height = (int)Math.floor(random.nextGaussian(mean, 16));
                height = Math.max(height, min);
                height = Math.min(height, max);
                spike.putInt("height", height);
                if (PROVIDER.roll(random, "cage_crystals")) spike.putBoolean("guarded", true);
                spikes.add(spike);
            }
        }
        config.put("spikes", spikes);
        return feature(config);
    }
}
