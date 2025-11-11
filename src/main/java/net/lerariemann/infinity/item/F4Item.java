package net.lerariemann.infinity.item;

import net.lerariemann.infinity.block.entity.InfinityPortalBlockEntity;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.ClientMethods;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.teleport.InfinityPortal;
import net.minecraft.BlockUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.*;

//? if neoforge {
/*@ParametersAreNonnullByDefault
*///?}
@MethodsReturnNonnullByDefault
public class F4Item extends Item implements PortalDataHolder.Destinable {
    static final BlockState OBSIDIAN = Blocks.OBSIDIAN.defaultBlockState();

    public F4Item(Properties settings) {
        super(settings);
    }

    public static MutableComponent getDimensionTooltip(@Nullable ResourceLocation dimension) {
        if (dimension == null) return Component.translatable(Blocks.NETHER_PORTAL.getDescriptionId());
        String name = dimension.toString();
        MutableComponent text = InfinityMethods.getDimensionNameAsText(dimension);
        if (name.contains("infinity:generated_") || name.equals(InfinityMethods.ofRandomDim))
            return text;
        return Component.translatable("tooltip.infinity.f4", text);
    }

    public static int getCharge(ItemStack f4) {
        return VersionMethods.getOrDefaultInt(f4, ModComponentTypes.CHARGE, 0);
    }

    @Override
    public ItemStack getStack() {
        return getDefaultInstance();
    }

    @Override
    public void appendHoverText(ItemStack stack,
                                //? if >1.21 {
                                /*TooltipContext
                                *///?} else {
                                Level
                                //?}
                                            context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        ResourceLocation dimension = VersionMethods.getDimensionIdentifier(stack);
        MutableComponent mutableText = getDimensionTooltip(dimension);
        tooltip.add(mutableText.withStyle(ChatFormatting.GRAY));
        MutableComponent mutableText2 = Component.translatable("tooltip.infinity.f4.charges", getCharge(stack));
        tooltip.add(mutableText2.withStyle(ChatFormatting.GRAY));
    }

