package net.codexarchonic.infinity.block.custom;

import com.mojang.serialization.MapCodec;
import net.codexarchonic.infinity.iridescence.Iridescence;
import net.codexarchonic.infinity.registry.core.ModItems;
import net.codexarchonic.infinity.util.VersionMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
//? =1.21.1
//import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
//? if >1.21.2 {
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.Block;
//?}
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
//? if =1.21.1 {
/*import static net.minecraft.world.ItemInteractionResult.SUCCESS;
*///?} else {
import static net.minecraft.world.InteractionResult.SUCCESS;
//?}

public class IridescenceCauldronBlock extends AbstractCauldronBlock {

	public IridescenceCauldronBlock(BlockBehaviour.Properties properties) {
		super(properties, null);
	}

	@Override
	protected double getContentHeight(BlockState state) {
		return 0.9375;
	}

	@Override
	public boolean isFull(BlockState state) {
		return true;
	}

	//? if >1.21.2 {
	private static final VoxelShape SHAPE_INSIDE = Block.column(12.0, 4.0, 15.0);
	private static final VoxelShape FILLED_SHAPE = Shapes.or(AbstractCauldronBlock.SHAPE, SHAPE_INSIDE);

	@Override
	protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
		return FILLED_SHAPE;
	}
	//?}

	//~ if =1.21.1 'InteractionResult' -> 'ItemInteractionResult' {
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
	//~}
		if (stack.is(Items.BUCKET)) {
			stack.setCount(stack.getCount()-1);
			player.getInventory().add(ModItems.IRIDESCENCE_BUCKET.get().getDefaultInstance());
			level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
			return SUCCESS;
		}
		return VersionMethods.passToDefaultBlockInteraction();
	}

	//? <1.21 {
	/*public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (player.getItemInHand(hand).getItem() == Items.BUCKET) {
			return useItemOn(player.getItemInHand(hand), state, level, pos, player, hand, hit);
		}
		return InteractionResult.PASS;
	}
	*///?}

	@Override
	public ItemStack getCloneItemStack(
			//~ if >1.21 'BlockGetter' -> 'LevelReader' {
			LevelReader level, BlockPos pos, BlockState state
			//~}
			//? >1.21.2
			, boolean includeData
	) {
		return Items.CAULDRON.getDefaultInstance();
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity
			//? if >1.21.2
			, InsideBlockEffectApplier effectApplier
			//? if >26
			//, boolean isPrecise
	) {
		if (level instanceof ServerLevel serverLevel)
			Iridescence.touchIridescence(entity, serverLevel);
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos
		//? if >26
	 	//,Direction direction
	) {
		return 3;
	}
}
