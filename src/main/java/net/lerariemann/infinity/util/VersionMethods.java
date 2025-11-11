package net.lerariemann.infinity.util;

import net.lerariemann.infinity.registry.core.ModComponentTypes;
//? if >1.21 {
import dev.architectury.registry.registries.RegistrySupplier;
/*import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
*///?}
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

public interface VersionMethods {
    static ResourceLocation id(String s) {
        //? if >1.21 {
        /*return ResourceLocation.parse(s);
        *///?} else {
        return ResourceLocation.tryParse(s);
         //?}
    }

    static ResourceLocation id(String namespace, String path) {
        //? if >=1.21 {
        /*return ResourceLocation.fromNamespaceAndPath(namespace, path);
        *///?} else {
        return new ResourceLocation(namespace, path);
         //?}
    }

    //? if <1.21 {
    static int getOrDefaultInt(ItemStack stack, String key, int i) {
        if (stack.hasTag()) {
            assert stack.getTag() != null;
            if (stack.getTag().contains(key))
                return stack.getTag().getInt(key);
        }
        return i;
    }

    static String getOrDefaultString(ItemStack stack, String key, String i) {
        if (stack.hasTag()) {
            assert stack.getTag() != null;
            if (stack.getTag().contains(key))
                return stack.getTag().getString(key);
        }
        return i;
    }

    static boolean contains(ItemStack stack, String key) {
        if (stack.hasTag()) {
            assert stack.getTag() != null;
            return stack.getTag().contains(key);
        }
        return false;
    }

    static ItemStack apply(ItemStack stack, String key, int i) {
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

    static ItemStack apply(ItemStack stack, String key, String i) {
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

    static @Nullable String getBiomeComponents(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getTag().getCompound("BlockEntityTag").getString("Biome");
        }
        return null;
    }

    static @Nullable String getDimensionComponents(ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().contains(ModComponentTypes.DESTINATION)) {
            return stack.getTag().getString(ModComponentTypes.DESTINATION);
        }
        return null;
    }

    static @Nullable ResourceLocation getDimensionIdentifier(ItemStack stack) {
        String dimension = getDimensionComponents(stack);
        if (dimension != null) return ResourceLocation.tryParse(dimension);
        else return null;
    }
    //?} else {
    /*static int getOrDefaultInt(ItemStack stack, RegistrySupplier<DataComponentType<Integer>> charge, int i) {
        return stack.getOrDefault(charge, i);
    }

    static @Nullable ResourceLocation getDimensionIdentifier(ItemStack stack) {
         return stack.getComponents().get(ModComponentTypes.DESTINATION.get());
    }

    static ItemStack apply(ItemStack stack, RegistrySupplier<DataComponentType<Integer>> charge, int i) {
        stack.applyComponents(DataComponentMap.builder()
                .set(charge, i).build());
        return stack;
    }
    *///?}

    static BlockPos clampToBounds(WorldGenLevel structureWorldAccess, BlockPos destination) {
        return structureWorldAccess.getWorldBorder().clampToBounds(
                //? if >1.21 {
                /*destination
                 *///?} else {
                destination.getX(), destination.getY(), destination.getZ()
                //?}
        );
    }

    static BlockBehaviour.Properties copyBlockProperties(Block b) {
        //? if >1.21 {
        /*return BlockBehaviour.Properties.ofFullCopy(b);
        *///?} else {
        return BlockBehaviour.Properties.copy(b);
        //?}
    }

    static <T> String getNameAsString(Registry<T> reg, T obj) {
        //? if >1.21 {
        /*return reg.wrapAsHolder(obj).getRegisteredName();
         *///?} else {
        return reg.getKey(obj).toString();
        //?}
    }

    static <T> Holder<T> getFromId(Registry<T> reg, ResourceLocation id) {
        //? if >1.21 {
        /*Optional<Holder.Reference<T>> entry = reg.getHolder(id);
        return entry.orElse(null);
        *///?} else {
        T obj = reg.get(id);
        return reg.wrapAsHolder(obj);
        //?}
    }

    static <T> String getRegisteredName(Holder<T> holder) {
        //? if >1.21 {
        /*return holder.getRegisteredName();
        *///?} else {
        return holder.unwrapKey().map(key -> key.location().toString()).orElse("[unregistered]");
        //?}
    }


    static
    //? if >1.21 {
    /*Holder<MobEffect>
     *///?} else {
    MobEffect
    //?}
    effectOrHolder(Holder<MobEffect> holder) {
        //? if >1.21 {
        /*return holder;
         *///?} else {
        return holder.value();
        //?}
    }
    //? if >1.21 {
    /*
     *///?} else {
    //?}
}