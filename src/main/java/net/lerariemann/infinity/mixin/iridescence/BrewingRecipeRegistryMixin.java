package net.lerariemann.infinity.mixin.iridescence;

import net.lerariemann.infinity.registry.core.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PotionBrewing.class)
public class BrewingRecipeRegistryMixin {
    @Inject(method = "isIngredient", at = @At("RETURN"), cancellable = true)
    void inj(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || stack.is(ModItems.CHROMATIC_MATTER.get()));
    }

    @Inject(method="hasPotionMix", at = @At("RETURN"), cancellable = true)
    void inj(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        if (ingredient.is(ModItems.CHROMATIC_MATTER.get())) {
            Optional<Holder<Potion>> optional = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion();
            if (optional.isPresent() && optional.get().is(ResourceLocation.withDefaultNamespace("awkward"))) cir.setReturnValue(true);
        }
    }

    @Inject(method = "mix", at = @At("HEAD"), cancellable = true)
    void inj2(ItemStack ingredient, ItemStack input, CallbackInfoReturnable<ItemStack> cir) {
        if (!input.isEmpty() && ingredient.is(ModItems.CHROMATIC_MATTER.get())) {
            Optional<Holder<Potion>> optional = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion();
            if (optional.isPresent() && optional.get().is(ResourceLocation.withDefaultNamespace("awkward")))
                cir.setReturnValue(ModItems.CHROMATIC_POTION.get().getDefaultInstance());
        }
    }
}
