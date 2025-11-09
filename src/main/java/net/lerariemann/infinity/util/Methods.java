package net.lerariemann.infinity.util;

import net.minecraft.resources.ResourceLocation;

public interface Methods {
    static ResourceLocation id(String s) {
        return ResourceLocation.parse(s);
    }
    static ResourceLocation id(String key, String value) {
        return ResourceLocation.fromNamespaceAndPath(key, value);
    }
}
