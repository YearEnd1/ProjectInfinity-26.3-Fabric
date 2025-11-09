package net.lerariemann.infinity.compat;

import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.world.level.Level;

public class PonderCompat {
    public static boolean isPonderLevel(Level world) {
        return world instanceof PonderLevel;
    }
}
