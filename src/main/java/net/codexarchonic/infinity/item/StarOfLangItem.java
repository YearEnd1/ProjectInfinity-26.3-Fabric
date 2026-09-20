package net.codexarchonic.infinity.item;

import net.codexarchonic.infinity.registry.core.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
//? if >26 {
/*import cc.cassian.rrv.backport.ItemStackTemplate;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
*///?}

public class StarOfLangItem extends Item {
    public StarOfLangItem(Properties settings) {
        super(settings);
    }

    //? if >26 {
    /*@Override
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemStack stack) {
        return new ItemStackTemplate(ModItems.STAR_OF_LANG.get());
    }
    *///?} else if >1.21.2 && neoforge {
    /*@Override
    public ItemStack getCraftingRemainder(ItemStack itemStack) {
        return new ItemStack(ModItems.STAR_OF_LANG.get());
    }
    *///?} else if neoforge && <1.21.2 {
    /*@Override
    public @NonNull ItemStack getCraftingRemainingItem(@NonNull ItemStack itemStack) {
        return new ItemStack(ModItems.STAR_OF_LANG.get());
    }

    @Override
    public boolean hasCraftingRemainingItem(@NonNull ItemStack stack) {
        return true;
    }
    *///?} else if fabric {
    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return ModItems.STAR_OF_LANG.get().getDefaultInstance();
    }
    //?}


}
