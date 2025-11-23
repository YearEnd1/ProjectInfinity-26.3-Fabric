package net.lerariemann.infinity.registry.core;

import dev.architectury.core.item.ArchitecturyBucketItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.item.*;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.platform.PlatformMethods;
import net.lerariemann.infinity.util.core.RandomProvider;
import net.lerariemann.infinity.util.var.ColorLogic;
//? if >1.21 {
import net.minecraft.core.component.DataComponents;
//?}
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
//? if >1.21.4 {
import net.minecraft.world.item.component.Consumable;
//?}
import net.minecraft.world.level.block.Block;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;
import static net.lerariemann.infinity.util.platform.PlatformMethods.*;

@SuppressWarnings("unused")
public class ModItems {

    public static final Item OMINOUS_TRIAL_KEY =
            //? if <1.21 {
            /*Items.AMETHYST_SHARD
            *///?} else {
            Items.OMINOUS_TRIAL_KEY
             //?}
    ;
    public static final Item VAULT =
            //? if <1.21 {
            /*Items.TNT
            *///?} else {
            Items.VAULT
             //?}
    ;

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);
    //block items
    public static final RegistrySupplier<Item> PORTAL_ITEM =
            register(ModBlocks.PORTAL.getId().getPath(), new Item.Properties().setId(registryKey(ModBlocks.PORTAL.getId().getPath())), (properties) -> new BlockItem(ModBlocks.PORTAL.get(),
                            colorProperties(ColorLogic.defaultPortal, properties)));
    public static final RegistrySupplier<Item> COSMIC_ALTAR_ITEM =
            registerBlockItemAfter(ModBlocks.COSMIC_ALTAR, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.LECTERN, BlockItem::new);
    public static final RegistrySupplier<Item> ALTAR_ITEM =
            registerBlockItemAfter(ModBlocks.ALTAR, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.LECTERN, BlockItem::new);
    public static final RegistrySupplier<Item> ANT_ITEM  =
            registerBlockItemAfter(ModBlocks.ANT, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.LODESTONE, BlockItem::new);
    public static final RegistrySupplier<Item> BOOK_BOX_ITEM =
            registerBlockItemAfter(ModBlocks.BOOK_BOX, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.CHISELED_BOOKSHELF, BlockItem::new);
    public static final RegistrySupplier<Item> CURSOR_ITEM  =
            registerBlockItemAfter(ModBlocks.CURSOR, CreativeModeTabs.COLORED_BLOCKS, Items.PINK_TERRACOTTA, BlockItem::new);
    public static final RegistrySupplier<Item> NETHERITE_SLAB_ITEM =
            registerBlockItemAfter(ModBlocks.NETHERITE_SLAB, CreativeModeTabs.BUILDING_BLOCKS, Items.NETHERITE_BLOCK, BlockItem::new);
    public static final RegistrySupplier<Item> NETHERITE_STAIRS_ITEM =
            registerBlockItemAfter(ModBlocks.NETHERITE_STAIRS, CreativeModeTabs.BUILDING_BLOCKS, Items.NETHERITE_BLOCK, BlockItem::new);
    public static final RegistrySupplier<Item> NOTES_BLOCK_ITEM =
            registerBlockItemAfter(ModBlocks.NOTES_BLOCK, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.NOTE_BLOCK, BlockItem::new);
    public static final RegistrySupplier<Item> TIME_BOMB_ITEM =
            registerBlockItemAfter(ModBlocks.TIME_BOMB, CreativeModeTabs.FUNCTIONAL_BLOCKS, VAULT, BlockItem::new);
    public static final RegistrySupplier<Item> IRIDESCENT_WOOL  =
            registerBlockItemAfter(ModBlocks.IRIDESCENT_WOOL, CreativeModeTabs.COLORED_BLOCKS, Items.PINK_WOOL, BlockItem::new);
    public static final RegistrySupplier<Item> IRIDESCENT_CARPET  =
            registerBlockItemAfter(ModBlocks.IRIDESCENT_CARPET, CreativeModeTabs.COLORED_BLOCKS, Items.PINK_CARPET, BlockItem::new);
    public static final RegistrySupplier<ChromaticBlockItem> CHROMATIC_WOOL  =
            registerBlockItemAfter(ModBlocks.CHROMATIC_WOOL, CreativeModeTabs.COLORED_BLOCKS, Items.PINK_WOOL,
                    (p, settings) -> new ChromaticBlockItem(p, colorProperties(ColorLogic.defaultChromatic, settings)));

    public static final RegistrySupplier<ChromaticBlockItem> CHROMATIC_CARPET  =
            registerBlockItemAfter(ModBlocks.CHROMATIC_CARPET, CreativeModeTabs.COLORED_BLOCKS, Items.PINK_CARPET,
                    (p, settings) -> new ChromaticBlockItem(p, colorProperties(ColorLogic.defaultChromatic, settings)));
    public static final RegistrySupplier<Item> BIOME_BOTTLE_ITEM =
            registerBlockItemAfter(ModBlocks.BIOME_BOTTLE, CreativeModeTabs.INGREDIENTS, Items.EXPERIENCE_BOTTLE, BiomeBottleItem::new);

    //spawn eggs
    public static final RegistrySupplier<InfinitySpawnEggItem> CHAOS_CREEPER_SPAWN_EGG =
            registerEgg("chaos_creeper_spawn_egg",  ModEntities.CHAOS_CREEPER, 0x91BD59, 0x78A7FF);
    public static final RegistrySupplier<InfinitySpawnEggItem> CHAOS_SKELETON_SPAWN_EGG =
            registerEgg("chaos_skeleton_spawn_egg",  ModEntities.CHAOS_SKELETON, 0xF3CFB9, 0x87A363);
    public static final RegistrySupplier<InfinitySpawnEggItem> CHAOS_SLIME_SPAWN_EGG =
            registerEgg("chaos_slime_spawn_egg",  ModEntities.CHAOS_SLIME, 0xFF66FF, 0xAA77DD);
    public static final RegistrySupplier<InfinitySpawnEggItem> CHAOS_PAWN_SPAWN_EGG =
            registerEgg("chaos_pawn_spawn_egg",  ModEntities.CHAOS_PAWN, 0x222222, 0xFFFFFF);
    public static final RegistrySupplier<InfinitySpawnEggItem> ANT_SPAWN_EGG =
            registerEgg("ant_spawn_egg",  ModEntities.ANT, 0x444444, 0xFFFFFF);
    public static final RegistrySupplier<InfinitySpawnEggItem> BISHOP_SPAWN_EGG =
            registerEgg("bishop_spawn_egg",  ModEntities.BISHOP, 0, 0xFFFFFF);

    public static RegistrySupplier<InfinitySpawnEggItem> registerEgg(String name, RegistrySupplier<? extends EntityType<? extends Mob>> eType, int backgroundColour, int colour) {
        RegistrySupplier<InfinitySpawnEggItem> registeredItem = register(name, new Item.Properties(), (properties) ->
                new InfinitySpawnEggItem(eType, backgroundColour, colour, properties));
        addEnd(registeredItem, CreativeModeTabs.SPAWN_EGGS);
        return registeredItem;
    }

    //bucket
    public static final RegistrySupplier<Item> IRIDESCENCE_BUCKET = register("iridescence_bucket", new Item.Properties() , (properties) ->
            new ArchitecturyBucketItem(PlatformMethods.getIridescenceStill(), properties.craftRemainder(Items.BUCKET).stacksTo(1)));

    //misc
    public static final RegistrySupplier<Item> FOOTPRINT =
            registerItemAfter("footprint", CreativeModeTabs.INGREDIENTS, Items.DISC_FRAGMENT_5, Item::new);
    public static final RegistrySupplier<TransfiniteKeyItem> TRANSFINITE_KEY =
            registerItemAfter("key", CreativeModeTabs.INGREDIENTS, OMINOUS_TRIAL_KEY, TransfiniteKeyItem::new);
    public static final RegistrySupplier<HomeItem> HOME_ITEM =
            registerItemAfter("fine_item", CreativeModeTabs.FOOD_AND_DRINKS, Items.MILK_BUCKET, HomeItem::new,
                    foodProperties(0, 0, true, 3f, Optional.empty()));

    public static final RegistrySupplier<ChromaticItem> CHROMATIC_MATTER =
            registerItemAfter("chromatic_matter", CreativeModeTabs.INGREDIENTS, Items.DISC_FRAGMENT_5, settings -> new ChromaticItem(
                    //? if >1.21
                    deferredIntComponent("chromatic_matter", ModComponentTypes.COLOR, ColorLogic.defaultChromatic))
            );
    public static final RegistrySupplier<Item> WHITE_MATTER =
            registerItemAfter("white_matter", CreativeModeTabs.INGREDIENTS, Items.DISC_FRAGMENT_5, Item::new);
    public static final RegistrySupplier<Item> BLACK_MATTER =
            registerItemAfter("black_matter", CreativeModeTabs.INGREDIENTS, Items.DISC_FRAGMENT_5, Item::new);
    public static final RegistrySupplier<IridescentStarItem> IRIDESCENT_STAR =
            registerItemAfter("iridescent_star", CreativeModeTabs.INGREDIENTS, Items.NETHER_STAR, IridescentStarItem::new,
                    glintProperties(new Item.Properties()).rarity(Rarity.UNCOMMON));
    public static final RegistrySupplier<? extends StarOfLangItem> STAR_OF_LANG =
            registerItemAfter("star_of_lang", CreativeModeTabs.INGREDIENTS, Items.NETHER_STAR, StarOfLangItem::new,
                    glintProperties(new Item.Properties()));

    public static final RegistrySupplier<F4Item> F4 =
            registerItemAfter("f4", CreativeModeTabs.TOOLS_AND_UTILITIES, Items.WRITABLE_BOOK, F4Item::new,
                    new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final RegistrySupplier<Item> DISC =
            registerItemAfter("disc", CreativeModeTabs.TOOLS_AND_UTILITIES, Items.MUSIC_DISC_PIGSTEP, Item::new,
                    new Item.Properties().rarity(Rarity.RARE));
    public static final RegistrySupplier<Item> IRIDESCENT_POTION =
            registerItemAfter("iridescent_potion", CreativeModeTabs.FOOD_AND_DRINKS, Items.HONEY_BOTTLE, settings ->
                            new IridescentPotionItem(glintProperties(settings).rarity(Rarity.UNCOMMON),
                                    //FIXME 1.21.8
                                    //? if >1.21.2 {
                                    4
                                    //?} else {
                                    /*RandomProvider.ruleInt("iridescencePotionLevel")
                                    *///?}

                            ));
    public static final RegistrySupplier<Item> CHROMATIC_POTION =
            registerItemAfter("chromatic_potion", CreativeModeTabs.FOOD_AND_DRINKS, Items.HONEY_BOTTLE, settings ->
                            new IridescentPotionItem(settings, 0));

    private static Item.Properties glintProperties(Item.Properties settings) {
        return settings
                //? if >1.21
                .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                ;
    }
    private static Item.Properties colorProperties(int defaultChromatic, Item.Properties properties) {
        return properties
                //? if >1.21
                .component(ModComponentTypes.COLOR.get(), defaultChromatic)
                ;
    }

    private static Item.Properties foodProperties(int nutrition, int saturation, boolean canAlwaysEat, float eatSeconds, Optional<ItemStack> usingConvertsTo) {
        //? if >1.21.3 {
        return new Item.Properties().food(new FoodProperties(nutrition ,saturation, canAlwaysEat), Consumable.builder().consumeSeconds(eatSeconds).build());
        //?} else if >1.21 {
        /*return new Item.Properties().food(new FoodProperties(nutrition ,saturation, canAlwaysEat, eatSeconds, usingConvertsTo, List.of()));
         *///?} else {
        /*var properties = new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation);
        if (canAlwaysEat) properties = properties.alwaysEat();
        return new Item.Properties().food(properties.build());
        *///?}
    }

    public static <T extends Item> RegistrySupplier<T> register(String item, Item.Properties settings, Function<Item.Properties, T> constructor) {
        return ITEMS.register(item, () -> constructor.apply(settings
                //? if >1.21.2
                .setId(ResourceKey.create(Registries.ITEM, InfinityMethods.getId(item)))
                ));
    }
    /**
     * Registers an item via Architectury API.
     */
    public static <T extends Item> RegistrySupplier<T> registerItemAfter(String id, ResourceKey<CreativeModeTab> group, Item item,
                                                           Function<Item.Properties, T> constructor, Item.Properties settings) {
        RegistrySupplier<T> registeredItem = register(id, settings, constructor);
        addAfter(registeredItem, group, item);
        return registeredItem;
    }
    public static <T extends Item> RegistrySupplier<T> registerItemAfter(String id, ResourceKey<CreativeModeTab> group, Item item,
                                                           Function<Item.Properties, T> constructor) {
        return registerItemAfter(id, group, item, constructor, new Item.Properties().setId(registryKey(id)));
    }

    public static <T extends Item> RegistrySupplier<T> registerBlockItem(RegistrySupplier<Block> block, Item.Properties settings,
                                                           BiFunction<Block, Item.Properties, T> constructor) {
        return register(block.getId().getPath(), blockItemProperties(block.getId().getPath())
                , (properties) -> constructor.apply(block.get(), settings));
    }

    public static Item.Properties blockItemProperties(String id) {
        return new Item.Properties()
                //? if >1.21.4
                .useBlockDescriptionPrefix().setId(registryKey(id))
        ;
    }
    /**
     * Registers a BlockItem via Architectury API.
     */
    public static <T extends Item> RegistrySupplier<T> registerBlockItemAfter(RegistrySupplier<Block> block, ResourceKey<CreativeModeTab> group, Item item,
                                                                Item.Properties settings,
                                                                BiFunction<Block, Item.Properties, T> constructor) {
        RegistrySupplier<T> registeredItem = registerBlockItem(block, settings, constructor);
        addAfter(registeredItem, group, item);
        return registeredItem;
    }
    /**
     * Registers a BlockItem through Architectury API.
     */
    public static <T extends Item> RegistrySupplier<T> registerBlockItemAfter(RegistrySupplier<Block> block, ResourceKey<CreativeModeTab> group, Item item,
                                                                BiFunction<Block, Item.Properties, T> constructor) {
        return registerBlockItemAfter(block, group, item, blockItemProperties(block.getId().getPath()), constructor);
    }

    public static void registerModItems() {
        addAfter(IRIDESCENCE_BUCKET, CreativeModeTabs.TOOLS_AND_UTILITIES, Items.MILK_BUCKET);
        InfinityMod.LOGGER.debug("Registering items for " + MOD_ID);
        ITEMS.register();
    }

    public static ResourceKey<Item> registryKey(String name) {
        return ResourceKey.create(Registries.ITEM, InfinityMethods.getId(name));
    }
}
