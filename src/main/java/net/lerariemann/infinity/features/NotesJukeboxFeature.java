package net.lerariemann.infinity.features;

import com.mojang.serialization.Codec;
import net.lerariemann.infinity.item.DiscItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
//? if >1.21 {
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.ConfigType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;
//?}

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
