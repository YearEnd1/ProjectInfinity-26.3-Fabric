package net.lerariemann.infinity.mixin.eiv;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
//? >1.21.4
import de.crafty.eiv.common.extra.FluidItemSpecialRenderer;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import java.awt.*;

//? if >1.21.4 {
@Pseudo
@Mixin(FluidItemSpecialRenderer.class)
// EIV only tints fluids if they are water. This provides the appropriate tinting to Iridescence.
public class FluidItemRendererMixin {

	@WrapOperation(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IIZ)V", at = @At(value = "INVOKE", target = "Ljava/awt/Color;getRed()I"))
	int fixRed(Color instance, Operation<Integer> original, @Local ItemStack stack) {
		if (stack.is(ModBlocks.IRIDESCENCE.get().asItem())) {
			return new Color(Iridescence.getTimeBasedColor()).getRed();
		}
		return original.call(instance);
	}

	@WrapOperation(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IIZ)V", at = @At(value = "INVOKE", target = "Ljava/awt/Color;getBlue()I"))
	int fixBlue(Color instance, Operation<Integer> original, @Local ItemStack stack) {
		if (stack.is(ModBlocks.IRIDESCENCE.get().asItem())) {
			return new Color(Iridescence.getTimeBasedColor()).getBlue();
		}
		return original.call(instance);
	}

	@WrapOperation(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IIZ)V", at = @At(value = "INVOKE", target = "Ljava/awt/Color;getGreen()I"))
	int fixGreen(Color instance, Operation<Integer> original, @Local ItemStack stack) {
		if (stack.is(ModBlocks.IRIDESCENCE.get().asItem())) {
			return new Color(Iridescence.getTimeBasedColor()).getGreen();
		}
		return original.call(instance);
	}
}
//?} else {
/*@Mixin(Iridescence.class)
public class FluidItemRendererMixin {}
*///?}