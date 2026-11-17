package net.codexarchonic.infinity.features;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.codexarchonic.infinity.util.InfinityMethods;
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

public class NotesHouseFeature implements Feature {
    public static final MapCodec<NotesHouseFeature> CODEC = Config.CODEC.xmap(NotesHouseFeature::new, feature -> feature.config);

    private final Config config;

    public NotesHouseFeature(Config config) {
        this.config = config;
    }

    @Override
    public MapCodec<NotesHouseFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel levelArg, ChunkGenerator generator, RandomSource randomArg, BlockPos originArg) {
        RandomSource random = randomArg;
        WorldGenLevel level = levelArg;
        BlockPos origin = originArg;
        int r = config.radius;
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

    public record Config(int radius) {
        public static final MapCodec<Config> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                (Codec.INT.fieldOf("radius")).forGetter(a -> a.radius)).apply(
                instance, NotesHouseFeature.Config::new));
    }
}
