package net.lerariemann.infinity.registry.var;

//? fabric {

import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
//?}
import com.google.common.collect.ImmutableSet;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

public class ModPoi {
    public static RegistrySupplier<PoiType> NEITHER_PORTAL;
    public static ResourceKey<PoiType> NEITHER_PORTAL_KEY;

    public static void registerPoi() {
        InfinityMod.LOGGER.debug("Registering POI for " + InfinityMod.MOD_ID);
        if (InfinityPlatform.INSTANCE.isFabric()) registerPoiFabric();
        else registerPoiArchitectury();
        NEITHER_PORTAL_KEY = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, InfinityMethods.getId("neither_portal"));

    }

    // Deferred handling of Poi through Architectury API, works on NeoForge.
    public static void registerPoiArchitectury() {
        final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(MOD_ID, Registries.POINT_OF_INTEREST_TYPE);
        NEITHER_PORTAL = POI_TYPES.register("neither_portal", () -> new PoiType(ImmutableSet.copyOf(ModBlocks.PORTAL.get().getStateDefinition().getPossibleStates()), 0, 1));
        POI_TYPES.register();
    }

    public static void registerPoiFabric() {
        //? fabric {
        PointOfInterestHelper.register(InfinityMethods.getId("neither_portal"), 0, 1,
                ImmutableSet.copyOf(ModBlocks.PORTAL.get().getStateDefinition().getPossibleStates()));
        //?}
    }
}