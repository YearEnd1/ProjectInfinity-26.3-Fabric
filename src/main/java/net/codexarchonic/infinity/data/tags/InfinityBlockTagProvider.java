package net.codexarchonic.infinity.data.tags;

//? fabric {

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.BuiltInRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.codexarchonic.infinity.registry.core.ModBlocks;
import net.codexarchonic.infinity.registry.var.ModTags;
import net.codexarchonic.infinity.util.VersionMethods;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

//~ if >26 '.BlockTagProvider'->'.BlockTagsProvider'
public class InfinityBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public InfinityBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		builder(ModTags.IRIDESCENT_BLOCKS).add(
				k(ModBlocks.IRIDESCENT_WOOL.get()),
				k(ModBlocks.IRIDESCENT_CARPET.get()),
				k(ModBlocks.IRIDESCENCE.get()),
				k(ModBlocks.COSMIC_ALTAR.get())
		);
		builder(BlockTags.WOOL).add(
				k(ModBlocks.IRIDESCENT_WOOL.get()),
				k(ModBlocks.CHROMATIC_WOOL.get())
		);
		builder(BlockTags.WOOL_CARPETS).add(
				k(ModBlocks.IRIDESCENT_CARPET.get()),
				k(ModBlocks.CHROMATIC_CARPET.get())
		);
		builder(BlockTags.MINEABLE_WITH_AXE).add(
				k(ModBlocks.BOOK_BOX.get())
		);
		builder(BlockTags.MINEABLE_WITH_PICKAXE).add(
				k(ModBlocks.ALTAR.get()),
				k(ModBlocks.CURSOR.get()),
				k(ModBlocks.NETHERITE_SLAB.get()),
				k(ModBlocks.NETHERITE_STAIRS.get()),
				k(ModBlocks.BIOME_BOTTLE.get()),
				k(ModBlocks.ANT.get()),
				k(ModBlocks.COSMIC_ALTAR.get()),
				k(ModBlocks.IRIDESCENCE_CAULDRON.get())
		);
		builder(BlockTags.PORTALS).add(
				k(ModBlocks.PORTAL.get())
		);
		builder(BlockTags.SLABS).add(
				k(ModBlocks.NETHERITE_SLAB.get())
		);
		builder(BlockTags.STAIRS).add(
				k(ModBlocks.NETHERITE_STAIRS.get())
		);
		builder(BlockTags.CAULDRONS).add(
				k(ModBlocks.IRIDESCENCE_CAULDRON.get())
		);
	}

	//? <1.21.2 {
	/*private FabricTagProvider<Block>.FabricTagBuilder builder(TagKey<Block> buckets) {
		return getOrCreateTagBuilder(buckets);
	}
	*///?}

	private TagKey<Block> convention(String id) {
		return TagKey.create(Registries.BLOCK, VersionMethods.id("c", id));
	}

	private TagKey<Block> infinity(String id) {
		return TagKey.create(Registries.BLOCK, VersionMethods.id("infinity", id));
	}
	private static ResourceKey<Block> k(Block value) {
		return BuiltInRegistries.BLOCK.getResourceKey(value).orElseThrow();
	}

}
//?}