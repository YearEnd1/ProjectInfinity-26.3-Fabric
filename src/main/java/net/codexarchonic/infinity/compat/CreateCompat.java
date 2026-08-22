package net.codexarchonic.infinity.compat;

//? if <1.21.2 {
/*import com.simibubi.create.api.contraption.train.PortalTrackProvider;
import com.simibubi.create.content.trains.track.AllPortalTracks;
import com.simibubi.create.content.trains.track.TrackBlock;
import com.simibubi.create.content.trains.track.TrackShape;
import net.createmod.catnip.math.BlockFace;
*///?} else if fabric {
import com.zurrtum.create.api.contraption.train.PortalTrackProvider;
import com.zurrtum.create.catnip.math.BlockFace;
import com.zurrtum.create.content.trains.track.AllPortalTracks;
import com.zurrtum.create.content.trains.track.TrackBlock;
import com.zurrtum.create.content.trains.track.TrackShape;
//?}
//? if <1.21.2 || fabric {
import net.codexarchonic.infinity.block.custom.RailHelper;
import net.codexarchonic.infinity.block.entity.InfinityPortalBlockEntity;
import net.codexarchonic.infinity.registry.core.ModBlocks;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.codexarchonic.infinity.util.VersionMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;
import java.util.Set;
//?}

public class CreateCompat {
    //? if <1.21.2 || fabric {
    private static PortalTrackProvider.Exit infinityPortalProvider(ServerLevel worldFrom, BlockFace blockFace) {
        if (!VersionMethods.allowNether(worldFrom.getServer())) {
            return null;
        }
        BlockPos posFrom = blockFace.getConnectedPos();
        if (worldFrom.getBlockEntity(posFrom) instanceof InfinityPortalBlockEntity ipbe
                && ipbe.isConnectedBothSides()) { //we only allow trains through if portals are in sync
            ServerLevel worldTo = ipbe.getDimensionAsWorld();
            BlockPos posTo = ipbe.getOtherSidePos();
            assert worldTo != null;
            assert posTo != null;

            BlockPos correctedPos = getCorrectedPos(worldFrom, posFrom, worldTo, posTo);

            Direction targetDirection = blockFace.getFace();
            Direction.Axis axisTo = worldTo.getBlockState(posTo).getValue(BlockStateProperties.HORIZONTAL_AXIS);
            if (targetDirection.getAxis().equals(axisTo)) {
                targetDirection = targetDirection.getClockWise();
            }
            return new PortalTrackProvider.Exit(worldTo, new BlockFace(correctedPos.relative(targetDirection), targetDirection.getOpposite()));
        }
        return null;
    }

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
        }
        else there = min_there + (int)((here - min_here)*(max_there-min_there)/(float)(max_here-min_here)); //might not be super good but feels decent

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
        AllPortalTracks.tryRegisterIntegration(InfinityMethods.getId("neither_portal"), CreateCompat::infinityPortalProvider);
    }

    public static void tryModifyRails(InfinityPortalBlockEntity ipbe) {
        Level w = ipbe.getLevel();
        if (w instanceof ServerLevel worldFrom) {
            BlockPos posFrom = ipbe.getBlockPos();
            BlockState state = worldFrom.getBlockState(posFrom);
            if (!state.is(ModBlocks.PORTAL.get())) return;
            Set<Direction> toCheck = state.getValue(BlockStateProperties.HORIZONTAL_AXIS).equals(Direction.Axis.X) ?
                    Set.of(Direction.NORTH, Direction.SOUTH) : Set.of(Direction.EAST, Direction.WEST);
            for (Direction dir : toCheck) {
                BlockPos posTrack = posFrom.relative(dir);
                BlockState bs = worldFrom.getBlockState(posTrack);
                if (bs.getBlock() instanceof TrackBlock)
                    modifyRails(ipbe, worldFrom, posTrack, bs);
            }
        }
    }

    public static void modifyRails(InfinityPortalBlockEntity ipbe, ServerLevel worldFrom,
                                   BlockPos posTrack, BlockState bs) {
        if (ipbe.isConnectedBothSides()) {
            if (!bs.hasProperty(TrackBlock.SHAPE)) return;
            worldFrom.setBlockAndUpdate(posTrack, ModBlocks.RAIL_HELPER.get().defaultBlockState());
            RailHelper.RHBEntity e = RailHelper.getBlockEntity(worldFrom, posTrack);
            e.trackBlock = BuiltInRegistries.BLOCK.getKey(bs.getBlock());
            e.shape = switch (bs.getValue(TrackBlock.SHAPE)) {
                case TN, TS -> "zo";
                default -> "xo";
            };
        }
        worldFrom.setBlockAndUpdate(posTrack, Blocks.AIR.defaultBlockState());
    }
    public static void reattachRails(ServerLevel world, BlockPos pos, RailHelper.RHBEntity e) {
        BlockState bs;
        Optional<Block> b = BuiltInRegistries.BLOCK.getOptional(e.trackBlock);
        if (b.isEmpty() || !(b.get() instanceof TrackBlock)) bs = Blocks.AIR.defaultBlockState();
        else bs = b.get().defaultBlockState().setValue(TrackBlock.SHAPE, e.shape.equals("zo") ? TrackShape.ZO : TrackShape.XO);
        world.setBlockAndUpdate(pos, bs);
    }
    //?}
}