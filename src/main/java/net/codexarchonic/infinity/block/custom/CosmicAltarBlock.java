package net.codexarchonic.infinity.block.custom;

import com.mojang.serialization.MapCodec;
import net.codexarchonic.infinity.block.entity.CosmicAltarBlockEntity;
import net.codexarchonic.infinity.registry.var.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import static net.codexarchonic.infinity.InfinityMod.random;

public class CosmicAltarBlock extends BaseEntityBlock implements Boopable {
    //? if >1.21
    public static final MapCodec<CosmicAltarBlock> CODEC = simpleCodec(CosmicAltarBlock::new);
    public CosmicAltarBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        appendBoop(builder);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (level.getBlockEntity(pos) instanceof CosmicAltarBlockEntity cabe) {
            if (cabe.populated && cabe.error.isEmpty()) {
                cabe.pedestals.forEach((i, p) -> p.forEach(pp -> addParticle(level, pos, pp, i)));
            }
        }
    }

    public static void addParticle(Level lv, BlockPos altarPos, BlockPos pedestalPos, int type) {
        SimpleParticleType spt = ModParticles.altarParticles.get(type).get();
        Vec3 start = pedestalPos.getCenter();
        Vec3 end = altarPos.getCenter();
        lv.addParticle(spt,
                end.x, end.y + 1.5, end.z,
                start.x - end.x - random.nextFloat(),
                start.y - end.y - random.nextFloat() - 1.5,
                start.z - end.z - random.nextFloat());
    }

    //? if >1.21 {
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    //?}

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CosmicAltarBlockEntity(pos, state);
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return null;
    }
}
