package net.codexarchonic.infinity.mixin.rrv;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.codexarchonic.infinity.iridescence.Iridescence;
import net.codexarchonic.infinity.registry.core.ModBlocks;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(targets = "cc.cassian.rrv.client.extra.FluidItemSpecialRenderer")
// RRV only tints fluids if they are water. This provides the appropriate tinting to Iridescence.
public class FluidItemRendererMixin {
	//? if <26 {
	@WrapMethod(method = "getColor")
	private static int fixColor(Fluid fluid, Operation<Integer> original) {
		if (fluid.defaultFluidState().createLegacyBlock().is(ModBlocks.IRIDESCENCE.get())) {
			return Iridescence.getTimeBasedColor();
		}
		return original.call(fluid);
	}
	//?}
}