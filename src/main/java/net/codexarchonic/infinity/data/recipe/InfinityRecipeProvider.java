package net.codexarchonic.infinity.data.recipe;

//? fabric {

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.codexarchonic.infinity.item.function.CollisionCraftingRecipe;
import net.codexarchonic.infinity.registry.var.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
//?if <1.21 {
/*import java.util.function.Consumer;
*///?}
import static net.codexarchonic.infinity.registry.core.ModItems.*;
import static net.minecraft.data.recipes.RecipeCategory.BUILDING_BLOCKS;
import static net.minecraft.data.recipes.RecipeCategory.MISC;
import static net.minecraft.world.item.Items.*;

public class InfinityRecipeProvider extends FabricRecipeProvider {
	public InfinityRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output
				//? if >1.21 {
				, registriesFuture
				//?}
		);
	}


	//? if >1.21.6 {
	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
		return new RecipeProvider(registryLookup, exporter) {

	@Override
	public void buildRecipes() {

	//?} else if >1.21 {

	/*@Override
	public void buildRecipes(RecipeOutput output) {

	*///?} else {
	/*@Override
	public void buildRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> output) {
	*///?}


		// start recipes

		shaped(RecipeCategory.REDSTONE, ALTAR_ITEM.get())
			.pattern("SSS")
			.pattern(" B ")
			.pattern(" S ")
			.define('S', STONE_SLAB)
			.define('B', BOOK_BOX_ITEM.get())
			// Create an advancement that gives you the recipe
			.unlockedBy(getHasName(BOOK_BOX_ITEM.get()), has(BOOK_BOX_ITEM.get()))
			.save(output);

		shaped(BUILDING_BLOCKS, ANT_ITEM.get())
			.pattern("WBW")
			.pattern("BMB")
			.pattern("WBW")
			.define('M', STAR_OF_LANG.get())
			.define('W', WHITE_CONCRETE)
			.define('B', BLACK_CONCRETE)
			// Create an advancement that gives you the recipe
			.unlockedBy(getHasName(STAR_OF_LANG.get()), has(STAR_OF_LANG.get()))
			.save(output);

		shaped(BUILDING_BLOCKS, ANT_ITEM.get())
			.pattern("WBW")
			.pattern("B B")
			.pattern("WBW")
			.define('W', WHITE_MATTER.get())
			.define('B', BLACK_MATTER.get())
			.unlockedBy(getHasName(WHITE_MATTER.get()), has(WHITE_MATTER.get()))
			.save(output, "infinity:ant1");

		shaped(MISC, COSMIC_ALTAR_ITEM.get())
			.pattern("LAL")
			.pattern("ASA")
			.pattern("LAL")
			.define('L', LODESTONE)
			.define('A', ALTAR_ITEM.get())
			.define('S', IRIDESCENT_STAR.get())
			// Create an advancement that gives you the recipe
			.unlockedBy(getHasName(IRIDESCENT_STAR.get()), has(IRIDESCENT_STAR.get()))
			.save(output);

		shaped(BUILDING_BLOCKS, CURSOR_ITEM.get(), 8)
			.pattern("BLB")
			.pattern("LSL")
			.pattern("BLB")
			.define('S', STAR_OF_LANG.get())
			.define('L', LIME_CONCRETE)
			.define('B', BLACK_CONCRETE)
			// Create an advancement that gives you the recipe
			.unlockedBy(getHasName(STAR_OF_LANG.get()), has(STAR_OF_LANG.get()))
			.save(output);

		shaped(MISC, F4.get())
			.pattern("OOO")
			.pattern("OSO")
			.pattern("OOO")
			.define('O', OBSIDIAN)
			.define('S', IRIDESCENT_STAR.get())
			// Create an advancement that gives you the recipe
			.unlockedBy(getHasName(IRIDESCENT_STAR.get()), has(IRIDESCENT_STAR.get()))
			.save(output);

		shaped(MISC, FOOTPRINT.get(), 8)
			.pattern("PPP")
			.pattern("P P")
			.pattern("PPP")
			.define('P', LIGHT_GRAY_STAINED_GLASS_PANE)
			// Create an advancement that gives you the recipe
			.unlockedBy(getHasName(LIGHT_GRAY_STAINED_GLASS_PANE), has(LIGHT_GRAY_STAINED_GLASS_PANE))
			.save(output);


		shaped(MISC, HOME_ITEM.get())
			.pattern("GGG")
			.pattern("GSG")
			.pattern("GGG")
			.define('S', STAR_OF_LANG.get())
			.define('G', GRASS_BLOCK)
			// Create an advancement that gives you the recipe
			.unlockedBy(getHasName(STAR_OF_LANG.get()), has(STAR_OF_LANG.get()))
			.save(output);

		shaped(BUILDING_BLOCKS, NETHERITE_SLAB_ITEM.get(), 6)
			.pattern("###")
			.define('#', NETHERITE_BLOCK)
			// Create an advancement that gives you the recipe
			.unlockedBy(getHasName(NETHERITE_BLOCK), has(NETHERITE_BLOCK))
			.save(output);

		shaped(BUILDING_BLOCKS, NETHERITE_STAIRS_ITEM.get(), 4)
			.pattern("#  ")
			.pattern("## ")
			.pattern("###")
			.define('#', NETHERITE_BLOCK)
			// Create an advancement that gives you the recipe
			.unlockedBy(getHasName(NETHERITE_BLOCK), has(NETHERITE_BLOCK))
			.save(output);

		stonecutterResultFromBase(output, BUILDING_BLOCKS, NETHERITE_BLOCK, NETHERITE_STAIRS_ITEM.get());
		stonecutterResultFromBase(output, BUILDING_BLOCKS, NETHERITE_BLOCK, NETHERITE_SLAB_ITEM.get(), 2);


		shaped(MISC, BLACK_MATTER.get())
			.pattern("BBB")
			.pattern("B B")
			.pattern("BBB")
			.define('B', BLACK_CONCRETE)
			// Create an advancement that gives you the recipe
			.unlockedBy(getHasName(BLACK_CONCRETE), has(BLACK_CONCRETE))
			.save(output);

		shaped(MISC, WHITE_MATTER.get())
			.pattern("BBB")
			.pattern("B B")
			.pattern("BBB")
			.define('B', WHITE_CONCRETE)
			// Create an advancement that gives you the recipe
			.unlockedBy(getHasName(WHITE_CONCRETE), has(WHITE_CONCRETE))
			.save(output);

		shaped(BUILDING_BLOCKS, IRIDESCENT_CARPET.get(), 3)
			.pattern("BB")
			.define('B', IRIDESCENT_WOOL.get())
			// Create an advancement that gives you the recipe
			.unlockedBy(getHasName(IRIDESCENT_WOOL.get()), has(IRIDESCENT_WOOL.get()))
			.save(output, "infinity:iridescent_carpet_from_wool");

		portal(LECTERN, ALTAR_ITEM.get()).save(output);
		portal(GLASS_BOTTLE, BIOME_BOTTLE_ITEM.get()).save(output);
		portal(BOOKSHELF, BOOK_BOX_ITEM.get()).save(output);
		portal(ItemTags.WOOL, CHROMATIC_WOOL).save(output, "infinity:chromatic_wool");
		portal(F4, F4, "recipe_info.portal.infinity.attuned").save(output);
		portal(CHROMATIC_POTION, IRIDESCENT_POTION, "").save(output, "infinity:iridescence_bottle_from_portal");
		iridescence(CHROMATIC_WOOL, IRIDESCENT_WOOL).save(output, "infinity:iridescent_wool");
		iridescence(CHROMATIC_CARPET, IRIDESCENT_CARPET).save(output, "infinity:iridescent_carpet");
		iridescence(STAR_OF_LANG, IRIDESCENT_STAR).save(output, "infinity:iridescent_star");
		portal(ItemTags.WOOL_CARPETS, CHROMATIC_CARPET, "recipe_info.portal.infinity.color").save(output);
		portal(ModTags.MATTER, CHROMATIC_MATTER).save(output, "infinity:chromatic_matter");
		iridescence(CHROMATIC_MATTER, CHROMATIC_MATTER, "recipe_info.iridescence.infinity.chromatic_matter").save(output);
		portal(AMETHYST_SHARD, TRANSFINITE_KEY.get(), "recipe_info.portal.infinity.attuned").save(output, "infinity:key_from_portal");
		iridescence(TRANSFINITE_KEY, TRANSFINITE_KEY, "recipe_info.iridescence.infinity.key").save(output, "infinity:key_from_iridescence");
		portal(TNT, TIME_BOMB_ITEM.get()).save(output, "infinity:timebomb_from_portal");
		color(ItemTags.WOOL, CHROMATIC_WOOL).save(output, "infinity:chromatic_coloring_wool");
		color(ItemTags.WOOL_CARPETS, CHROMATIC_CARPET).save(output, "infinity:chromatic_coloring_carpet");
		new BiomeBottleCombiningRecipeBuilder().save(output, "infinity:biome_bottle_combining");
		new ChromaCarpetRecipeBuilder().save(output, "infinity:chromatic_carpet_from_wool");
		new F4RechargingRecipeBuilder().save(output, "infinity:f4_recharging");

		// end recipes
		}

			private RecipeBuilder color(TagKey<Item> input, Supplier<? extends Item> output) {

				//? if >1.21.2 {
				return new ChromaticColoringRecipeBuilder(tag(input), output.get());
				 //?} else {
				/*return new ChromaticColoringRecipeBuilder(Ingredient.of(input), output.get());
				*///?}
			}
			private RecipeBuilder portal(TagKey<Item> input, Supplier<? extends Item> output) {
				//? if >1.21.2 {
				return portal(tag(input), output.get());
				//?} else {
				/*return portal(Ingredient.of(input), output.get());
				*///?}
			}

			private RecipeBuilder portal(TagKey<Item> input, Supplier<? extends Item> output, String lore) {
				//? if >1.21.2 {
				return collision(CollisionCraftingRecipe.Type.PORTAL, tag(input), output.get(), lore);
				//?} else {
				/*return collision(CollisionCraftingRecipe.Type.PORTAL, Ingredient.of(input), output.get(), lore);
				 *///?}
			}


			private CollisionRecipeBuilder collision(CollisionCraftingRecipe.Type type, Ingredient input, ItemLike output, String lore) {
				return new CollisionRecipeBuilder(type, input, output, lore);
			}
			private CollisionRecipeBuilder collision(CollisionCraftingRecipe.Type type, ItemLike input, ItemLike output, String lore) {
				return collision(type, Ingredient.of(input), output, lore);
			}

			private CollisionRecipeBuilder collision(CollisionCraftingRecipe.Type type, ItemLike input, ItemLike output) {
				return collision(type, input, output, "");
			}

			private CollisionRecipeBuilder portal(ItemLike input, ItemLike output) {
				return collision(CollisionCraftingRecipe.Type.PORTAL, input, output);
			}
			private CollisionRecipeBuilder portal(Ingredient input, ItemLike output) {
				return collision(CollisionCraftingRecipe.Type.PORTAL, input, output, "");
			}

			private CollisionRecipeBuilder portal(ItemLike input, ItemLike output, String lore) {
				return collision(CollisionCraftingRecipe.Type.PORTAL, input, output, lore);
			}

			private RecipeBuilder portal(Ingredient input, Item output, String lore) {
				return collision(CollisionCraftingRecipe.Type.PORTAL, input, output, lore);
			}

			private CollisionRecipeBuilder iridescence(ItemLike input, ItemLike output) {
				return collision(CollisionCraftingRecipe.Type.IRIDESCENCE, input, output);
			}

			private CollisionRecipeBuilder iridescence(ItemLike input, ItemLike output, String lore) {
				return collision(CollisionCraftingRecipe.Type.IRIDESCENCE, input, output, lore);
			}

			private RecipeBuilder portal(Supplier<? extends Item> input, Supplier<? extends Item> output, String lore) {
				return portal(input.get(), output.get(), lore);
			}

			private CollisionRecipeBuilder iridescence(Supplier<? extends Item> input, Supplier<? extends Item> output, String lore) {
				return iridescence(input.get(), output.get(), lore);
			}

			private CollisionRecipeBuilder iridescence(Supplier<? extends Item> input, Supplier<? extends Item> output) {
				return iridescence(input.get(), output.get());
			}

	//? if <1.21.6 {
			/*private ShapedRecipeBuilder shaped(RecipeCategory recipeCategory, Item item) {
				return ShapedRecipeBuilder.shaped(recipeCategory, item);
			}
			private ShapedRecipeBuilder shaped(RecipeCategory recipeCategory, Item item, int i) {
				return ShapedRecipeBuilder.shaped(recipeCategory, item, i);
			}
			*///?} else {
			private void stonecutterResultFromBase(RecipeOutput output, RecipeCategory recipeCategory, Item item1, Item item) {
				stonecutterResultFromBase(recipeCategory, item1, item);
			}
			private void stonecutterResultFromBase(RecipeOutput output, RecipeCategory recipeCategory, Item item1, Item item, int i) {
				stonecutterResultFromBase(recipeCategory, item1, item, i);
			}
			};
	}
			//?}

	@Override
	public String getName() {
		return "infinity";
	}
}
//?}