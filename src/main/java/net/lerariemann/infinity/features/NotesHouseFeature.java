package net.lerariemann.infinity.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class NotesHouseFeature extends Feature<NotesHouseFeature.Config> {
    public NotesHouseFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        RandomSource random = context.random();
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        int r = context.config().radius;
        for (int i = -r; i <= r; i++) for (int j = -r; j <= r; j++) {
            BlockPos local = origin.offset(i, 0, j);
            if (!Blocks.IRON_DOOR.defaultBlockState().canSurvive(level, local)) continue;
            boolean open = random.nextBoolean();
            Direction dir = InfinityMethods.getRandomHorizontalDirection(random);
            setBlock(level, local, Blocks.IRON_DOOR.defaultBlockState()
                    .setValue(DoorBlock.FACING, dir)
                    .setValue(DoorBlock.OPEN, open)
                    .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
            setBlock(level, local.above(), Blocks.IRON_DOOR.defaultBlockState()
                    .setValue(DoorBlock.FACING, dir)
                    .setValue(DoorBlock.OPEN, open)
                    .setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
            setBlock(level, local.above(2), Blocks.STICKY_PISTON.defaultBlockState()
                    .setValue(PistonBaseBlock.FACING, Direction.UP)
                    .setValue(PistonBaseBlock.EXTENDED, false));
            setBlock(level, local.above(3), Blocks.NOTE_BLOCK.defaultBlockState()
                    .setValue(NoteBlock.INSTRUMENT, InfinityMethods.getRandomInstrument())
                    .setValue(NoteBlock.NOTE, random.nextInt(24)));
        }
        return true;
    }

    public record Config(int radius) implements FeatureConfiguration {
        public static final Codec<NotesHouseFeature.Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                (Codec.INT.fieldOf("radius")).forGetter(a -> a.radius)).apply(
                instance, NotesHouseFeature.Config::new));
    }
}
