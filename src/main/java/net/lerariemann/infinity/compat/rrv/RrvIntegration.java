package net.lerariemann.infinity.compat.rrv;

//? if >1.21.7 {

import cc.cassian.rrv.api.ReliableRecipeViewerPlugin;
import cc.cassian.rrv.api.recipe.ItemView;
import cc.cassian.rrv.common.recipe.ServerRecipeManager;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.compat.rrv.iridescence_crafting.IridescenceCraftingServerRecipe;
import net.lerariemann.infinity.compat.rrv.iridescence_crafting.IridescenceCraftingViewRecipe;
import net.lerariemann.infinity.compat.rrv.portal_crafting.PortalCraftingServerRecipe;
import net.lerariemann.infinity.compat.rrv.portal_crafting.PortalCraftingViewRecipe;
import net.lerariemann.infinity.item.function.CollisionCraftingRecipe;

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
        });
        // Portal Crafting - Clientside
        ItemView.addClientRecipeWrapper(PortalCraftingServerRecipe.TYPE, modRecipe -> Collections.singletonList(new PortalCraftingViewRecipe(modRecipe)));
        // Iridescence Crafting - Clientside
        ItemView.addClientRecipeWrapper(IridescenceCraftingServerRecipe.TYPE, modRecipe -> Collections.singletonList(new IridescenceCraftingViewRecipe(modRecipe)));

    }
}
//?}