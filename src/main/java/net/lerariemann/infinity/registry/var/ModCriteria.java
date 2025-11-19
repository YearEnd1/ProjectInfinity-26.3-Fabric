package net.lerariemann.infinity.registry.var;

import net.lerariemann.infinity.block.entity.BiomeBottleBlockEntity;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Supplier;

//? if >1.21 {
import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.advancements.CriterionTrigger;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;
//?} else {
/*import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.advancements.CriteriaTriggers;
*///?}

@MethodsReturnNonnullByDefault
public class ModCriteria {
    public static abstract class FriendlyScoredCriterion extends SimpleCriterionTrigger<ScoredConditions> {
        //? if >1.21 {
        @Override
        public Codec<ScoredConditions> codec() {
            return ScoredConditions.CODEC;
        }
        //?} else {
        /*@Override
        public ScoredConditions createInstance(JsonObject jsonObject, ContextAwarePredicate lootContextPredicate, DeserializationContext advancementEntityPredicateDeserializer) {
            int score = jsonObject.getAsJsonPrimitive("amount").getAsInt();
            return new ScoredConditions(lootContextPredicate, score, getId());
        }
        *///?}
    }

    public static class DimensionOpenedCriterion extends FriendlyScoredCriterion {
        static final ResourceLocation ID = InfinityMethods.getId("dims_open");
        public void trigger(ServerPlayer player) {
            this.trigger(player, (conditions) -> conditions.test(player.getStats().getValue(ModStats.DIMS_OPENED_STAT)));
        }
        //? if <1.21 {
        /*public ResourceLocation getId() {
            return ID;
        }
        *///?}
    }

    public static class DimensionClosedCriterion extends FriendlyScoredCriterion {
        static final ResourceLocation ID = InfinityMethods.getId("dims_closed");
        public void trigger(ServerPlayer player) {
            this.trigger(player, (conditions) -> conditions.test(player.getStats().getValue(ModStats.WORLDS_DESTROYED_STAT)));
        }
        //? if <1.21 {
        /*public ResourceLocation getId() {
            return ID;
        }
        *///?}
    }

    public static class BiomeBottleCriterion extends FriendlyScoredCriterion {
        static final ResourceLocation ID = InfinityMethods.getId("bottle");
        public void trigger(ServerPlayer player, BiomeBottleBlockEntity be) {
            this.trigger(player, (conditions) -> conditions.test(be.charge));
        }
        //? if <1.21 {
        /*public ResourceLocation getId() {
            return ID;
        }
        *///?}
    }

    public static class WhoRemainsCriterion extends SimpleCriterionTrigger<EmptyConditions> {
        static final ResourceLocation ID = InfinityMethods.getId("who_remains");
        public void trigger(ServerPlayer player) {
            this.trigger(player, (conditions) -> true);
        }
        //? if >1.21 {
        @Override
        public Codec<EmptyConditions> codec() {
            return EmptyConditions.CODEC;
        }
        //?} else {
        /*public ResourceLocation getId() {
            return ID;
        }
        @Override
        public EmptyConditions createInstance(JsonObject jsonObject, ContextAwarePredicate lootContextPredicate, DeserializationContext advancementEntityPredicateDeserializer) {
            return new EmptyConditions(lootContextPredicate, ID);
        }
        *///?}
    }

    public static class IridescentCriterion extends SimpleCriterionTrigger<IridescenceConditions> {
        static final ResourceLocation ID = InfinityMethods.getId("iridescence");
        public void trigger(ServerPlayer player, boolean willing, int level) {
            this.trigger(player, (conditions) -> conditions.test(willing, level));
        }
        //? if >1.21 {
        @Override
        public Codec<IridescenceConditions> codec() {
            return IridescenceConditions.CODEC;
        }
        //?} else {
        /*public ResourceLocation getId() {
            return ID;
        }
        @Override
        public IridescenceConditions createInstance(JsonObject jsonObject, ContextAwarePredicate lootContextPredicate, DeserializationContext advancementEntityPredicateDeserializer) {
            JsonPrimitive willing = jsonObject.getAsJsonPrimitive("willing");
            JsonPrimitive minLevel = jsonObject.getAsJsonPrimitive("minLevel");
            return new IridescenceConditions(lootContextPredicate, willing == null ? null : willing.getAsBoolean(),
                    minLevel == null ? null : minLevel.getAsInt(), getId());
        }
        *///?}
    }

    public static class ConvertMobCriterion extends SimpleCriterionTrigger<DataConditions> {
        static final ResourceLocation ID = InfinityMethods.getId("convert_mob");
        public void trigger(ServerPlayer player, LivingEntity e) {
            this.trigger(player, (conditions) -> conditions.test(BuiltInRegistries.ENTITY_TYPE.getKey(e.getType()).toString()));
        }
        //? if >1.21 {
        @Override
        public Codec<DataConditions> codec() {
            return DataConditions.CODEC;
        }
        //?} else {
        /*public ResourceLocation getId() {
            return ID;
        }
        @Override
        public DataConditions createInstance(JsonObject jsonObject, ContextAwarePredicate lootContextPredicate, DeserializationContext advancementEntityPredicateDeserializer) {
            String data = jsonObject.getAsJsonPrimitive("data").getAsString();
            return new DataConditions(lootContextPredicate, data, ID);
        }
        *///?}
    }

