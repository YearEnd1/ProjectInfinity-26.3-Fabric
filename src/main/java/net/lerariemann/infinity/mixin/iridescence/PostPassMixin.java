package net.lerariemann.infinity.mixin.iridescence;

//? if =1.20.1 {
/*
import net.lerariemann.infinity.util.loading.ShaderLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalTime;
*///?} else {
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.systems.RenderPass;
import net.lerariemann.infinity.iridescence.IridescentUniform;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?}
import net.minecraft.client.renderer.PostPass;
import net.lerariemann.infinity.access.PostPassAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.Map;

@Mixin(PostPass.class)
public class PostPassMixin implements PostPassAccess {
    @Unique
    boolean infinity$is = false;

    @Override
    public void infinity$setIs() {
        infinity$is = true;
    }

    //? if =1.20.1 {
    /*@Shadow @Final private EffectInstance effect;

    @Inject(method = "process", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;getInstance()Lnet/minecraft/client/Minecraft;"))
    void inj(float partialTicks, CallbackInfo ci) {
        if (infinity$is) {
            effect.safeGetUniform("IridTimeSec").set(((int)(LocalTime.now().toNanoOfDay() / 1000000)) / 1000.0f);
            effect.safeGetUniform("IridLevel").set((float)ShaderLoader.iridLevel.get());
            effect.safeGetUniform("IridProgress").set((float)ShaderLoader.iridProgress.get());
            effect.safeGetUniform("IridDistortion").set(Minecraft.getInstance().options.screenEffectScale().get().floatValue());
        }
    }
    *///?}

    //? neoforge && >1.21.2 {
    @Inject(method = "lambda$addToFrame$5", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setVertexBuffer(ILcom/mojang/blaze3d/buffers/GpuBuffer;)V"))
    private void inj(ResourceHandle resourcehandle, GpuBufferSlice projectionMatrixBuffer, Map targets, CallbackInfo ci, @Local RenderPass renderPass) {
        if (infinity$is) IridescentUniform.trySet(renderPass);
    }
    //?} else fabric && >1.21.2 {
	/*@Inject(method = "method_67884", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setVertexBuffer(ILcom/mojang/blaze3d/buffers/GpuBuffer;)V"))
	private void inj(ResourceHandle resourcehandle, GpuBufferSlice projectionMatrixBuffer, Map targets, CallbackInfo ci, @Local RenderPass renderPass) {
		if (infinity$is) IridescentUniform.trySet(renderPass);
	}
	*///?}
}
