package net.lerariemann.infinity.util;

import net.minecraft.util.Identifier;

public interface VersionMethods {
    static Identifier id(String s) {
        return Identifier.of(s);
    }
    static Identifier id(String key, String value) {
        return Identifier.of(key, value);
    }
}
