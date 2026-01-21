package net.lerariemann.infinity.util.platform.forge;

//? forge {
/*import dev.architectury.platform.forge.EventBuses;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.compat.CreateCompat;
import net.lerariemann.infinity.compat.ModCompat;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.core.ModEntities;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.registry.core.ModStatusEffects;
import net.lerariemann.infinity.registry.var.ModStats;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.lerariemann.infinity.util.platform.forge.compat.CanaryCompat;
import net.lerariemann.infinity.util.platform.forge.compat.RadiumCompat;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class InfinityModForge {
    @SuppressWarnings("for removal")
    public InfinityModForge() {
        // Register compat file ASAP to prevent a Canary crash.
        if (ModList.get().isLoaded("canary"))
            CanaryCompat.writeCompatFile();
        // Register compat file ASAP to prevent a Radium crash.
        if (ModList.get().isLoaded("radium"))
            RadiumCompat.writeCompatFile();
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        IEventBus eventBus = context.getModEventBus();
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(InfinityMod.MOD_ID, context.getModEventBus());
        // Run our common setup.
        InfinityMod.init();
        // Run our client setup.
        if (FMLEnvironment.dist == Dist.CLIENT) InfinityModForgeClient.initializeClient(context);
        // Run any remaining tasks that require waiting for the registry to freeze on MinecraftForge.
        FluidTypes.registerFluidTypes(eventBus);
        ModFluidsForge.registerModFluids(eventBus);
        ModEffectsForge.register(eventBus);
        ResourceManagerHelper.registerBuiltinResourcePack(
                InfinityMethods.getId(String.valueOf(InfinityPlatform.version)),
                FabricLoader.getInstance().getModContainer(InfinityMod.MOD_ID).orElseThrow(),
                Component.literal("Infinite Dimensions - Multiversioned Resources"),
                ResourcePackActivationType.ALWAYS_ENABLED);
    }

    @SubscribeEvent
    public static void registerSpawns(SpawnPlacementRegisterEvent event) {
        ModEntities.registerSpawnRestrictions(event);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        ModStats.load();
        ModStatusEffects.IRIDESCENT_EFFECT = ModEffectsForge.IRIDESCENT_EFFECT.getHolder().get();
        ModStatusEffects.IRIDESCENT_SETUP = ModEffectsForge.IRIDESCENT_SETUP.getHolder().get();
        ModStatusEffects.IRIDESCENT_COOLDOWN = ModEffectsForge.IRIDESCENT_COOLDOWN.getHolder().get();
        ModStatusEffects.AFTERGLOW = ModEffectsForge.AFTERGLOW.getHolder().get();
        ModBlocks.registerFlammableBlocks();
        ModItemFunctions.registerDispenserBehaviour();
        if (ModCompat.CREATE) {
            CreateCompat.register();
        }
    }
}
*///?}