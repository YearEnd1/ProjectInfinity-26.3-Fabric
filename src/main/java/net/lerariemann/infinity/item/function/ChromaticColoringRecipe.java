package net.lerariemann.infinity.item.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.registry.core.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
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
    public boolean matches(CraftingInput input, Level world) {
        boolean foundChroma = false;
        for (int k = 0; k < input.size(); k++) {
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
    public @NotNull ItemStack assemble(CraftingInput input, HolderLookup.Provider lookup) {
        ItemStack chroma = ItemStack.EMPTY;
        int i = 0;
        for (int k = 0; k < input.size(); k++) {
            ItemStack itemStack = input.getItem(k);
            if (itemStack.is(ModItems.CHROMATIC_MATTER.get())) chroma = itemStack;
            else if (!itemStack.isEmpty()) {
                i++;
            }
        }
        assert !chroma.isEmpty();
        if (i > 0) {
            ItemStack result = output.copyWithCount(i);
            result.applyComponents(chroma.getComponents());
            return result;
        }
        else return chroma.getItem().getDefaultInstance();
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInput craftingRecipeInput) {
        NonNullList<ItemStack> defaultedList = NonNullList.withSize(craftingRecipeInput.size(), ItemStack.EMPTY);
        int j = 0;
        for (int i = 0; i < defaultedList.size(); i++) {
            ItemStack itemStack = craftingRecipeInput.getItem(i);
            if (itemStack.is(ModItems.CHROMATIC_MATTER.get()))
                defaultedList.set(i, itemStack.copyWithCount(1));
            else if (!itemStack.isEmpty()) j++;
        }
        return j > 0 ? defaultedList : NonNullList.withSize(craftingRecipeInput.size(), ItemStack.EMPTY);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width*height > 1;
    }

    public Ingredient getInput() {
        return input;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider registriesLookup) {
        return output;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModItemFunctions.CHROMATIC_COLORING.get();
    }

    public record Serializer(BiFunction<Ingredient, ItemStack, ChromaticColoringRecipe> func)
            implements RecipeSerializer<ChromaticColoringRecipe> {

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
    }
}
