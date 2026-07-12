package net.codexarchonic.infinity.compat.rrv.iridescence_crafting;

//? if >1.21.4 {

import cc.cassian.rrv.api.recipe.ReliableClientRecipeType;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;
import net.codexarchonic.infinity.registry.core.ModBlocks;
import net.codexarchonic.infinity.registry.core.ModItems;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import java.util.List;

public class IridescenceCraftingViewType implements ReliableClientRecipeType {

    protected static final IridescenceCraftingViewType INSTANCE = new IridescenceCraftingViewType();

    @Override
    public Component getDisplayName() {
        return Component.translatable("emi.category.infinity.collision_iridescence");
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
        return ModItems.IRIDESCENCE_BUCKET.get().getDefaultInstance();
    }

    @Override
    public List<ItemStack> getCraftReferences() {
        return List.of(ModItems.IRIDESCENCE_BUCKET.get().getDefaultInstance(), new ItemStack(ModBlocks.IRIDESCENCE.get()));
    }

}
//?}