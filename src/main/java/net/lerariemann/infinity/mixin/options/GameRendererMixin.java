package net.lerariemann.infinity.mixin.options;

import net.lerariemann.infinity.access.GameRendererAccess;
import net.lerariemann.infinity.iridescence.Iridescence;
//? if >1.21.2 {
import net.lerariemann.infinity.iridescence.IridescentUniform;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import org.spongepowered.asm.mixin.Unique;
 //?}
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
    //? if <1.21.2 {
    /*@Shadow
    @Final
    private ResourceManager resourceManager;
    @Shadow
    protected abstract void loadEffect(ResourceLocation id);
    *///?} else {
    @Shadow
    public abstract void setPostEffect(ResourceLocation par1);
    //?}

    //? if <1.21.2 {
    /*@Override
    public void infinity$loadPP(ResourceLocation id) {
        if (resourceManager.getResource(id).isPresent()) {
            loadEffect(id);
        }
        else {
            Minecraft.getInstance().options.updateResourcePacks(Minecraft.getInstance().getResourcePackRepository());
        }
    }
    *///?}

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

    //? if >1.21.2 {
    @Unique
    private IridescentUniform infinity$iridUniform;

    @Unique
    @Override
    public IridescentUniform infinity$getIridescentUniform() {
        return infinity$iridUniform;
    }

    @Inject(method="<init>", at= @At(value = "TAIL"))
    void initUniform(Minecraft minecraft, ItemInHandRenderer itemInHandRenderer, RenderBuffers renderBuffers,
             //? if >1.21.9
             /*BlockRenderDispatcher blockRenderDispatcher,*/
             CallbackInfo ci) {
        this.infinity$iridUniform = new IridescentUniform();
    }
    @Inject(method="render", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlobalSettingsUniform;update(IIDJLnet/minecraft/client/DeltaTracker;I)V"))
    void updateUniform(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        infinity$iridUniform.tryUpdate();
    }
    @Inject(method="close", at= @At(value = "TAIL"))
    void closeUniform(CallbackInfo ci) {
        this.infinity$iridUniform.close();
    }
    //?}
}
