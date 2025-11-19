package net.lerariemann.infinity.compat.eiv.portal_crafting;

//? if >1.21.4 {

import de.crafty.eiv.common.api.recipe.EivRecipeType;
import de.crafty.eiv.common.api.recipe.IEivServerRecipe;
import de.crafty.eiv.common.recipe.util.EivTagUtil;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.List;

public class PortalCraftingServerRecipe implements IEivServerRecipe {

    //Create a server recipe type (the id does not have to match your client side viewtype id)
    public static final EivRecipeType<PortalCraftingServerRecipe> TYPE = EivRecipeType.register(
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
        tag.put("ingredient", EivTagUtil.writeIngredient(this.input));
        tag.put("result", EivTagUtil.encodeItemStackOnServer(this.output));
        tag.put("lore", StringTag.valueOf(this.lore));
    }

    @Override
    public void loadFromTag(CompoundTag tag) {
        this.input = EivTagUtil.readIngredient(NbtUtils.getCompound(tag, "ingredient"));
        this.output = EivTagUtil.decodeItemStackOnClient(NbtUtils.getCompound(tag, "result"));
        this.lore = NbtUtils.getString(tag, "lore");
    }

    @Override
    public EivRecipeType<? extends IEivServerRecipe> getRecipeType() {
        return TYPE;
    }
}

//?}