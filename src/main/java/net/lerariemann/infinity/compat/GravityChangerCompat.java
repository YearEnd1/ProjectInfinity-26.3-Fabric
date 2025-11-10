package net.lerariemann.infinity.compat;

//? if <1.21
/*import gravity_changer.api.GravityChangerAPI;*/
import net.lerariemann.infinity.access.InfinityOptionsAccess;
import net.minecraft.server.level.ServerLevel;

public class GravityChangerCompat  {
    //? if <1.21 {
    /*public static void changeMavity(ServerLevel world) {
        GravityChangerAPI.setDimensionGravityStrength(world, getMavity(world));
    }

    public static double getMavity(ServerLevel world) {
        double mavity;
        try {
            mavity = ((InfinityOptionsAccess) world).infinity$getOptions().getMavity();
        } catch (Exception e) {
            mavity = 1.0;
        }
        return mavity;
    }
    *///?}
}