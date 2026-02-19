package net.lerariemann.infinity.item.function;

import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.core.RegistryAccess;
//? if >1.21 {
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.core.HolderLookup;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;

import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class ChromaCarpetRecipe extends CustomRecipe {
    public ChromaCarpetRecipe(
            //? if <1.21
            /*ResourceLocation id,*/
            CraftingBookCategory craftingRecipeCategory) {
        super(
                //? if <1.21
                /*id,*/
                craftingRecipeCategory);
    }

    public boolean matches(
            //? if >1.21 {
            CraftingInput
             //?} else {
            /*CraftingContainer
            *///?}
            craftingRecipeInput, Level world) {
        Integer color = null;
        int k_first = 0;
        boolean carpetDone = false;
        //? if >1.21 {
        int w = craftingRecipeInput.width();
        int size = craftingRecipeInput.size();
        //?} else {
        /*int w = craftingRecipeInput.getWidth();
        int size = craftingRecipeInput.getContainerSize();
        *///?}
        for (int k = 0; k < size; k++) {
            ItemStack itemStack = craftingRecipeInput.getItem(k);
            if (itemStack.is(ModItems.CHROMATIC_WOOL.get())) {
                if (carpetDone) return false;
                int newColor = VersionMethods.getOrDefaultInt(itemStack, ModComponentTypes.COLOR, 0);
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

    public @NonNull ItemStack assemble(
            //? if >1.21 {
            CraftingInput
             //?} else {
            /*CraftingContainer
            *///?}
            craftingRecipeInput,
            //? if >1.21 {
            HolderLookup.Provider
            //?} else {
            /*RegistryAccess
            *///?}
            registryAccess
    ) {
        int color = 0;
        //? if >1.21 {
        
        int size = craftingRecipeInput.size();
        //?} else {
        /*int size = craftingRecipeInput.getContainerSize();
        *///?}
        for (int k = 0; k < size; k++) {
            ItemStack itemStack = craftingRecipeInput.getItem(k);
            if (itemStack.is(ModItems.CHROMATIC_WOOL.get())) {
                color = VersionMethods.getOrDefaultInt(itemStack, ModComponentTypes.COLOR, 0);
                break;
            }
        }
        ItemStack result = ModItems.CHROMATIC_CARPET.get().getDefaultInstance();
        VersionMethods.apply(result, ModComponentTypes.COLOR, color);
        return result.copyWithCount(3);
    }

    //? if <1.21.2 {
    /*@Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 && height >= 1;
    }
    *///?}

    @Override
    public RecipeSerializer<ChromaCarpetRecipe> getSerializer() {
        return ModItemFunctions.CARPET.get();
    }
}

