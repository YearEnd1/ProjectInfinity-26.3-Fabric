package net.lerariemann.infinity.mixin.options;

import net.lerariemann.infinity.access.GameRendererAccess;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.options.InfinityOptions;
import net.lerariemann.infinity.util.loading.ShaderLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements GameRendererAccess {
    @Shadow
    @Final
    private ResourceManager resourceManager;
    @Shadow
    protected abstract void loadEffect(ResourceLocation id);

    @Override
    public void infinity$loadPP(ResourceLocation id) {
        if (resourceManager.getResource(id).isPresent()) {
            loadEffect(id);
        }
        else {
            Minecraft.getInstance().options.updateResourcePacks(Minecraft.getInstance().getResourcePackRepository());
        }
    }

    @Inject(method = "checkEntityPostEffect", at = @At("TAIL"), cancellable = true)
    private void preserveShaderThirdPerson(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (Iridescence.shouldApplyShader(client.player))
            ShaderLoader.reloadShaders(client, true, true);
        InfinityOptions options = InfinityOptions.ofClient();
        if (options.getShader().isEmpty()) {
            ci.cancel();
        } else {
            ShaderLoader.reloadShaders(Minecraft.getInstance(), true, false);
        }
    }
}
