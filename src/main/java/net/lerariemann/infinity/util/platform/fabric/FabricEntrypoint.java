package net.lerariemann.infinity.util.platform.fabric;

//? fabric {

/*import net.lerariemann.infinity.InfinityMod;
import net.fabricmc.api.ModInitializer;
import net.lerariemann.infinity.compat.CreateCompat;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.core.ModEntities;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.registry.var.ModStats;
import net.lerariemann.infinity.util.platform.InfinityPlatform;

public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        ModFluidsFabric.registerModFluids();
        InfinityMod.init();
        // Run any remaining tasks that require waiting for the registry to freeze on NeoForge.
        ModEntities.registerSpawnRestrictions();
        ModStats.load();
        ModBlocks.registerFlammableBlocks();
        ModItemFunctions.registerDispenserBehaviour();
        //FIXME - look into create fly?
        //? if <1.21.2 {
        /^if (InfinityPlatform.INSTANCE.isModLoaded("create"))
            CreateCompat.register();
        ^///?}
    }
}
*///?}