    //? if >1.21 {
    /*@Nullable
     *///?}
    public static ItemStack placePortal(Level world, Player player, ItemStack stack, BlockPos lowerCenter,
                                    int size_x, int size_y) {
        Direction.Axis dir2 = player.getDirection().getClockWise(Direction.Axis.Y).getAxis();

        int charges = getCharge(stack);
        int useCharges = player.isCreative() ? 0 : 2*(2 + size_x + size_y);
        if (charges < useCharges) {
            if (!world.isClientSide())
                player.sendSystemMessage(Component.translatable("error.infinity.f4.no_charges", useCharges));
            return null;
        }
        BlockPos lowerLeft = lowerCenter.relative(dir2, -(size_x/2));
        ResourceLocation id = VersionMethods.getDimensionIdentifier(stack);
        boolean doNotRenderPortal = (world.isClientSide && (id == null || !id.getPath().contains("generated_")));
        if (PortalDataHolder.isDestinationRandom(id))
            id = InfinityMethods.getRandomId(world.random);
        int obsNotReplaced = 0;

        //placing the portal
        for (int x = -1; x <= size_x; x++) {
            if (!world.setBlockAndUpdate(lowerLeft.relative(dir2, x).above(size_y), OBSIDIAN)) obsNotReplaced++;
            if (!world.setBlockAndUpdate(lowerLeft.relative(dir2, x).below(), OBSIDIAN)) obsNotReplaced++;
        }
        for (int y = 0; y < size_y; y++) {
            if (!world.setBlockAndUpdate(lowerLeft.relative(dir2, -1).relative(Direction.UP, y), OBSIDIAN)) obsNotReplaced++;
            if (!world.setBlockAndUpdate(lowerLeft.relative(dir2, size_x).relative(Direction.UP, y), OBSIDIAN)) obsNotReplaced++;
            if (!doNotRenderPortal) for (int x = 0; x < size_x; x++) {
                BlockPos pos = lowerLeft.relative(dir2, x).relative(Direction.UP, y);
                world.setBlockAndUpdate(pos,
                        ((id == null) ? Blocks.NETHER_PORTAL : ModBlocks.PORTAL.get())
                                .defaultBlockState().setValue(NetherPortalBlock.AXIS, dir2));
                if (id != null && world.getBlockEntity(pos) instanceof InfinityPortalBlockEntity ipbe) {
                    ipbe.setData(world.getServer(), id);
                }
            }
        }
        useCharges -= obsNotReplaced;

        world.playSound(player, player.blockPosition(), SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 1, 0.75f);
        return VersionMethods.apply(stack, ModComponentTypes.CHARGE, charges-useCharges);

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (world.isClientSide)
            ClientMethods.setF4Screen(player);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    public static InteractionResultHolder<ItemStack> deploy(Level world, Player player, InteractionHand hand) {
        Direction dir = player.getDirection();
        Direction.Axis dir2 = dir.getClockWise(Direction.Axis.Y).getAxis();
        BlockPos lowerCenter = player.blockPosition().relative(dir, 4);
        ItemStack stack = player.getItemInHand(hand);

        int size_x = VersionMethods.getOrDefaultInt(stack, ModComponentTypes.SIZE_X, 3);
        int size_y = VersionMethods.getOrDefaultInt(stack, ModComponentTypes.SIZE_Y, 3);
        if (size_y % 2 == 0) {
            double d = dir2.equals(Direction.Axis.X) ? player.position().x : player.position().z;
            if (d % 1 > 0.5) { //player on the positive side of the block
                lowerCenter = lowerCenter.relative(dir2, 1);
            }
        }

        int lowerY = lowerCenter.getY();
        if (world.isOutsideBuildHeight(lowerY)) return InteractionResultHolder.pass(stack);

        //finding a place position
        int i;
        boolean positionFound = true;
        for (i = 0; i <= 8 && !world.isOutsideBuildHeight(lowerY + i + size_y); i++) {
            positionFound = true;
            for (int j = 0; j <= size_y+1 && positionFound; j++) for (int k = -1; k <= size_x; k++) {
                BlockState bs = world.getBlockState(lowerCenter.above(i+j-1).relative(dir2, k - (size_x /2)));
                if (bs.canBeReplaced()) continue;
                if (bs.is(Blocks.OBSIDIAN)) {
                    if (j == 0 || j == size_y+1 || k == -1 || k == size_x) continue;
                    i += j-1;
                }
                else i += j;
                positionFound = false;
                break;
            }
            if (positionFound) break;
        }
        if (!positionFound) return InteractionResultHolder.pass(stack);

        ItemStack newStack = placePortal(world, player, stack.copy(), lowerCenter.above(i), size_x, size_y);
        if (newStack == null) return InteractionResultHolder.pass(stack);
        return InteractionResultHolder.consume(player.isCreative() ? stack : newStack);
    }

    public static boolean isPortal(BlockState state) {
        return state.is(Blocks.NETHER_PORTAL) || state.is(ModBlocks.PORTAL.get());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;
        ItemStack stack = context.getItemInHand();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState bs = world.getBlockState(pos);
        if (bs.canBeReplaced()) {
            pos = pos.below();
            bs = world.getBlockState(pos);
        }
        InteractionHand hand = context.getHand();

        if (isPortal(bs)) {
            ItemStack newStack = useOnPortalBlock(world, pos, stack.copy());
            if (!player.isCreative()) player.setItemInHand(hand, newStack);
            return InteractionResult.CONSUME;
        }
        pos = pos.above(bs.is(Blocks.OBSIDIAN) ? 1 : 2);

        Direction.Axis dir2 =
                player.getDirection().getAxis().equals(Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;

        int size_x = VersionMethods.getOrDefaultInt(stack, ModComponentTypes.SIZE_X, 3);
        int size_y = VersionMethods.getOrDefaultInt(stack, ModComponentTypes.SIZE_Y, 3);

        //validating the place position
        for (int j = -1; j <= size_y; j++) for (int k = -1; k <= size_x; k++) {
            bs = world.getBlockState(pos.above(j).relative(dir2, k - (size_x /2)));
            if (bs.is(Blocks.OBSIDIAN) && (j == -1 || j == size_y || k == -1 || k == size_x)) continue;
            if (!bs.canBeReplaced()) return InteractionResult.FAIL;
        }

        ItemStack newStack = placePortal(world, player, context.getItemInHand().copy(), pos,
                size_x, size_y);
        if (newStack == null) {
            return InteractionResult.FAIL;
        }
        if (!player.isCreative()) player.setItemInHand(hand, newStack);
        return InteractionResult.CONSUME;
    }

    public static boolean checkIfValidAxis(Direction.Axis axisFound, Direction.Axis axisBeingChecked, @Nullable Direction.Axis forceAxis) {
        if (forceAxis == null) {
            return axisBeingChecked.isVertical() || axisFound.equals(axisBeingChecked);
        }
        return axisFound.equals(forceAxis);
    }

    public static boolean checkNeighbors(Level world, BlockPos bp,
                                         Collection<Direction> primaryOffsets, //possible directions in which blocks of other portals may be
                                         Collection<Direction> secondaryOffsets, //possible secondary directions from a neighboring obsidian in case we found a corner
                                         int max, @Nullable Direction.Axis forcePrimaryAxis) {
        int i = 0;
        boolean checkCorners = !secondaryOffsets.isEmpty();
        for (Direction dir : primaryOffsets) {
            BlockState bs = world.getBlockState(bp.relative(dir));
            Direction.Axis axis = dir.getAxis();
            if (isPortal(bs)) {
                Direction.Axis axisFound = bs.getValue(BlockStateProperties.HORIZONTAL_AXIS);
                if (checkIfValidAxis(axisFound, axis, forcePrimaryAxis))
                    if (++i > max) return true;
            }
            if (checkCorners && bs.is(Blocks.OBSIDIAN)) {
                Direction.Axis forceAxis = axis.isHorizontal() ? axis : null;
                return checkNeighbors(world, bp.relative(dir), secondaryOffsets, Set.of(), 0, forceAxis);
            }
        }
        return false;
    }

    public static void checkObsidianRemovalSides(Level world, BlockPos bp,
                                                 Set<BlockPos> toRemove,
                                                 Set<BlockPos> toLeave,
                                                 Direction direction) {
        if (world.getBlockState(bp).is(Blocks.OBSIDIAN)) {
            boolean bl = direction.getAxis().isVertical();
            Set<Direction> primaryOffsets =  bl ? Set.of(direction) : Set.of(direction,
                    direction.getClockWise(Direction.Axis.Y),
                    direction.getCounterClockWise(Direction.Axis.Y));
            Set<Direction> secondaryOffsets = bl ? Set.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST) :
                    Set.of(Direction.UP, Direction.DOWN);
            if (checkNeighbors(world, bp, primaryOffsets, secondaryOffsets, 0, null)) {
                toLeave.add(bp);
                return;
            }
            toRemove.add(bp);
        }
    }

    public static ItemStack useOnPortalBlock(Level world, BlockPos origin, ItemStack stack) {
        Direction.Axis axis = world.getBlockState(origin).getValue(NetherPortalBlock.AXIS);
        Direction positive = axis.equals(Direction.Axis.X) ? Direction.EAST : Direction.SOUTH;
        BlockUtil.FoundRectangle portal = InfinityPortal.getRect(world, origin);
        Set<BlockPos> toRemove = new HashSet<>();
        Set<BlockPos> toLeave = new HashSet<>();
        for (int i = -1; i <= portal.axis1Size; i++) {
            checkObsidianRemovalSides(world, portal.minCorner.relative(axis, i).above(-1), toRemove, toLeave, Direction.DOWN);
            checkObsidianRemovalSides(world, portal.minCorner.relative(axis, i).above(portal.axis2Size), toRemove, toLeave, Direction.UP);
        }
        for (int j = -1; j <= portal.axis2Size; j++) {
            checkObsidianRemovalSides(world, portal.minCorner.relative(axis, -1).above(j), toRemove, toLeave, positive.getOpposite());
            checkObsidianRemovalSides(world, portal.minCorner.relative(axis, portal.axis1Size).above(j), toRemove, toLeave, positive);
        }
        int obsidian = 0;
        for (BlockPos bp : toRemove) if (!toLeave.contains(bp)) { //double check since we're checking the corners twice
            world.setBlock(bp,  Blocks.AIR.defaultBlockState(), 3, 0);
            obsidian++;
        }
        for (int i = 0; i < portal.axis1Size; i++) for (int j = 0; j < portal.axis2Size; j++)
            world.setBlock(portal.minCorner.relative(axis, i).above(j), Blocks.AIR.defaultBlockState(), 3, 0);
        world.playSound(null, origin, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1, 0.75f);
        return VersionMethods.apply(stack, ModComponentTypes.CHARGE, getCharge(stack)+obsidian);

    }
}