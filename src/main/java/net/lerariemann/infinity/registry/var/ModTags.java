package net.lerariemann.infinity.registry.var;

import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static TagKey<Block> IRIDESCENT_BLOCKS = InfinityPlatform.INSTANCE.createBlockTag("iridescent");
    public static TagKey<Item> IRIDESCENT_ITEMS = InfinityPlatform.INSTANCE.createItemTag("iridescent");
    public static TagKey<Item> MATTER = InfinityPlatform.INSTANCE.createItemTag("matter");
}
