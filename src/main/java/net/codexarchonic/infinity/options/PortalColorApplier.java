package net.codexarchonic.infinity.options;

import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import java.awt.Color;
import java.util.Random;
import java.util.function.Function;

public interface PortalColorApplier extends Function<BlockPos, Integer> {
    static PortalColorApplier of(ResourceLocation id, MinecraftServer server) {
        return of(id, InfinityOptions.readData(server, id));
    }
    static PortalColorApplier of(ResourceLocation id, CompoundTag defaultData) {
        CompoundTag data = InfinityMod.provider.easterizer.optionmap.get(id.getPath());
        if (data == null) data = defaultData;
        return of(data, (int)InfinityMethods.getNumericFromId(id));
    }
    static PortalColorApplier of(CompoundTag data, int defaultColor) {
        if (!data.contains("portal_color")) return new Simple(defaultColor);
        //? if >1.21.2 {
        if (data.getInt("portal_color").isPresent())
        //?} else {
        /*if (data.getTagType("portal_color") == Tag.TAG_INT)
        *///?}
            return new Simple(NbtUtils.getInt(data, "portal_color"));
        CompoundTag applierData = NbtUtils.getCompound(data, "portal_color");
        return switch (NbtUtils.getString(applierData, "type", "")) {
            case "simple" -> new Simple(NbtUtils.getInt(applierData, "value"));
            case "checker" -> new Checker(NbtUtils.getList(applierData, "values", Tag.TAG_INT));
            case "random_hue" -> new RandomHue(applierData);
            case "random" -> RandomColor.INSTANCE;
            default -> new Simple(defaultColor);
        };
    }

    Integer apply(BlockPos pos);

    record Simple(int value) implements PortalColorApplier {
        @Override
        public Integer apply(BlockPos pos) {
            return value;
        }
    }

    record Checker(ListTag values) implements PortalColorApplier {
        @Override
        public Integer apply(BlockPos pos) {
            int mod = InfinityMethods.properMod(pos.getX() + pos.getY() + pos.getZ(), values.size());
            return NbtUtils.getInt(values, mod);
        }
    }

    enum RandomColor implements PortalColorApplier {
        INSTANCE;
        @Override
        public Integer apply(BlockPos pos) {
            return (new Random(pos.hashCode())).nextInt();
        }
    }

    record RandomHue(float saturation, float brightness, float detail) implements PortalColorApplier {
        public RandomHue(CompoundTag applierData) {
            this(NbtUtils.getFloat(applierData, "saturation", 1.0f),
                    NbtUtils.getFloat(applierData, "brightness", 1.0f),
                    NbtUtils.getFloat(applierData, "detail", 12.0f));
        }

        @Override
        public Integer apply(BlockPos pos) {
            float hue = (pos.getX() + pos.getY() + pos.getZ()) / detail;
            return Color.HSBtoRGB(hue - (int)hue, saturation, brightness) & 0xFFFFFF;
        }
    }
}
