package net.lerariemann.infinity.item.function;

import net.lerariemann.infinity.block.custom.BiomeBottleBlock;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.registry.core.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class BiomeBottleCombiningRecipe extends CustomRecipe {
    public BiomeBottleCombiningRecipe(CraftingBookCategory craftingRecipeCategory) {
        super(craftingRecipeCategory);
    }

    public boolean matches(CraftingInput craftingRecipeInput, Level world) {
        ItemStack stack1 = null;
        boolean bl = true;
        int charge = 0;
        for (int k = 0; k < craftingRecipeInput.size(); k++) {
            if (!bl) return false;
            ItemStack itemStack = craftingRecipeInput.getItem(k);
            if (!itemStack.isEmpty()) {
                if (Block.byItem(itemStack.getItem()) instanceof BiomeBottleBlock) {
                    charge += BiomeBottleBlock.getCharge(itemStack);
                    if (stack1 == null) {
                        stack1 = itemStack;
                        bl = !BiomeBottleBlock.isEmpty(stack1);
                    }
                    else bl = !BiomeBottleBlock.isEmpty(itemStack)
                            && BiomeBottleBlock.getBiome(stack1).equals(BiomeBottleBlock.getBiome(itemStack));
                }
                else return false;
            }
        }
        return bl && (stack1 != null) && (charge < BiomeBottleBlock.maxAllowedCharge);
    }

    public @NotNull ItemStack assemble(CraftingInput craftingRecipeInput, HolderLookup.Provider wrapperLookup) {
        ItemStack stack1 = ItemStack.EMPTY;
        int i = 0;
        int charge = 0;
        for (int k = 0; k < craftingRecipeInput.size(); k++) {
            ItemStack itemStack = craftingRecipeInput.getItem(k);
            if (!itemStack.isEmpty()) {
                if (Block.byItem(itemStack.getItem()) instanceof BiomeBottleBlock) {
                    if (stack1 == ItemStack.EMPTY) stack1 = itemStack;
                    charge += BiomeBottleBlock.getCharge(itemStack);
                    i += 1;
                }
            }
        }
        if (i < 2) return ModItems.BIOME_BOTTLE_ITEM.get().getDefaultInstance();
        ItemStack result = stack1.copy();
        BiomeBottleBlock.updateCharge(result, charge);
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width*height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModItemFunctions.BIOME_BOTTLE_COMBINING.get();
    }
}
