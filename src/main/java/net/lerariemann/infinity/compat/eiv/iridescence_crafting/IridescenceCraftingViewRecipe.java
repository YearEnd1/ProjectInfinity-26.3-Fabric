package net.lerariemann.infinity.compat.eiv.iridescence_crafting;

//? if >1.21.4 {

import de.crafty.eiv.common.api.recipe.IEivRecipeViewType;
import de.crafty.eiv.common.api.recipe.IEivViewRecipe;
import de.crafty.eiv.common.recipe.inventory.RecipeViewMenu;
import de.crafty.eiv.common.recipe.inventory.SlotContent;
import net.lerariemann.infinity.item.PortalDataHolder;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.var.ColorLogic;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.List;

public class IridescenceCraftingViewRecipe implements IEivViewRecipe {

    private final SlotContent input, output;
    private String lore;

    public IridescenceCraftingViewRecipe(IridescenceCraftingServerRecipe modRecipe) {
        var output = modRecipe.getResult();
        DataComponentPatch.Builder b = DataComponentPatch.builder();
        if (output.getItem() instanceof PortalDataHolder) {
            b = b.set(ModComponentTypes.COLOR.get(), ColorLogic.defaultPortal);
            b = b.set(DataComponents.CUSTOM_MODEL_DATA, InfinityMethods.getColoredModel(ColorLogic.defaultPortal));
        }
        if (output.getItem() instanceof PortalDataHolder.Destinable) {
            b = b.set(ModComponentTypes.DESTINATION.get(), ResourceLocation.parse("infinity:generated_0"));
        }
        output.applyComponentsAndValidate(b.build());
        this.lore = modRecipe.getLore();
        this.input = SlotContent.of(modRecipe.getIngredient());
        this.output = SlotContent.of(output);
    }

    @Override
    public IEivRecipeViewType getViewType() {
        return IridescenceCraftingViewType.INSTANCE;
    }

    @Override
    public void bindSlots(RecipeViewMenu.SlotFillContext slotFillContext) {
        slotFillContext.bindOptionalSlot(0, this.input, RecipeViewMenu.OptionalSlotRenderer.DEFAULT);
        slotFillContext.bindOptionalSlot(1, SlotContent.of(Ingredient.of(ModBlocks.IRIDESCENCE.get())), RecipeViewMenu.OptionalSlotRenderer.DEFAULT);
        slotFillContext.bindOptionalSlot(2, this.output, RecipeViewMenu.OptionalSlotRenderer.DEFAULT);
        if (this.lore != null && I18n.exists(this.lore)) {
            slotFillContext.addAdditionalStackModifier(2, (stack, tooltip) -> {
                tooltip.add(Component.translatable(this.lore));
            });
        }
    }

    @Override
    public List<SlotContent> getIngredients() {
        return List.of(this.input);
    }

    @Override
    public List<SlotContent> getResults() {
        return List.of(this.output);
    }
}
//?}