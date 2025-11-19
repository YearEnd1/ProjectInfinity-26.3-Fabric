package net.lerariemann.infinity.item;

import net.lerariemann.infinity.block.custom.BiomeBottleBlock;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import java.util.List;

public class BiomeBottleItem extends BlockItem {
    public BiomeBottleItem(Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    public void appendHoverText(ItemStack stack,
            //? if >1.21 {
            TooltipContext
             //?} else {
            /*Level
            *///?}
            context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        ResourceLocation biome = VersionMethods.getBiomeComponents(stack);
        if (biome != null) {
            tooltip.add(Component.translatable(Util.makeDescriptionId("biome", biome)).withStyle(ChatFormatting.GRAY));
        }
        else {
            MutableComponent mutableText = Component.translatable("caption.infinity.biomebottle.empty");
            tooltip.add(mutableText.withStyle(ChatFormatting.GRAY));
        }
        if (type.isAdvanced()) {
            tooltip.add(Component.translatable("caption.infinity.biomebottle.charge", BiomeBottleBlock.getCharge(stack)).withStyle(ChatFormatting.GRAY));
        }
    }
}
