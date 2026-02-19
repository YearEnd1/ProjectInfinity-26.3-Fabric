package net.lerariemann.infinity.block.custom;

import net.lerariemann.infinity.block.entity.BiomeBottleBlockEntity;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.var.ModCriteria;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.var.BishopBattle;
import net.minecraft.core.BlockPos;
//? if >1.21 {
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.CustomModelData;
//?}
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.Map;

public class AltarBlock extends Block {
    public static final int numColors = 13;
    public static final BooleanProperty FLOWER = BooleanProperty.create("flower");
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, numColors - 1);
    public static final VoxelShape BASE_SHAPE = Block.box(1.5, 0, 1.5, 14.5, 14, 14.5);
    public static final VoxelShape TOP_SHAPE = Block.box(0, 14, 0, 16, 16, 16);
    public static final VoxelShape LEG1 = Block.box(0, 0, 0, 3, 3, 3);
    public static final VoxelShape LEG2 = Block.box(0, 0, 13, 3, 3, 16);
    public static final VoxelShape LEG3 = Block.box(13, 0, 0, 16, 3, 3);
    public static final VoxelShape LEG4 = Block.box(13, 0, 13, 16, 3, 16);
    public static final VoxelShape SHAPE = Shapes.or(BASE_SHAPE, TOP_SHAPE, LEG1, LEG2, LEG3, LEG4);

    public AltarBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(defaultBlockState().setValue(COLOR, 0).setValue(FLOWER, false));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR);
        builder.add(FLOWER);
    }

    public static void setColor(Level world, BlockPos pos, BlockState state, int i) {
        world.setBlockAndUpdate(pos, state.setValue(COLOR, i));
    }

    //? if >1.21 {
    public static DataComponentPatch toComponentChanges(BlockState state) {
        int color = state.getValue(AltarBlock.COLOR);
        boolean flower = state.getValue(AltarBlock.FLOWER);
        if (color > 0 || flower) return DataComponentPatch.builder().set(DataComponents.BLOCK_STATE,
                        new BlockItemStateProperties(Map.of())
                                .with(AltarBlock.COLOR, color)
                                .with(AltarBlock.FLOWER, flower))
                .set(DataComponents.CUSTOM_MODEL_DATA, VersionMethods.getColoredModel(color +
                        (flower ? AltarBlock.numColors : 0)))
                .build();
        return DataComponentPatch.EMPTY;
    }
    //?} else {
    /*public static CompoundTag toComponentChanges(BlockState state) {
        CompoundTag compoundTag = new CompoundTag();
        int color = state.getValue(AltarBlock.COLOR);
        boolean flower = state.getValue(AltarBlock.FLOWER);
        if (color > 0 || flower) {
            compoundTag.putInt("CustomModelData", color +
                    (flower ? AltarBlock.numColors : 0));
            compoundTag.putInt(AltarBlock.COLOR.getName(), color);
            compoundTag.putBoolean(AltarBlock.FLOWER.getName(), flower);
        }
        return compoundTag;
    }
    *///?}

    @Override
    public ItemStack getCloneItemStack(
            //? if >1.21 {
            LevelReader
             //?} else {
            /*BlockGetter
                    *///?}
            world, BlockPos pos, BlockState state
            //? if >1.21.2
            , boolean bl
    ) {
        ItemStack res = super.getCloneItemStack(world, pos, state
        //? if >1.21.2
        , bl
        );
        //? if >1.21 {
        res.applyComponentsAndValidate(toComponentChanges(state));
        //?} else {
        /*res.setTag(toComponentChanges(state));
        *///?}
        return res;
    }

    public void testRituals(ServerLevel world, BlockPos pos, ServerPlayer player) {
        //biome spreading
        if (world.getBlockEntity(pos.above()) instanceof BiomeBottleBlockEntity bbbe) {
            ModCriteria.BIOME_BOTTLE.get().trigger(player, bbbe);
            bbbe.startTicking();
        }
    }

    @Override
    //? if >1.21 {
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
    //?} else {
    /*public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    *///?}
        ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        //rituals
        if (world instanceof ServerLevel serverWorld) {
            if (itemStack.isEmpty()) testRituals(serverWorld, pos, (ServerPlayer)player);
        }
        //coloration
        if (itemStack.getItem() instanceof DyeItem) {
            int i = getColor(itemStack, state);
            if (i>=0 && state.getValue(COLOR) != i) {
                setColor(world, pos, state, i);
                world.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1f, 1f);
                if (world instanceof ServerLevel)
                    world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state.setValue(COLOR, i)));
            }
            return InteractionResult.SUCCESS;
        }
        if (itemStack.is(Items.SUNFLOWER)) {
            world.setBlockAndUpdate(pos, state.setValue(FLOWER, !state.getValue(FLOWER)));
            world.playSound(null, pos, SoundEvents.AZALEA_LEAVES_PLACE, SoundSource.BLOCKS, 1f, 1f);
            return InteractionResult.SUCCESS;
        }
        //? if >1.21 {
        return super.useWithoutItem(state, world, pos, player, hit);
        //?} else {
        /*return super.use(state, world, pos, player, hand, hit);
        *///?}
    }

    public static int getColor(ItemStack itemStack, BlockState oldState) {
        int i = -1;
        if (itemStack.is(Items.RED_DYE)) i = 1;
        if (itemStack.is(Items.ORANGE_DYE)) i = 2;
        if (itemStack.is(Items.YELLOW_DYE)) i = 3;
        if (itemStack.is(Items.LIME_DYE)) i = oldState.getValue(COLOR) == 6 ? 5 : 4;
        if (itemStack.is(Items.GREEN_DYE)) i = oldState.getValue(COLOR) == 4 ? 5 : 6;
        if (itemStack.is(Items.LIGHT_BLUE_DYE)) i = 7;
        if (itemStack.is(Items.CYAN_DYE)) i = 8;
        if (itemStack.is(Items.BLUE_DYE)) i = 9;
        if (itemStack.is(Items.PURPLE_DYE)) i = 10;
        if (itemStack.is(Items.MAGENTA_DYE)) i = 11;
        if (itemStack.is(Items.PINK_DYE)) i = 12;
        if (itemStack.is(Items.GRAY_DYE)) i = 0;
        return i;
    }
}
