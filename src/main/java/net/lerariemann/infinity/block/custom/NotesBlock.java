package net.lerariemann.infinity.block.custom;

import net.lerariemann.infinity.block.entity.ChromaticBlockEntity;
import net.lerariemann.infinity.options.InfinityOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class NotesBlock extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty TICKING = BooleanProperty.create("ticking");
    public NotesBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false).setValue(TICKING, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
        builder.add(TICKING);
    }

    @Override
    //? if >1.21 {
    protected
    //?} else {
    /*public
    *///?}
    boolean isRandomlyTicking(BlockState state) {
        return state.hasProperty(TICKING) && state.getValue(TICKING);
    }

    @Override
    //? if >1.21 {
    protected
     //?} else {
    /*public
        *///?}
    void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock,
                         //? if >1.21.2 {
                         Orientation orientation
                         //?} else {
                          /*BlockPos sourcePos
                         *///?}
            , boolean notify) {
        boolean bl = world.hasNeighborSignal(pos);
        if (bl != state.getValue(POWERED)) {
            if (bl) this.play(null, world, pos);
            world.setBlock(pos, state.setValue(POWERED, bl), 3);
        }
    }

    public boolean isTicking(ServerLevel world, BlockState state) {
        return state.getValue(TICKING) && (!InfinityOptions.access(world).isHaunted());
    }

    @Override
    //? if >1.21 {
    protected
     //?} else {
    /*public
    *///?}
     void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (isTicking(world, state) && random.nextInt(115) == 0) {
            this.play(null, world, pos);
        }
    }
    
    @Override
    //? if <1.21 {
    /*public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        *///?} else {
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        //?}
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            boolean bl = state.getValue(TICKING);
            world.setBlock(pos, state.setValue(TICKING, !bl), 3);
            play(player, world, pos);
            player.awardStat(Stats.TUNE_NOTEBLOCK);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    //? if >1.21 {
    protected
     //?} else {
    /*public
        *///?}
     void attack(BlockState state, Level world, BlockPos pos, Player player) {
        if (!world.isClientSide()) {
            play(player, world, pos);
            player.awardStat(Stats.PLAY_NOTEBLOCK);
        }
    }

    public void play(@Nullable Entity entity, Level world, BlockPos pos) {
        if (world.getBlockState(pos.above()).isAir()) {
            world.blockEvent(pos, this, 0, 0);
            world.gameEvent(entity, GameEvent.NOTE_BLOCK_PLAY, pos);
        }
    }

    @Override
    //? if >1.21 {
    protected
     //?} else {
    /*public
        *///?}
     boolean triggerEvent(BlockState state, Level world, BlockPos pos, int type, int data) {
        NoteBlockInstrument noteBlockInstrument;
        float f;
        if (world.getBlockEntity(pos.below()) instanceof ChromaticBlockEntity e) {
            noteBlockInstrument = NoteBlockInstrument.GUITAR;
            f = (float)Math.pow(2.0, 2 * (e.brightness / 255f) - 1);
        }
        else {
            NoteBlockInstrument[] instruments = NoteBlockInstrument.values();
            noteBlockInstrument = instruments[world.random.nextInt(instruments.length-7)];
            if (noteBlockInstrument.isTunable()) {
                int i = world.random.nextInt(24);
                f = NoteBlock.getPitchFromNote(i);
                world.addParticle(ParticleTypes.NOTE, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, i / 24.0, 0.0F, 0.0F);
            } else {
                f = 1.0F;
            }
        }

        world.playSeededSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, noteBlockInstrument.getSoundEvent(), SoundSource.RECORDS, 3.0F, f, world.random.nextLong());
        return true;
    }
}
