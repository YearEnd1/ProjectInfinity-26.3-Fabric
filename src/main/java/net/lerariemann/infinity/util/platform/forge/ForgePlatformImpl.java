package net.lerariemann.infinity.util.platform.forge;

//? forge {
/*import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ForgePlatformImpl implements InfinityPlatform {
    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @Override
    public String loader() {
        return "forge";
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
*///?}