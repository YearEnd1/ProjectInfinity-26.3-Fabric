package net.lerariemann.infinity.block.custom;

import net.lerariemann.infinity.util.platform.PlatformMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

//? if >1.21 {
/*import com.mojang.serialization.MapCodec;
import javax.annotation.ParametersAreNonnullByDefault;
*///?}
import java.util.Objects;
import java.util.logging.Handler;

@MethodsReturnNonnullByDefault
//? if >1.21 {
/*@ParametersAreNonnullByDefault
 *///?}
public class AntBlock extends HorizontalDirectionalBlock {
    //? if >1.21 {
    /*public static final MapCodec<AntBlock> CODEC = simpleCodec(AntBlock::new);
    *///?}

    public AntBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    //? if >1.21 {
    /*@Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
    *///?}
    
    private static boolean inverseExists(Block down) {
        var s = BuiltInRegistries.BLOCK.getKey(down);
        var state = down.defaultBlockState();
        if (PlatformMethods.isInBlack(state)) {
            return BuiltInRegistries.BLOCK.containsKey(VersionMethods.id(s.toString().replace("black", "white")));
        }
        if (PlatformMethods.isInWhite(state)) {
            return BuiltInRegistries.BLOCK.containsKey(VersionMethods.id(s.toString().replace("white", "black")));
        }
        return false;
    }
    public static boolean isSafeToRecolor(Level world, BlockPos pos) {
        return inverseExists(world.getBlockState(pos).getBlock()) && (world.getBlockEntity(pos) == null);
    }

    @Nullable
    public static Block recolor(Block down, boolean toWhite) {
        String s = VersionMethods.getNameAsString(BuiltInRegistries.BLOCK, down);
        var state = down.defaultBlockState();
        if (PlatformMethods.isInBlack(state)) {
            return toWhite ? BuiltInRegistries.BLOCK.get(VersionMethods.id(s.replace("black", "white"))) : down;
        }
        if (PlatformMethods.isInWhite(state)) {
            return toWhite ? down : BuiltInRegistries.BLOCK.get(VersionMethods.id(s.replace("white", "black")));
        }
        return null;
    }

    @Nullable
    public static Clockwiseness getCW(Block down) {
        String s = VersionMethods.getNameAsString(BuiltInRegistries.BLOCK, down);
        if (s.contains("black")) {
            return Clockwiseness.CCW;
        }
        if (s.contains("white")) {
            return Clockwiseness.CW;
        }
        return null;
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, net.minecraft.util.RandomSource random) {
        super.tick(state, world, pos, random);
        if (isSafeToRecolor(world, pos.below())) {
            this.safeMove(state, world, pos);
        }
    }

    @Override
    public InteractionResult
    //? if >1.21 {
     /*useWithoutItem
    *///?} else {
    use
    //?}
    (BlockState state, Level world, BlockPos pos, Player player,
                                            //? if <1.21 {
                                            InteractionHand hand,
                                            //?}
                                            BlockHitResult hit) {
        return move(state, world, pos);
    }

    private InteractionResult move(BlockState blockState, Level world, BlockPos pos) {
        if (isSafeToRecolor(world, pos.below())) return safeMove(blockState, world, pos);
        return InteractionResult.FAIL;
    }

    private InteractionResult safeMove(BlockState blockState, Level world, BlockPos pos) {
        BlockState down = world.getBlockState(pos.below());
        Clockwiseness clockwiseness = getCW(down.getBlock());
        if (clockwiseness == null) return InteractionResult.FAIL;
        Direction direction = blockState.getValue(FACING);
        Direction direction2 = clockwiseness == Clockwiseness.CW ? direction.getClockWise() : direction.getCounterClockWise();
        BlockPos blockPos = pos.relative(direction2);
        if (world.isLoaded(blockPos) && world.getBlockState(blockPos).canBeReplaced()) {
            switch (clockwiseness) {
                case CW:
                    world.setBlock(pos.below(), Objects.requireNonNull(recolor(down.getBlock(), false)).withPropertiesOf(down), 19);
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    world.setBlock(blockPos, blockState.setValue(FACING, direction2), 3);
                    break;
                case CCW:
                    world.setBlock(pos.below(), Objects.requireNonNull(recolor(down.getBlock(), true)).withPropertiesOf(down), 19);
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    world.setBlock(blockPos, blockState.setValue(FACING, direction2), 3);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }
    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleTick(pos, this, 1);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public enum Clockwiseness {
        CW,
        CCW
    }
}
