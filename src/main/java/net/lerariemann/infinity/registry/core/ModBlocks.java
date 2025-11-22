package net.lerariemann.infinity.registry.core;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.platform.PlatformMethods;
import net.lerariemann.infinity.block.custom.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;
import java.util.function.Supplier;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, Registries.BLOCK);

    public static final RegistrySupplier<Block> COSMIC_ALTAR = register("cosmic_altar",
            CosmicAltarBlock::new, VersionMethods.copyBlockProperties(Blocks.NETHERITE_BLOCK).lightLevel(state -> 15).noOcclusion());
    public static final RegistrySupplier<Block> ALTAR = register("altar",
            AltarBlock::new, VersionMethods.copyBlockProperties(Blocks.STONE).noOcclusion().lightLevel(state -> state.getValue(AltarBlock.FLOWER) ? 15 : 0));
    public static final RegistrySupplier<Block> ANT = register("ant",
            AntBlock::new, (VersionMethods.copyBlockProperties(Blocks.WHITE_CONCRETE)));
    public static final RegistrySupplier<Block> BOOK_BOX = register("book_box",
            BookBoxBlock::new, (VersionMethods.copyBlockProperties(Blocks.BOOKSHELF)));
    public static final RegistrySupplier<Block> CURSOR = register("cursor", 
            Block::new, (BlockBehaviour.Properties.of().strength(1.8f).mapColor(MapColor.COLOR_GREEN).sound(SoundType.STONE)));
    public static final RegistrySupplier<Block> PORTAL = register("neither_portal",
            InfinityPortalBlock::new, (VersionMethods.copyBlockProperties(Blocks.NETHER_PORTAL)));
    public static final RegistrySupplier<Block> NETHERITE_STAIRS = register("netherite_stairs",
            (properties)-> new ModStairsBlock(Blocks.NETHERITE_BLOCK.defaultBlockState(), properties), VersionMethods.copyBlockProperties(Blocks.NETHERITE_BLOCK));
    public static final RegistrySupplier<Block> NETHERITE_SLAB = register("netherite_slab", 
            SlabBlock::new, (VersionMethods.copyBlockProperties(Blocks.NETHERITE_BLOCK)));
    public static final RegistrySupplier<Block> TIME_BOMB = register("timebomb", 
            TimeBombBlock::new, (VersionMethods.copyBlockProperties(Blocks.BEDROCK).noOcclusion().lightLevel(state -> 15)));
    public static final RegistrySupplier<LiquidBlock> IRIDESCENCE = PlatformMethods.getIridBlockForReg();
    public static final RegistrySupplier<Block> BIOME_BOTTLE = register("biome_bottle", 
            BiomeBottleBlock::new, (VersionMethods.copyBlockProperties(Blocks.BEACON).lightLevel(state -> state.getValue(BiomeBottleBlock.LEVEL))
                    .sound(SoundType.GLASS)));
    public static final RegistrySupplier<Block> IRIDESCENT_WOOL = register("iridescent_wool", 
            IridescentBlock::new, (VersionMethods.copyBlockProperties(Blocks.MAGENTA_WOOL)));
    public static final RegistrySupplier<Block> IRIDESCENT_CARPET = register("iridescent_carpet", 
            IridescentBlock.Carpet::new, (VersionMethods.copyBlockProperties(Blocks.MAGENTA_CARPET)));
    public static final RegistrySupplier<Block> CHROMATIC_WOOL = register("chromatic_wool", 
            ChromaticBlock::new, (VersionMethods.copyBlockProperties(Blocks.MAGENTA_WOOL)));
    public static final RegistrySupplier<Block> CHROMATIC_CARPET = register("chromatic_carpet", 
            ChromaticBlock.Carpet::new, (VersionMethods.copyBlockProperties(Blocks.MAGENTA_CARPET)));
    public static final RegistrySupplier<IridescentKelpBlock> IRIDESCENT_KELP = register("iridescent_kelp",
            IridescentKelpBlock::new, (VersionMethods.copyBlockProperties(Blocks.KELP).mapColor(MapColor.COLOR_MAGENTA)));
    public static final RegistrySupplier<IridescentKelpBlock.Plant> IRIDESCENT_KELP_PLANT = register("iridescent_kelp_plant",
            IridescentKelpBlock.Plant::new, (VersionMethods.copyBlockProperties(Blocks.KELP).mapColor(MapColor.COLOR_MAGENTA)));
    public static final RegistrySupplier<Block> NOTES_BLOCK = register("notes_block", 
            NotesBlock::new, (VersionMethods.copyBlockProperties(Blocks.NOTE_BLOCK).randomTicks()));
    public static final RegistrySupplier<Block> HAUNTED_AIR = BLOCKS.register("haunted_air",
            ()-> new HauntedBlock(Blocks.AIR));
    public static final RegistrySupplier<RailHelper> RAIL_HELPER = register("rail_helper",
            RailHelper::new, (BlockBehaviour.Properties.of().strength(-1.0F, 3600000.8F).mapColor(MapColor.NONE)
                    .noLootTable().noOcclusion().isValidSpawn((state, getter, pos, type)-> false).pushReaction(PushReaction.BLOCK)
                    //? if >1.21 {
                    .noTerrainParticles()
                    //?}
                    ));

    public static void registerModBlocks() {
        InfinityMod.LOGGER.debug("Registering blocks for " + MOD_ID);
        BLOCKS.register();
    }

    public static void registerFlammableBlocks() {
        PlatformMethods.registerFlammableBlock(ModBlocks.IRIDESCENT_WOOL, 60, 30);
        PlatformMethods.registerFlammableBlock(ModBlocks.IRIDESCENT_CARPET, 20, 60);
        PlatformMethods.registerFlammableBlock(ModBlocks.CHROMATIC_WOOL, 60, 30);
        PlatformMethods.registerFlammableBlock(ModBlocks.CHROMATIC_CARPET, 20, 60);
    }

    private static <T extends Block> RegistrySupplier<T> register(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties settings) {
        return register(name, blockFactory, settings, true);
    }

    private static <T extends Block> RegistrySupplier<T> register(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        // Create a registry key for the block
        ResourceKey<Block> blockKey = keyOfBlock(name);
        // Create the block instance
        Supplier<Block> block = ()-> blockFactory.apply(settings.setId(blockKey));
        return (RegistrySupplier<T>) BLOCKS.register(name, block);
    }

    public static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, InfinityMethods.getId(name));
    }

    public static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, InfinityMethods.getId(name));
    }
}