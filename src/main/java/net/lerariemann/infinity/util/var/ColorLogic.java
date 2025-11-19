package net.lerariemann.infinity.util.var;

import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import java.awt.*;
import java.util.Map;

public interface ColorLogic {
    Map<String, Integer> chromaticColors = Map.ofEntries(Map.entry("white", 0xFFFFFF),
            Map.entry("light_gray", 0x8F8F86),
            Map.entry("gray", 0x262F2F),
            Map.entry("black", 0x000000),
            Map.entry("brown", 0x6B340F),
            Map.entry("red", 0xA30300),
            Map.entry("orange", 0xFF6200),
            Map.entry("yellow", 0xFFCC00),
            Map.entry("lime", 0x7FBF00),
            Map.entry("green", 0x425C00),
            Map.entry("cyan", 0x00727C),
            Map.entry("light_blue", 0x21BEEA),
            Map.entry("blue", 0x1B2592),
            Map.entry("purple", 0x6214A2),
            Map.entry("magenta", 0xB22AAD),
            Map.entry("pink", 0xFF86AE));
    int defaultChromatic = 0x9154DB;
    int defaultPortal = 0x00EEFF;

    static int getChromaticColor(DyeColor dye) {
        return chromaticColors.getOrDefault(dye.getName(), 0xFFFFFF);
    }

    static int getPureHue(double hue) {
        return getPureHue((float)hue);
    }
    static int getPureHue(float hue) {
        return Color.HSBtoRGB(hue, 1.0F, 1.0F) & 0xFFFFFF;
    }
    static boolean matchesPureHue(int rgb, int pureHue) {
        Color c = new Color(rgb);
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        return (Math.abs(hsb[0] * 360 - pureHue) <= 1) && (hsb[1] >= 1) && (hsb[2] >= 1);
    }

    String[] vanillaColors = {"white", "light_gray", "gray", "black", "brown", "red", "orange", "yellow", "lime", "green",
            "light_blue", "blue", "cyan", "purple", "magenta", "pink"};
    Map<TagKey<Block>, String> supportedBlockTypes = Map.ofEntries(
            Map.entry(BlockTags.WOOL, "$_wool"),
            Map.entry(BlockTags.WOOL_CARPETS, "$_carpet"));

    static Block getBlockByColor(String color, TagKey<Block> type) {
        return VersionMethods.getBlock(VersionMethods.id(supportedBlockTypes.get(type)
                .replace("$", color)));
    }
    static Block getBlockByColor(DyeColor color, TagKey<Block> type) {
        return getBlockByColor(color.getName(), type);
    }

    static DyeColor getColorByState(BlockState state) {
        for (TagKey<Block> key : supportedBlockTypes.keySet()) if (state.is(key)) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
            return DyeColor.byName(
                    id.getPath().replace(supportedBlockTypes.get(key).replace("$", ""), ""),
                    null);
        }
        return null;
    }
    static boolean isSupported(BlockState state) {
        for (TagKey<Block> key : supportedBlockTypes.keySet()) if (state.is(key)) return true;
        return false;
    }

    static <T extends Comparable<T>> BlockState applyPropertyFrom(BlockState newState, BlockState oldState, Property<T> property) {
        return newState.trySetValue(property, oldState.getValue(property));
    }

    static BlockState recolor(String color, BlockState state) {
        for (TagKey<Block> key : supportedBlockTypes.keySet()) if (state.is(key)) {
            BlockState newState = getBlockByColor(color, key).defaultBlockState();
            for (Property<?> property: state.getProperties())
                newState = applyPropertyFrom(newState, state, property);
            return newState;
        }
        return null;
    }
}
