package net.codexarchonic.infinity.registry.core;

import com.mojang.serialization.MapCodec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.features.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import static net.codexarchonic.infinity.InfinityMod.MOD_ID;

public class ModFeatures {
    public static RegistrySupplier<TrunkPlacerType<WonkyTrunkPlacer>> WONKY_TRUNK;

    public static final DeferredRegister<MapCodec<? extends Feature>> FEATURES = DeferredRegister.create(MOD_ID, Registries.FEATURE_TYPE);
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNKS = DeferredRegister.create(MOD_ID, Registries.TRUNK_PLACER_TYPE);


    public static void registerFeatures() {
        InfinityMod.LOGGER.debug("Registering features for " + InfinityMod.MOD_ID);
        FEATURES.register("random_end_island", () -> RandomEndIslandFeature.CODEC);
        FEATURES.register("random_dungeon", () -> RandomDungeonFeature.CODEC);
        FEATURES.register("random_columns", () -> RandomColumnsFeature.CODEC);
        FEATURES.register("random_flat_mushroom", () -> RandomFlatMushroomFeature.CODEC);
        FEATURES.register("random_round_mushroom", () -> RandomRoundMushroomFeature.CODEC);
        FEATURES.register("random_ceiling_blob", () -> RandomCeilingBlobFeature.CODEC);
        FEATURES.register("random_cube", () -> RandomCubeFeature.CODEC);
        FEATURES.register("random_shape", () -> RandomShapeFeature.CODEC);
        FEATURES.register("random_text", () -> TextFeature.CODEC);
        FEATURES.register("madhouse", () -> NotesHouseFeature.CODEC);
        FEATURES.register("jukebox", () -> NotesJukeboxFeature.CODEC);
        FEATURES.register("portal_setupper", () -> RandomPortalSetupper.CODEC);
        FEATURES.register("bonus_chest", () -> RandomBonusChestFeature.CODEC);
        FEATURES.register("end_gateway", () -> RandomEndGatewayFeature.CODEC);
        WONKY_TRUNK = TRUNKS.register("wonky", () -> new TrunkPlacerType<>(WonkyTrunkPlacer.CODEC));
        FEATURES.register();
        TRUNKS.register();
    }
}
