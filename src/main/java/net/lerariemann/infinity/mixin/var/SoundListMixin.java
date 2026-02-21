package net.lerariemann.infinity.mixin.var;

import net.lerariemann.infinity.util.config.SoundScanner;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(targets = "net.minecraft.client.sounds.SoundManager$Preparations")
public class SoundListMixin {
    @Shadow private Map<ResourceLocation, Resource> soundCache;

    @Inject(method = "listResources(Lnet/minecraft/server/packs/resources/ResourceManager;)V",
    at = @At("RETURN"))
    void inj(ResourceManager resourceManager, CallbackInfo ci) {
        SoundScanner.instance = new SoundScanner(soundCache);
    }
}
