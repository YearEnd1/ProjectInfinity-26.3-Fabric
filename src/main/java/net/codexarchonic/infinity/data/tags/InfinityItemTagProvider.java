package net.codexarchonic.infinity.data.tags;

//? fabric {

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.BuiltInRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.codexarchonic.infinity.registry.core.ModItems;
import net.codexarchonic.infinity.registry.var.ModTags;
import net.codexarchonic.infinity.util.VersionMethods;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

//~ if >26 '.ItemTagProvider'->'.ItemTagsProvider'
public class InfinityItemTagProvider extends FabricTagProvider.ItemTagProvider {
	public InfinityItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		builder(convention("buckets")).add(k(ModItems.IRIDESCENCE_BUCKET.get()));
		builder(ModTags.IRIDESCENT_ITEMS).add(
				k(ModItems.IRIDESCENT_WOOL.get()),
				k(ModItems.IRIDESCENT_CARPET.get()),
				k(ModItems.IRIDESCENCE_BUCKET.get()),
				k(ModItems.IRIDESCENT_STAR.get()),
				k(ModItems.IRIDESCENT_POTION.get()),
				k(ModItems.CHROMATIC_POTION.get()),
				k(ModItems.COSMIC_ALTAR_ITEM.get())
		);
		builder(ModTags.MATTER).add(
				k(ModItems.BLACK_MATTER.get()),
				k(ModItems.CHROMATIC_MATTER.get()),
				k(ModItems.WHITE_MATTER.get())
		);
		builder(ItemTags.WOOL).add(
				k(ModItems.IRIDESCENT_WOOL.get()),
				k(ModItems.CHROMATIC_WOOL.get())
		);
	    //? <1.21 {
		/*builder(ItemTags.MUSIC_DISCS).add(
				k(ModItems.DISC.get())
		);
	    *///?}
		builder(ItemTags.WOOL_CARPETS).add(
				k(ModItems.IRIDESCENT_CARPET.get()),
				k(ModItems.CHROMATIC_CARPET.get())
		);
	}

	//? <1.21.2 {
	/*private FabricTagProvider<Item>.FabricTagBuilder builder(TagKey<Item> buckets) {
		return getOrCreateTagBuilder(buckets);
	}
	*///?}

	private TagKey<Item> convention(String id) {
		return TagKey.create(Registries.ITEM, VersionMethods.id("c", id));
	}

	private TagKey<Item> infinity(String id) {
		return TagKey.create(Registries.ITEM, VersionMethods.id("infinity", id));
	}
	private static ResourceKey<Item> k(Item value) {
		return BuiltInRegistries.ITEM.getResourceKey(value).orElseThrow();
	}

}
//?}