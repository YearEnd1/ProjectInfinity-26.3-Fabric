package net.codexarchonic.infinity.registry.var;

import net.codexarchonic.infinity.block.entity.BiomeBottleBlockEntity;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.registry.registries.DeferredRegister;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;
import java.util.function.Supplier;

import static net.codexarchonic.infinity.InfinityMod.MOD_ID;

@NullMarked
public class ModCriteria {
    public static abstract class FriendlyScoredCriterion extends SimpleCriterionTrigger<ScoredConditions> {
        @Override
        public Codec<ScoredConditions> codec() {
            return ScoredConditions.CODEC;
        }
    }

    public static class DimensionOpenedCriterion extends FriendlyScoredCriterion {
        static final ResourceLocation ID = InfinityMethods.getId("dims_open");
        public void trigger(ServerPlayer player) {
            this.trigger(player, (conditions) -> conditions.test(player.getStats().getValue(ModStats.DIMS_OPENED_STAT)));
        }
    }

    public static class DimensionClosedCriterion extends FriendlyScoredCriterion {
        static final ResourceLocation ID = InfinityMethods.getId("dims_closed");
        public void trigger(ServerPlayer player) {
            this.trigger(player, (conditions) -> conditions.test(player.getStats().getValue(ModStats.WORLDS_DESTROYED_STAT)));
        }
    }

    public static class BiomeBottleCriterion extends FriendlyScoredCriterion {
        static final ResourceLocation ID = InfinityMethods.getId("bottle");
        public void trigger(ServerPlayer player, BiomeBottleBlockEntity be) {
            this.trigger(player, (conditions) -> conditions.test(be.charge));
        }
    }

    public static class WhoRemainsCriterion extends SimpleCriterionTrigger<EmptyConditions> {
        static final ResourceLocation ID = InfinityMethods.getId("who_remains");
        public void trigger(ServerPlayer player) {
            this.trigger(player, (conditions) -> true);
        }

        @Override
        public Codec<EmptyConditions> codec() {
            return EmptyConditions.CODEC;
        }
    }

    public static class IridescentCriterion extends SimpleCriterionTrigger<IridescenceConditions> {
        static final ResourceLocation ID = InfinityMethods.getId("iridescence");
        public void trigger(ServerPlayer player, boolean willing, int level) {
            this.trigger(player, (conditions) -> conditions.test(willing, level));
        }

        @Override
        public Codec<IridescenceConditions> codec() {
            return IridescenceConditions.CODEC;
        }
    }

    public static class ConvertMobCriterion extends SimpleCriterionTrigger<DataConditions> {
        static final ResourceLocation ID = InfinityMethods.getId("convert_mob");
        public void trigger(ServerPlayer player, LivingEntity e) {
            this.trigger(player, (conditions) -> conditions.test(BuiltInRegistries.ENTITY_TYPE.getKey(e.getType()).toString()));
        }

        @Override
        public Codec<DataConditions> codec() {
            return DataConditions.CODEC;
        }
    }

    public record EmptyConditions(Optional<Holder<LootItemCondition>> player) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<EmptyConditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(EmptyConditions::player)
                        )
                        .apply(instance, EmptyConditions::new)
        );
    }

    public record ScoredConditions(Optional<Holder<LootItemCondition>> player, int score) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<ScoredConditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(ScoredConditions::player),
                                Codec.INT.fieldOf("amount").forGetter(ScoredConditions::score)
                        )
                        .apply(instance, ScoredConditions::new)
        );

        public boolean test(int stat) {
            return stat >= this.score;
        }
    }

    public record DataConditions(Optional<Holder<LootItemCondition>> player, String data) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<DataConditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(DataConditions::player),
                                Codec.STRING.fieldOf("data").forGetter(DataConditions::data)
                        )
                        .apply(instance, DataConditions::new)
        );

        public boolean test(String data) {
            return data.equals(this.data);
        }
    }

    public record IridescenceConditions(Optional<Holder<LootItemCondition>> player, Optional<Boolean> willing, Optional<Integer> minLevel) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<IridescenceConditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(IridescenceConditions::player),
                                Codec.BOOL.optionalFieldOf("willing").forGetter(IridescenceConditions::willing),
                                Codec.INT.optionalFieldOf("min_level").forGetter(IridescenceConditions::minLevel)
                        )
                        .apply(instance, IridescenceConditions::new)
        );

        public boolean test(boolean isWilling, int level) {
            return (willing.isEmpty() || isWilling == willing.get()) && (minLevel.isEmpty() || level >= minLevel.get());
        }
    }

    public static Supplier<DimensionOpenedCriterion> DIMS_OPENED;
    public static Supplier<DimensionClosedCriterion> DIMS_CLOSED;
    public static Supplier<WhoRemainsCriterion> WHO_REMAINS;
    public static Supplier<IridescentCriterion> IRIDESCENT;
    public static Supplier<BiomeBottleCriterion> BIOME_BOTTLE;
    public static Supplier<ConvertMobCriterion> CONVERT_MOB;

    public static final DeferredRegister<CriterionTrigger<?>> CRITERIA = DeferredRegister.create(MOD_ID, Registries.TRIGGER_TYPE);

    public static void registerCriteria() {
        DIMS_OPENED = register(DimensionOpenedCriterion.ID, DimensionOpenedCriterion::new);
        DIMS_CLOSED = register(DimensionClosedCriterion.ID, DimensionClosedCriterion::new);
        WHO_REMAINS = register(WhoRemainsCriterion.ID, WhoRemainsCriterion::new);
        IRIDESCENT = register(IridescentCriterion.ID, IridescentCriterion::new);
        BIOME_BOTTLE = register(BiomeBottleCriterion.ID, BiomeBottleCriterion::new);
        CONVERT_MOB = register(ConvertMobCriterion.ID, ConvertMobCriterion::new);
        CRITERIA.register();
    }

    public static <S extends SimpleCriterionTrigger.SimpleInstance, T extends SimpleCriterionTrigger<S>>
    Supplier<T> register(ResourceLocation id, Supplier<T> getter) {
        return CRITERIA.register(id.getPath(), getter);
    }
}
