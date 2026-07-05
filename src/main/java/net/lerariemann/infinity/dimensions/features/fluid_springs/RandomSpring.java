package net.lerariemann.infinity.dimensions.features.fluid_springs;

import net.lerariemann.infinity.dimensions.RandomFeaturesList;
import net.lerariemann.infinity.dimensions.features.Placement;
import net.lerariemann.infinity.dimensions.features.RandomisedFeature;
import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.levelgen.feature.SpringFeature;

import static net.lerariemann.infinity.dimensions.features.Placement.heightRange;

public class RandomSpring extends RandomisedFeature {
    public RandomSpring(RandomFeaturesList lst) {
        super(lst, "spring");
        id = "spring_feature";
        savePlacement();
    }

    public CompoundTag feature() {
        CompoundTag config = new CompoundTag();
        boolean whack = random.nextDouble() < 0.1;
        if (whack) {
            config.putInt("hole_count", random.nextInt(7));
            config.putInt("rock_count", random.nextInt(7));
        }
        else {
            boolean open = random.nextBoolean();
            config.putInt("hole_count", open ? 1 : 0);
            config.putInt("rock_count", open ? 4 : 5);
        }
        config.putBoolean("requires_block_below", random.nextBoolean());

        CompoundTag fluid = PROVIDER.randomElement(random, ConfigType.FLUIDS);
        CompoundTag fluidConf = new CompoundTag();
        fluidConf.putString("Name", NbtUtils.getString(fluid, "fluidName", "minecraft:water"));
        CompoundTag properties = new CompoundTag();
        properties.putBoolean("falling", true);
        fluidConf.put("Properties", properties);

        config.put("state", fluidConf);
        config.putString("valid_blocks", NbtUtils.getString(daddy.default_block, "Name"));
        return feature(config);
    }

    public ListTag placement() {
        Placement res = new Placement();
        int center = random.nextInt(daddy.min_y, daddy.min_y + daddy.height);
        int sigma = random.nextInt(daddy.height);
        res.addCount(1 + random.nextInt(32));
        res.addInSquare();
        res.addHeightRange(heightRange(center - sigma,center + sigma, "trapezoid"));
        res.addBiome();
        return res.data;
    }
}
