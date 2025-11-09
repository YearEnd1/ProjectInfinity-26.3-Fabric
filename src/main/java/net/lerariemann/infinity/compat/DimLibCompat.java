package net.lerariemann.infinity.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.dimension.LevelStem;
import qouteall.dimlib.api.DimensionAPI;

public class DimLibCompat {
    public static void add(MinecraftServer server, ResourceLocation id, LevelStem dimensionOptions) {
        DimensionAPI.addDimension(server, id, dimensionOptions);
    }
}
