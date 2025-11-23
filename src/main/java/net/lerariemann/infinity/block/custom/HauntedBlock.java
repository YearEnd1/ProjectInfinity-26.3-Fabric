package net.lerariemann.infinity.block.custom;

import net.lerariemann.infinity.options.InfinityOptions;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@MethodsReturnNonnullByDefault
@Deprecated
public class HauntedBlock extends Block {
    BlockState original;

    public HauntedBlock(Block original) {
        super(VersionMethods.copyBlockProperties(original).setId(ModBlocks.registryKey("haunted_air")));
        this.original = original.defaultBlockState();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return original.getCollisionShape(world, pos);
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return original.getShape(world, pos);
    }
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return original.getRenderShape();
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onPlace(state, world, pos, oldState, notify);
        int t = InfinityOptions.access(world).getHauntingTicks(world.random);
        if (t > 0) world.scheduleTick(pos, this, t);
    }
    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        super.tick(state, world, pos, random);
        world.setBlockAndUpdate(pos, original);
    }
    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        world.setBlockAndUpdate(pos, original);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return 15;
    }
    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }
}
