package net.lerariemann.infinity.item.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lerariemann.infinity.block.custom.BiomeBottleBlock;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.List;

public class SetLevelLootFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetLevelLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance).apply(instance, SetLevelLootFunction::new)
    );

    private SetLevelLootFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }

    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return ModItemFunctions.SET_BIOME_BOTTLE_LEVEL.get();
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        BiomeBottleBlock.updateCharge(stack);
        return stack;
    }
}
