package net.lerariemann.infinity.registry.var;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.lerariemann.infinity.block.entity.BiomeBottleBlockEntity;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import java.util.Optional;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

public class ModCriteria {
    public static class DimensionOpenedCriterion extends SimpleCriterionTrigger<ScoredConditions> {
        @Override
        public Codec<ScoredConditions> codec() {
            return ScoredConditions.CODEC;
        }

        public void trigger(ServerPlayer player) {
            this.trigger(player, (conditions) -> conditions.test(player.getStats().getValue(ModStats.DIMS_OPENED_STAT)));
        }
    }

    public static class DimensionClosedCriterion extends SimpleCriterionTrigger<ScoredConditions> {
        @Override
        public Codec<ScoredConditions> codec() {
            return ScoredConditions.CODEC;
        }

        public void trigger(ServerPlayer player) {
            this.trigger(player, (conditions) -> conditions.test(player.getStats().getValue(ModStats.WORLDS_DESTROYED_STAT)));
        }
    }

    public static class BiomeBottleCriterion extends SimpleCriterionTrigger<ScoredConditions> {
        @Override
        public Codec<ScoredConditions> codec() {
            return ScoredConditions.CODEC;
        }

        public void trigger(ServerPlayer player, BiomeBottleBlockEntity be) {
            this.trigger(player, (conditions) -> conditions.test(be.charge));
        }
    }

    public static class WhoRemainsCriterion extends SimpleCriterionTrigger<EmptyConditions> {
        public void trigger(ServerPlayer player) {
            this.trigger(player, (conditions) -> true);
        }

        @Override
        public Codec<EmptyConditions> codec() {
            return EmptyConditions.CODEC;
        }
    }

    public static class IridescentCriterion extends SimpleCriterionTrigger<IridescenceConditions> {
        public void trigger(ServerPlayer player, boolean willing, int level) {
            this.trigger(player, (conditions) -> conditions.test(willing, level));
        }

        @Override
        public Codec<IridescenceConditions> codec() {
            return IridescenceConditions.CODEC;
        }
    }

    public static class ConvertMobCriterion extends SimpleCriterionTrigger<DataConditions> {
        @Override
        public Codec<DataConditions> codec() {
            return DataConditions.CODEC;
        }

        public void trigger(ServerPlayer player, LivingEntity e) {
            this.trigger(player, (conditions) -> conditions.test(BuiltInRegistries.ENTITY_TYPE.getKey(e.getType()).toString()));
        }
    }

    public record EmptyConditions(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<EmptyConditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(EmptyConditions::player)
                        )
                        .apply(instance, EmptyConditions::new)
        );
    }

    public record ScoredConditions(Optional<ContextAwarePredicate> player, int score) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<ScoredConditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(ScoredConditions::player),
                                Codec.INT.fieldOf("amount").forGetter(ScoredConditions::score)
                        )
                        .apply(instance, ScoredConditions::new)
        );

        public boolean test(int stat) {
            return stat >= this.score;
        }
    }

    public record DataConditions(Optional<ContextAwarePredicate> player, String data) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<DataConditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(DataConditions::player),
                                Codec.STRING.fieldOf("data").forGetter(DataConditions::data)
                        )
                        .apply(instance, DataConditions::new)
        );

        public boolean test(String data) {
            return data.equals(this.data);
        }
    }

    public record IridescenceConditions(Optional<ContextAwarePredicate> player, Optional<Boolean> willing, Optional<Integer> minLevel) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<IridescenceConditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(IridescenceConditions::player),
                                Codec.BOOL.optionalFieldOf("willing").forGetter(IridescenceConditions::willing),
                                Codec.INT.optionalFieldOf("min_level").forGetter(IridescenceConditions::minLevel)
                        )
                        .apply(instance, IridescenceConditions::new)
        );

        public boolean test(boolean isWilling, int level) {
            return (willing.isEmpty() || isWilling == willing.get()) && (minLevel.isEmpty() || level >= minLevel.get());
        }
    }

    public static RegistrySupplier<DimensionOpenedCriterion> DIMS_OPENED;
    public static RegistrySupplier<DimensionClosedCriterion> DIMS_CLOSED;
    public static RegistrySupplier<WhoRemainsCriterion> WHO_REMAINS;
    public static RegistrySupplier<IridescentCriterion> IRIDESCENT;
    public static RegistrySupplier<BiomeBottleCriterion> BIOME_BOTTLE;
    public static RegistrySupplier<ConvertMobCriterion> CONVERT_MOB;

    public static final DeferredRegister<CriterionTrigger<?>> CRITERIA = DeferredRegister.create(MOD_ID, Registries.TRIGGER_TYPE);

    public static void registerCriteria() {
        DIMS_OPENED = CRITERIA.register("dims_open", DimensionOpenedCriterion::new);
        DIMS_CLOSED = CRITERIA.register("dims_closed", DimensionClosedCriterion::new);
        WHO_REMAINS = CRITERIA.register("who_remains", WhoRemainsCriterion::new);
        IRIDESCENT = CRITERIA.register("iridescence", IridescentCriterion::new);
        BIOME_BOTTLE = CRITERIA.register("bottle", BiomeBottleCriterion::new);
        CONVERT_MOB = CRITERIA.register("convert_mob", ConvertMobCriterion::new);
        CRITERIA.register();
    }
}
