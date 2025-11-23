package net.lerariemann.infinity.util.platform;

//? fabric {
/*import net.lerariemann.infinity.util.platform.fabric.FabricPlatformImpl;
*///?}
//? neoforge {
import net.lerariemann.infinity.util.platform.neoforge.NeoForgePlatformImpl;
//?}
//? forge {
/*import net.lerariemann.infinity.util.platform.forge.ForgePlatformImpl;
 *///?}

import java.nio.file.Path;

public interface InfinityPlatform {
    int version =
            //? if >1.21.6 {
            22;
            //?} else if >1.21 {
            /*21;
             *///?} else {
            /*20;
    *///?}

    //? fabric {
    /*InfinityPlatform INSTANCE = new FabricPlatformImpl();
    *///?}
    //? neoforge {
    InfinityPlatform INSTANCE = new NeoForgePlatformImpl();
    //?}
    //? forge {
    /*InfinityPlatform INSTANCE = new ForgePlatformImpl();
     *///?}

    boolean isModLoaded(String modid);
    String loader();
    Path getConfigFolder();
    boolean isDevelopment();
    void registerPayloadsServer();
    void registerPayloadsClient();
}
