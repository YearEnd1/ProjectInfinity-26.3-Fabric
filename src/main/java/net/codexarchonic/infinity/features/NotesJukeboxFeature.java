package net.codexarchonic.infinity.features;

import com.mojang.serialization.Codec;
import net.codexarchonic.infinity.item.DiscItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

public class NotesJukeboxFeature extends Feature<NoneFeatureConfiguration> {
    public NotesJukeboxFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin().below();
        setBlock(level, origin, Blocks.JUKEBOX.defaultBlockState());
        RandomSource random = context.random();
        Direction dir = InfinityMethods.getRandomHorizontalDirection(random);
        setBlock(level, origin.relative(dir), Blocks.HOPPER.defaultBlockState().setValue(HopperBlock.FACING, dir.getOpposite()));
        if (level.getBlockEntity(origin.relative(dir)) instanceof HopperBlockEntity hpbe) {
            ItemStack stack = DiscItem.getRandom(context.level().getServer());
            hpbe.setItem(0, stack);
        }
        return true;
    }
}
