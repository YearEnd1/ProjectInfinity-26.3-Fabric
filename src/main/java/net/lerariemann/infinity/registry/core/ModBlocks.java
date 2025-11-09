package net.lerariemann.infinity.registry.core;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.util.platform.PlatformMethods;
import net.lerariemann.infinity.block.custom.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, Registries.BLOCK);

    public static final RegistrySupplier<Block> COSMIC_ALTAR = BLOCKS.register("cosmic_altar", () ->
            new CosmicAltarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).lightLevel(state -> 15).noOcclusion()));
    public static final RegistrySupplier<Block> ALTAR = BLOCKS.register("altar", () ->
            new AltarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion().lightLevel(state -> state.getValue(AltarBlock.FLOWER) ? 15 : 0)));
    public static final RegistrySupplier<Block> ANT = BLOCKS.register("ant", () ->
            new AntBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE)));
    public static final RegistrySupplier<Block> BOOK_BOX = BLOCKS.register("book_box", () ->
            new BookBoxBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BOOKSHELF)));
    public static final RegistrySupplier<Block> CURSOR = BLOCKS.register("cursor", () ->
            new Block(BlockBehaviour.Properties.of().strength(1.8f).mapColor(MapColor.COLOR_GREEN).sound(SoundType.STONE)));
    public static final RegistrySupplier<Block> PORTAL = BLOCKS.register("neither_portal", () ->
            new InfinityPortalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_PORTAL)));
    public static final RegistrySupplier<Block> NETHERITE_STAIRS = BLOCKS.register("netherite_stairs", () ->
            new ModStairsBlock(Blocks.NETHERITE_BLOCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)));
    public static final RegistrySupplier<Block> NETHERITE_SLAB = BLOCKS.register("netherite_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)));
    public static final RegistrySupplier<Block> TIME_BOMB = BLOCKS.register("timebomb", () ->
            new TimeBombBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK).noOcclusion().lightLevel(state -> 15)));
    public static final RegistrySupplier<LiquidBlock> IRIDESCENCE = PlatformMethods.getIridBlockForReg();
    public static final RegistrySupplier<Block> BIOME_BOTTLE = BLOCKS.register("biome_bottle", () ->
            new BiomeBottleBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEACON).lightLevel(state -> state.getValue(BiomeBottleBlock.LEVEL))
                    .sound(SoundType.GLASS)));
    public static final RegistrySupplier<Block> IRIDESCENT_WOOL = BLOCKS.register("iridescent_wool", () ->
            new IridescentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_WOOL)));
    public static final RegistrySupplier<Block> IRIDESCENT_CARPET = BLOCKS.register("iridescent_carpet", () ->
            new IridescentBlock.Carpet(BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_CARPET)));
    public static final RegistrySupplier<Block> CHROMATIC_WOOL = BLOCKS.register("chromatic_wool", () ->
            new ChromaticBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_WOOL)));
    public static final RegistrySupplier<Block> CHROMATIC_CARPET = BLOCKS.register("chromatic_carpet", () ->
            new ChromaticBlock.Carpet(BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_CARPET)));
    public static final RegistrySupplier<IridescentKelpBlock> IRIDESCENT_KELP = BLOCKS.register("iridescent_kelp", () ->
            new IridescentKelpBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.KELP).mapColor(MapColor.COLOR_MAGENTA)));
    public static final RegistrySupplier<IridescentKelpBlock.Plant> IRIDESCENT_KELP_PLANT = BLOCKS.register("iridescent_kelp_plant", () ->
            new IridescentKelpBlock.Plant(BlockBehaviour.Properties.ofFullCopy(Blocks.KELP).mapColor(MapColor.COLOR_MAGENTA)));
    public static final RegistrySupplier<Block> NOTES_BLOCK = BLOCKS.register("notes_block", () ->
            new NotesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NOTE_BLOCK).randomTicks()));
    public static final RegistrySupplier<Block> HAUNTED_AIR = BLOCKS.register("haunted_air", () ->
            new HauntedBlock(Blocks.AIR));
    public static final RegistrySupplier<RailHelper> RAIL_HELPER = BLOCKS.register("rail_helper", () ->
            new RailHelper(BlockBehaviour.Properties.of().strength(-1.0F, 3600000.8F).mapColor(MapColor.NONE).noLootTable().noOcclusion().isValidSpawn(Blocks::never).noTerrainParticles().pushReaction(PushReaction.BLOCK)));

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
}