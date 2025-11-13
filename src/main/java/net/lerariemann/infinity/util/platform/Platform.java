package net.lerariemann.infinity.util.platform;

//? fabric {
import net.lerariemann.infinity.util.platform.fabric.FabricPlatformImpl;
//?}
//? neoforge {
/*import net.lerariemann.infinity.util.platform.neoforge.NeoForgePlatformImpl;
*///?}

import java.nio.file.Path;

public interface Platform {
    boolean above21 =
            //? if >1.21 {
            /*true;
             *///?} else {
            false;
    //?}

    //? fabric {
    Platform INSTANCE = new FabricPlatformImpl();
    //?}
    //? neoforge {
    /*Platform INSTANCE = new NeoForgePlatformImpl();
    *///?}

    boolean isModLoaded(String modid);
    String loader();
    Path getConfigFolder();
    boolean isDevelopment();
    void registerPayloadsServer();
    void registerPayloadsClient();
}
