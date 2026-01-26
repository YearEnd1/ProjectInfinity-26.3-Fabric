package net.lerariemann.infinity.data.tags;

//? fabric {

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.registry.var.ModTags;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class InfinityItemTagProvider extends FabricTagProvider.ItemTagProvider {
	public InfinityItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		valueLookupBuilder(convention("buckets")).add(ModItems.IRIDESCENCE_BUCKET.get());
		valueLookupBuilder(ModTags.IRIDESCENT_ITEMS).add(
				ModItems.IRIDESCENT_WOOL.get(),
				ModItems.IRIDESCENT_CARPET.get(),
				ModItems.IRIDESCENCE_BUCKET.get(),
				ModItems.IRIDESCENT_STAR.get(),
				ModItems.IRIDESCENT_POTION.get(),
				ModItems.CHROMATIC_POTION.get(),
				ModItems.COSMIC_ALTAR_ITEM.get()
		);
		valueLookupBuilder(ModTags.MATTER).add(
				ModItems.BLACK_MATTER.get(),
				ModItems.CHROMATIC_MATTER.get(),
				ModItems.WHITE_MATTER.get()
		);
		valueLookupBuilder(ItemTags.WOOL).add(
				ModItems.IRIDESCENT_WOOL.get(),
				ModItems.CHROMATIC_WOOL.get()
		);
		valueLookupBuilder(ItemTags.WOOL_CARPETS).add(
				ModItems.IRIDESCENT_CARPET.get(),
				ModItems.CHROMATIC_CARPET.get()
		);
	}

	//? <1.21.2 {
	/*private FabricTagProvider<Item>.FabricTagBuilder valueLookupBuilder(TagKey<Item> buckets) {
		return getOrCreateTagBuilder(buckets);
	}
	*///?}

	private TagKey<Item> convention(String id) {
		return TagKey.create(Registries.ITEM, VersionMethods.id("c", id));
	}

	private TagKey<Item> infinity(String id) {
		return TagKey.create(Registries.ITEM, VersionMethods.id("infinity", id));
	}
}
//?}