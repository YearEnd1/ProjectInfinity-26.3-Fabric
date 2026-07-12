package net.codexarchonic.infinity.features;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.codexarchonic.infinity.registry.core.ModFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class WonkyTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<WonkyTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
            instance -> trunkPlacerParts(instance)
                    .and(
                            instance.group(
                                    ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("weight_up", 1f).forGetter(a -> a.weightUp),
                                    ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("weight_down", 0.1f).forGetter(a -> a.weightDown),
                                    ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("weight_side", 0.5f).forGetter(a -> a.weightSide)
                            )
                    )
                    .apply(instance, WonkyTrunkPlacer::new));
    final float weightUp;
    final float weightDown;
    final float weightSide;
    Direction currentDir;

    public WonkyTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight, float weightUp, float weightDown, float weightSide) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
        this.weightUp = Math.max(weightUp, 1f);
        this.weightDown = Math.max(weightDown, 1f);
        this.weightSide = Math.max(weightDown, 1f);
        currentDir = Direction.UP;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModFeatures.WONKY_TRUNK.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, int height, BlockPos startPos, TreeConfiguration config) {
        setDirtAt(world, replacer, random, startPos.below(), config);
        BlockPos curr = startPos;
        this.placeLog(world, replacer, random, startPos, config);
        for (int i = 0; i < height; i++) {
            double d = random.nextDouble() * (weightUp + weightDown + weightSide);
            Direction dir;
            if (d < weightUp) dir = Direction.UP;
            else if (d < weightUp + weightDown) dir = Direction.DOWN;
            else {
                dir = switch (random.nextInt(4)) {
                    case 1 -> Direction.WEST;
                    case 2 -> Direction.EAST;
                    case 3 -> Direction.NORTH;
                    default -> Direction.SOUTH;
                };
            }
            if (!this.validTreePos(world, curr.relative(dir))) dir = Direction.UP;
            curr = curr.relative(dir);
            currentDir = dir;
            this.placeLog(world, replacer, random, curr, config);
        }
        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(curr, 0, false));
    }

    @Override


    protected boolean placeLog(
            LevelSimulatedReader world,
            BiConsumer<BlockPos, BlockState> replacer,
            RandomSource random,
            BlockPos pos,
            TreeConfiguration config,
            Function<BlockState, BlockState> function
    ) {
        if (this.validTreePos(world, pos)) {
            BlockState bs = function.apply(config.trunkProvider.getState(random, pos));
            Direction.Axis axis = currentDir.getAxis();
            if (bs.hasProperty(BlockStateProperties.AXIS)) bs = bs.setValue(BlockStateProperties.AXIS, axis);
            if (bs.hasProperty(BlockStateProperties.HORIZONTAL_AXIS) && axis.isHorizontal()) bs = bs.setValue(BlockStateProperties.HORIZONTAL_AXIS, axis);
            if (bs.hasProperty(BlockStateProperties.FACING)) bs = bs.setValue(BlockStateProperties.FACING, currentDir);
            if (bs.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && axis.isHorizontal()) bs = bs.setValue(BlockStateProperties.HORIZONTAL_FACING, currentDir);
            if (bs.hasProperty(BlockStateProperties.FACING_HOPPER) && currentDir != Direction.UP) bs = bs.setValue(BlockStateProperties.FACING_HOPPER, currentDir);
            replacer.accept(pos, bs);
            return true;
        } else {
            return false;
        }
    }
}
