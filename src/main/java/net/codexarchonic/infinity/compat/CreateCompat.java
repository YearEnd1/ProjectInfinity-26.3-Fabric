package net.codexarchonic.infinity.compat;

import net.codexarchonic.infinity.block.custom.RailHelper;
import net.codexarchonic.infinity.block.entity.InfinityPortalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Create mod integration. On 26.3 there is no Create build available, so this class keeps the
 * public surface used by the rest of the mod but performs no Create-specific work.
 */
public class CreateCompat {

    public static BlockPos getCorrectedPos(ServerLevel worldFrom, BlockPos posFrom, ServerLevel worldTo, BlockPos posTo) {
        Direction.Axis axisFrom = worldFrom.getBlockState(posFrom).getValue(BlockStateProperties.HORIZONTAL_AXIS);
        boolean fromX = axisFrom.equals(Direction.Axis.X);

        BlockPos maxHere = getBound(worldFrom, posFrom, axisFrom, true);
        int max_here = fromX ? maxHere.getX() : maxHere.getZ();
        BlockPos minHere = getBound(worldFrom, posFrom, axisFrom, false);
        int min_here = fromX ? minHere.getX() : minHere.getZ();

        Direction.Axis axisTo = worldTo.getBlockState(posTo).getValue(BlockStateProperties.HORIZONTAL_AXIS);
        boolean toX = axisTo.equals(Direction.Axis.X);

        BlockPos maxThere = getBound(worldTo, posTo, axisTo, true);
        int max_there = toX ? maxThere.getX() : maxThere.getZ();
        BlockPos minThere = getBound(worldTo, posTo, axisTo, false);
        int min_there = toX ? minThere.getX() : minThere.getZ();

        int here = fromX ? posFrom.getX() : posFrom.getZ();
        int there;
        if (max_there - min_there == max_here - min_here) {
            there = min_there + (here - min_here);
        } else {
            there = min_there + (int) ((here - min_here) * (max_there - min_there) / (float) (max_here - min_here));
        }

        return new BlockPos(toX ? there : posTo.getX(), posTo.getY(), toX ? posTo.getZ() : there);
    }

    public static BlockPos getBound(ServerLevel world, BlockPos pos, Direction.Axis axis, boolean positive) {
        BlockPos bp = pos;
        int i = positive ? 1 : -1;
        while (world.getBlockState(bp).getBlock() instanceof NetherPortalBlock) {
            bp = bp.relative(axis, i);
        }
        return bp.relative(axis, -i);
    }

    public static void register() {
        // no Create on 26.3
    }

    public static void tryModifyRails(InfinityPortalBlockEntity ipbe) {
        // no Create on 26.3
    }

    public static void reattachRails(ServerLevel world, BlockPos pos, RailHelper.RHBEntity e) {
        // no Create on 26.3
    }
}
