package net.lerariemann.infinity.util.platform.fabric;

//? fabric {
import net.lerariemann.infinity.util.platform.Platform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class FabricPlatformImpl implements Platform {

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
        ModPayloadsFabric.registerPayloadsServer();
    }
    @Override
    public void registerPayloadsClient() {
        ModPayloadsFabric.registerPayloadsClient();
    }
}
//?}