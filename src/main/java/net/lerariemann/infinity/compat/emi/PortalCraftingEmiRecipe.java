package net.lerariemann.infinity.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.lerariemann.infinity.item.PortalDataHolder;
import net.lerariemann.infinity.item.function.CollisionCraftingRecipe;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.var.ColorLogic;
import net.minecraft.core.RegistryAccess;
//? if >1.21 {
/*import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.core.component.DataComponentPatch;
*///?}
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import java.util.Objects;

public class PortalCraftingEmiRecipe extends BasicEmiRecipe {
    String lore;

    public PortalCraftingEmiRecipe(ResourceLocation id, CollisionCraftingRecipe recipe, RegistryAccess registryManager) {
        super(EmiCompat.PORTAL_CRAFTING, id, 118, 18);
        this.inputs.add(EmiIngredient.of(recipe.getInput()));
        ItemStack out = recipe.getResultItem(registryManager);
        //? if >1.21 {
        /*DataComponentPatch.Builder b = DataComponentPatch.builder();
        if (out.getItem() instanceof PortalDataHolder) {
            b = b.set(ModComponentTypes.COLOR.get(), ColorLogic.defaultPortal);
        }
        if (out.getItem() instanceof PortalDataHolder.Destinable) {
            b = b.set(ModComponentTypes.DESTINATION.get(), VersionMethods.id("infinity:generated_0"));
        }
        out.applyComponentsAndValidate(b.build());
        *///?} else {
        var b = new CompoundTag();
        if (out.getItem() instanceof PortalDataHolder) {
            b.putInt(ModComponentTypes.COLOR, ColorLogic.defaultPortal);
        }
        if (out.getItem() instanceof PortalDataHolder.Destinable) {
            b.putString(ModComponentTypes.DESTINATION, VersionMethods.id("infinity:generated_0").toString());
        }
        out.setTag(b);
        //?}
        this.outputs.add(EmiStack.of(out));
        this.lore = recipe.getLore();
        if (!Objects.equals(lore, "empty")) {
            this.width = 130;
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(inputs.getFirst(), 0, 0);
        widgets.addTexture(EmiTexture.PLUS, 26, 2);
        widgets.add(new SlotWidget(EmiCompat.PORTAL_WORKSTATION, 48, 0));
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 70, 0);
        widgets.addSlot(outputs.getFirst(), 100, 0).recipeContext(this);

        // lore handling
        if (!Objects.equals(lore, "empty")) {
            EmiCompat.addInfo(widgets, 120, 5, Component.translatable(lore));
        }
    }
}
