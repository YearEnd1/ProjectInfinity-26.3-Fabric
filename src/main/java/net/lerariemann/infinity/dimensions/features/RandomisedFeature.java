package net.lerariemann.infinity.dimensions.features;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.dimensions.RandomDimension;
import net.lerariemann.infinity.util.core.CommonIO;
import net.lerariemann.infinity.dimensions.RandomFeaturesList;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.lerariemann.infinity.util.core.RandomProvider;
import net.minecraft.nbt.*;

import java.util.Random;

public abstract class RandomisedFeature {
    protected final RandomProvider PROVIDER;
    protected String id;
    protected String name;
    protected Random random;
    protected RandomFeaturesList parent;
    protected RandomDimension daddy;

    public RandomisedFeature(RandomFeaturesList lst, String namecore) {
        this(InfinityMethods.getRandomSeed(lst.random), lst, namecore);
    }

    public RandomisedFeature(long i, RandomFeaturesList lst, String namecore) {
        random = new Random(i);
        id = namecore;
        name = namecore + "_" + i;
        parent = lst;
        daddy = parent.parent.parent;
        PROVIDER = parent.PROVIDER;
    }

    public String fullName() {
        return InfinityMod.MOD_ID + ":" + name;
    }

    protected void savePlacement() {
        CompoundTag moredata = new CompoundTag();
        moredata.put("feature", feature());
        moredata.put("placement", placement());
        CommonIO.write(moredata, parent.storagePath + "/worldgen/placed_feature", name + ".json");
    }

    protected void savePlacement(String feature) {
        CompoundTag moredata = new CompoundTag();
        moredata.putString("feature", feature);
        moredata.put("placement", placement());
        CommonIO.write(moredata, parent.storagePath + "/worldgen/placed_feature", name + ".json");
    }

    public CompoundTag genBlockOrFluid() {
        CompoundTag block2;
        if (parent.roll("solid_lakes")) {
            block2 = PROVIDER.randomElement(random, ConfigType.BLOCKS_FEATURES);
        }
        else {
            block2 = NbtUtils.nameToElement(
                    PROVIDER.randomName(random, ConfigType.FLUIDS));
        }
        return block2;
    }

    public void addRandomBlockProvider(CompoundTag config, String key, ConfigType group) {
        config.put(key, PROVIDER.randomBlockProvider(random, group));
    }
    public void addRandomBlock(CompoundTag config, String key, ConfigType group) {
        CompoundTag block = PROVIDER.randomElement(random, group);
        config.put(key, block);
    }

    public void addRandomIntProvider(CompoundTag config, String key, int lbound, int bound) {
        config.put(key, NbtUtils.randomIntProvider(random, lbound, bound, true));
    }

    public abstract CompoundTag feature();

    public abstract ListTag placement();

    public CompoundTag feature(CompoundTag config) {
        CompoundTag res = new CompoundTag();
        res.putString("type", id);
        res.put("config22", config);
        return res;
    }
}