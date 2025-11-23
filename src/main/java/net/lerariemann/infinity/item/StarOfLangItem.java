package net.lerariemann.infinity.item;

import net.lerariemann.infinity.registry.core.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StarOfLangItem extends Item {
    public StarOfLangItem(Properties settings) {
        super(settings);
    }

    //? if >1.21.2 {
    @Override
    public ItemStack getCraftingRemainder(ItemStack itemStack) {
        return new ItemStack(ModItems.STAR_OF_LANG.get());
    }
    //?}

    //? if neoforge && <1.21.2 {
    /*@Override
    public @NotNull ItemStack getCraftingRemainingItem(@NotNull ItemStack itemStack) {
        return new ItemStack(ModItems.STAR_OF_LANG.get());
    }

    @Override
    public boolean hasCraftingRemainingItem(@NotNull ItemStack stack) {
        return true;
    }
    *///?}

    //? if fabric {
    /*@Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return ModItems.STAR_OF_LANG.get().getDefaultInstance();
    }
    *///?}


}
