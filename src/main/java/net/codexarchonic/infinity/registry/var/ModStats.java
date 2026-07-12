package net.codexarchonic.infinity.registry.var;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;

import static net.codexarchonic.infinity.InfinityMod.MOD_ID;
import static net.codexarchonic.infinity.util.InfinityMethods.getId;

public class ModStats {
    public static ResourceLocation DIMS_OPENED = getId("dimensions_opened_stat");
    public static Stat<ResourceLocation> DIMS_OPENED_STAT;
    public static ResourceLocation WORLDS_DESTROYED = getId("worlds_destroyed_stat");
    public static Stat<ResourceLocation> WORLDS_DESTROYED_STAT;
    public static ResourceLocation PORTALS_OPENED = getId("portals_opened_stat");
    public static Stat<ResourceLocation> PORTALS_OPENED_STAT;
    public static ResourceLocation IRIDESCENCE = getId("iridescence_stat");
    public static Stat<ResourceLocation> IRIDESCENCE_STAT;

    public static void load() {
        DIMS_OPENED_STAT = Stats.CUSTOM.get(DIMS_OPENED);
        PORTALS_OPENED_STAT = Stats.CUSTOM.get(PORTALS_OPENED);
        WORLDS_DESTROYED_STAT = Stats.CUSTOM.get(WORLDS_DESTROYED);
        IRIDESCENCE_STAT = Stats.CUSTOM.get(IRIDESCENCE);
    }

    public static final DeferredRegister<ResourceLocation> STATS = DeferredRegister.create(MOD_ID, Registries.CUSTOM_STAT);


    public static void registerStats() {
        STATS.register(DIMS_OPENED, () -> DIMS_OPENED);
        STATS.register(PORTALS_OPENED, () -> PORTALS_OPENED);
        STATS.register(WORLDS_DESTROYED, () -> WORLDS_DESTROYED);
        STATS.register(IRIDESCENCE, () -> IRIDESCENCE);
        STATS.register();
    }
}

