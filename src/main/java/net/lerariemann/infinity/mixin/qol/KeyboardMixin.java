package net.lerariemann.infinity.mixin.qol;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KeyboardHandler.class)
public class KeyboardMixin {
    /* disable f4 being a hardcoded toggle postprocessing shaders key, as the mod makes it rebindable */
    @WrapOperation(method="keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;togglePostEffect()V"))
    void disableF4(GameRenderer instance, Operation<Void> original) {
    }
}
