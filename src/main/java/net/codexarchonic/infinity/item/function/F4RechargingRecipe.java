package net.codexarchonic.infinity.item.function;

import net.codexarchonic.infinity.item.F4Item;
import net.codexarchonic.infinity.registry.core.ModComponentTypes;
import net.codexarchonic.infinity.registry.core.ModItemFunctions;
import net.codexarchonic.infinity.registry.core.ModItems;
import net.codexarchonic.infinity.util.VersionMethods;

import net.minecraft.core.NonNullList;
//? if >1.21 {
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.core.HolderLookup;
//?}
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class F4RechargingRecipe extends CustomRecipe {
    public F4RechargingRecipe(
            //? if <1.21
            //ResourceLocation id,
            CraftingBookCategory craftingRecipeCategory) {
        super(
                //? if <1.21
                //id,
                craftingRecipeCategory);
    }

    public boolean matches(
            //~ if >1.21 'CraftingContainer'->'CraftingInput' {
            CraftingInput
             //~}
            craftingRecipeInput, Level world) {
        boolean foundF4 = false;
        //? if >1.21 {
        int size = craftingRecipeInput.size();
         //?} else {
        /*int size = craftingRecipeInput.getContainerSize();
        *///?}
        for (int k = 0; k < size; k++) {
            ItemStack itemStack = craftingRecipeInput.getItem(k);
            if (itemStack.is(ModItems.F4.get())) {
                if (foundF4) return false;
                foundF4 = true;
            }
            else if (!itemStack.is(Items.OBSIDIAN)) return false;
        }
        return foundF4;
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
            wrapperLookup) {
        ItemStack f4 = null;
        int i = 0;
        //? if >1.21 {
        int size = craftingRecipeInput.size();
         //?} else {
        /*int size = craftingRecipeInput.getContainerSize();
        *///?}
        for (int k = 0; k < size; k++) {
            ItemStack itemStack = craftingRecipeInput.getItem(k);
            if (itemStack.is(ModItems.F4.get())) f4 = itemStack;
            else if (!itemStack.isEmpty()) i++;
        }
        assert f4 != null;
        int charge = F4Item.getCharge(f4);
        if (i == 0) {
            if (charge > 0) return Items.OBSIDIAN.getDefaultInstance().copyWithCount(Math.min(charge, 64)); //"removing obsidian" recipe
        }
        ItemStack result = f4.copyWithCount(1);
        VersionMethods.apply(result, ModComponentTypes.CHARGE, charge+i);
        if (charge + i == 0) { //"clearing data" recipe
            VersionMethods.remove(result, ModComponentTypes.DESTINATION);
            VersionMethods.remove(result, ModComponentTypes.COLOR);
        }
        return result;
    }

    @Override
    public @NonNull NonNullList<ItemStack> getRemainingItems(
            //? if >1.21 {
            CraftingInput
             //?} else {
            /*CraftingContainer
            *///?}
            craftingRecipeInput) {
        //? if >1.21 {
        int size = craftingRecipeInput.size();
        //?} else {
        /*int size = craftingRecipeInput.getContainerSize();
        *///?}
        NonNullList<ItemStack> defaultedList = NonNullList.withSize(size, ItemStack.EMPTY);
        ItemStack f4 = null;
        int f4pos = 0;
        for (int i = 0; i < defaultedList.size(); i++) {
            ItemStack itemStack = craftingRecipeInput.getItem(i);
            if (itemStack.is(ModItems.F4.get())) {
                f4 = itemStack;
                f4pos = i;
            }
            else if (!itemStack.isEmpty()) return defaultedList;//"adding obsidian" recipe
        }
        assert f4 != null;
        int charge = F4Item.getCharge(f4);
        if (charge == 0) return defaultedList; //"clearing data" recipe
        charge = Math.max(charge - 64, 0);
        ItemStack result = f4.copyWithCount(1);
        VersionMethods.apply(result, ModComponentTypes.CHARGE, charge);
        defaultedList.set(f4pos, result);//"adding obsidian" recipe
        return defaultedList;
    }

    //? if <1.21.2 {
    /*@Override
    public boolean canCraftInDimensions(int width, int height) {
        return width*height >= 2;
    }
    *///?}

    @Override
    public @NonNull RecipeSerializer<F4RechargingRecipe> getSerializer() {
        return ModItemFunctions.F4_RECHARGING.get();
    }
}

