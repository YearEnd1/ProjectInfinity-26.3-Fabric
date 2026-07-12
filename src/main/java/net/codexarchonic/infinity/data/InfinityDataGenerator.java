package net.codexarchonic.infinity.data;

//? fabric {

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.codexarchonic.infinity.data.loot.InfinityBlockLootTableProvider;
import net.codexarchonic.infinity.data.recipe.InfinityRecipeProvider;
import net.codexarchonic.infinity.data.tags.InfinityBlockTagProvider;
import net.codexarchonic.infinity.data.tags.InfinityEntityTagProvider;
import net.codexarchonic.infinity.data.tags.InfinityFluidTagProvider;
import net.codexarchonic.infinity.data.tags.InfinityItemTagProvider;

public class InfinityDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		//tags
		pack.addProvider(InfinityBlockTagProvider::new);
		pack.addProvider(InfinityItemTagProvider::new);
		pack.addProvider(InfinityFluidTagProvider::new);
		pack.addProvider(InfinityEntityTagProvider::new);
		//loot tables
		pack.addProvider(InfinityBlockLootTableProvider::new);
		// advancements - buggy on 1.20 and really don't change enough to matter
//		pack.addProvider(InfinityAdvancementProvider::new);
		// recipes
		pack.addProvider(InfinityRecipeProvider::new);
	}
}
//?}