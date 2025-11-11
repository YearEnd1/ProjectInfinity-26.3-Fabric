package net.lerariemann.infinity.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.lerariemann.infinity.item.function.ChromaticColoringRecipe;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.item.function.CollisionCraftingRecipe;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.platform.PlatformMethods;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.RegistryAccess;
//? if >1.21 {
/*import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.RecipeHolder;
*///?} else {
import net.minecraft.world.item.alchemy.PotionUtils;
//?}
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import java.util.ArrayList;
import java.util.List;

@EmiEntrypoint
public class EmiCompat implements EmiPlugin {
    public static final ResourceLocation INFINITY_SPRITES = InfinityMethods.getId( "textures/gui/emi_simplified_textures.png");
    public static final EmiStack PORTAL_WORKSTATION = EmiStack.of(ModItems.PORTAL_ITEM.get());
    public static final EmiRecipeCategory PORTAL_CRAFTING
            = new EmiRecipeCategory(ModItemFunctions.PORTAL_CRAFTING.getId(), PORTAL_WORKSTATION, new EmiTexture(INFINITY_SPRITES, 0, 0, 16, 16));
    public static EmiStack IRIDESCENCE_WORKSTATION = EmiStack.of(PlatformMethods.getIridescenceStill().get());
    public static EmiStack IRIDESCENCE_CATALYST = IRIDESCENCE_WORKSTATION.copy().setRemainder(IRIDESCENCE_WORKSTATION);

    public static final EmiRecipeCategory IRIDESCENCE_CRAFTING
            = new EmiRecipeCategory(ModItemFunctions.IRIDESCENCE_CRAFTING.getId(), IRIDESCENCE_WORKSTATION, new EmiTexture(INFINITY_SPRITES, 0, 0, 16, 16));
    public static final EmiRecipeCategory CHROMATIC
            = new EmiRecipeCategory(InfinityMethods.getId("chromatic"),
            EmiStack.of(ModBlocks.CHROMATIC_WOOL.get()), new EmiTexture(INFINITY_SPRITES, 0, 0, 16, 16));

