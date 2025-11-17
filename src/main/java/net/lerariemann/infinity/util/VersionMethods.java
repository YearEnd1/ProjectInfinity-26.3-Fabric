package net.lerariemann.infinity.util;

import net.lerariemann.infinity.registry.core.ModComponentTypes;
//? if >1.21 {
/*import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.core.component.DataComponents;
*///?} else {
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.alchemy.PotionUtils;
//?}
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface VersionMethods {
    @SuppressWarnings("for removal")
    static ResourceLocation id(String s) {
        //? if >1.21 {
        /*return ResourceLocation.parse(s);
        *///?} else {
        return new ResourceLocation(s);
         //?}
    }

    @SuppressWarnings("for removal")
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

    static String getOrNullString(ItemStack stack, String key) {
        return getOrDefaultString(stack, key, null);
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

    static void remove(ItemStack result, String destination) {
        if (result.hasTag()) {
            result.removeTagKey(destination);
        }
    }

    static Optional<Potion> getOrEmptyPotion(ItemStack input) {
        return Optional.of(PotionUtils.getPotion(input));
    }

    static boolean isPotion(Potion input, Potion awkward) {
        return input.equals(awkward);
    }

    //?} else {
    /*static int getOrDefaultInt(ItemStack stack, RegistrySupplier<DataComponentType<Integer>> component, int i) {
        return stack.getOrDefault(component.get(), i);
    }
    static String getOrDefaultString(ItemStack stack, RegistrySupplier<DataComponentType<String>> component, String s) {
        return stack.getOrDefault(component.get(), s);
    }

    static String getOrNullString(ItemStack stack, RegistrySupplier<DataComponentType<String>> component) {
        return stack.get(component.get());
    }

    static @Nullable ResourceLocation getDimensionIdentifier(ItemStack stack) {
         return stack.getComponents().get(ModComponentTypes.DESTINATION.get());
    }

    static ItemStack apply(ItemStack stack, RegistrySupplier<DataComponentType<Integer>> charge, int i) {
        stack.applyComponents(DataComponentMap.builder()
                .set(charge.get(), i).build());
        return stack;
    }

    static Optional<Holder<Potion>> getOrEmptyPotion(ItemStack input) {
        return input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion();
    }

    static boolean isPotion(Holder<Potion> input, Holder<Potion> awkward) {
        return input.value().equals(awkward.value());
    }

    static boolean contains(ItemStack currStack, RegistrySupplier<DataComponentType<String>> dyeColor) {
        return currStack.has(dyeColor.get());
    }

    static <T> T remove(ItemStack stack, RegistrySupplier<DataComponentType<T>> component) {
        return stack.remove(component.get());
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

    static @Nullable ResourceLocation getBiomeComponents(ItemStack stack) {
        //? if >1.21 {
        /*return stack.getComponents().get(ModComponentTypes.BIOME_CONTENTS.get());
        *///?} else {
        if (stack.getTag() != null) {
            return InfinityMethods.getId(stack.getTag().getCompound("BlockEntityTag").getString("Biome"));
        }
        return null;
        //?}
    }

    //? if forge {
    /*static MobEffect effectOrHolder(Holder<? extends MobEffect> holder) {
        return holder.value();
    }
    *///?} else {
    static
    //? if >1.21 {
    /*Holder<MobEffect>
     *///?} else {
    MobEffect
    //?}
    effectOrHolder(Holder<
            MobEffect> holder) {
        //? if >1.21 {
        /*return holder;
         *///?} else {
        return holder.value();
        //?}
    }
    //?}

    static MobEffect unholdEffect(
            //? if >1.21 {
            /*Holder<MobEffect>
             *///?} else {
            MobEffect
            //?}
            holder) {
        //? if >1.21 {
        /*return holder.value();
         *///?} else {
        return holder;
        //?}
    }

    static int opaque(int color) {
        return color | -16777216;
    }

    static void consumeWithRemainder(Player player, ItemStack stack, ItemStack defaultInstance) {
        //? if >1.21 {
        /*boolean creative = player.hasInfiniteMaterials();
        *///?} else {
        boolean creative = player.isCreative();
        //?}
        if (!creative) {
            stack.setCount(stack.getCount()-1);
            player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
        }
    }
}
//? if >1.21 {

//?} else {
//?}