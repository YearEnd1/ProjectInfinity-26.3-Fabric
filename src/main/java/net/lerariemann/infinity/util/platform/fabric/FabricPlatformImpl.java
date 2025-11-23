package net.lerariemann.infinity.util.platform.fabric;

//? fabric {
/*import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class FabricPlatformImpl implements InfinityPlatform {

    @Override
    public boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    @Override
    public String loader() {
        return "fabric";
    }

    @Override
    public Path getConfigFolder() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public boolean isDevelopment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void registerPayloadsServer() {
        //? if >1.21 {
        ModPayloadsFabric.registerPayloadsServer();
        //?}
    }
    @Override
    public void registerPayloadsClient() {
        //? if >1.21 {
        ModPayloadsFabric.registerPayloadsClient();
        //?}
    }
}
*///?}