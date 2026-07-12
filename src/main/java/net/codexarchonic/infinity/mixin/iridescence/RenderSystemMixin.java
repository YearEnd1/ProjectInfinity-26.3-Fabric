package net.codexarchonic.infinity.mixin.iridescence;

//? if >1.21.2 {
import com.mojang.blaze3d.systems.RenderPass;
import net.codexarchonic.infinity.iridescence.IridescentUniform;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?}
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {
    //? if >1.21.2 {
    @Inject(method = "bindDefaultUniforms", at = @At("TAIL"))
    private static void inj(RenderPass renderPass, CallbackInfo ci) {
        IridescentUniform.trySet(renderPass);
    }
    //?}
}
