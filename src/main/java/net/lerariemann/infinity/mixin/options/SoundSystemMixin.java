package net.lerariemann.infinity.mixin.options;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.lerariemann.infinity.options.InfinityOptions;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SoundEngine.class)
public class SoundSystemMixin {
    @ModifyExpressionValue(method = "calculatePitch", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/sounds/SoundInstance;getPitch()F"))
    float inj(float original) {
        InfinityOptions options = InfinityOptions.ofClient();
        return (!options.isEmpty()) ? options.getSoundPitch().apply(original) : original;
    }
}
