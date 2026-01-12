package net.lerariemann.infinity.compat.rrv.portal_crafting;

//? if >1.21.4 {

import cc.cassian.rrv.api.recipe.ReliableClientRecipeType;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import java.util.List;

public class PortalCraftingViewType implements ReliableClientRecipeType {

    protected static final PortalCraftingViewType INSTANCE = new PortalCraftingViewType();

    @Override
    public Component getDisplayName() {
        return Component.translatable("emi.category.infinity.collision_portal");
    }

    @Override
    public int getDisplayWidth() {
        return 100;
    }

    @Override
    public int getDisplayHeight() {
        return 25;
    }

    @Override
    public ResourceLocation getGuiTexture() {
        return InfinityMethods.getId("textures/gui/collision.png");
    }

    @Override
    public int getSlotCount() {
        return 3;
    }

    @Override
    public void placeSlots(RecipeViewMenu.SlotDefinition slotDefinition) {
        slotDefinition.addItemSlot(0, 5, 5);
        slotDefinition.addItemSlot(1, 42, 5);
        slotDefinition.addItemSlot(2, 80, 5);
    }

    @Override
    public ResourceLocation getId() {
        return InfinityMethods.getId("portal_crafting");
    }

    @Override
    public ItemStack getIcon() {
        return ModItems.PORTAL_ITEM.get().getDefaultInstance();
    }

    @Override
    public List<ItemStack> getCraftReferences() {
        return List.of(ModItems.PORTAL_ITEM.get().getDefaultInstance());
    }

}
//?}