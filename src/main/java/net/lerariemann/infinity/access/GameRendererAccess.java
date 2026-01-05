package net.lerariemann.infinity.access;

//? if >1.21.2 {
import net.lerariemann.infinity.iridescence.IridescentUniform;
 //?}
import net.minecraft.resources.ResourceLocation;

public interface GameRendererAccess {
    //? if >1.21.2 {
    IridescentUniform infinity$getIridescentUniform();
    //?} else {
    /*void infinity$loadPP(ResourceLocation id);
    *///?}
}
