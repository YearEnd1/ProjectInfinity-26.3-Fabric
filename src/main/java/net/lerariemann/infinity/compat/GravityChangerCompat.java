package net.lerariemann.infinity.compat;

//? if <1.21 && fabric
/*import gravity_changer.api.GravityChangerAPI;*/
import net.minecraft.server.level.ServerLevel;

import static net.lerariemann.infinity.util.InfinityMethods.getMavity;

public class GravityChangerCompat {
    //? if <1.21 && fabric {
    /*public static void changeMavity(ServerLevel world) {
        GravityChangerAPI.setDimensionGravityStrength(world, getMavity(world));
    }

    *///?}
}