    //? if >1.21 {
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
    //?} else {
    /*public static class EmptyConditions extends AbstractCriterionTriggerInstance {

        public EmptyConditions(ContextAwarePredicate player, ResourceLocation ID) {
            super(ID, player);
        }

        public static EmptyConditions create(ResourceLocation ID) {
            return new EmptyConditions(ContextAwarePredicate.ANY, ID);
        }

        public JsonObject serializeToJson(SerializationContext predicateSerializer) {
            return super.serializeToJson(predicateSerializer);
        }
    }

    public static class ScoredConditions extends AbstractCriterionTriggerInstance {
        private final int score;

        public ScoredConditions(ContextAwarePredicate player, int score, ResourceLocation ID) {
            super(ID, player);
            this.score = score;
        }

        public boolean test(int stat) {
            return stat >= this.score;
        }

        public static ScoredConditions create(int i, ResourceLocation ID) {
            return new ScoredConditions(ContextAwarePredicate.ANY, i, ID);
        }

        public JsonObject serializeToJson(SerializationContext predicateSerializer) {
            JsonObject jsonObject = super.serializeToJson(predicateSerializer);
            jsonObject.add("amount", new JsonPrimitive(this.score));
            return jsonObject;
        }
    }

    public static class DataConditions extends AbstractCriterionTriggerInstance {
        private final String data;

        public DataConditions(ContextAwarePredicate player, String data, ResourceLocation ID) {
            super(ID, player);
            this.data = data;
        }

        public boolean test(String data) {
            return data.equals(this.data);
        }
        public static DataConditions create(String data, ResourceLocation ID) {
            return new DataConditions(ContextAwarePredicate.ANY, data, ID);
        }
        public JsonObject serializeToJson(SerializationContext predicateSerializer) {
            JsonObject jsonObject = super.serializeToJson(predicateSerializer);
            jsonObject.add("data", new JsonPrimitive(this.data));
            return jsonObject;
        }
    }


    public static class IridescenceConditions extends AbstractCriterionTriggerInstance {
        private final Boolean willing;
        private final Integer minLevel;

        public IridescenceConditions(ContextAwarePredicate player, Boolean willing, Integer minLevel, ResourceLocation ID) {
            super(ID, player);
            this.willing = willing;
            this.minLevel = minLevel;
        }

        public boolean test(boolean isWilling, int level) {
            return (willing == null || isWilling == willing) && (minLevel == null || level >= minLevel);
        }

        public static ScoredConditions create(int i, ResourceLocation ID) {
            return new ScoredConditions(ContextAwarePredicate.ANY, i, ID);
        }

        public JsonObject serializeToJson(SerializationContext predicateSerializer) {
            JsonObject jsonObject = super.serializeToJson(predicateSerializer);
            if (willing != null) jsonObject.add("willing", new JsonPrimitive(willing));
            if (minLevel != null) jsonObject.add("minLevel", new JsonPrimitive(minLevel));
            return jsonObject;
        }
    }
    *///?}

    public static Supplier<DimensionOpenedCriterion> DIMS_OPENED;
    public static Supplier<DimensionClosedCriterion> DIMS_CLOSED;
    public static Supplier<WhoRemainsCriterion> WHO_REMAINS;
    public static Supplier<IridescentCriterion> IRIDESCENT;
    public static Supplier<BiomeBottleCriterion> BIOME_BOTTLE;
    public static Supplier<ConvertMobCriterion> CONVERT_MOB;

    //? if >1.21 {
    public static final DeferredRegister<CriterionTrigger<?>> CRITERIA = DeferredRegister.create(MOD_ID, Registries.TRIGGER_TYPE);
     //?}

    public static void registerCriteria() {
        DIMS_OPENED = register(DimensionOpenedCriterion.ID, DimensionOpenedCriterion::new);
        DIMS_CLOSED = register(DimensionClosedCriterion.ID, DimensionClosedCriterion::new);
        WHO_REMAINS = register(WhoRemainsCriterion.ID, WhoRemainsCriterion::new);
        IRIDESCENT = register(IridescentCriterion.ID, IridescentCriterion::new);
        BIOME_BOTTLE = register(BiomeBottleCriterion.ID, BiomeBottleCriterion::new);
        CONVERT_MOB = register(ConvertMobCriterion.ID, ConvertMobCriterion::new);
        //? if >1.21 {
        CRITERIA.register();
        //?}
    }

    public static <S extends
            //? if >1.21 {
            SimpleCriterionTrigger.SimpleInstance
            //?} else {
            /*AbstractCriterionTriggerInstance
            *///?}
            , T extends SimpleCriterionTrigger<S>>
    Supplier<T> register(ResourceLocation id, Supplier<T> getter) {
        //? if >1.21 {
        return CRITERIA.register(id.getPath(), getter);
         //?} else {
        /*T criterion = CriteriaTriggers.register(getter.get());
        return () -> criterion;
        *///?}
    }
}
