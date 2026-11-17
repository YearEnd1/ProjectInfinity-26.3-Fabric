package net.codexarchonic.infinity.item.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.codexarchonic.infinity.block.custom.BiomeBottleBlock;
import net.codexarchonic.infinity.registry.core.ModItemFunctions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
//? if <26
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jspecify.annotations.NonNull;

//? if >1.21 {
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
//?}

public class SetLevelLootFunction extends LootItemConditionalFunction {
    //? if >1.21 {
    public static final MapCodec<SetLevelLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance).apply(instance, SetLevelLootFunction::new)
    );
    protected SetLevelLootFunction(java.util.Optional<net.minecraft.core.Holder<LootItemCondition>> conditions) {
        super(conditions);
    }
    //?} else {
    /*protected SetLevelLootFunction(LootItemCondition[] lootItemConditions) {
        super(lootItemConditions);
    }
    *///?}

    @Override
    protected @NonNull ItemStack run(ItemStack stack, LootContext context) {
        BiomeBottleBlock.updateCharge(stack);
        return stack;
    }

    //? if <26 {
    @Override
    public @NonNull LootItemFunctionType
    //? if >1.21 {
    <? extends LootItemConditionalFunction>
    //?}
    getType() {
        return ModItemFunctions.SET_BIOME_BOTTLE_LEVEL.get();
    }
    //?} else {
    /*@Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
    }
    *///?}

    //? if <1.21 {
    /*public static class Serializer extends LootItemConditionalFunction.Serializer<SetLevelLootFunction> {
        public Serializer() {
        }
        public @NonNull SetLevelLootFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditions) {
            return new SetLevelLootFunction(new LootItemCondition[0]);
        }
    }
    *///?}
}
