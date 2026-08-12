package net.codexarchonic.infinity.item;

import net.codexarchonic.infinity.block.custom.BiomeBottleBlock;
import net.codexarchonic.infinity.util.VersionMethods;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
//? if >1.21.2 {
import net.minecraft.world.item.component.TooltipDisplay;
//?} else {
/*import java.util.List;
*///?}
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;


import java.util.function.Consumer;

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
                                context,
                                //? if >1.21.2 {
                                TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder
                                //?} else {
                                /*List<Component> tooltip
                                *///?}
                                , TooltipFlag type) {
        //? if >1.21.2 {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, type);
        //?} else {
        /*super.appendHoverText(stack, context, tooltip, type);
        Consumer<Component> tooltipAdder = tooltip::add;
        *///?}
        ResourceLocation biome = VersionMethods.getBiomeComponents(stack);
        if (biome != null) {
            tooltipAdder.accept(Component.translatable(Util.makeDescriptionId("biome", biome)).withStyle(ChatFormatting.GRAY));
        }
        else {
            MutableComponent mutableText = Component.translatable("caption.infinity.biomebottle.empty");
            tooltipAdder.accept(mutableText.withStyle(ChatFormatting.GRAY));
        }
        if (type.isAdvanced()) {
            tooltipAdder.accept(Component.translatable("caption.infinity.biomebottle.charge", BiomeBottleBlock.getCharge(stack)).withStyle(ChatFormatting.GRAY));
        }
    }
}
