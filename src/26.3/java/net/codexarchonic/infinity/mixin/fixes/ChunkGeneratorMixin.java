package net.codexarchonic.infinity.mixin.fixes;

import com.google.common.base.Suppliers;
import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.access.FeatureOrderAccess;
import net.codexarchonic.infinity.util.loading.CompatibleFeatureOrder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin implements FeatureOrderAccess {
    @Shadow @Final @Mutable
    private Supplier<List<FeatureSorter.StepFeatureData>> featuresPerStep;
    @Shadow @Final
    protected BiomeSource biomeSource;
    @Shadow @Final
    private Function<Holder<Biome>, BiomeGenerationSettings> generationSettingsGetter;
    @Unique
    private boolean infinity$featureOrderRecovery;

    @Override
    public void infinity$enableFeatureOrderRecovery() {
        if (infinity$featureOrderRecovery) return;
        infinity$featureOrderRecovery = true;
        Supplier<List<FeatureSorter.StepFeatureData>> original = featuresPerStep;
        featuresPerStep = Suppliers.memoize(() -> {
            try {
                return original.get();
            } catch (IllegalStateException error) {
                if (error.getMessage() == null || !error.getMessage().startsWith("Feature order cycle found")) throw error;
                InfinityMod.LOGGER.warn("Resolving conflicting biome feature orders for an Infinity generator: {}", error.getMessage());
                return CompatibleFeatureOrder.recover(List.copyOf(biomeSource.possibleBiomes()),
                        holder -> generationSettingsGetter.apply(holder).features());
            }
        });
    }
}
