package net.lerariemann.infinity.registry.var;

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
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

public class ModPlacementModifiers {
    public static class CenterProximityPlacementModifier extends PlacementFilter {
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
        protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
            return pos.getX()*pos.getX() + pos.getY()*pos.getY() + pos.getZ()*pos.getZ() < this.radius*this.radius;
        }

        @Override
        public @NotNull PlacementModifierType<?> type() {
            return PlacementModifierType.RARITY_FILTER;
        }
    }

    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(MOD_ID, Registries.PLACEMENT_MODIFIER_TYPE);
    public static final RegistrySupplier<PlacementModifierType<?>> PROXIMITY = register("center_proximity",
            CenterProximityPlacementModifier.MODIFIER_CODEC);

    static <P extends PlacementModifier> PlacementModifierType<P> getType(MapCodec<P> codec) {
        //? if >1.21 {
        return () -> codec;
         //?} else {
        /*return codec::codec;
        *///?}
    }
    static RegistrySupplier<PlacementModifierType<?>> register(String id, MapCodec<? extends PlacementModifier> codec) {
            return PLACEMENT_MODIFIER_TYPES.register(id, () -> getType(codec));
    }
    public static void registerModifiers() {
        PLACEMENT_MODIFIER_TYPES.register();
    }
}
