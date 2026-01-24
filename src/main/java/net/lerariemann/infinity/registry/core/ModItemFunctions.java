package net.lerariemann.infinity.registry.core;

//? if <1.21.2 {
/*import dev.architectury.registry.item.ItemPropertiesRegistry;
*///?}
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.lerariemann.infinity.block.custom.BiomeBottleBlock;
import net.lerariemann.infinity.item.function.*;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.BlockPos;
//? if >1.21 {
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.item.crafting.*;
//?} else {
/*import net.minecraft.core.BlockSource;

import net.minecraft.world.ticks.ContainerSingleItem;
*///?}
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
//? if >1.21.2 {
//?} else {
/*import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
*///?}
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

public class ModItemFunctions {
    public static final DeferredRegister<LootItemFunctionType
            //? if >1.21 {
            <?>
             //?}
            > LOOT_FUNCTION_TYPES =
            DeferredRegister.create(MOD_ID, Registries.LOOT_FUNCTION_TYPE);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(MOD_ID, Registries.RECIPE_SERIALIZER);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(MOD_ID, Registries.RECIPE_TYPE);

    public static RegistrySupplier<LootItemFunctionType
            //? if >1.21 {
            <SetLevelLootFunction>
             //?}
            > SET_BIOME_BOTTLE_LEVEL =
            LOOT_FUNCTION_TYPES.register("set_biome_bottle_level", () -> new LootItemFunctionType
                    //? if >1.21 {
                    <>(SetLevelLootFunction.CODEC)
                    //?} else {
                    /*(new SetLevelLootFunction.Serializer())
                    *///?}
                    );
    public static RegistrySupplier<LootItemFunctionType
            //? if >1.21 {
            <SetAltarStateLootFunction>
             //?}
            > SET_ALTAR_STATE =
            LOOT_FUNCTION_TYPES.register("set_altar_state", () -> new LootItemFunctionType
                            //? if >1.21 {
                            <>(SetAltarStateLootFunction.CODEC)
                             //?} else {
                            /*(new SetAltarStateLootFunction.Serializer())
                    *///?}
            );

    //special recipe serializers
    public static RegistrySupplier<RecipeSerializer<BiomeBottleCombiningRecipe>> BIOME_BOTTLE_COMBINING =
            RECIPE_SERIALIZERS.register("biome_bottle_combining", () ->
                    //? if >1.21.2 {
                    new CustomRecipe.Serializer
                    //?} else {
                    /*new SimpleCraftingRecipeSerializer
                    *///?}
                    <>(BiomeBottleCombiningRecipe::new));
    public static RegistrySupplier<RecipeSerializer<F4RechargingRecipe>> F4_RECHARGING =
            RECIPE_SERIALIZERS.register("f4_recharging", () ->
                    //? if >1.21.2 {
                    new CustomRecipe.Serializer
                    //?} else {
                    /*new SimpleCraftingRecipeSerializer
                     *///?}
                    <>(F4RechargingRecipe::new));
    public static RegistrySupplier<RecipeSerializer<ChromaCarpetRecipe>> CARPET =
            RECIPE_SERIALIZERS.register("chroma_carpet", () ->
                    //? if >1.21.2 {
                    new CustomRecipe.Serializer
                    //?} else {
                    /*new SimpleCraftingRecipeSerializer
                     *///?}
                    <>(ChromaCarpetRecipe::new));
    //regular recipe serializers
    public static RegistrySupplier<RecipeSerializer<ChromaticColoringRecipe>> CHROMATIC_COLORING =
            RECIPE_SERIALIZERS.register("chromatic_coloring", () ->
                    new ChromaticColoringRecipe.Serializer(ChromaticColoringRecipe::new));
    public static RegistrySupplier<RecipeSerializer<CollisionCraftingRecipe>> PORTAL_CRAFTING =
            RECIPE_SERIALIZERS.register("collision_portal", () ->
                    new CollisionCraftingRecipe.Serializer(CollisionCraftingRecipe.OfPortal::new));
    public static RegistrySupplier<RecipeSerializer<CollisionCraftingRecipe>> IRIDESCENCE_CRAFTING =
            RECIPE_SERIALIZERS.register("collision_iridescence", () ->
                    new CollisionCraftingRecipe.Serializer(CollisionCraftingRecipe.OfIridescence::new));
    //recipe types
    public static RegistrySupplier<RecipeType<CollisionCraftingRecipe>> PORTAL_CRAFTING_TYPE =
            RECIPE_TYPES.register("collision_portal", () -> CollisionCraftingRecipe.Type.PORTAL);
    public static RegistrySupplier<RecipeType<CollisionCraftingRecipe>> IRIDESCENCE_CRAFTING_TYPE =
            RECIPE_TYPES.register("collision_iridescence", () -> CollisionCraftingRecipe.Type.IRIDESCENCE);
    public static RegistrySupplier<RecipeType<ChromaticColoringRecipe>> CHROMATIC_COLORING_TYPE =
            RECIPE_TYPES.register("chromatic_coloring", () -> ChromaticColoringRecipe.Type.INSTANCE);

    public static void registerItemFunctions() {
        LOOT_FUNCTION_TYPES.register();
        RECIPE_SERIALIZERS.register();
        RECIPE_TYPES.register();
    }

