package net.lerariemann.infinity.registry.var;

import static net.lerariemann.infinity.util.platform.PlatformMethods.createItemTag;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static net.lerariemann.infinity.util.platform.PlatformMethods.createBlockTag;

public class ModTags {
    public static TagKey<Block> IRIDESCENT_BLOCKS = createBlockTag("iridescent");
    public static TagKey<Item> IRIDESCENT_ITEMS = createItemTag("iridescent");
    public static TagKey<Item> MATTER = createItemTag("matter");
}
