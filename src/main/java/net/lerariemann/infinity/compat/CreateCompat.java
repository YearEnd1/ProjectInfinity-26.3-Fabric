package net.lerariemann.infinity.compat;

//? if <1.21.2 {
/*import com.simibubi.create.api.contraption.train.PortalTrackProvider;
import com.simibubi.create.content.trains.track.AllPortalTracks;
import com.simibubi.create.content.trains.track.TrackBlock;
import com.simibubi.create.content.trains.track.TrackShape;
import net.createmod.catnip.math.BlockFace;
*///?}
import net.lerariemann.infinity.block.custom.RailHelper;
import net.lerariemann.infinity.block.entity.InfinityPortalBlockEntity;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import java.util.Optional;
import java.util.Set;

public class CreateCompat {
    //? if <1.21.2 {
    /*private static PortalTrackProvider.Exit infinityPortalProvider(ServerLevel worldFrom, BlockFace blockFace) {
        MinecraftServer server = worldFrom.getServer();
        //? if <1.21
        /^if (!server.isNetherEnabled()) return null;^/
        //? if >1.21.9 {
        server.getGameRules().getRule(GameRules.RULE_ALLOW_NETHER);
        //?}
        BlockPos posFrom = blockFace.getConnectedPos();
        if (worldFrom.getBlockEntity(posFrom) instanceof InfinityPortalBlockEntity ipbe
                && ipbe.isConnectedBothSides()) { //we only allow trains through if portals are in sync
            ServerLevel worldTo = ipbe.getDimensionAsWorld();
            BlockPos posTo = ipbe.getOtherSidePos();
            assert posTo != null;
            Direction targetDirection = blockFace.getFace();
            Direction.Axis axisTo = worldTo.getBlockState(posTo).getValue(BlockStateProperties.HORIZONTAL_AXIS);
            if (targetDirection.getAxis().equals(axisTo)) {
                targetDirection = targetDirection.getClockWise();
            }
            return new PortalTrackProvider.Exit(worldTo, new BlockFace(posTo.relative(targetDirection), targetDirection.getOpposite()));
        }
        return null;
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
    *///?}
}