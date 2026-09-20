package net.codexarchonic.infinity.data.tags;

//? fabric {

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.codexarchonic.infinity.registry.core.ModEntities;
import net.codexarchonic.infinity.util.VersionMethods;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

//~ if >26 '.EntityTypeTagProvider'->'.EntityTypeTagsProvider'
public class InfinityEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider {
	public InfinityEntityTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		//? if >1.21 {
		valueLookupBuilder(EntityTypeTags.ARTHROPOD).add(
				ModEntities.ANT.get()
		);
		//?}
		valueLookupBuilder(EntityTypeTags.SKELETONS).add(
				ModEntities.CHAOS_SKELETON.get()
		);
		valueLookupBuilder(EntityTypeTags.FROG_FOOD).add(
				ModEntities.CHAOS_SLIME.get()
		);
	}

	//? <1.21.2 {
	/*private FabricTagProvider<EntityType<?>>.FabricTagBuilder valueLookupBuilder(TagKey<EntityType<?>> buckets) {
		return getOrCreateTagBuilder(buckets);
	}
	*///?}

	private TagKey<EntityType<?>> convention(String id) {
		return TagKey.create(Registries.ENTITY_TYPE, VersionMethods.id("c", id));
	}

	private TagKey<EntityType<?>> infinity(String id) {
		return TagKey.create(Registries.ENTITY_TYPE, VersionMethods.id("infinity", id));
	}
}
//?}