    @Override
    public void register(EmiRegistry registry) {
        RecipeManager manager = registry.getRecipeManager();
        RegistryAccess registryManager = Minecraft.getInstance().level.registryAccess();

        //collision recipes
        registry.addCategory(PORTAL_CRAFTING);
        registry.addWorkstation(PORTAL_CRAFTING, PORTAL_WORKSTATION);
        for (
                //? if >1.21 {
                /*RecipeHolder<CollisionCraftingRecipe>
                *///?} else {
                CollisionCraftingRecipe
                //?}
                recipe : manager.getAllRecipesFor(ModItemFunctions.PORTAL_CRAFTING_TYPE.get())) {
            //? if >1.21 {
            /*registry.addRecipe(new PortalCraftingEmiRecipe(recipe.id(), recipe.value(), registryManager));
            *///?} else {
            registry.addRecipe(new PortalCraftingEmiRecipe(recipe.getId(), recipe, registryManager));
            //?}
        }
        registry.addCategory(IRIDESCENCE_CRAFTING);
        registry.addWorkstation(IRIDESCENCE_CRAFTING, IRIDESCENCE_WORKSTATION);
        for (
            //? if >1.21 {
            /*RecipeHolder<CollisionCraftingRecipe>
            *///?} else {
            CollisionCraftingRecipe
            //?}
                recipe : manager.getAllRecipesFor(ModItemFunctions.IRIDESCENCE_CRAFTING_TYPE.get())) {
            //? if >1.21 {
            /*registry.addRecipe(new IridesenceCraftingEmiRecipe(recipe.id(), recipe.value(), registryManager));
            *///?} else {
            registry.addRecipe(new IridesenceCraftingEmiRecipe(recipe.getId(), recipe, registryManager));
            //?}
        }

        //item interactions when clicked on chromatic blocks
        registry.addCategory(CHROMATIC);
        registry.addWorkstation(CHROMATIC, EmiStack.of(ModBlocks.CHROMATIC_WOOL.get()));
        registry.addWorkstation(CHROMATIC, EmiStack.of(ModBlocks.CHROMATIC_CARPET.get()));
        registry.addRecipe(ChromaticEmiRecipe.withInfo("color", ModItems.CHROMATIC_MATTER.get()));
        registry.addRecipe(ChromaticEmiRecipe.withInfo("hue", ModItems.IRIDESCENT_STAR.get()));
        registry.addRecipe(ChromaticEmiRecipe.of("saturation_plus", Items.AMETHYST_SHARD));
        registry.addRecipe(ChromaticEmiRecipe.of("saturation_minus", ModItems.FOOTPRINT.get()));
        registry.addRecipe(ChromaticEmiRecipe.of("brightness_plus", ModItems.WHITE_MATTER.get()));
        registry.addRecipe(ChromaticEmiRecipe.of("brightness_minus", ModItems.BLACK_MATTER.get()));

        //recoloring with chroma matter in the crafting grid
        EmiStack matter = EmiStack.of(ModItems.CHROMATIC_MATTER.get());
        EmiStack matter_catalyst = matter.copy().setRemainder(matter);
        for (
            //? if >1.21 {
            /*RecipeHolder<ChromaticColoringRecipe> recipeHolder
             *///?} else {
                ChromaticColoringRecipe recipe
                        //?}
                        : manager.getAllRecipesFor(ModItemFunctions.CHROMATIC_COLORING_TYPE.get())) {

            //? if >1.21 {
            /*var id = recipeHolder.id();
            var recipe = recipeHolder.value()
             *///?} else {
            var id = recipe.getId();
            //?}

            List<EmiIngredient> input = new ArrayList<>();
            input.add(matter_catalyst);
            input.add(EmiIngredient.of(recipe.getInput()));

            registry.addRecipe(new EmiCraftingRecipe(input,
                    EmiStack.of(recipe.getResultItem(registryManager)),
                    id));
        }

        //chroma carpet from wool
        List<EmiIngredient> input = new ArrayList<>();
        input.add(EmiStack.of(ModItems.CHROMATIC_WOOL.get()));
        input.add(EmiStack.of(ModItems.CHROMATIC_WOOL.get()));
        registry.addRecipe(new EmiCraftingRecipe(input,
                EmiStack.of(ModItems.CHROMATIC_CARPET.get().getDefaultInstance().copyWithCount(3)),
                InfinityMethods.getId("/chromatic_carpet")));

        //brewing iridescence
        ItemStack awkward = Items.POTION.getDefaultInstance();
        //? if >1.21 {
        /*awkward.update(DataComponents.POTION_CONTENTS,
                PotionContents.EMPTY,
                Potions.AWKWARD,
                PotionContents::withPotion);
        *///?} else {
        PotionUtils.setPotion(awkward, Potions.AWKWARD);
        //?}
        EmiRecipe chromaBrew = new EmiBrewingRecipe(EmiStack.of(awkward),
                EmiIngredient.of(Ingredient.of(ModItems.CHROMATIC_MATTER.get())),
                EmiStack.of(ModItems.CHROMATIC_POTION.get().getDefaultInstance()),
                InfinityMethods.getId("/chromatic_bottle"));
        registry.addRecipe(chromaBrew);

        //bottling iridescence
        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(InfinityMethods.getId("/iridescence/bottling"))
                .leftInput(EmiStack.of(Items.GLASS_BOTTLE.getDefaultInstance()))
                .rightInput(IRIDESCENCE_CATALYST, false)
                .output(EmiStack.of(ModItems.IRIDESCENT_POTION.get().getDefaultInstance()))
                .build());

        //star of lang effects
        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(InfinityMethods.getId("/iridescence/wool_uncolor"))
                .leftInput(EmiStack.of(ModItems.IRIDESCENT_WOOL.get().getDefaultInstance()))
                .rightInput(EmiStack.of(ModItems.STAR_OF_LANG.get().getDefaultInstance()), true)
                .output(EmiStack.of(ModItems.CHROMATIC_WOOL.get().getDefaultInstance()))
                .build());
        registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(InfinityMethods.getId("/iridescence/carpet_uncolor"))
                .leftInput(EmiStack.of(ModItems.IRIDESCENT_CARPET.get().getDefaultInstance()))
                .rightInput(EmiStack.of(ModItems.STAR_OF_LANG.get().getDefaultInstance()), true)
                .output(EmiStack.of(ModItems.CHROMATIC_CARPET.get().getDefaultInstance()))
                .build());
    }

    public static void addInfo(WidgetHolder widgets, int x, int y, Component info) {
        widgets.add(new TextWidgetWithTooltip(Component.literal("ℹ").withStyle(ChatFormatting.GRAY).getVisualOrderText(),
                x, y, 0xFFFFFF, false, info));
    }

    public static class TextWidgetWithTooltip extends TextWidget {
        Component tooltip;

        public TextWidgetWithTooltip(FormattedCharSequence text, int x, int y, int color, boolean shadow, Component tooltip) {
            super(text, x, y, color, shadow);
            this.tooltip = tooltip;
        }
        public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
            return List.of(ClientTooltipComponent.create(tooltip.getVisualOrderText()));
        }
    }
}
