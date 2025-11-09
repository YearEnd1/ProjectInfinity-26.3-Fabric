package net.lerariemann.infinity.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public interface Boopable {
    BooleanProperty BOOP = BooleanProperty.create("boop");
    static boolean getBoop(BlockState state) {
        return state.hasProperty(BOOP) ? state.getValue(BOOP) : false;
    }
    default void appendBoop(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BOOP);
    }
    static void boop(Level w, BlockPos pos) {
        BlockState bs = w.getBlockState(pos);
        if (bs.hasProperty(BOOP)) w.setBlockAndUpdate(pos, bs.setValue(BOOP, !bs.getValue(BOOP)));
    }
}
