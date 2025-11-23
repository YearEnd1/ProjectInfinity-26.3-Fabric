package net.lerariemann.infinity.util.platform.neoforge;

//? neoforge {
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class NeoForgePlatformImpl implements InfinityPlatform {
    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @Override
    public String loader() {
        return "neoforge";
    }

    @Override
    public Path getConfigFolder() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean isDevelopment() {
        return !FMLEnvironment.production;
    }
    @Override
    public void registerPayloadsServer() {};
    @Override
    public void registerPayloadsClient() {};
}
//?}