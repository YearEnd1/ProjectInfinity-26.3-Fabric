package net.lerariemann.infinity.util.core;

import net.lerariemann.infinity.InfinityMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import java.util.Random;

/** Contains various common use methods for working with {@link CompoundTag} objects. */
public interface NbtUtils {
    static String getString(CompoundTag data, String key, String def) {
        return data.contains(key, Tag.TAG_STRING) ? data.getString(key) : def;
    }
    static CompoundTag getCompound(CompoundTag data, String key, CompoundTag def) {
        return data.contains(key, Tag.TAG_COMPOUND) ? data.getCompound(key) : def;
    }
    static float getFloat(CompoundTag data, String key, float def) {
        return data.contains(key, Tag.TAG_DOUBLE) ? data.getFloat(key) : def;
    }
    static int getInt(CompoundTag data, String key, int def) {
        return data.contains(key, Tag.TAG_INT) ? data.getInt(key) : def;
    }
    static double getDouble(CompoundTag data, String key, double def) {
        return data.contains(key, Tag.TAG_DOUBLE) ? data.getDouble(key) : def;
    }
    static boolean getBoolean(CompoundTag data, String key, boolean def) {
        return data.contains(key) ? data.getBoolean(key) : def;
    }
    static ListTag getList(CompoundTag data, String key, ListTag def, byte nbttype) {
        return data.contains(key) ? data.getList(key, nbttype) : def;
    }

    static String getString(CompoundTag data, String key) {
        return data.getString(key);
    }
    static CompoundTag getCompound(CompoundTag data, String key) {
        return data.getCompound(key);
    }
    static float getFloat(CompoundTag data, String key) {
        return data.getFloat(key);
    }
    static int getInt(CompoundTag data, String key) {
        return data.getInt(key);
    }
    static double getDouble(CompoundTag data, String key) {
        return data.getDouble(key);
    }
    static boolean getBoolean(CompoundTag data, String key) {
        return data.getBoolean(key);
    }
    static short getShort(CompoundTag data, String key) {
        return data.getShort(key);
    }
    static ListTag getList(CompoundTag data, String key, byte nbttype) {
        return data.getList(key, nbttype);
    }
    static int getInt(ListTag data, int key) {
        return data.getInt(key);
    }

    static String elementToName(Tag e) {
        if (e instanceof CompoundTag compound) return NbtUtils.getString(compound, "Name");
        else return e.getAsString();
    }
    static CompoundTag nameToElement(String block) {
        CompoundTag res = new CompoundTag();
        res.putString("Name", block);
        return res;
    }
    static CompoundTag nameToFluid(String block) {
        CompoundTag res = new CompoundTag();
        res.putString("Name", block);
        res.putString("fluidName", block);
        return res;
    }

    static CompoundTag blockToSimpleStateProvider(CompoundTag block) {
        CompoundTag res = new CompoundTag();
        res.putString("type", "minecraft:simple_state_provider");
        res.put("state", block);
        return res;
    }

    //? if <1.21 {
    static CompoundTag genBounds(int lbound, int bound) {
        CompoundTag value = new CompoundTag();
        value.putInt("min_inclusive", lbound);
        value.putInt("max_inclusive", bound);
        return value;
    }

    static CompoundTag genBounds(Random random, int lbound, int bound) {
        int a = random.nextInt(lbound, bound);
        int b = random.nextInt(lbound, bound);
        return genBounds(Math.min(a, b), Math.max(a, b));
    }
    //?}

    static void addBounds(CompoundTag res, int lbound, int bound) {
        //? if >1.21 {
        /*res.putInt("min_inclusive", lbound);
        res.putInt("max_inclusive", bound);
        *///?} else {
        res.put("value", genBounds(lbound, bound));
        //?}
    }

    static void addBounds(CompoundTag res, Random random, int lbound, int bound) {
        int a = random.nextInt(lbound, bound);
        int b = random.nextInt(lbound, bound);
        addBounds(res, Math.min(a, b), Math.max(a, b));
    }

    static Tag randomIntProvider(Random random, int bound, boolean acceptDistributions) {
        return randomIntProvider(random, 0, bound, acceptDistributions);
    }

