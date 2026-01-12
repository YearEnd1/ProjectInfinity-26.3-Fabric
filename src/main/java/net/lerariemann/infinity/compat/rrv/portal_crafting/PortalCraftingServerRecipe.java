package net.lerariemann.infinity.compat.rrv.portal_crafting;

//? if >1.21.4 {

import cc.cassian.rrv.api.TagUtil;
import cc.cassian.rrv.api.recipe.ReliableServerRecipeType;
import cc.cassian.rrv.api.recipe.ReliableServerRecipe;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class PortalCraftingServerRecipe implements ReliableServerRecipe {

    //Create a server recipe type (the id does not have to match your client side viewtype id)
    public static final ReliableServerRecipeType<PortalCraftingServerRecipe> TYPE = ReliableServerRecipeType.register(
            InfinityMethods.getId("portal_crafting"),
            () -> new PortalCraftingServerRecipe(null, null, null)
    );
    private Ingredient input;
    private ItemStack output;
    private String lore;

    public PortalCraftingServerRecipe(Ingredient input, ItemStack output, String lore) {
        this.input = input;
        this.output = output;
        this.lore = lore;
    }

    public Ingredient getIngredient() {
        return this.input;
    }

    public ItemStack getResult() {
        return this.output;
    }

    public String getLore() {
        return this.lore;
    }

    @Override
    public void writeToTag(CompoundTag tag) {
        tag.put("ingredient", TagUtil.writeIngredient(this.input));
        tag.put("result", TagUtil.encodeItemStackOnServer(this.output));
        tag.put("lore", StringTag.valueOf(this.lore));
    }

    @Override
    public void loadFromTag(CompoundTag tag) {
        this.input = TagUtil.readIngredient(NbtUtils.getCompound(tag, "ingredient"));
        this.output = TagUtil.decodeItemStackOnClient(NbtUtils.getCompound(tag, "result"));
        this.lore = NbtUtils.getString(tag, "lore");
    }

    @Override
    public ReliableServerRecipeType<? extends ReliableServerRecipe> getRecipeType() {
        return TYPE;
    }
}

//?}