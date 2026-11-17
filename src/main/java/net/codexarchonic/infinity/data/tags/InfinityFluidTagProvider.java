package net.codexarchonic.infinity.data.tags;

//? fabric {

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.BuiltInRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.codexarchonic.infinity.util.VersionMethods;
import net.codexarchonic.infinity.util.platform.fabric.ModFluidsFabric;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import java.util.concurrent.CompletableFuture;

public class InfinityFluidTagProvider extends FabricTagProvider.FluidTagProvider {
	public InfinityFluidTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		builder(FluidTags.WATER).add(
				k(ModFluidsFabric.IRIDESCENCE_FLOWING.get()),
				k(ModFluidsFabric.IRIDESCENCE_STILL.get())
		);
	}

	//? <1.21.2 {
	/*private FabricTagProvider<Fluid>.FabricTagBuilder builder(TagKey<Fluid> buckets) {
		return getOrCreateTagBuilder(buckets);
	}
	*///?}

	private TagKey<Fluid> convention(String id) {
		return TagKey.create(Registries.FLUID, VersionMethods.id("c", id));
	}

	private TagKey<Fluid> infinity(String id) {
		return TagKey.create(Registries.FLUID, VersionMethods.id("infinity", id));
	}
	private static ResourceKey<Fluid> k(Fluid value) {
		return BuiltInRegistries.FLUID.getResourceKey(value).orElseThrow();
	}

}
//?}