package net.lerariemann.infinity.compat.emi;

//? if <1.21.2 {

/*import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.lerariemann.infinity.item.function.CollisionCraftingRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
//? if >1.21 {
import net.minecraft.world.item.crafting.RecipeHolder;
//?}
import java.util.Objects;

public class IridesenceCraftingEmiRecipe extends BasicEmiRecipe {
    String lore;

    public IridesenceCraftingEmiRecipe(ResourceLocation id, CollisionCraftingRecipe recipe, RegistryAccess registryManager) {
        super(EmiCompat.IRIDESCENCE_CRAFTING, id, 118, 18);
        this.inputs.add(EmiIngredient.of(recipe.getInput()));
        this.outputs.add(EmiStack.of(recipe.getResultItem(registryManager)));
        this.lore = recipe.getLore();
        if (!Objects.equals(lore, "empty")) {
            this.width = 130;
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(inputs.get(0), 0, 0);
        widgets.addTexture(EmiTexture.PLUS, 26, 2);
        widgets.addSlot(EmiCompat.IRIDESCENCE_WORKSTATION, 48, 0);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 70, 0);
        widgets.addSlot(outputs.get(0), 100, 0).recipeContext(this);

        // lore handling
        if (!Objects.equals(lore, "empty")) {
            EmiCompat.addInfo(widgets, 120, 5, Component.translatable(lore));
        }
    }
}

*///?}