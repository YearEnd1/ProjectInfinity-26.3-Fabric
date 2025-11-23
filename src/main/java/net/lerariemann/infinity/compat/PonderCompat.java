package net.lerariemann.infinity.compat;

//? if <1.21.2
/*import net.createmod.ponder.api.level.PonderLevel;*/
import net.minecraft.world.level.Level;

public class PonderCompat {
    public static boolean isPonderLevel(Level world) {
        //? if <1.21.2 {
        /*return world instanceof PonderLevel;
        *///?} else {
        return false;
        //?}

    }
}
