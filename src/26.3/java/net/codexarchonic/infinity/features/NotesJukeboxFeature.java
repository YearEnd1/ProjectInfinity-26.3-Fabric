package net.codexarchonic.infinity.features;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import net.codexarchonic.infinity.item.DiscItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

public class NotesJukeboxFeature implements Feature {
    public static final MapCodec<NotesJukeboxFeature> CODEC = MapCodec.unit(new NotesJukeboxFeature());


    @Override
    public MapCodec<NotesJukeboxFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel levelArg, ChunkGenerator generator, RandomSource randomArg, BlockPos originArg) {
        WorldGenLevel level = levelArg;
        BlockPos origin = originArg.below();
        setBlock(level, origin, Blocks.JUKEBOX.defaultBlockState());
        RandomSource random = randomArg;
        Direction dir = InfinityMethods.getRandomHorizontalDirection(random);
        setBlock(level, origin.relative(dir), Blocks.HOPPER.defaultBlockState().setValue(HopperBlock.FACING, dir.getOpposite()));
        if (level.getBlockEntity(origin.relative(dir)) instanceof HopperBlockEntity hpbe) {
            ItemStack stack = DiscItem.getRandom(levelArg.getServer());
            hpbe.setItem(0, stack);
        }
        return true;
    }
}
