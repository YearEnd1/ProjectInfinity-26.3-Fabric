package net.lerariemann.infinity.mixin.iridescence;

import net.lerariemann.infinity.util.loading.ShaderLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalTime;
import java.util.List;

@Mixin(PostChain.class)
public abstract class PostEffectProcessorMixin {
    //FIXME 1.21.10
    //? if <1.21.2 {
    /*@Shadow
    private float time;
    @Final
    @Shadow
    private String name;
    @Final
    @Shadow
    private List<PostPass> passes;


    @Shadow
    private float lastStamp;
    *///?}

    //? if =1.21.1 {
    /*@Shadow protected abstract void setFilterMode(int texFilter);

    @Shadow public abstract void setUniform(String name, float value);

    /^ Hook for the iridescence shader to receive the time uniform ^/
    @Inject(method="process", at = @At("HEAD"), cancellable = true)
    void inj(float tickDelta, CallbackInfo ci) {
        if (name.contains("infinity")) {
            setUniform("IridTimeSec", ((int)(LocalTime.now().toNanoOfDay() / 1000000)) / 1000.0f);
            setUniform("IridLevel", ShaderLoader.iridLevel.get());
            setUniform("IridProgress", (float)ShaderLoader.iridProgress.get());
            setUniform("IridDistortion", Minecraft.getInstance().options.screenEffectScale().get().floatValue());

            time += tickDelta;

            while (time > 2000.0F) {
                time -= 2000.0F;
            }

            int i = 9728;

            for (PostPass postEffectPass : passes) {
                int j = postEffectPass.getFilterMode();
                if (i != j) {
                    setFilterMode(j);
                    i = j;
                }

                postEffectPass.process(this.time / 2000.0F);
            }

            this.setFilterMode(9728);
            ci.cancel();
        }
    }
    *///?} else if =1.20.1 {
    /*/^ Hook for the iridescence shader to receive the time uniform ^/
    @Inject(method="process", at = @At("HEAD"), cancellable = true)
    void inj(float tickDelta, CallbackInfo ci) {
        if (name.contains("infinity")) {
            if (tickDelta < this.lastStamp) {
                this.time += 1.0F - this.lastStamp;
                this.time += tickDelta;
            } else {
                this.time += tickDelta - this.lastStamp;
            }

            for(this.lastStamp = tickDelta; this.time > 2.0F; this.time -= 4000.0F) {
            }

            this.passes.forEach(postEffectPass -> postEffectPass.process(this.time / 4000.0F));
            ci.cancel();
        }
    }
    *///?}
}