    static Tag randomIntProvider(Random random, int lbound, int bound, boolean acceptDistributions) {
        int i = random.nextInt(acceptDistributions ? 6 : 4);
        CompoundTag res = new CompoundTag();
        switch(i) {
            case 0 -> {
                res.putString("type", "constant");
                res.putInt("value", random.nextInt(lbound, bound));
                return res;
            }
            case 1, 2 -> {
                res.putString("type", i==1 ? "uniform" : "biased_to_bottom");
                addBounds(res, random, lbound, bound);
                return res;
            }
            case 4 -> {
                res.putString("type", "clamped");
                //? if >1.21 {
                /*addBounds(res, random, lbound, bound);
                res.put("source", randomIntProvider(random, lbound, bound, false));
                *///?} else {
                CompoundTag value = genBounds(random, lbound, bound);
                value.put("source", randomIntProvider(random, lbound, bound, false));
                res.put("value", value);
                //?}
                return res;
            }
            case 3 -> {
                res.putString("type", "clamped_normal");
                //? if >1.21 {
                /*addBounds(res, random, lbound, bound);
                res.putDouble("mean", lbound + random.nextDouble()*(bound-lbound));
                res.putDouble("deviation", random.nextExponential());
                *///?} else {
                CompoundTag value = genBounds(random, lbound, bound);
                value.putDouble("mean", lbound + random.nextDouble()*(bound-lbound));
                value.putDouble("deviation", random.nextExponential());
                res.put("value", value);
                //?}
                return res;
            }
            case 5 -> {
                res.putString("type", "weighted_list");
                int j = 2 + random.nextInt(0, 5);
                ListTag list = new ListTag();
                for (int k=0; k<j; k++) {
                    CompoundTag element = new CompoundTag();
                    element.put("data", randomIntProvider(random, lbound, bound, false));
                    element.putInt("weight", random.nextInt(100));
                    list.add(element);
                }
                res.put("distribution", list);
                return res;
            }
        }
        return res;
    }

    static CompoundTag randomHeightProvider(Random random, int lbound, int bound, boolean acceptDistributions, boolean trim) {
        int i = random.nextInt(acceptDistributions ? 6 : 5);
        String[] types = new String[]{"uniform", "biased_to_bottom", "very_biased_to_bottom", "trapezoid", "constant", "weighted_list"};
        CompoundTag res = new CompoundTag();
        res.putString("type", types[i]);
        switch (i) {
            case 4 -> {
                CompoundTag value = new CompoundTag();
                value.putInt("absolute", random.nextInt(lbound, bound));
                res.put("value", value);
            }
            case 0, 1, 2, 3 -> {
                CompoundTag min_inclusive = new CompoundTag();
                CompoundTag max_inclusive = new CompoundTag();
                int min, max;
                if (!trim && (i == 3)) {
                    int center = random.nextInt(lbound, bound);
                    int sigma = random.nextInt(bound-lbound);
                    min = center - sigma;
                    max = center + sigma;
                }
                else {
                    int k = random.nextInt(lbound, bound);
                    int j = random.nextInt(lbound, bound);
                    min = Math.min(k, j);
                    max = Math.max(k, j);
                }
                min_inclusive.putInt("absolute", min);
                max_inclusive.putInt("absolute", max);
                res.put("min_inclusive", min_inclusive);
                res.put("max_inclusive", max_inclusive);
                int randomBound = max - min;
                if (randomBound <= 1) {
                    InfinityMod.LOGGER.debug("Corrected random bound of: {} to 2!", randomBound);
                    randomBound = 2;
                }
                if (i==3 && random.nextBoolean()) res.putInt("plateau", random.nextInt(1, randomBound));
                else if (i!=0) res.putInt("inner", 1 + (int)Math.floor(random.nextExponential()));
            }
            case 5 -> {
                int j = 2 + random.nextInt(0, 5);
                ListTag list = new ListTag();
                for (int k=0; k<j; k++) {
                    CompoundTag element = new CompoundTag();
                    element.put("data", randomHeightProvider(random, lbound, bound, false, trim));
                    element.putInt("weight", random.nextInt(100));
                    list.add(element);
                }
                res.put("distribution", list);
            }
        }
        return res;
    }

    static CompoundTag randomFloatProvider(Random random, float lbound, float bound) {
        int i = random.nextInt(3);
        String[] types = new String[]{"uniform", "clamped_normal", "trapezoid"};
        CompoundTag res = new CompoundTag();
        res.putString("type", types[i]);
        float a = random.nextFloat(lbound, bound);
        float b = random.nextFloat(lbound, bound);
        float min = Math.min(a, b);
        float max = Math.max(a, b);
        CompoundTag value =
                //? if >1.21 {
                /*res;
                *///?} else {
                new CompoundTag();
                //?}
        switch (i) {
            case 0 -> {
                value.putFloat("max_exclusive", max);
                value.putFloat("min_inclusive", min);
            }
            case 1 -> {
                value.putFloat("max", max);
                value.putFloat("min", min);
                value.putFloat("mean", random.nextFloat(min, max));
                value.putFloat("deviation", random.nextFloat(max - min));
            }
            case 2 -> {
                value.putFloat("max", max);
                value.putFloat("min", min);
                value.putFloat("plateau", random.nextFloat(max - min));
            }
        }
        //? if <1.21 {
        res.put("value", value);
        //?}
        return res;
    }
}
