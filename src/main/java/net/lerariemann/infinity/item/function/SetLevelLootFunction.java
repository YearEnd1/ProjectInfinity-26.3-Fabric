package net.lerariemann.infinity.item.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.lerariemann.infinity.block.custom.BiomeBottleBlock;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

//? if >1.21 {
/*import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
*///?}

public class SetLevelLootFunction extends LootItemConditionalFunction {
    //? if >1.21 {
    /*public static final MapCodec<SetLevelLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance).apply(instance, SetLevelLootFunction::new)
    );
    protected SetLevelLootFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }
    *///?} else {
    protected SetLevelLootFunction(LootItemCondition[] lootItemConditions) {
        super(lootItemConditions);
    }
    //?}

    @Override
    protected @NotNull ItemStack run(ItemStack stack, LootContext context) {
        BiomeBottleBlock.updateCharge(stack);
        return stack;
    }

    @Override
    public @NotNull LootItemFunctionType
    //? if >1.21 {
    /*<? extends LootItemConditionalFunction>
    *///?}
    getType() {
        return ModItemFunctions.SET_BIOME_BOTTLE_LEVEL.get();
    }

    //? if <1.21 {
    public static class Serializer extends LootItemConditionalFunction.Serializer<SetLevelLootFunction> {
        public Serializer() {
        }
        public @NotNull SetLevelLootFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditions) {
            return new SetLevelLootFunction(new LootItemCondition[0]);
        }
    }
    //?}
}
