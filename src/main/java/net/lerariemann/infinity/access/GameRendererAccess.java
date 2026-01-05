package net.lerariemann.infinity.access;

import net.lerariemann.infinity.iridescence.IridescentUniform;
import net.minecraft.resources.ResourceLocation;

public interface GameRendererAccess {
    void infinity$loadPP(ResourceLocation id);

    //?if >1.21.2 {
    IridescentUniform infinity$getIridescentUniform();
    //?}
}
