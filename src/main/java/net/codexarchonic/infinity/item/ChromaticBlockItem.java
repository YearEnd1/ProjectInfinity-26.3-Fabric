package net.codexarchonic.infinity.item;

import net.codexarchonic.infinity.block.entity.ChromaticBlockEntity;
import net.codexarchonic.infinity.registry.core.ModComponentTypes;
import net.codexarchonic.infinity.registry.core.ModItems;
import net.codexarchonic.infinity.util.VersionMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ChromaticBlockItem extends BlockItem implements PortalDataHolder {
    public ChromaticBlockItem(Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);
        if (!result.consumesAction()) {
            return result;
        }
        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        if (player != null && player.getItemInHand(InteractionHand.OFF_HAND)
                .is(ModItems.CHROMATIC_MATTER.get())
                && world.getBlockEntity(pos) instanceof ChromaticBlockEntity cbe) {
            cbe.setColor(VersionMethods.getOrDefaultInt(stack, ModComponentTypes.COLOR, 0xFFFFFF));
        }
        return result;
    }

    @Override
    public ItemStack getStack() {
        return getDefaultInstance();
    }
}
