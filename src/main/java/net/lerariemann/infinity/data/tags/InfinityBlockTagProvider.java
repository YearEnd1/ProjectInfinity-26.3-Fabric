package net.lerariemann.infinity.data.tags;

//? fabric {

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.var.ModTags;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class InfinityBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public InfinityBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		valueLookupBuilder(ModTags.IRIDESCENT_BLOCKS).add(
				ModBlocks.IRIDESCENT_WOOL.get(),
				ModBlocks.IRIDESCENT_CARPET.get(),
				ModBlocks.IRIDESCENCE.get(),
				ModBlocks.COSMIC_ALTAR.get()
		);
		valueLookupBuilder(BlockTags.WOOL).add(
				ModBlocks.IRIDESCENT_WOOL.get(),
				ModBlocks.CHROMATIC_WOOL.get()
		);
		valueLookupBuilder(BlockTags.WOOL_CARPETS).add(
				ModBlocks.IRIDESCENT_CARPET.get(),
				ModBlocks.CHROMATIC_CARPET.get()
		);
		valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE).add(
				ModBlocks.BOOK_BOX.get()
		);
		valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(
				ModBlocks.ALTAR.get(),
				ModBlocks.CURSOR.get(),
				ModBlocks.NETHERITE_SLAB.get(),
				ModBlocks.NETHERITE_STAIRS.get(),
				ModBlocks.BIOME_BOTTLE.get(),
				ModBlocks.ANT.get(),
				ModBlocks.COSMIC_ALTAR.get(),
				ModBlocks.IRIDESCENCE_CAULDRON.get()
		);
		valueLookupBuilder(BlockTags.PORTALS).add(
				ModBlocks.PORTAL.get()
		);
		valueLookupBuilder(BlockTags.SLABS).add(
				ModBlocks.NETHERITE_SLAB.get()
		);
		valueLookupBuilder(BlockTags.STAIRS).add(
				ModBlocks.NETHERITE_STAIRS.get()
		);
		valueLookupBuilder(BlockTags.CAULDRONS).add(
				ModBlocks.IRIDESCENCE_CAULDRON.get()
		);
	}

	//? <1.21.2 {
	/*private FabricTagProvider<Block>.FabricTagBuilder valueLookupBuilder(TagKey<Block> buckets) {
		return getOrCreateTagBuilder(buckets);
	}
	*///?}

	private TagKey<Block> convention(String id) {
		return TagKey.create(Registries.BLOCK, VersionMethods.id("c", id));
	}

	private TagKey<Block> infinity(String id) {
		return TagKey.create(Registries.BLOCK, VersionMethods.id("infinity", id));
	}
}
//?}