package net.codexarchonic.infinity.mixin.qol;

import net.codexarchonic.infinity.util.loading.GeneratedBiomeNames;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Map;

@Mixin(ClientLanguage.class)
public class ClientLanguageMixin {
    @Shadow @Final private Map<String, String> storage;
    @Inject(method = "getOrDefault", at = @At("RETURN"), cancellable = true)
    private void infinity$generatedBiomeName(String key, String fallback, CallbackInfoReturnable<String> callback) {
        String name = GeneratedBiomeNames.translate(key, storage);
        if (name != null) callback.setReturnValue(name);
    }
    @Inject(method = "has", at = @At("RETURN"), cancellable = true)
    private void infinity$hasGeneratedBiomeName(String key, CallbackInfoReturnable<Boolean> callback) {
        if (GeneratedBiomeNames.matches(key)) callback.setReturnValue(true);
    }
}
