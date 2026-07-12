package net.codexarchonic.infinity.mixin.options;

import net.codexarchonic.infinity.access.InfinityOptionsAccess;
import net.codexarchonic.infinity.options.InfinityOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin implements InfinityOptionsAccess {
    @Shadow
    public static Minecraft getInstance() {
        return null;
    }
    @Shadow
    public ClientLevel level;

    @Unique
    public InfinityOptions infinity$options;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void resetOptions(GameConfig args, CallbackInfo ci) {
        infinity$options = InfinityOptions.empty();
    }

    @Unique
    public InfinityOptions infinity$getOptions() {
        return InfinityOptions.nullSafe(infinity$options);
    }

    @Unique
    public void infinity$setOptions(InfinityOptions options) {
        infinity$options = options;
        if (level != null) ((InfinityOptionsAccess) level).infinity$setOptions(options);
    }
}
