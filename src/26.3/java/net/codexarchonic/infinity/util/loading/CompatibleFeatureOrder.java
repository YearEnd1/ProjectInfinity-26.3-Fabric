package net.codexarchonic.infinity.util.loading;

import net.codexarchonic.infinity.access.FeatureOrderAccess;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class CompatibleFeatureOrder {
    public static void enable(ResourceKey<Level> dimension, LevelStem stem) {
        if (dimension.identifier().getNamespace().equals("infinity")) {
            ((FeatureOrderAccess) stem.generator()).infinity$enableFeatureOrderRecovery();
        }
    }

    public static List<FeatureSorter.StepFeatureData> recover(
            List<Holder<Biome>> biomes,
            Function<Holder<Biome>, List<HolderSet<PlacedFeature>>> features) {
        List<Holder<Biome>> sources = new ArrayList<>(biomes);
        sources.sort(Comparator.comparing(holder -> holder.unwrapKey()
                .map(key -> key.identifier().toString()).orElse("")));
        Map<PlacedFeature, String> names = new IdentityHashMap<>();
        for (Holder<Biome> source : sources) {
            for (HolderSet<PlacedFeature> step : features.apply(source)) {
                for (Holder<PlacedFeature> holder : step) {
                    String name = holder.unwrapKey().map(key -> key.identifier().toString())
                            .orElseGet(() -> String.format("~%09d", names.size()));
                    names.merge(holder.value(), name, (first, second) -> first.compareTo(second) <= 0 ? first : second);
                }
            }
        }
        Comparator<Holder<PlacedFeature>> order = Comparator.comparing(holder -> names.get(holder.value()));
        return FeatureSorter.buildFeaturesPerStep(sources, source -> {
            List<HolderSet<PlacedFeature>> result = new ArrayList<>();
            for (HolderSet<PlacedFeature> step : features.apply(source)) {
                Set<PlacedFeature> seen = Collections.newSetFromMap(new IdentityHashMap<>());
                List<Holder<PlacedFeature>> sorted = new ArrayList<>();
                for (Holder<PlacedFeature> holder : step) {
                    if (seen.add(holder.value())) sorted.add(holder);
                }
                sorted.sort(order);
                result.add(HolderSet.direct(sorted));
            }
            return result;
        }, true);
    }
}
