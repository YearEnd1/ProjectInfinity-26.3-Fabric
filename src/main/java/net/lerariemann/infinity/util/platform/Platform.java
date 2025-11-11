package net.lerariemann.infinity.util.platform;

//? fabric {
import net.lerariemann.infinity.util.platform.fabric.FabricPlatformImpl;
import net.lerariemann.infinity.util.platform.fabric.ModPayloadsFabric;
//?}
//? neoforge {
/*import net.lerariemann.infinity.util.platform.neoforge.NeoForgePlatformImpl;
*///?}

import java.nio.file.Path;

public interface Platform {

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