    public static void registerDispenserBehaviour() {
        DispenserBlock.registerBehavior(ModItems.IRIDESCENCE_BUCKET.get(), new DefaultDispenseItemBehavior() {
            public @NotNull ItemStack execute(BlockSource pointer, ItemStack stack) {
                DispensibleContainerItem fluidModificationItem = (DispensibleContainerItem)stack.getItem();
                //? if >1.21 {
                Level world = pointer.level();
                BlockPos pos = pointer.pos();
                BlockState state = pointer.state();
                //?} else {
                /*Level world = pointer.getLevel();
                BlockPos pos = pointer.getPos();
                BlockState state = pointer.getBlockState();
                *///?}
                BlockPos blockPos = pos.relative(state.getValue(DispenserBlock.FACING));
                if (fluidModificationItem.emptyContents(null, world, blockPos, null)) {
                    fluidModificationItem.checkExtraContent(null, world, stack, blockPos);
                    //? if >1.21 {
                    return this.consumeWithRemainder(pointer, stack, Items.BUCKET.getDefaultInstance());
                    //?} else {
                    /*return Items.BUCKET.getDefaultInstance();
                    *///?}
                } else {
                    return new DefaultDispenseItemBehavior().dispense(pointer, stack);
                }
            }
        });
    }

    public static Optional<ItemStack> getResultNoMetadata(ServerLevel w, ItemStack inputItem,
                                          RecipeType<CollisionCraftingRecipe> recipeType) {
        //? if >1.21 {
        Optional<RecipeHolder<CollisionCraftingRecipe>> match = w.
                //? if >1.21.2 {
                        recipeAccess()
                //?} else {
                /*getRecipeManager()
                 *///?}
                .getRecipeFor(recipeType, new SingleRecipeInput(inputItem), w);
        if (match.isEmpty()) return Optional.empty();
        return Optional.of(match.get().value().getResultItem(
                //? if <1.21.2
                /*w.registryAccess()*/
        ));
        //?} else {
        /*Optional<CollisionCraftingRecipe> match = w.getRecipeManager()
                .getRecipeFor(recipeType, new ContainerSingleItem() {

                    @Override
                    public ItemStack getItem(int i) {
                        return inputItem;
                    }

                    @Override
                    public ItemStack removeItem(int i, int j) {
                        return inputItem;
                    }

                    @Override
                    public void setItem(int i, ItemStack itemStack) {}

                    @Override
                    public void setChanged() {}

                    @Override
                    public boolean stillValid(Player player) {
                        return false;
                    }
                }, w);
        if (match.isEmpty()) return Optional.empty();
        return Optional.of(match.get().getResultItem(w.registryAccess()));
        *///?}
    }

    public static boolean checkCollisionRecipes(ServerLevel w, ItemEntity itemEntity,
                                             RecipeType<CollisionCraftingRecipe> recipeType,
                                             //? if >1.21 {
                                             Function<Item, Optional<DataComponentPatch>> componentFunction
                                             //?} else {
                                             /*Function<Item, Optional<CompoundTag>> componentFunction
                                             *///?}
    ) {
        if (itemEntity.isRemoved()) return false;
        ItemStack itemStack = itemEntity.getItem();
        ItemStack resStack = getResultNoMetadata(w, itemStack, recipeType).orElse(null);
        if (resStack == null) return false;
        if (resStack.is(itemStack.getItem())) resStack = itemStack.copy();
        componentFunction.apply(resStack.getItem()).ifPresent(
                //? if >1.21 {
                resStack::applyComponentsAndValidate
                 //?} else {
                /*resStack::setTag
                *///?}
        );

        Vec3 v = itemEntity.getDeltaMovement();
        ItemEntity result = new ItemEntity(w, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
                resStack.copyWithCount(itemEntity.getItem().getCount()),
                -v.x, -v.y, -v.z);
        w.addFreshEntity(result);
        result.setPortalCooldown(20);
        itemEntity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
        return true;
    }

    public static void registerModelPredicates() {
        //? if <1.21.4 {
        /*ItemPropertiesRegistry.register(ModItems.TRANSFINITE_KEY.get(), InfinityMethods.getId("key"), (stack, world, entity, seed) -> {
            ResourceLocation id = ModItems.TRANSFINITE_KEY.get().getDestination(stack);
            String s = id.toString();
            if (s.contains("infinity:generated_")) return 0.01f;
            return switch(s) {
                case InfinityMethods.ofRandomDim -> 0.02f;
                case "minecraft:the_end" -> 0.03f;
                case "infinity:pride" -> 0.04f;
                default -> 0;
            };
        });
        ItemPropertiesRegistry.register(ModItems.BIOME_BOTTLE_ITEM.get(), InfinityMethods.getId("bottle"),
                (stack, world, entity, seed) -> {
                    int charge = BiomeBottleBlock.getCharge(stack);
                    return Mth.clamp(charge / 1000.0f, 0f, 1f);
                });
        ItemPropertiesRegistry.register(ModItems.F4.get(), InfinityMethods.getId("f4"),
                (stack, world, entity, seed) -> {
                    ResourceLocation id = ModItems.F4.get().getDestination(stack);
                    if (id == null) return 0.0f;
                    if (id.toString().equals(InfinityMethods.ofRandomDim)) return 0.02f;
                    return 0.01f;
                });
        *///?}
    }
}
