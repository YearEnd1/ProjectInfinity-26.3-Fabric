package net.lerariemann.infinity.registry.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.features.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

public class ModFeatures {
    public static RegistrySupplier<RandomEndIslandFeature> RANDOM_END_ISLAND;
    public static RegistrySupplier<RandomDungeonFeature> RANDOM_DUNGEON;
    public static RegistrySupplier<RandomColumnsFeature> RANDOM_COLUMNS;
    public static RegistrySupplier<RandomFlatMushroomFeature> RANDOM_FLAT_MUSHROOM;
    public static RegistrySupplier<RandomRoundMushroomFeature> RANDOM_ROUND_MUSHROOM;
    public static RegistrySupplier<RandomCeilingBlobFeature> RANDOM_CEILING_BLOB;
    public static RegistrySupplier<RandomBonusChestFeature> RANDOM_BONUS_CHEST;
    public static RegistrySupplier<RandomEndGatewayFeature> RANDOM_END_GATEWAY;
    public static RegistrySupplier<RandomCubeFeature> RANDOM_CUBE;
    public static RegistrySupplier<RandomShapeFeature> RANDOM_STAR;
    public static RegistrySupplier<TextFeature> RANDOM_TEXT;
    public static RegistrySupplier<RandomPortalSetupper> PORTAL_SETUPPER;
    public static RegistrySupplier<TrunkPlacerType<WonkyTrunkPlacer>> WONKY_TRUNK;

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(MOD_ID, Registries.FEATURE);
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNKS = DeferredRegister.create(MOD_ID, Registries.TRUNK_PLACER_TYPE);

    public static <C extends FeatureConfiguration, F extends Feature<C>> RegistrySupplier<F> register(String name, F feature) {
        return FEATURES.register(name, () -> feature);
    }
    public static void registerFeatures() {
        InfinityMod.LOGGER.debug("Registering features for " + InfinityMod.MOD_ID);
        RANDOM_END_ISLAND = register("random_end_island", new RandomEndIslandFeature(BlockStateConfiguration.CODEC));
        RANDOM_DUNGEON = register("random_dungeon", new RandomDungeonFeature(RandomDungeonFeature.Config.CODEC));
        RANDOM_COLUMNS = register("random_columns", new RandomColumnsFeature(RandomColumnsFeature.Config.CODEC));
        RANDOM_FLAT_MUSHROOM = register("random_flat_mushroom", new RandomFlatMushroomFeature(RandomMushroomFeature.Config.CODEC));
        RANDOM_ROUND_MUSHROOM = register("random_round_mushroom", new RandomRoundMushroomFeature(RandomMushroomFeature.Config.CODEC));
        RANDOM_CEILING_BLOB = register("random_ceiling_blob", new RandomCeilingBlobFeature(RandomCeilingBlobFeature.Config.CODEC));
        RANDOM_CUBE = register("random_cube", new RandomCubeFeature(RandomCubeFeature.Config.CODEC));
        RANDOM_STAR = register("random_shape", new RandomShapeFeature(RandomShapeFeature.Config.CODEC));
        RANDOM_TEXT = register("random_text", new TextFeature(TextFeature.Config.CODEC));
        PORTAL_SETUPPER = register("portal_setupper", new RandomPortalSetupper((RandomPortalSetupper.Config.CODEC)));
        RANDOM_BONUS_CHEST = register("bonus_chest", new RandomBonusChestFeature(RandomBonusChestFeature.Config.CODEC));
        RANDOM_END_GATEWAY = register("end_gateway", new RandomEndGatewayFeature(RandomEndGatewayFeature.Config.CODEC));
        WONKY_TRUNK = TRUNKS.register("wonky", () -> new TrunkPlacerType<>(codecOrCodec(WonkyTrunkPlacer.CODEC)));
        FEATURES.register();
        TRUNKS.register();
    }


    static <T>
    //? if >1.21 {
    /*MapCodec<T>
     *///?} else {
    Codec<T>
    //?}
    codecOrCodec(MapCodec<T> codec) {
        //? if >1.21 {
        /*return codec;
         *///?} else {
        return codec.codec();
        //?}
    }
}