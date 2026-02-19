package net.lerariemann.infinity.item;

import net.lerariemann.infinity.block.entity.ChromaticBlockEntity;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class ChromaticBlockItem extends BlockItem implements PortalDataHolder {
    public ChromaticBlockItem(Block block, Properties settings) {
        super(block, settings);
    }

    //todo: implement this properly
    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level world, @Nullable Player player, ItemStack stack, BlockState state) {
        boolean bl = updateCustomBlockEntityTag(world, player, pos, stack);
        if (player != null && player.getItemInHand(InteractionHand.OFF_HAND)
                .is(ModItems.CHROMATIC_MATTER.get())
                && world.getBlockEntity(pos) instanceof ChromaticBlockEntity cbe) {
            cbe.setColor(VersionMethods.getOrDefaultInt(stack, ModComponentTypes.COLOR, 0xFFFFFF));
        }
        return bl;
    }

    @Override
    public ItemStack getStack() {
        return getDefaultInstance();
    }
}
