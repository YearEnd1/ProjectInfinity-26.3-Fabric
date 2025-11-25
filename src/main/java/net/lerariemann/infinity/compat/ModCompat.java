package net.lerariemann.infinity.compat;

import net.lerariemann.infinity.util.platform.InfinityPlatform;

public class ModCompat {

    /**
     * CC: Tweaked
     * Optional config API, used for ingame configuration.
     */
    public static final boolean COMPUTERCRAFT = InfinityPlatform.INSTANCE.isModLoaded("computercraft");

    /**
     * Cloth Config
     * Optional config API, used for ingame configuration.
     */
    public static final boolean CLOTH_CONFIG = InfinityPlatform.INSTANCE.isModLoaded("cloth-config") || InfinityPlatform.INSTANCE.isModLoaded("cloth_config");

    /**
     * Create/Create Fabric/Create Fly
     * Used to allow Create trains to pass through Infinity Portals.
     */
    public static final boolean CREATE = InfinityPlatform.INSTANCE.isModLoaded("create");

    /**
     * DimLib
     * Immersive Portals runtime dimension registration.
     */
    public static final boolean DIMLIB = InfinityPlatform.INSTANCE.isModLoaded("dimlib");

    /**
     * Extended Item View (EIV)
     * 1.21.4 focused recipe viewer
     */
    public static final boolean EIV = InfinityPlatform.INSTANCE.isModLoaded("eiv");

    /**
     * FTB Essentials/Fabric Essentials
     * Prevents our /warp command from merging with theirs.
     */
    public static final boolean ESSENTIALS = InfinityPlatform.INSTANCE.isModLoaded("ftbessentials") || InfinityPlatform.INSTANCE.isModLoaded("fabric-essentials");

    /**
     * Gravity Changer
     * Optional compatibility for Mavity on 1.20.
     */
    public static final boolean GRAVITY_CHANGER = InfinityPlatform.INSTANCE.isModLoaded("gravity_changer_q");

    /**
     * Ponder
     * Create's library for ingame viewing - prevents a crash from us assuming a simulated world is real.
     */
    public static final boolean PONDER = InfinityPlatform.INSTANCE.isModLoaded("create") || InfinityPlatform.INSTANCE.isModLoaded("ponder");
}
