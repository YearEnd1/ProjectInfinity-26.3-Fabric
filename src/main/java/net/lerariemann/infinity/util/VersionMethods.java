package net.lerariemann.infinity.util;

import dev.architectury.registry.registries.RegistrySupplier;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
//? if >1.21 {
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class VersionMethods {
    public static ResourceLocation id(String s) {
        //? if >1.21 {
        return ResourceLocation.parse(s);
        //?} else {
        /*return ResourceLocation.tryParse(s);
         *///?}
    }

    public static ResourceLocation id(String namespace, String path) {
        //? if >=1.21 {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
        //?} else {
        /*return new ResourceLocation(namespace, path);
         *///?}
    }

    //? if <1.21 {
    /*public static int getOrDefaultInt(ItemStack stack, String key, int i) {
        if (stack.hasTag()) {
            assert stack.getTag() != null;
            if (stack.getTag().contains(key))
                return stack.getTag().getInt(key);
        }
        return i;
    }

    public static String getOrDefaultString(ItemStack stack, String key, String i) {
        if (stack.hasTag()) {
            assert stack.getTag() != null;
            if (stack.getTag().contains(key))
                return stack.getTag().getString(key);
        }
        return i;
    }

    public static boolean contains(ItemStack stack, String key) {
        if (stack.hasTag()) {
            assert stack.getTag() != null;
            return stack.getTag().contains(key);
        }
        return false;
    }

    public static ItemStack apply(ItemStack stack, String key, int i) {
        if (stack.hasTag()) {
            assert stack.getTag() != null;
            stack.getTag().putInt(key, i);
            return stack;
        } else {
            CompoundTag nbtCompound = new CompoundTag();
            nbtCompound.putInt(key, i);
            stack.setTag(nbtCompound);
        }
        return stack;
    }

    public static ItemStack apply(ItemStack stack, String key, String i) {
        if (stack.hasTag()) {
            assert stack.getTag() != null;
            stack.getTag().putString(key, i);
            return stack;
        } else {
            CompoundTag nbtCompound = new CompoundTag();
            nbtCompound.putString(key, i);
            stack.setTag(nbtCompound);
        }
        return stack;
    }

    public static @Nullable String getBiomeComponents(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getTag().getCompound("BlockEntityTag").getString("Biome");
        }
        return null;
    }

    public static @Nullable String getDimensionComponents(ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().contains(ModComponentTypes.DESTINATION)) {
            return stack.getTag().getString(ModComponentTypes.DESTINATION);
        }
        return null;
    }

    public static @Nullable ResourceLocation getDimensionIdentifier(ItemStack stack) {
        String dimension = getDimensionComponents(stack);
        if (dimension != null) return ResourceLocation.tryParse(dimension);
        else return null;
    }

    public static void remove(ItemStack stack, String key) {
        if (stack.hasTag()) {
            stack.removeTagKey(key);
        }
    }
    *///?} else {

    public static int getOrDefaultInt(ItemStack stack, RegistrySupplier<DataComponentType<Integer>> charge, int i) {
        return stack.getOrDefault(charge, i);
    }
     public static @Nullable ResourceLocation getDimensionIdentifier(ItemStack stack) {
         return stack.getComponents().get(ModComponentTypes.DESTINATION.get());
    }

    public static ItemStack apply(ItemStack stack, RegistrySupplier<DataComponentType<Integer>> charge, int i) {
        stack.applyComponents(DataComponentMap.builder()
                .set(charge, i).build());
        return stack;

    }

    public static <T> T remove(ItemStack stack, Supplier<? extends DataComponentType<? extends T>> componentType) {
        return stack.remove(componentType);
    }
    //?}

    public static BlockPos clampToBounds(WorldGenLevel structureWorldAccess, BlockPos destination) {
        return structureWorldAccess.getWorldBorder().clampToBounds(
                //? if >1.21 {
                destination
                 //?} else {
                /*destination.getX(), destination.getY(), destination.getZ()
                *///?}
        );
    }
}