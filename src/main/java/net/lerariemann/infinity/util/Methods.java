package net.lerariemann.infinity.util;

import net.lerariemann.infinity.util.platform.Platform;
import net.minecraft.resources.ResourceLocation;

public interface Methods {
    static boolean isModLoaded(String mod) {
        return Platform.INSTANCE.isModLoaded(mod);
    }

    static ResourceLocation id(String s) {
        return ResourceLocation.parse(s);
    }
    static ResourceLocation id(String key, String value) {
        return ResourceLocation.fromNamespaceAndPath(key, value);
    }
}
