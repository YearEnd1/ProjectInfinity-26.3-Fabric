package net.codexarchonic.infinity.data.recipe;


import net.minecraft.data.recipes.RecipeBuilder;
//? >1.21 {
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.advancements.Criterion;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.resources.ResourceKey;
import net.codexarchonic.infinity.item.function.ChromaCarpetRecipe;
//?} else {
/*import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.data.recipes.FinishedRecipe;
import net.codexarchonic.infinity.registry.core.ModItemFunctions;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTriggerInstance;
*///?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

import static net.codexarchonic.infinity.registry.core.ModBlocks.BIOME_BOTTLE;

public class ChromaCarpetRecipeBuilder implements RecipeBuilder {

	ChromaCarpetRecipeBuilder() {}

	@Override
	public RecipeBuilder group(@Nullable String groupName) {
		return null;
	}

	@Override
	public Item getResult() {
		return BIOME_BOTTLE.get().asItem();
	}

	//? if >1.21.2 {
	public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> id) {
		ChromaCarpetRecipe recipe = new ChromaCarpetRecipe(CraftingBookCategory.MISC);
		recipeOutput.accept(id, recipe, null);
	}
	//?} else if >1.21 {
	/*public void save(RecipeOutput recipeOutput, ResourceLocation id) {
		ChromaCarpetRecipe recipe = new ChromaCarpetRecipe(CraftingBookCategory.MISC);
		recipeOutput.accept(id, recipe, null);
	}
	*///?}

	//? >1.21 {

	@Override
	public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
		return null;
	}
	//?} else {
	/*@Override
	public RecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
		return null;
	}

	@Override
	public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, ResourceLocation recipeId) {
		finishedRecipeConsumer.accept(new CombiningRecipeResult());
	}

	private static class CombiningRecipeResult implements FinishedRecipe {
		@Override
		public void serializeRecipeData(JsonObject json) {
			json.addProperty("category", "misc");
		}

		@Override
		public ResourceLocation getId() {
			return InfinityMethods.getId("chromatic_carpet_from_wool");
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ModItemFunctions.CARPET.get();
		}

		@Override
		public @Nullable JsonObject serializeAdvancement() {
			return null;
		}

		@Override
		public @Nullable ResourceLocation getAdvancementId() {
			return null;
		}
	}
	*///?}

}
