package net.codexarchonic.infinity.item.function;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.codexarchonic.infinity.registry.core.ModItemFunctions;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
//? if >1.21 {
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
//?} else {
/*import net.minecraft.world.ticks.ContainerSingleItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.GsonHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
*///?}
import net.minecraft.world.level.Level;

public abstract class CollisionCraftingRecipe
        //? if >1.21 {
        implements Recipe<SingleRecipeInput>
        //?} else {
        /*implements Recipe<ContainerSingleItem>
        *///?}
         {
    //? if =1.20.1 {
    /*protected final ResourceLocation id;
    *///?}
    protected final Ingredient input;
    protected final ItemStack output;
    protected final String lore;

    public CollisionCraftingRecipe(
            //? if =1.20.1 {
            /*ResourceLocation id,
            *///?}
            Ingredient input, ItemStack output, String lore) {
        //? if =1.20.1 {
        /*this.id = id;
        *///?}
        this.input = input;
        this.output = output;
        this.lore = lore;
    }

    @Override
    public boolean matches(
            //? if >1.21 {
            SingleRecipeInput
             //?} else {
            /*ContainerSingleItem
            *///?}
            input, Level world) {
        if (world.isClientSide()) return false;
        return this.input.test(input.
                //? if >1.21 {
                item
                //?} else {
                /*getFirstItem
                *///?}
                ());
    }

    @Override
    public ItemStack assemble(
            //? if >1.21 {
            SingleRecipeInput
             //?} else {
            /*ContainerSingleItem
            *///?}
            input,
            //? if >1.21 {
            HolderLookup.Provider
             //?} else {
            /*RegistryAccess
            *///?}
            lookup) {
        return output;
    }

    //? if <1.21.2 {
    /*@Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }
    *///?}

    //? if <1.21.2
    //@Override
    public ItemStack getResultItem(
            //? if =1.21.1 {
            /*HolderLookup.Provider registriesLookup
            *///?} else if =1.20.1 {
            /*RegistryAccess registryAccess
            *///?}
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

    public record Serializer(
            //? if =1.20.1 {
            /*Function4<ResourceLocation, Ingredient, ItemStack, String, CollisionCraftingRecipe> func
             *///?} else {
            Function3<Ingredient, ItemStack, String, CollisionCraftingRecipe> func
            //?}
    ) implements RecipeSerializer<CollisionCraftingRecipe> {

        //? if >1.21 {
        @Override
        public MapCodec<CollisionCraftingRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                    //? if >1.21.2 {
                            Ingredient.CODEC
                    //?} else {
                            /*Ingredient.CODEC_NONEMPTY
                    *///?}
                            .fieldOf("input").forGetter(recipe -> recipe.input),
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
        //?} else {
        /*@Override
        public CollisionCraftingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getNonNull(json, "input"));
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            String lore;
            try {
                lore = GsonHelper.getAsString(json, "lore");
            } catch (Throwable e) {
                lore = "";
            }
            return func.apply(id, input, output, lore);
        }

        @Override
        public CollisionCraftingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ResourceLocation id2 = buf.readResourceLocation();
            Ingredient input = Ingredient.fromNetwork(buf);
            ItemStack output = buf.readItem();
            String lore = buf.readUtf();
            return func.apply(id2, input, output, lore);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CollisionCraftingRecipe recipe) {
            buf.writeResourceLocation(recipe.id);
            recipe.input.toNetwork(buf);
            buf.writeItem(recipe.output);
            buf.writeUtf(recipe.lore);
        }
        *///?}
    }

    public static class OfPortal extends CollisionCraftingRecipe {
        public OfPortal(
                //? if =1.20.1 {
                /*ResourceLocation id,
                 *///?}
                Ingredient input, ItemStack output, String lore) {
            super(
                    //? if =1.20.1 {
                    /*id,
                     *///?}
                    input, output, lore);
        }

        //? if <1.21 {
        /*@Override
        public ResourceLocation getId() {
            return this.id;
        }
        *///?}

        @Override
        public RecipeSerializer<? extends Recipe<
                //? if >1.21 {
                SingleRecipeInput
                 //?} else {
                /*ContainerSingleItem
                *///?}
                >> getSerializer() {
            return ModItemFunctions.PORTAL_CRAFTING.get();
        }
        @Override
        public RecipeType<? extends Recipe<
                //? if >1.21 {
                SingleRecipeInput
                 //?} else {
                /*ContainerSingleItem
                *///?}
                >> getType() {
            return ModItemFunctions.PORTAL_CRAFTING_TYPE.get();
        }
        //? if >1.21.2 {
        @Override
        public PlacementInfo placementInfo() {
            return PlacementInfo.create(getInput());
        }

        @Override
        public RecipeBookCategory recipeBookCategory() {
            return RecipeBookCategories.CRAFTING_MISC;
        }
        @Override
        public boolean isSpecial() {
            return true;
        }
        //?}
    }

    public static class OfIridescence extends CollisionCraftingRecipe {
        public OfIridescence(
                //? if =1.20.1 {
                /*ResourceLocation id,
                 *///?}
                Ingredient input, ItemStack output, String lore) {
            super(
                    //? if =1.20.1 {
                    /*id,
                     *///?}
                    input, output, lore);
        }

        //? if <1.21 {
        /*@Override
        public ResourceLocation getId() {
            return this.id;
        }
        *///?}

        @Override
        public RecipeSerializer<CollisionCraftingRecipe> getSerializer() {
            return ModItemFunctions.IRIDESCENCE_CRAFTING.get();
        }
        @Override
        public RecipeType<CollisionCraftingRecipe> getType() {
            return ModItemFunctions.IRIDESCENCE_CRAFTING_TYPE.get();
        }

        //? if >1.21.2 {
        @Override
        public PlacementInfo placementInfo() {
            return PlacementInfo.create(getInput());
        }

        @Override
        public RecipeBookCategory recipeBookCategory() {
            return RecipeBookCategories.CRAFTING_MISC;
        }

        @Override
        public boolean isSpecial() {
            return true;
        }

        //?}
    }
}
