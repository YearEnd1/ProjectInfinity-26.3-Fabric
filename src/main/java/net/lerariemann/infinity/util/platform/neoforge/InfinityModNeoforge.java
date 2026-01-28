package net.lerariemann.infinity.util.platform.neoforge;

//? neoforge {
/*import net.lerariemann.infinity.InfinityMod;
//? if >1.21.4 {
import net.lerariemann.infinity.compat.ModCompat;
import net.lerariemann.infinity.compat.rrv.RrvIntegration;
//?}
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.core.ModEntities;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.registry.payload.ModPayloads;
import net.lerariemann.infinity.registry.var.ModStats;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

@Mod(MOD_ID)
@EventBusSubscriber(modid = MOD_ID)
public class InfinityModNeoforge {
    public InfinityModNeoforge(IEventBus eventBus, ModContainer container) {
        // Run our common setup.
        InfinityMod.init();
        // Run any remaining tasks that require waiting for the registry to freeze on NeoForge.
        FluidTypes.registerFluidTypes(eventBus);
        ModFluidsNeoforge.registerModFluids();
        ModEffectsNeoforge.register(eventBus);
        //? if <1.21.2 {
        /^if (InfinityPlatform.INSTANCE.isModLoaded("create"))
            net.lerariemann.infinity.compat.CreateCompat.register();
        ^///?}
    }

    @SubscribeEvent
    private static void initCommonResources(AddPackFindersEvent event) {
        event.addPackFinders(
                InfinityMethods.getId("resourcepacks/" + InfinityPlatform.version),
                PackType.SERVER_DATA,
                Component.literal("Infinite Dimensions - Multiversioned Resources"),
                PackSource.BUILT_IN,
                true,
                Pack.Position.TOP
        );
        event.addPackFinders(
                InfinityMethods.getId("resourcepacks/" + InfinityPlatform.version),
                PackType.CLIENT_RESOURCES,
                Component.literal("Infinite Dimensions - Multiversioned Resources"),
                PackSource.BUILT_IN,
                true,
                Pack.Position.TOP
        );
    }

    @SubscribeEvent
    public static void registerSpawns(RegisterSpawnPlacementsEvent event) {
        ModEntities.registerSpawnRestrictions(event);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        ModStats.load();
        ModBlocks.registerFlammableBlocks();
        ModItemFunctions.registerDispenserBehaviour();
        // this is less than ideal but i cannot figure out why the proper solution isn't working
        //? if >1.21.4 {
        if (ModCompat.RRV)
            RrvIntegration.initialize();
        //?}
    }

    @SubscribeEvent
    public static void registerServerPayloads(final RegisterPayloadHandlersEvent event) {
        ModPayloads.registerNeoforge(event);
    }
}
*///?}