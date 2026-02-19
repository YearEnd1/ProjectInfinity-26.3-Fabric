package net.lerariemann.infinity.item;

import net.lerariemann.infinity.registry.core.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class StarOfLangItem extends Item {
    public StarOfLangItem(Properties settings) {
        super(settings);
    }

    //? if >1.21.2 && neoforge {
    /*@Override
    public ItemStack getCraftingRemainder(ItemStack itemStack) {
        return new ItemStack(ModItems.STAR_OF_LANG.get());
    }
    *///?}

    //? if neoforge && <1.21.2 {
    /*@Override
    public @NonNull ItemStack getCraftingRemainingItem(@NonNull ItemStack itemStack) {
        return new ItemStack(ModItems.STAR_OF_LANG.get());
    }

    @Override
    public boolean hasCraftingRemainingItem(@NonNull ItemStack stack) {
        return true;
    }
    *///?}

    //? if fabric {
    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return ModItems.STAR_OF_LANG.get().getDefaultInstance();
    }
    //?}


}
