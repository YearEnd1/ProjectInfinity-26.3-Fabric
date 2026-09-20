package net.codexarchonic.infinity.compat.rrv;

//? if >1.21.7 {

import cc.cassian.rrv.api.ReliableRecipeViewerPlugin;
import cc.cassian.rrv.api.recipe.ItemView;
//~ if >26 'Server'->'Client'
import cc.cassian.rrv.common.builtin.brewing.BrewingServerRecipe;
import cc.cassian.rrv.common.recipe.ServerRecipeManager;
import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.compat.rrv.iridescence_crafting.IridescenceCraftingServerRecipe;
import net.codexarchonic.infinity.compat.rrv.iridescence_crafting.IridescenceCraftingViewRecipe;
import net.codexarchonic.infinity.compat.rrv.portal_crafting.PortalCraftingServerRecipe;
import net.codexarchonic.infinity.compat.rrv.portal_crafting.PortalCraftingViewRecipe;
import net.codexarchonic.infinity.item.function.CollisionCraftingRecipe;
import net.codexarchonic.infinity.registry.core.ModItems;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Collections;

public class RrvIntegration implements ReliableRecipeViewerPlugin {
    boolean initialized = false;
    @Override
    public void onIntegrationInitialize() {
        if (!initialized)
            initialize();
        initialized = true;
    }

    public static void initialize() {
        InfinityMod.LOGGER.info("Initializing Infinite Dimensions - RRV integration");
        // Serverside recipes
        ItemView.addServerRecipeProvider(recipeList -> {
            // Portal Crafting - serverside
            ServerRecipeManager.INSTANCE.getRecipesForType(CollisionCraftingRecipe.Type.PORTAL).forEach(recipe -> recipeList.add(new PortalCraftingServerRecipe(recipe.getInput(), recipe.getResultItem(), recipe.getLore())));
            // Iridescence Crafting - serverside
            ServerRecipeManager.INSTANCE.getRecipesForType(CollisionCraftingRecipe.Type.IRIDESCENCE).forEach(recipe -> recipeList.add(new IridescenceCraftingServerRecipe(recipe.getInput(), recipe.getResultItem(), recipe.getLore())));
            // Chromatic Potions
            //? if <26
            recipeList.add(new BrewingServerRecipe(ModItems.CHROMATIC_POTION.get().getDefaultInstance(), Ingredient.of(ModItems.CHROMATIC_MATTER.get()), PotionContents.createItemStack(Items.POTION, Potions.AWKWARD)));
        });
        //? if >26 {
        /*ItemView.addClientRecipeProvider(recipeList->{
            recipeList.add(new BrewingClientRecipe(InfinityMethods.getId("chromatic_potion"), ModItems.CHROMATIC_POTION.get().getDefaultInstance(), Ingredient.of(ModItems.CHROMATIC_MATTER.get()), PotionContents.createItemStack(Items.POTION, Potions.AWKWARD)));
        });
        *///?}
        // Portal Crafting - Clientside
        ItemView.addClientRecipeWrapper(PortalCraftingServerRecipe.TYPE, modRecipe -> Collections.singletonList(new PortalCraftingViewRecipe(modRecipe)));
        // Iridescence Crafting - Clientside
        ItemView.addClientRecipeWrapper(IridescenceCraftingServerRecipe.TYPE, modRecipe -> Collections.singletonList(new IridescenceCraftingViewRecipe(modRecipe)));

    }
}
//?}