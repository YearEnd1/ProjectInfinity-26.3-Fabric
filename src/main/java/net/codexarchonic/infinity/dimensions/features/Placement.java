package net.codexarchonic.infinity.dimensions.features;

import net.minecraft.nbt.*;

import java.util.List;

public class Placement {
    public ListTag data;

    public Placement() {
        data = new ListTag();
    }

    public void addSingleRule(String name, String name_param, Tag b) {
        data.add(singleRule(name, name_param, b));
    }

    public void addEmptyRule(String name) {
        data.add(ofType(name));
    }

    public void addBlockPredicateFilter(CompoundTag predicate) {
        addSingleRule("block_predicate_filter", "predicate", predicate);
    }

    public void addCount(int b) {
        addSingleRule("count", "count", IntTag.valueOf(b));
    }

    public void addCountEveryLayer(int b) {
        addSingleRule("count_on_every_layer", "count", IntTag.valueOf(b));
    }

    public void addRarityFilter(int b) {
        addSingleRule("rarity_filter", "chance", IntTag.valueOf(b));
    }

    public void addWaterDepthFilter(int b) {
        addSingleRule("surface_water_depth_filter", "max_water_depth", IntTag.valueOf(b));
    }

    public void addInSquare() {
        addEmptyRule("in_square");
    }

    public void addBiome() {
        addEmptyRule("biome");
    }

    public void addHeightmap(String s) {
        addSingleRule("heightmap", "heightmap", StringTag.valueOf(s));
    }

    public void addHeightRange(CompoundTag heightProvider) {
        addSingleRule("height_range", "height", heightProvider);
    }

    public static ListTag everylayerBiome(int count) {
        Placement res = new Placement();
        res.addCountEveryLayer(count);
        res.addBiome();
        return res.data;
    }

    public static ListTag uniform(int count) {
        Placement res = new Placement();
        res.addCount(count);
        res.addInSquare();
        res.addHeightRange(fullHeightRange());
        return res.data;
    }

    public static ListTag floating(int chance, int a, int b) {
        Placement res = new Placement();
        res.addRarityFilter(chance);
        if (a==b) a+=1;
        res.addHeightRange(uniformHeightRange(Math.min(a, b), Math.max(a, b)));
        res.addBlockPredicateFilter(not(ofType("solid")));
        res.addBiome();
        return res.data;
    }

    public static CompoundTag singleRule(String type, String name_param, Tag b) {
        CompoundTag res = ofType(type);
        res.put(name_param, b);
        return res;
    }

    public static CompoundTag ofType(String type) {
        CompoundTag res = new CompoundTag();
        res.putString("type", "minecraft:"+type);
        return res;
    }

    public static CompoundTag fullHeightRange() {
        CompoundTag res = ofType("uniform");
        CompoundTag max_inclusive = new CompoundTag();
        CompoundTag min_inclusive = new CompoundTag();
        max_inclusive.putInt("below_top", 0);
        min_inclusive.putInt("above_bottom", 0);
        res.put("max_inclusive", max_inclusive);
        res.put("min_inclusive", min_inclusive);
        return res;
    }

    public static CompoundTag uniformHeightRange(int min, int max) {
        return heightRange(min, max, "uniform");
    }

    public static CompoundTag heightRange(int min, int max, String type) {
        CompoundTag res = ofType(type);
        CompoundTag max_inclusive = new CompoundTag();
        CompoundTag min_inclusive = new CompoundTag();
        max_inclusive.putInt("absolute", max);
        min_inclusive.putInt("absolute", min);
        res.put("max_inclusive", max_inclusive);
        res.put("min_inclusive", min_inclusive);
        return res;
    }

    public static CompoundTag matchingBlocks(String block) {
        return singleRule("matching_blocks", "blocks", StringTag.valueOf(block));
    }

    public static CompoundTag matchingFluids(String s) {
        return singleRule("matching_fluids", "fluids", StringTag.valueOf(s));
    }
    public static CompoundTag not(CompoundTag predicate) {
        return singleRule("not", "predicate", predicate);
    }

    public static ListTag offsetToNbt(List<Integer> offset) {
        ListTag offsetnbt = new ListTag();
        offset.forEach(a -> offsetnbt.add(IntTag.valueOf(a)));
        return offsetnbt;
    }
}
