package net.lerariemann.infinity.registry.core;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.lerariemann.infinity.entity.client.*;
import net.lerariemann.infinity.entity.custom.*;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
//? if >1.21.9 {
/*import net.minecraft.world.entity.MobSpawnType;
import static net.minecraft.world.entity.MobSpawnType.*;
*///?} else if >1.21 {
import net.minecraft.world.entity.SpawnPlacementTypes;
import static net.minecraft.world.entity.SpawnPlacementTypes.*;
//?} else {
/*import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import static net.minecraft.world.entity.SpawnPlacements.Type.*;
*///?}
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
//? if forge {
/*import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import static net.minecraftforge.event.entity.SpawnPlacementRegisterEvent.Operation.REPLACE;
*///?}
//? if neoforge {
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import static net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE;
//?}

import java.util.Optional;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

public class ModEntities {
    public static void copy(Mob from, Mob e) {
        //? if >1.21.9 {
        /*e.moveOrInterpolateTo(from.position());
        *///?} else if >1.21.6 {
        e.moveOrInterpolateTo(from.position(), from.getYRot(), from.getXRot());
        //?} else {
        /*e.moveTo(from.getX(), from.getY(), from.getZ(), from.getYRot(), from.getXRot());
         *///?}
        e.setHealth(from.getHealth());
        e.yBodyRot = from.yBodyRot;
        if (from.hasCustomName()) {
            e.setCustomName(from.getCustomName());
            e.setCustomNameVisible(from.isCustomNameVisible());
        }
        if (from.isPersistenceRequired()) {
            e.setPersistenceRequired();
        }
        e.setInvulnerable(from.isInvulnerable());
        e.setItemInHand(InteractionHand.MAIN_HAND, from.getItemInHand(InteractionHand.MAIN_HAND));
        e.setItemInHand(InteractionHand.OFF_HAND, from.getItemInHand(InteractionHand.OFF_HAND));
    }

