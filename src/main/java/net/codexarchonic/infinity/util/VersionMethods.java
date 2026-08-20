package net.codexarchonic.infinity.util;

import net.codexarchonic.infinity.access.GameRendererAccess;
import net.codexarchonic.infinity.registry.core.ModComponentTypes;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.minecraft.client.Minecraft;
//? if >1.21 {
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomModelData;
//?} else {
/*import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionUtils;
*///?}
//? if >1.21.2 {
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.apache.logging.log4j.LogManager;
//?} else {
/*import net.minecraft.world.InteractionResultHolder;
*///?}
//? if =1.21.1
//import net.minecraft.world.ItemInteractionResult;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface VersionMethods {
    static void loadPP(Minecraft client, String sid) {
        //? if >1.21.2 {
            client.gameRenderer.setPostEffect(InfinityMethods.getId(sid));
            LogManager.getLogger().info(client.gameRenderer.currentPostEffect());
        //?} else {
        /*((GameRendererAccess)(client.gameRenderer)).infinity$loadPP(InfinityMethods.getId("shaders/" + sid + ".json"));
         *///?}
    }

    @SuppressWarnings("for removal")
    static ResourceLocation id(String s) {
        //? if >1.21 {
        return ResourceLocation.parse(s);
        //?} else {
        /*return new ResourceLocation(s);
         *///?}
    }

    @SuppressWarnings("for removal")
    static ResourceLocation id(String namespace, String path) {
        //? if >=1.21 {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
        //?} else {
        /*return new ResourceLocation(namespace, path);
         *///?}
    }

    static ServerLevel getPlayerLevel(ServerPlayer player) {
        //? if >1.21.2 {
        return player.level();
        //?} else {
        /*return player.serverLevel();
         *///?}
    }

    static void saveBlockEntityData(
            //? if < 1.21.2 {
            /*CompoundTag nbt
             *///?} else {
            ValueOutput nbt
            //?}
            , CompoundTag data) {
        //? if < 1.21.2 {
        /*nbt.put("infdata", data);
         *///?} else {
        nbt.store("infdata", CompoundTag.CODEC, data);
        //?}
    }

    static CompoundTag loadBlockEntityData(
            //? if >1.21.2 {
            ValueInput tag
            //?} else {
            /*CompoundTag tag
             *///?}
            ) {
        //? if >1.21.2 {
        return tag.read("infdata", CompoundTag.CODEC).orElse(new CompoundTag());
        //?} else {
        /*return NbtUtils.getCompound(tag, "infdata", tag);
        *///?}
    }

    //? if <1.21 {
    /*static int getOrDefaultInt(ItemStack stack, String key, int i) {
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

    static @Nullable ResourceLocation getDimensionResourceLocation(ItemStack stack) {
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

    *///?} else {
    static int getOrDefaultInt(ItemStack stack, RegistrySupplier<DataComponentType<Integer>> component, int i) {
        return stack.getOrDefault(component.get(), i);
    }
    static String getOrDefaultString(ItemStack stack, RegistrySupplier<DataComponentType<String>> component, String s) {
        return stack.getOrDefault(component.get(), s);
    }

    static String getOrNullString(ItemStack stack, RegistrySupplier<DataComponentType<String>> component) {
        return stack.get(component.get());
    }

    static @Nullable ResourceLocation getDimensionResourceLocation(ItemStack stack) {
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

    //?}

    static BlockPos clampToBounds(WorldGenLevel structureWorldAccess, BlockPos destination) {
        return structureWorldAccess.getWorldBorder().clampToBounds(
                //? if >1.21 {
                destination
                 //?} else {
                /*destination.getX(), destination.getY(), destination.getZ()
                *///?}
        );
    }

    static BlockBehaviour.Properties copyBlockProperties(Block b) {
        //? if >1.21 {
        return BlockBehaviour.Properties.ofFullCopy(b);
        //?} else {
        /*return BlockBehaviour.Properties.copy(b);
        *///?}
    }

    static <T> String getNameAsString(Registry<T> reg, T obj) {
        //? if >1.21 {
        return reg.wrapAsHolder(obj).getRegisteredName();
         //?} else {
        /*return reg.getKey(obj).toString();
        *///?}
    }

    static <T> Holder<T> getFromId(Registry<T> reg, ResourceLocation id) {
        //? if >1.21.2 {
        Optional<Holder.Reference<T>> entry = reg.get(id);
        return entry.orElse(null);
        //?} else if >1.21 {
        /*Optional<Holder.Reference<T>> entry = reg.getHolder(id);
        return entry.orElse(null);
        *///?} else {
        /*T obj = reg.get(id);
        return reg.wrapAsHolder(obj);
        *///?}
    }

    static <T> String getRegisteredName(Holder<T> holder) {
        //? if >1.21 {
        return holder.getRegisteredName();
        //?} else {
        /*return holder.unwrapKey().map(key -> key.location().toString()).orElse("[unregistered]");
        *///?}
    }

    static @Nullable ResourceLocation getBiomeComponents(ItemStack stack) {
        //? if >1.21 {
        return stack.getComponents().get(ModComponentTypes.BIOME_CONTENTS.get());
        //?} else {
        /*if (stack.getTag() != null) {
            return VersionMethods.id(stack.getTag().getCompound("BlockEntityTag").getString("Biome"));
        }
        return null;
        *///?}
    }

    //? if forge {
    /*static MobEffect effectOrHolder(Holder<? extends MobEffect> holder) {
        return holder.value();
    }
    *///?} else {
    static
    //? if >1.21 {
    Holder<MobEffect>
     //?} else {
    /*MobEffect
    *///?}
    effectOrHolder(Holder<
            MobEffect> holder) {
        //? if >1.21 {
        return holder;
         //?} else {
        /*return holder.value();
        *///?}
    }
    //?}

    static MobEffect unholdEffect(
            //? if >1.21 {
            Holder<MobEffect>
             //?} else {
            /*MobEffect
            *///?}
            holder) {
        //? if >1.21 {
        return holder.value();
         //?} else {
        /*return holder;
        *///?}
    }

    //? if <1.21 {
    /*static boolean gameEventsEqual(GameEvent first, GameEvent second) {
        return first.equals(second);
    }
    *///?} else {
    static boolean gameEventsEqual(Holder<GameEvent> first, Holder.Reference<GameEvent> second) {
        return first.is(second);
    }
    //?}

    static int opaque(int color) {
        return color | -16777216;
    }

    static void consumeWithRemainder(Player player, ItemStack stack, ItemStack defaultInstance) {
        //? if >1.21 {
        boolean creative = player.hasInfiniteMaterials();
        //?} else {
        /*boolean creative = player.isCreative();
        *///?}
        if (!creative) {
            stack.setCount(stack.getCount()-1);
            player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
        }
    }

    static Item getItem(String name) {
        return getFromRegistry(BuiltInRegistries.ITEM, id(name));
    }
    static Block getBlock(String name) {
        return getFromRegistry(BuiltInRegistries.BLOCK, id(name));
    }
    static Block getBlock(ResourceLocation name) {
        return getFromRegistry(BuiltInRegistries.BLOCK, name);
    }
    static Block getBlock(ResourceKey<Block> name) {
        return getFromRegistry(BuiltInRegistries.BLOCK, name);
    }

    static <T extends Comparable<T>> T getPropertyOrElse(BlockState state, Property<T> prop, T def) {
        //? if >1.21.4 {
        return state.getValueOrElse(prop, def);
        //?} else {
        /*return state.getOptionalValue(prop).orElse(def);
        *///?}
    }

    static void teleportTo(Player player, ServerLevel level, Vec3 pos, float yaw, float pitch) {
        //? if >1.21.4 {
        player.teleportTo(level, pos.x, pos.y, pos.z, Set.of(), yaw, pitch, true);
        //?} else {
        /*player.teleportTo(level, pos.x, pos.y, pos.z, Set.of(), yaw, pitch);
        *///?}
    }

    //? if >1.21.2 {
    static InteractionResult sidedSuccess(ItemStack itemStack, boolean clientSide) {
        return InteractionResult.SUCCESS_SERVER.heldItemTransformedTo(itemStack);
    }
    static InteractionResult sidedSuccess(boolean clientSide) {
        return InteractionResult.SUCCESS_SERVER;
    }
    static InteractionResult success(ItemStack itemInHand) {
        return InteractionResult.SUCCESS.heldItemTransformedTo(itemInHand);
    }
    static InteractionResult pass(ItemStack stack) {
        return InteractionResult.PASS;
    }
    static InteractionResult consume(ItemStack stack) {
        return InteractionResult.CONSUME;
    }
    static ItemStack getInteractionHolderResult(InteractionResult result) {
        if (result instanceof InteractionResult.Success success) {
            return success.heldItemTransformedTo();
        }
        return null;
    }

    static CustomModelData getColoredModel(int color) {
        return new CustomModelData(List.of(), List.of(), List.of(String.valueOf(color)), List.of());
    }

    static <T> T getFromRegistry(Registry<T> registry, ResourceLocation name) {
        return registry.getValue(name);
    }
    static <T> T getFromRegistry(Registry<T> registry, ResourceKey<T> name) {
        return registry.getValue(name);
    }

    static <T> Optional<Holder.Reference<T>> getOptionalFromRegistry(Registry<T> registry, ResourceKey<T> name) {
        return registry.get(name);
    }

    static <T> Registry<T> getRegistry(RegistryAccess access, ResourceKey<Registry<T>> jukeboxSong) {
        return access.lookupOrThrow(jukeboxSong);
    }

    //?} else {
    /*static InteractionResultHolder<ItemStack> sidedSuccess(ItemStack itemStack, boolean clientSide) {
        return InteractionResultHolder.sidedSuccess(itemStack, clientSide);
    }
    static InteractionResult sidedSuccess(boolean clientSide) {
        return InteractionResult.sidedSuccess(clientSide);
    }
    static InteractionResultHolder<ItemStack> success(ItemStack itemInHand) {
        return InteractionResultHolder.success(itemInHand);
    }
    static InteractionResultHolder<ItemStack> pass(ItemStack stack) {
        return InteractionResultHolder.pass(stack);
    }
    static InteractionResultHolder<ItemStack> consume(ItemStack stack) {
        return InteractionResultHolder.consume(stack);
    }
    //? if >1.21 {
    static CustomModelData getColoredModel(int color) {
        return new CustomModelData(color);
    }
    //?}
    static ItemStack getInteractionHolderResult(InteractionResultHolder<ItemStack> result) {
        return result.getObject();
    }
    static <T> T getFromRegistry(Registry<T> registry, ResourceLocation name) {
        return registry.get(name);
    }
    static <T> T getFromRegistry(Registry<T> registry, ResourceKey<T> name) {
        return registry.get(name);
    }
      static <T> Optional<Holder.Reference<T>> getOptionalFromRegistry(Registry<T> registry, ResourceKey<T> name) {
        return registry.getHolder(name);
    }

    static <T> Registry<T> getRegistry(RegistryAccess access, ResourceKey<Registry<T>> jukeboxSong) {
        return access.registryOrThrow(jukeboxSong);
    }
    *///?}
    static <T> HolderLookup.RegistryLookup<T> getRegistry(HolderLookup.Provider lookup, ResourceKey<Registry<T>> lootTable) {
        return lookup.lookupOrThrow(lootTable);
    }

    static boolean allowNether(@Nullable MinecraftServer server) {
        if (server == null) return true;
        //? if >1.21.9 {
        /*return server.getGameRules().getRule(GameRules.RULE_ALLOW_NETHER).get();
         *///?} else if >1.21 {
        if (server instanceof DedicatedServer dedicatedServer) return dedicatedServer.getProperties().allowNether;
        else return true;
        //?} else {
        /*return server.isNetherEnabled();
        *///?}
    }

    //? if =1.21.1 {
    /*static ItemInteractionResult passToDefaultBlockInteraction() {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    *///?} else {
    static InteractionResult passToDefaultBlockInteraction() {
        return InteractionResult.PASS;
    }
    //?}
}

