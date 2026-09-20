package net.codexarchonic.infinity.mixin.iridescence;

//? if <1.21.2 {

/*import net.codexarchonic.infinity.access.PostPassAccess;
import net.codexarchonic.infinity.util.loading.ShaderLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostPass;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.PostChain;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
*///?} else {
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import net.codexarchonic.infinity.access.PostPassAccess;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Set;

@Mixin(PostChain.class)
public abstract class PostEffectProcessorMixin implements PostPassAccess {
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
    @Unique
    boolean infinity$is = false;

    @Override
    public void infinity$setIs() {
        infinity$is = true;
    }

    //? if >1.21.2 {
    @Shadow @Final private List<PostPass> passes;

    @Inject(method = "load", at = @At("RETURN"))
    private static void inj2(PostChainConfig config, TextureManager textureManager, Set<ResourceLocation> externalTargets, ResourceLocation name, CachedOrthoProjectionMatrixBuffer projection,
                             //? if >26
                             //CachedOrthoProjectionMatrixBufferMatrixBuffer projectionMatrixBuffer,
                             CallbackInfoReturnable<PostChain> cir) {
        if (name.toString().contains("infinity")) {
            ((PostPassAccess)cir.getReturnValue()).infinity$setIs();
        }
    }

    @Inject(method = "addToFrame", at = @At("HEAD"))
    void inj(FrameGraphBuilder frameGraphBuilder, int width, int height, PostChain.TargetBundle targetBundle, CallbackInfo ci) {
        if (infinity$is) {
            for (PostPass pass : passes) {
                ((PostPassAccess)pass).infinity$setIs();
            }
        }
    }
    //?}

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

            this.passes.forEach(postEffectPass -> {
                ((PostPassAccess)postEffectPass).infinity$setIs();
                postEffectPass.process(this.time / 4000.0F);
            });
            ci.cancel();
        }
    }
    *///?}
}
