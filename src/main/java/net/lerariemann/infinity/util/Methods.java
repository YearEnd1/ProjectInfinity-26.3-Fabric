package net.lerariemann.infinity.util;

import net.minecraft.resources.ResourceLocation;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

public interface Methods {
    static ResourceLocation id(String s) {
        //? if >1.21 {
        return ResourceLocation.parse(s);
        //?} else {
        /*return ResourceLocation.tryParse(s);
        *///?}
    }

    static ResourceLocation ofInfinity(String path) {
        return id(MOD_ID, path);
    }

    static ResourceLocation ofVanilla(String path) {
        return id("minecraft", path);
    }

    static ResourceLocation id(String namespace, String path) {
        //? if >=1.21 {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
         //?} else {
        /*return new ResourceLocation(namespace, path);
        *///?}
    }
}
