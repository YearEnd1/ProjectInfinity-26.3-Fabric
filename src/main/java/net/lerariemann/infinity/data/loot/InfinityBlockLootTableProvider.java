package net.lerariemann.infinity.data.loot;

//? fabric {

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class InfinityBlockLootTableProvider extends FabricBlockLootTableProvider {

	public InfinityBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(dataOutput
				//? if >1.21
				, registryLookup
		);
	}

	@Override
	public void generate() {
		//todo altar
		dropSelf(ModBlocks.ANT.get());
		//todo biome bottle
		dropSelf(ModBlocks.BOOK_BOX.get());
		//todo chromatic carpet
		//todo chromatic wool
		dropSelf(ModBlocks.COSMIC_ALTAR.get());
		dropSelf(ModBlocks.CURSOR.get());
		dropSelf(ModBlocks.IRIDESCENT_CARPET.get());
		dropSelf(ModBlocks.IRIDESCENT_WOOL.get());
		dropOther(ModBlocks.IRIDESCENT_KELP.get(), Items.KELP);
		dropOther(ModBlocks.IRIDESCENT_KELP_PLANT.get(), Items.KELP);
		createSlabItemTable(ModBlocks.NETHERITE_SLAB.get());
		dropSelf(ModBlocks.NETHERITE_STAIRS.get());
		dropOther(ModBlocks.IRIDESCENCE_CAULDRON.get(), Items.CAULDRON);
	}
}
//?}