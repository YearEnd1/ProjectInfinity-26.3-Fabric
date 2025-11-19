package net.lerariemann.infinity.item.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.lerariemann.infinity.block.custom.AltarBlock;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

//? if >1.21 {
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
//?}

public class SetAltarStateLootFunction extends LootItemConditionalFunction {
    //? if >1.21 {
    public static final MapCodec<SetAltarStateLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance).apply(instance, SetAltarStateLootFunction::new)
    );
    private SetAltarStateLootFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }
    //?} else {
    /*protected SetAltarStateLootFunction(LootItemCondition[] lootItemConditions) {
        super(lootItemConditions);
    }
    *///?}

    @Override
    public @NotNull LootItemFunctionType
        //? if >1.21 {
        <? extends LootItemConditionalFunction>
         //?}
    getType() {
        return ModItemFunctions.SET_ALTAR_STATE.get();
    }

    @Override
    protected @NotNull ItemStack run(ItemStack stack, LootContext context) {
        //? if >1.21.2 {
        BlockState st = context.getOptionalParameter(LootContextParams.BLOCK_STATE);
        //?} else {
        /*BlockState st = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        *///?}
        if (st != null) {
            //? if >1.21 {
            stack.applyComponentsAndValidate(AltarBlock.toComponentChanges(st));
            //?} else {
            /*stack.setTag(AltarBlock.toComponentChanges(st));
            *///?}
        }
        return stack;
    }

    //? if <1.21 {
    /*public static class Serializer extends LootItemConditionalFunction.Serializer<SetAltarStateLootFunction> {
        public Serializer() {
        }
        public @NotNull SetAltarStateLootFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditions) {
            return new SetAltarStateLootFunction(new LootItemCondition[0]);
        }
    }
    *///?}
}

