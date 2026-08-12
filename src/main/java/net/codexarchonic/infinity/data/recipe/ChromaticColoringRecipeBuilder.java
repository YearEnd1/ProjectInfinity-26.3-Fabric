package net.codexarchonic.infinity.data.recipe;


import net.codexarchonic.infinity.item.function.ChromaticColoringRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
//? >1.21 {
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.advancements.Criterion;
//?} else {
/*import net.minecraft.data.recipes.FinishedRecipe;
import net.codexarchonic.infinity.registry.core.ModItemFunctions;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTriggerInstance;
*///?}
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class ChromaticColoringRecipeBuilder implements RecipeBuilder {
	private final Ingredient input;
	private final Item output;

	ChromaticColoringRecipeBuilder(Ingredient input, ItemLike output) {
		this.input = input;
		this.output = output.asItem();

	}

	@Override
	public RecipeBuilder group(@Nullable String groupName) {
		return null;
	}



	@Override
	public Item getResult() {
		return this.output;
	}

	//? if >1.21.2 {
	public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> id) {
		ChromaticColoringRecipe recipe = new ChromaticColoringRecipe(input, output.getDefaultInstance());
		recipeOutput.accept(id, recipe, null);
	}
	//?}

	//? >1.21 {
	public void save(RecipeOutput recipeOutput, ResourceLocation id) {
		//? if <1.21.2 {
		/*ChromaticColoringRecipe recipe = new ChromaticColoringRecipe(input, output.getDefaultInstance());
		recipeOutput.accept(id, recipe, null);
		*///?} else {
		save(recipeOutput, ResourceKey.create(Registries.RECIPE, id));
		//?}
	}

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
		finishedRecipeConsumer.accept(new CollisionRecipeResult());
	}

	private class CollisionRecipeResult implements FinishedRecipe {
		@Override
		public void serializeRecipeData(JsonObject json) {
			json.add("input", input.toJson());
			JsonObject result = new JsonObject();
			result.addProperty("item", BuiltInRegistries.ITEM.getKey(output).toString());
			json.add("output", result);
		}

		@Override
		public ResourceLocation getId() {
			return BuiltInRegistries.ITEM.getKey(output);
		}

		@Override
		public RecipeSerializer<?> getType() {
			return  ModItemFunctions.CHROMATIC_COLORING.get();
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
