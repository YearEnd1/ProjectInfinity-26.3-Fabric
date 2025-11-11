package net.lerariemann.infinity.item.function;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
//? if >1.21 {
/*import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
*///?} else {
import net.minecraft.world.ticks.ContainerSingleItem;
//?}
import net.minecraft.world.level.Level;

@MethodsReturnNonnullByDefault
public abstract class CollisionCraftingRecipe implements Recipe<
        //? if >1.21 {
        /*SingleRecipeInput
        *///?} else {
        ContainerSingleItem
        //?}
        > {
    private final Ingredient input;
    private final ItemStack output;
    private final String lore;

    public CollisionCraftingRecipe(Ingredient input, ItemStack output, String lore) {
        this.input = input;
        this.output = output;
        this.lore = lore;
    }

    @Override
    public boolean matches(
            //? if >1.21 {
            /*SingleRecipeInput
             *///?} else {
            ContainerSingleItem
            //?}
            input, Level world) {
        if (world.isClientSide()) return false;
        return this.input.test(input.
                //? if >1.21 {
                /*item
                *///?} else {
                getFirstItem
                //?}
                ());
    }

    @Override
    public ItemStack assemble(
            //? if >1.21 {
            /*SingleRecipeInput
             *///?} else {
            ContainerSingleItem
            //?}
            input,
            //? if >1.21 {
            /*HolderLookup.Provider
             *///?} else {
            RegistryAccess
            //?}
            lookup) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(
            //? if >1.21 {
            /*HolderLookup.Provider registriesLookup
            *///?} else {
            RegistryAccess registryAccess
            //?}
    ) {
        return output.copy();
    }

    public Ingredient getInput() {
        return input;
    }
    public String getLore() { return lore; }

    public enum Type implements RecipeType<CollisionCraftingRecipe> {
        PORTAL,
        IRIDESCENCE
    }

    public record Serializer(Function3<Ingredient, ItemStack, String, CollisionCraftingRecipe> func)
            implements RecipeSerializer<CollisionCraftingRecipe> {

        //? if >1.21 {
        /*@Override
        public MapCodec<CollisionCraftingRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                            Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(recipe -> recipe.input),
                            ItemStack.STRICT_CODEC.fieldOf("output").forGetter(recipe -> recipe.output),
                            ExtraCodecs.NON_EMPTY_STRING.fieldOf("lore").forGetter(recipe -> recipe.lore))
                    .apply(instance, func));
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CollisionCraftingRecipe> streamCodec() {
            return StreamCodec.of(this::write, this::read);
        }

        private CollisionCraftingRecipe read(RegistryFriendlyByteBuf buf) {
            Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
            String str = ByteBufCodecs.STRING_UTF8.decode(buf);
            return func.apply(input, output, str);
        }

        private void write(RegistryFriendlyByteBuf buf, CollisionCraftingRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.input);
            ItemStack.STREAM_CODEC.encode(buf, recipe.output);
            ByteBufCodecs.STRING_UTF8.encode(buf, recipe.lore);
        }
        *///?} else {
        @Override
        public CollisionCraftingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getNonNull(json, "input"));
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            String lore = GsonHelper.getAsString(json, "lore");
            return func.apply(input, output, lore);
        }

        @Override
        public CollisionCraftingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            ItemStack output = buf.readItem();
            String lore = buf.readUtf();
            return func.apply(input, output, lore);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CollisionCraftingRecipe recipe) {
            recipe.input.toNetwork(buf);
            buf.writeItem(recipe.output);
            buf.writeUtf(recipe.lore);
        }
        //?}
    }

    public static class OfPortal extends CollisionCraftingRecipe {
        ItemStack output;
        public OfPortal(Ingredient input, ItemStack output, String lore) {
            super(input, output, lore);
            this.output = output;
        }

        //? if <1.21 {
        @Override
        public ResourceLocation getId() {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(output.getItem());
            return VersionMethods.id(id.getNamespace(), id.getPath()+"_of_iridescence");
        }
        //?}

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ModItemFunctions.PORTAL_CRAFTING.get();
        }
        @Override
        public RecipeType<?> getType() {
            return ModItemFunctions.PORTAL_CRAFTING_TYPE.get();
        }
    }

    public static class OfIridescence extends CollisionCraftingRecipe {
        ItemStack output;
        public OfIridescence(Ingredient input, ItemStack output, String lore) {
            super(input, output, lore);
            this.output = output;
        }

        //? if <1.21 {
        @Override
        public ResourceLocation getId() {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(output.getItem());
            return VersionMethods.id(id.getNamespace(), id.getPath()+"_of_iridescence");
        }
        //?}

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ModItemFunctions.IRIDESCENCE_CRAFTING.get();
        }
        @Override
        public RecipeType<?> getType() {
            return ModItemFunctions.IRIDESCENCE_CRAFTING_TYPE.get();
        }
    }
}
