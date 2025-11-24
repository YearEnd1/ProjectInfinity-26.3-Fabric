package net.lerariemann.infinity.registry.core;

import com.mojang.datafixers.types.Type;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
//? if fabric && >1.21.2 {
/*import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityType;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
*///?}
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.block.custom.RailHelper;
import net.lerariemann.infinity.block.entity.*;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static Type<?> type(String id) {
        if (!InfinityPlatform.INSTANCE.isFabric())
            return Util.fetchChoiceType(References.BLOCK_ENTITY, "infinity:"+ id);
        else return null;
    }
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(InfinityMod.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<InfinityPortalBlockEntity>> NEITHER_PORTAL =
            BLOCK_ENTITY_TYPES.register("neither_portal", () ->
                    create(InfinityPortalBlockEntity::new,
                            ModBlocks.PORTAL.get())
                            //? if fabric || <1.21.2
                            /*.build(type("neither_portal"))*/
            );
    public static final RegistrySupplier<BlockEntityType<CosmicAltarBlockEntity>> COSMIC_ALTAR =
            BLOCK_ENTITY_TYPES.register("cosmic_altar", () ->
                    create(CosmicAltarBlockEntity::new,
                            ModBlocks.COSMIC_ALTAR.get())
                            //? if fabric || <1.21.2
                            /*.build(type("cosmic_altar"))*/
            );
    public static final RegistrySupplier<BlockEntityType<BiomeBottleBlockEntity>> BIOME_BOTTLE =
            BLOCK_ENTITY_TYPES.register("biome_bottle", () ->
                    create(BiomeBottleBlockEntity::new,
                            ModBlocks.BIOME_BOTTLE.get())
                            //? if fabric || <1.21.2
                            /*.build(type("biome_bottle"))*/
            );
    public static final RegistrySupplier<BlockEntityType<ChromaticBlockEntity>> CHROMATIC =
            BLOCK_ENTITY_TYPES.register("chromatic", () ->
                    create(ChromaticBlockEntity::new,
                            ModBlocks.CHROMATIC_WOOL.get(),
                            ModBlocks.CHROMATIC_CARPET.get())
                            //? if fabric || <1.21.2
                            /*.build(type("chromatic"))*/
            );
    public static final RegistrySupplier<BlockEntityType<RailHelper.RHBEntity>> RAIL_HELPER =
            BLOCK_ENTITY_TYPES.register("rail_helper_block_entity", () ->
                    create(RailHelper.RHBEntity::new,
                            ModBlocks.RAIL_HELPER.get())
            //? if fabric || <1.21.2
            /*.build(type("rail_helper_block_entity"))*/
            );

    public static <T extends BlockEntity>
    //? if >1.21.2 && fabric {
    /*FabricBlockEntityTypeBuilder
    *///?} else if >1.21.2 {
    BlockEntityType
    //?} else {
    /*BlockEntityType.Builder
    *///?}
    <T>
    create(
            //? if >1.21.2 && fabric {
            /*FabricBlockEntityTypeBuilder.Factory
            *///?} else if >1.21.2 {
            BlockEntityType.BlockEntitySupplier
             //?} else {
            /*BlockEntityType.BlockEntitySupplier
            *///?}
            <? extends T> factory, Block... blocks) {
        //? if >1.21.2 && fabric {
        /*return FabricBlockEntityTypeBuilder.create(factory, blocks);
        *///?} else if >1.21.2 {
        return new BlockEntityType<>(factory, blocks);
        //?} else {
        /*return BlockEntityType.Builder.of(factory, blocks);
        *///?}
    }

    public static void registerBlockEntities() {
        BLOCK_ENTITY_TYPES.register();
    }
}