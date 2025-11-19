package net.lerariemann.infinity.item.function;

import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
//? if >1.21 {
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingInput;
//?} else {
/*import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
*///?}
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class ChromaticColoringRecipe implements CraftingRecipe {
    private final Ingredient input;
    private final ItemStack output;

    public ChromaticColoringRecipe(Ingredient input, ItemStack output) {
        this.input = input;
        this.output = output;
    }
    @Override
    public @NotNull CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public boolean matches(
            //? if >1.21 {
            CraftingInput
            //?} else {
            /*CraftingContainer
            *///?}
                    input, Level world) {
        boolean foundChroma = false;
        for (int k = 0; k < input.
                //? if >1.21 {
                size
                //?} else {
                /*getContainerSize
                 *///?}
                (); k++) {
            ItemStack itemStack = input.getItem(k);
            if (itemStack.is(ModItems.CHROMATIC_MATTER.get())) {
                if (foundChroma) return false;
                foundChroma = true;
            }
            else if (!itemStack.isEmpty()) {
                if (!this.input.test(itemStack)) return false;
            }
        }
        return foundChroma;
    }

    public enum Type implements RecipeType<ChromaticColoringRecipe> {
        INSTANCE
    }
    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public @NotNull ItemStack assemble(
            //? if >1.21 {
            CraftingInput
             //?} else {
            /*CraftingContainer
            *///?}
            input,
            //? if >1.21 {
            HolderLookup.Provider
            //?} else {
            /*RegistryAccess
            *///?}
            lookup) {
        ItemStack chroma = ItemStack.EMPTY;
        int i = 0;
        for (int k = 0; k < input.
                //? if >1.21 {
                size
                 //?} else {
                /*getContainerSize
                *///?}
                (); k++) {
            ItemStack itemStack = input.getItem(k);
            if (itemStack.is(ModItems.CHROMATIC_MATTER.get())) chroma = itemStack;
            else if (!itemStack.isEmpty()) {
                i++;
            }
        }
        assert !chroma.isEmpty();
        if (i > 0) {
            ItemStack result = output.copyWithCount(i);
            //? if >1.21 {
            result.applyComponents(chroma.getComponents());
            //?} else {
            /*result.setTag(chroma.getTag());
            *///?}
            return result;
        }
        else return chroma.getItem().getDefaultInstance();
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(
            //? if >1.21 {
            CraftingInput
            //?} else {
            /*CraftingContainer
            *///?}
            input) {
        int size = input.
                //? if >1.21 {
                size
                 //?} else {
                /*getContainerSize
                *///?}
                ();
        NonNullList<ItemStack> defaultedList = NonNullList.withSize(size, ItemStack.EMPTY);
        int j = 0;
        for (int i = 0; i < defaultedList.size(); i++) {
            ItemStack itemStack = input.getItem(i);
            if (itemStack.is(ModItems.CHROMATIC_MATTER.get()))
                defaultedList.set(i, itemStack.copyWithCount(1));
            else if (!itemStack.isEmpty()) j++;
        }
        return j > 0 ? defaultedList : NonNullList.withSize(size, ItemStack.EMPTY);
    }

    //? if <1.21 {
    /*@Override
    public ResourceLocation getId() {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(output.getItem());
        return VersionMethods.id(id.getNamespace(), id.getPath()+"_chromatic");
    }
    *///?}

    //? if <1.21.2 {
    /*@Override
    public boolean canCraftInDimensions(int width, int height) {
        return width*height > 1;
    }
    *///?}

    public Ingredient getInput() {
        return input;
    }

//    @Override
    public @NotNull ItemStack getResultItem(
            //? if =1.21.1 {
            /*HolderLookup.Provider registriesLookup
            *///?} else if =1.20.1 {
            /*RegistryAccess registryAccess
            *///?}
    ) {
        return output;
    }

    @Override
    public @NotNull RecipeSerializer<ChromaticColoringRecipe> getSerializer() {
        return ModItemFunctions.CHROMATIC_COLORING.get();
    }

    public record Serializer(BiFunction<Ingredient, ItemStack, ChromaticColoringRecipe> func)
            implements RecipeSerializer<ChromaticColoringRecipe> {

        //? if >1.21 {
        @Override
        public @NotNull MapCodec<ChromaticColoringRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                            Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(recipe -> recipe.input),
                            ItemStack.STRICT_CODEC.fieldOf("output").forGetter(recipe -> recipe.output))
                    .apply(instance, func));
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, ChromaticColoringRecipe> streamCodec() {
            return StreamCodec.of(this::write, this::read);
        }

        private ChromaticColoringRecipe read(RegistryFriendlyByteBuf buf) {
            Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
            return func.apply(input, output);
        }

        private void write(RegistryFriendlyByteBuf buf, ChromaticColoringRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.input);
            ItemStack.STREAM_CODEC.encode(buf, recipe.output);
        }
        //?} else {
        /*@Override
        public ChromaticColoringRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getNonNull(json, "input"));
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            return func.apply(input, output);
        }

        @Override
        public ChromaticColoringRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            ItemStack output = buf.readItem();
            return func.apply(input, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ChromaticColoringRecipe recipe) {
            recipe.input.toNetwork(buf);
            buf.writeItem(recipe.output);
        }
        *///?}
    }
}