    public static final DeferredRegister<EntityType<?>> INFINITY_ENTITIES = DeferredRegister.create(MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<ChaosSlime>> CHAOS_SLIME = INFINITY_ENTITIES
            .register("chaos_slime", () ->
                    //? if >1.21 {
                    
                    EntityType.Builder.of(ChaosSlime::new, MobCategory.MONSTER)
                    .sized(0.52f, 0.52f)
                    .clientTrackingRange(10)
                    .build(type("chaos_slime"))
                     //?} else {
                    /*FabricEntityTypeBuilder.create(MobCategory.MONSTER, ChaosSlime::new)
                    .dimensions(EntityDimensions.scalable(0.52f, 0.52f))
                    .trackRangeBlocks(10)
                    .build()
                    *///?}
            );
    public static final RegistrySupplier<EntityType<ChaosSkeleton>> CHAOS_SKELETON = INFINITY_ENTITIES
            .register("chaos_skeleton", () -> EntityType.Builder.of(ChaosSkeleton::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.99f)
                    .clientTrackingRange(8)
                    //? if >1.21
                    .ridingOffset(-0.7F)
                    .build(type("chaos_skeleton")));
    public static final RegistrySupplier<EntityType<ChaosCreeper>> CHAOS_CREEPER = INFINITY_ENTITIES
            .register("chaos_creeper", () -> EntityType.Builder.of(ChaosCreeper::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.7f)
                    .clientTrackingRange(8)
                    .build(type("chaos_creeper")));
    public static final RegistrySupplier<EntityType<ChaosPawn>> CHAOS_PAWN = INFINITY_ENTITIES
            .register("chaos_pawn", () -> EntityType.Builder.of(ChaosPawn::new, MobCategory.MONSTER)
                    .sized(0.6f, 2.0f)
                    .clientTrackingRange(10)
                    //? if >1.21
                    .ridingOffset(-0.7F)
                    .build(type("chaos_pawn")));
    public static final RegistrySupplier<EntityType<AntEntity>> ANT = INFINITY_ENTITIES
            .register("ant", () -> EntityType.Builder.of(AntEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 0.3f)
                    .clientTrackingRange(10)
                    .build(type("ant")));
    public static final RegistrySupplier<EntityType<BishopEntity>> BISHOP = INFINITY_ENTITIES
            .register("bishop", () -> EntityType.Builder.of(BishopEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 2.7f)
                    //? if >1.21 {
                    .eyeHeight(1.75f)
                    .nameTagOffset(2.05F)
                    .ridingOffset(-0.7F)
                    //?}
                    .clientTrackingRange(10)
                    .build(type("bishop")));

    public static void registerEntities() {
        INFINITY_ENTITIES.register();
        registerAttributes();
    }

    //? if >1.21.4 {
    public static ResourceKey<EntityType<?>> type(String id) {
        return ResourceKey.create(Registries.ENTITY_TYPE, InfinityMethods.getId(id));
    }
    //?} else {
    /*public static String type(String id) {
        if (InfinityPlatform.INSTANCE.isFabric() && InfinityPlatform.version > 20) {
            return null;
        }
        else return "infinity:"+id;
    }
    *///?}

    public static void registerAttributes() {
        EntityAttributeRegistry.register(CHAOS_SLIME, ChaosSlime::createAttributes);
        EntityAttributeRegistry.register(CHAOS_SKELETON, AbstractSkeleton::createAttributes);
        EntityAttributeRegistry.register(CHAOS_CREEPER, ChaosCreeper::createAttributes);
        EntityAttributeRegistry.register(CHAOS_PAWN, ChaosPawn::createAttributes);
        EntityAttributeRegistry.register(ANT, AntEntity::createAttributes);
        EntityAttributeRegistry.register(BISHOP, BishopEntity::createAttributes);
    }

    public static <T extends Mob> void registerSafe(EntityType<T> type,
                                                    //? if >1.21.9 {
                                                    /*MobSpawnType
                                                    *///?} else if >1.21 {
                                                    SpawnPlacementType
                                                    //?} else {
                                                    /*SpawnPlacements.Type
                                                    *///?}
                                                    location, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> predicate,
                                                    //? if neoforge {
                                                    RegisterSpawnPlacementsEvent event
                                                     //?} else if forge {
                                                    /*SpawnPlacementRegisterEvent event
                                                    *///?} else {
                                                    /*Object event
                                                    *///?}
    ) {
        //? if fabric {
        /*if (!SpawnPlacements.DATA_BY_TYPE.containsKey(type))
            SpawnPlacements.register(type, location, heightmapType, predicate);
        *///?} else {
        event.register(type, location, heightmapType, predicate, REPLACE);
        //?}
    }

    public static void registerSpawnRestrictions(
            //? if neoforge {
            RegisterSpawnPlacementsEvent event
            //?} else if forge {
            /*SpawnPlacementRegisterEvent event
            *///?}
    ) {
        //? if fabric {
        /*Object event = null;
        SpawnPlacements.register(CHAOS_SLIME.get(), ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChaosSlime::canSpawn);
        SpawnPlacements.register(CHAOS_SKELETON.get(), ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::canChaosMonsterSpawn);
        SpawnPlacements.register(CHAOS_CREEPER.get(), ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::canChaosMonsterSpawn);
        SpawnPlacements.register(CHAOS_PAWN.get(), ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::canChessFigureSpawn);
        SpawnPlacements.register(ANT.get(), ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::canChessFigureSpawn);
        SpawnPlacements.register(BISHOP.get(), ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::canChessFigureSpawn);
        *///?} else {
        event.register(CHAOS_SLIME.get(), ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChaosSlime::canSpawn, REPLACE);
        event.register(CHAOS_SKELETON.get(), ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::canChaosMonsterSpawn, REPLACE);
        event.register(CHAOS_CREEPER.get(), ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::canChaosMonsterSpawn, REPLACE);
        event.register(CHAOS_PAWN.get(), ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::canChessFigureSpawn, REPLACE);
        event.register(ANT.get(), ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::canChessFigureSpawn, REPLACE);
        event.register(BISHOP.get(), ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModEntities::canChessFigureSpawn, REPLACE);
        //?}

        registerSafe(EntityType.SNIFFER, ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, event);
        registerSafe(EntityType.CAMEL, ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, event);
        registerSafe(EntityType.ZOGLIN, ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, event);
        //? if >1.21
        registerSafe(EntityType.BREEZE, ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, event);
    }

    public static boolean canChaosMonsterSpawn(EntityType<? extends Monster> type, ServerLevelAccessor world,

                                               //? if >1.21.2 {
                                               net.minecraft.world.entity.EntitySpawnReason
                                                       //?} else {
                                                       /*net.minecraft.world.entity.MobSpawnType
                                                        *///?}
                                               spawnReason, BlockPos pos, RandomSource random) {
        return Monster.checkMonsterSpawnRules(type, world, spawnReason, pos, random)
                && InfinityMethods.chaosMobsEnabled();
    }
    public static boolean canChessFigureSpawn(EntityType<? extends AbstractChessFigure> type, ServerLevelAccessor world,
                                              //? if >1.21.2 {
                                              net.minecraft.world.entity.EntitySpawnReason
                                              //?} else {
                                              /*net.minecraft.world.entity.MobSpawnType
                                               *///?}
                                              spawnReason, BlockPos pos, RandomSource random) {
        return Mob.checkMobSpawnRules(type, world, spawnReason, pos, random)
                && world.getDifficulty() != Difficulty.PEACEFUL
                && InfinityMethods.chaosMobsEnabled();
    }

    public static void registerEntityRenderers() {
        EntityModelLayerRegistry.register(AntModel.MODEL_LAYER, AntModel::getTexturedModelData);
        EntityModelLayerRegistry.register(BishopModel.MODEL_LAYER, BishopModel::getTexturedModelData);

        EntityRendererRegistry.register(CHAOS_SLIME, ChaosSlimeRenderer::new);
        EntityRendererRegistry.register(CHAOS_SKELETON, ChaosSkeletonRenderer::new);
        EntityRendererRegistry.register(CHAOS_CREEPER, ChaosCreeperRenderer::new);
        EntityRendererRegistry.register(CHAOS_PAWN, ChaosPawnRenderer::new);
        EntityRendererRegistry.register(ANT, AntRenderer::new);
        EntityRendererRegistry.register(BISHOP, BishopRenderer::new);
    }
}
