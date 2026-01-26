package net.lerariemann.infinity.data;

//? fabric {

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.lerariemann.infinity.data.loot.InfinityBlockLootTableProvider;
import net.lerariemann.infinity.data.recipe.InfinityRecipeProvider;
import net.lerariemann.infinity.data.tags.InfinityBlockTagProvider;
import net.lerariemann.infinity.data.tags.InfinityEntityTagProvider;
import net.lerariemann.infinity.data.tags.InfinityFluidTagProvider;
import net.lerariemann.infinity.data.tags.InfinityItemTagProvider;

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