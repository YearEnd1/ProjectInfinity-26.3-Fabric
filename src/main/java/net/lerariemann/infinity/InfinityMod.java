package net.lerariemann.infinity;

import net.lerariemann.infinity.access.MinecraftServerAccess;
import net.lerariemann.infinity.dimensions.RandomText;
import net.lerariemann.infinity.registry.core.*;
import net.lerariemann.infinity.registry.payload.ModPayloads;
import net.lerariemann.infinity.registry.var.*;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.lerariemann.infinity.util.config.ConfigManager;
import net.lerariemann.infinity.util.core.RandomProvider;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Random;

public class InfinityMod {
    public static final String MOD_ID = "infinity";
    public static final Logger LOGGER = LoggerFactory.getLogger("Infinite Dimensions");
    public static Path configPath = InfinityPlatform.INSTANCE.getConfigFolder().resolve(MOD_ID);
    public static Path utilPath = configPath.resolve(".util");
    public static Path invocationLock = configPath.resolve("modular/invocation.lock");
    public static Path amendmentPath = configPath.resolve("amendments.json");

    public static Path commonConfigPathInJar;
    public static RandomProvider provider;
    public static Random random = new Random(); //do not use this in dimgen, only in emergent block behaviour

    public static void updateProvider(MinecraftServer server) {
        RandomProvider p = new RandomProvider(server);
        p.kickGhostsOut(server.registryAccess());
        provider = p;
        if (!((MinecraftServerAccess)server).infinity$needsInvocation()) {
            provider.updateDependencies();
        }
    }

    public static void init() {
        commonConfigPathInJar = InfinityPlatform.INSTANCE.getRootPathInJar("config/common");
        ConfigManager.unpackDefaultConfigs();
        ConfigManager.updateInvocationUnlock();
        //? if >1.21 {
        ModComponentTypes.registerComponentTypes();
        //?}
        ModStatusEffects.registerModEffects();
        ModItemFunctions.registerItemFunctions();
        ModEntities.registerEntities();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModPayloads.registerC2SPacketsReceivers();
        ModBlockEntities.registerBlockEntities();
        ModPoi.registerPoi();
        ModCommands.registerCommands();
        ModDensityFunctionTypes.registerFunctions();
        ModMaterialConditions.registerConditions();
        ModMaterialRules.registerRules();
        ModPlacementModifiers.registerModifiers();
        ModStructureTypes.registerStructures();
        ModSounds.registerSounds();
        ModFeatures.registerFeatures();
        ModStats.registerStats();
        ModCriteria.registerCriteria();
        RandomText.walkPaths();
        provider = new RandomProvider();
    }
}
