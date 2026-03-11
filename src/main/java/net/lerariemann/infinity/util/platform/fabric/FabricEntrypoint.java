package net.lerariemann.infinity.util.platform.fabric;

//? fabric {

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.api.transfer.v1.fluid.CauldronFluidContent;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.loader.api.FabricLoader;
import net.lerariemann.infinity.InfinityMod;
import net.fabricmc.api.ModInitializer;
import net.lerariemann.infinity.compat.CreateCompat;
import net.lerariemann.infinity.compat.ModCompat;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.core.ModEntities;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.registry.var.ModItemGroups;
import net.lerariemann.infinity.registry.var.ModStats;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.network.chat.Component;

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
        if (ModCompat.CREATE)
            CreateCompat.register();
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register(ModItemGroups::buildContents);
        ResourceManagerHelper.registerBuiltinResourcePack(
                InfinityMethods.getId(String.valueOf(InfinityPlatform.version)),
                FabricLoader.getInstance().getModContainer(InfinityMod.MOD_ID).orElseThrow(),
                Component.literal("Infinite Dimensions - Multiversioned Resources"),
                ResourcePackActivationType.ALWAYS_ENABLED);
        CauldronFluidContent.registerCauldron(ModBlocks.IRIDESCENCE_CAULDRON.get(), ModFluidsFabric.IRIDESCENCE_STILL.get(), FluidConstants.BUCKET, null);
    }
}
//?}