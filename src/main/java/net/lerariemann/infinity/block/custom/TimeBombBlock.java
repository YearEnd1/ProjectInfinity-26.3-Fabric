package net.lerariemann.infinity.block.custom;

import net.lerariemann.infinity.access.Timebombable;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.registry.var.ModCriteria;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.registry.var.ModSounds;
import net.lerariemann.infinity.registry.var.ModStats;
import net.lerariemann.infinity.util.teleport.PortalCreator;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

@MethodsReturnNonnullByDefault
public class TimeBombBlock extends Block {
    public static  final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final VoxelShape TOP_SHAPE = Block.box(3, 0, 3, 13, 2, 13);
    public static final VoxelShape BOTTOM_SHAPE = Block.box(3, 12, 3, 13, 14, 13);
    public static final VoxelShape INNER_SHAPE = Block.box(4, 2, 4, 12, 12, 12);
    public static final VoxelShape TIP_SHAPE = Block.box(7, 14, 7, 9, 16, 9);
    public static final VoxelShape SHAPE = Shapes.or(BOTTOM_SHAPE, TOP_SHAPE, INNER_SHAPE, TIP_SHAPE);
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public TimeBombBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    void eraseWorld(ServerLevel world) {
        ((Timebombable)world).infinity$timebomb();
        Path path = world.getServer().getWorldPath(LevelResource.DATAPACK_DIR).resolve(world.dimension().location().getPath());
        try {
            FileUtils.deleteDirectory(path.toFile());
        } catch (IOException ignored) {
        }
    }

    static AreaEffectCloud genCloud(Level world, BlockPos pos) {
        AreaEffectCloud e = new AreaEffectCloud(world, pos.getCenter().x(), pos.getCenter().y(), pos.getCenter().z());
        e.setParticle(ParticleTypes.DRAGON_BREATH);
        e.setRadius(1.0f);
        e.setDuration(3500);
        e.setRadiusPerTick(0.006f);
        world.levelEvent(LevelEvent.PARTICLES_DRAGON_FIREBALL_SPLASH, pos, 1);
        return e;
    }

    @Override
    //? if <1.21 {
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
     //?} else {
    /*protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        *///?}
        if (state.getValue(ACTIVE)) {
            if (world instanceof ServerLevel w && !InfinityMethods.isTimebombed(w)) {
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                world.getEntities(EntityTypeTest.forClass(AreaEffectCloud.class), AABB.ofSize(pos.getCenter(), 1.0, 1.0, 1.0), Entity::isAlive).
                        forEach(e -> e.remove(Entity.RemovalReason.DISCARDED));
                return InteractionResult.SUCCESS;
            } //remove after regenerating a dimension
            return InteractionResult.FAIL;
        }
        else if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            if (player.isShiftKeyDown()) {
                if (InfinityMethods.isInfinity(world)) {
                    if (world instanceof ServerLevel w && !InfinityMethods.isTimebombed(w)) {
                        eraseWorld(w);
                        world.addFreshEntity(genCloud(world, pos));
                        player.awardStat(ModStats.WORLDS_DESTROYED_STAT, 1);
                        ModCriteria.DIMS_CLOSED.get().trigger((ServerPlayer)player);
                        world.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
                        world.playSound(null, pos, ModSounds.IVORY_MUSIC_CHALLENGER_EVENT, SoundSource.RECORDS, 1f, 1f);
                    } //activate
                    return InteractionResult.SUCCESS;
                }
                else if (world instanceof ServerLevel) {
                    world.playSound(null, pos, ModSounds.portalFail, SoundSource.BLOCKS, 1f, 1f);
                    player.sendSystemMessage(Component.translatable("error.infinity.timebomb.fail"));
                }
            }
            else {
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                if (!player.isCreative()) player.getInventory().add(ModItems.TIME_BOMB_ITEM.get().getDefaultInstance());
                return InteractionResult.SUCCESS;
            } //pick up
        }
        return InteractionResult.PASS;
    }
}
