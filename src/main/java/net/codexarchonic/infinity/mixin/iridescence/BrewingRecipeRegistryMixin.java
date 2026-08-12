package net.codexarchonic.infinity.mixin.iridescence;

import net.codexarchonic.infinity.registry.core.ModItems;
import net.codexarchonic.infinity.util.VersionMethods;
//? if >1.21 {
//?}
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionBrewing.class)
public class BrewingRecipeRegistryMixin {
    @Inject(method = "isIngredient", at = @At("RETURN"), cancellable = true)
    //? if <1.21
    //private static
    void chromaticMatterIsIngredient(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || stack.is(ModItems.CHROMATIC_MATTER.get()));
    }

    @Inject(method="hasPotionMix", at = @At("RETURN"), cancellable = true)
    //? if <1.21
    //private static
    void chromaticMatterHasMix(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        if (ingredient.is(ModItems.CHROMATIC_MATTER.get())) {
            var optional = VersionMethods.getOrEmptyPotion(input);
            if (optional.isPresent() && VersionMethods.isPotion(optional.get(), Potions.AWKWARD)) cir.setReturnValue(true);
        }
    }

    @Inject(method = "mix", at = @At("HEAD"), cancellable = true)
    //? if <1.21
    //private static
    void craftChromaticPotion(ItemStack ingredient, ItemStack input, CallbackInfoReturnable<ItemStack> cir) {
        if (!input.isEmpty() && ingredient.is(ModItems.CHROMATIC_MATTER.get())) {
            var optional = VersionMethods.getOrEmptyPotion(input);
            if (optional.isPresent() && VersionMethods.isPotion(optional.get(), Potions.AWKWARD))
                cir.setReturnValue(ModItems.CHROMATIC_POTION.get().getDefaultInstance());
        }
    }
}
