package net.codexarchonic.infinity.data.recipe;


import net.codexarchonic.infinity.item.function.CollisionCraftingRecipe;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
//? >1.21 {
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.advancements.triggers.Criterion;
//?} else {
/*import net.minecraft.data.recipes.FinishedRecipe;
import net.codexarchonic.infinity.registry.core.ModItemFunctions;
import com.google.gson.JsonObject;
import net.minecraft.advancements.triggers.CriterionTriggerInstance;
*///?}
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
//? if >26
//import cc.cassian.rrv.backport.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.Locale;
import java.util.function.Consumer;

public class CollisionRecipeBuilder implements RecipeBuilder {
	private final CollisionCraftingRecipe.Type type;
	private final Ingredient input;
	private final Item output;
	private final String lore;

	CollisionRecipeBuilder(CollisionCraftingRecipe.Type type, Ingredient input, ItemLike output, String lore) {
		//? if >1.21 {
		if (lore.isEmpty()) {
			this.lore = "empty";
		} else {
			this.lore = lore;
		}
		//?} else {
		/*this.lore = lore;
		*///?}
		this.type = type;
		this.input = input;
		this.output = output.asItem();

	}

	@Override
	public RecipeBuilder group(@Nullable String groupName) {
		return null;
	}

	public ResourceLocation getDefaultId() {
		return InfinityMethods.getId(BuiltInRegistries.ITEM.getKey(output).getPath() + "_from_" + type.name().toLowerCase(Locale.ROOT));
	}

	public Item getResult() {
		return this.output;
	}

	//? if >1.21.2 && <26 {
	public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> id) {
		CollisionCraftingRecipe recipe = switch (type) {
			case IRIDESCENCE -> new CollisionCraftingRecipe.OfIridescence(input, output.getDefaultInstance(), lore);
			case PORTAL -> new CollisionCraftingRecipe.OfPortal(input, output.getDefaultInstance(), lore);
		};
		recipeOutput.accept(id, recipe, null);
	}
	//?}

	//? if >26 {
	/*public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> id) {
		CollisionCraftingRecipe recipe = switch (type) {
			case IRIDESCENCE -> new CollisionCraftingRecipe.OfIridescence(input, new ItemStackTemplate(output), lore);
			case PORTAL -> new CollisionCraftingRecipe.OfPortal(input, new ItemStackTemplate(output), lore);
		};
		recipeOutput.accept(id, recipe, null);
	}

	@Override
	public ResourceKey<Recipe<?>> defaultId() {
		return ResourceKey.create(Registries.RECIPE, getDefaultId());
	}
	*///?}

	//? >1.21 {
	public void save(RecipeOutput recipeOutput, ResourceLocation id) {
		//? if <1.21.2 {
		/*CollisionCraftingRecipe recipe = switch (type) {
			case IRIDESCENCE -> new CollisionCraftingRecipe.OfIridescence(input, output.getDefaultInstance(), lore);
			case PORTAL -> new CollisionCraftingRecipe.OfPortal(input, output.getDefaultInstance(), lore);
		};
		recipeOutput.accept(id, recipe, null);
		*///?} else {
		save(recipeOutput, ResourceKey.create(Registries.RECIPE, id));
		//?}
	}

	@Override
	public void save(RecipeOutput recipeOutput) {
		save(recipeOutput, getDefaultId());
	}

	@Override
	public void save(RecipeOutput recipeOutput, String id) {
		save(recipeOutput, ResourceLocation.parse(id));
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

	@Override
	public void save(Consumer<FinishedRecipe> finishedRecipeConsumer, String recipeId) {
		save(finishedRecipeConsumer, new ResourceLocation(recipeId));
	}

	private class CollisionRecipeResult implements FinishedRecipe {
		@Override
		public void serializeRecipeData(JsonObject json) {
			if (!lore.isEmpty()) {
				json.addProperty("lore", lore);
			}

			json.add("input", input.toJson());
			JsonObject result = new JsonObject();
			result.addProperty("item", BuiltInRegistries.ITEM.getKey(output).toString());
			json.add("output", result);
		}

		@Override
		public ResourceLocation getId() {
			return getDefaultId();
		}

		@Override
		public RecipeSerializer<?> getType() {
			return switch (type) {
				case IRIDESCENCE -> ModItemFunctions.IRIDESCENCE_CRAFTING.get();
				case PORTAL -> ModItemFunctions.PORTAL_CRAFTING.get();
			};
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
