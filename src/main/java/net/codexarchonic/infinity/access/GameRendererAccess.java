package net.codexarchonic.infinity.access;

//? if >1.21.2 {
import net.codexarchonic.infinity.iridescence.IridescentUniform;
 //?}


public interface GameRendererAccess {
    //? if >1.21.2 {
    IridescentUniform infinity$getIridescentUniform();
    //?} else {
    /*void infinity$loadPP(ResourceLocation id);
    *///?}
}
