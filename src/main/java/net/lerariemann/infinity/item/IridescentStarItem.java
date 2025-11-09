package net.lerariemann.infinity.item;

import net.lerariemann.infinity.block.custom.AltarBlock;
import net.lerariemann.infinity.block.custom.IridescentBlock;
import net.lerariemann.infinity.block.entity.ChromaticBlockEntity;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class IridescentStarItem extends Item {
    public IridescentStarItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return super.useOn(context);
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState oldState = world.getBlockState(pos);
        boolean reverse = player.isShiftKeyDown();
        if (world.getBlockEntity(pos) instanceof ChromaticBlockEntity cbe) {
            cbe.onIridStarUse(reverse);
        }
        else if (oldState.getBlock() instanceof AltarBlock) {
            AltarBlock.setColor(world, pos, oldState,
                     InfinityMethods.properMod(oldState.getValue(AltarBlock.COLOR) + (reverse ? -1 : 1),
                             AltarBlock.numColors));
        }
        else if (oldState.getBlock() instanceof IridescentBlock) {
            world.setBlockAndUpdate(pos, oldState.setValue(IridescentBlock.COLOR_OFFSET,
                    InfinityMethods.properMod(oldState.getValue(IridescentBlock.COLOR_OFFSET) + (reverse ? -1 : 1),
                    IridescentBlock.num_models)));
        }
        else return super.useOn(context);
        if (!world.isClientSide())
            world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1f, 1f);
        return InteractionResult.SUCCESS;
    }
}
