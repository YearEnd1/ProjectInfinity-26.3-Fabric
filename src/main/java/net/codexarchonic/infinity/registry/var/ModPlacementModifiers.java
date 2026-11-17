package net.codexarchonic.infinity.registry.var;

import com.mojang.serialization.MapCodec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import static net.codexarchonic.infinity.InfinityMod.MOD_ID;

public class ModPlacementModifiers {
    public static class CenterProximityPlacementModifier implements PlacementFilter {
        public static final MapCodec<CenterProximityPlacementModifier> MODIFIER_CODEC = (ExtraCodecs.POSITIVE_INT.fieldOf("radius")).xmap(
                CenterProximityPlacementModifier::new, a -> a.radius);
        private final int radius;

        private CenterProximityPlacementModifier(int radius) {
            this.radius = radius;
        }

        public static CenterProximityPlacementModifier of(int radius) {
            return new CenterProximityPlacementModifier(radius);
        }

        @Override
        public boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
            long x = pos.getX();
            long y = pos.getY();
            long z = pos.getZ();
            return x * x + y * y + z * z < (long) radius * radius;
        }

        @Override
        public MapCodec<? extends PlacementFilter> codec() {
            return MODIFIER_CODEC;
        }
    }

    public static final DeferredRegister<MapCodec<? extends PlacementModifier>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(MOD_ID, Registries.PLACEMENT_MODIFIER_TYPE);
    public static final RegistrySupplier<MapCodec<? extends PlacementModifier>> PROXIMITY =
            PLACEMENT_MODIFIER_TYPES.register("center_proximity", () -> CenterProximityPlacementModifier.MODIFIER_CODEC);

    public static void registerModifiers() {
        PLACEMENT_MODIFIER_TYPES.register();
    }
}
