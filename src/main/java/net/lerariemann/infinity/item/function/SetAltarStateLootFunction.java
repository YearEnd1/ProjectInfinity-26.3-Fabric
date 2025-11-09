package net.lerariemann.infinity.item.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lerariemann.infinity.block.custom.AltarBlock;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.List;

public class SetAltarStateLootFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetAltarStateLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance).apply(instance, SetAltarStateLootFunction::new)
    );

    private SetAltarStateLootFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }

    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return ModItemFunctions.SET_ALTAR_STATE.get();
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        BlockState st = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (st != null) stack.applyComponentsAndValidate(AltarBlock.toComponentChanges(st));
        return stack;
    }
}

