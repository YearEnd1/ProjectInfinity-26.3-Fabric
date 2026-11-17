package net.codexarchonic.infinity.data.recipe;


import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.item.function.F4RechargingRecipe;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
//? >1.21 {
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.resources.ResourceKey;
//?} else {
/*import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.data.recipes.FinishedRecipe;
import net.codexarchonic.infinity.registry.core.ModItemFunctions;
import com.google.gson.JsonObject;
import net.minecraft.advancements.triggers.CriterionTriggerInstance;
*///?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

import static net.codexarchonic.infinity.registry.core.ModItems.F4;

public class F4RechargingRecipeBuilder implements RecipeBuilder {

	F4RechargingRecipeBuilder() {}

	@Override
	public RecipeBuilder group(@Nullable String groupName) {
		return null;
	}



	public Item getResult() {
		return F4.get();
	}

	//? if >1.21.2 {
	public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> id) {
		recipeOutput.accept(id, F4RechargingRecipe.INSTANCE, null);
	}
	public ResourceKey<Recipe<?>> defaultId() {
		return ResourceKey.create(Registries.RECIPE, InfinityMethods.getId("f4_recharging"));
	}
	//?} else if >1.21 {
	/*public void save(RecipeOutput recipeOutput, ResourceLocation id) {
		F4RechargingRecipe recipe = new F4RechargingRecipe(CraftingBookCategory.MISC);
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
			return InfinityMethods.getId("f4_recharging");
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ModItemFunctions.F4_RECHARGING.get();
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
