package net.lerariemann.infinity.item.function;

import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.registry.core.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ChromaCarpetRecipe extends CustomRecipe {
    public ChromaCarpetRecipe(CraftingBookCategory craftingRecipeCategory) {
        super(craftingRecipeCategory);
    }

    public boolean matches(CraftingInput craftingRecipeInput, Level world) {
        Integer color = null;
        int k_first = 0;
        boolean carpetDone = false;
        int w = craftingRecipeInput.width();
        for (int k = 0; k < craftingRecipeInput.size(); k++) {
            ItemStack itemStack = craftingRecipeInput.getItem(k);
            if (itemStack.is(ModItems.CHROMATIC_WOOL.get())) {
                if (carpetDone) return false;
                int newColor = itemStack.getOrDefault(ModComponentTypes.COLOR.get(), 0);
                if (color == null) {
                    color = newColor;
                    if (k % w == w - 1) return false;
                    k_first = k;
                }
                else {
                    if (k - k_first != 1 || color != newColor) return false;
                    carpetDone = true;
                }
            }
            else if (!itemStack.isEmpty()) return false;
        }
        return carpetDone;
    }

    public @NotNull ItemStack assemble(CraftingInput craftingRecipeInput, HolderLookup.Provider wrapperLookup) {
        int color = 0;
        for (int k = 0; k < craftingRecipeInput.size(); k++) {
            ItemStack itemStack = craftingRecipeInput.getItem(k);
            if (itemStack.is(ModItems.CHROMATIC_WOOL.get())) {
                color = itemStack.getOrDefault(ModComponentTypes.COLOR.get(), 0);
                break;
            }
        }
        ItemStack result = ModItems.CHROMATIC_CARPET.get().getDefaultInstance();
        result.applyComponents(DataComponentMap.builder() //"adding obsidian" recipe
                .set(ModComponentTypes.COLOR.get(), color)
                .build());
        return result.copyWithCount(3);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 && height >= 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModItemFunctions.CARPET.get();
    }
}

