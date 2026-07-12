package net.codexarchonic.infinity.mixin.core;

import net.codexarchonic.infinity.registry.core.ModBlocks;
import net.codexarchonic.infinity.block.entity.InfinityPortalBlockEntity;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.codexarchonic.infinity.util.core.RandomProvider;
import net.codexarchonic.infinity.util.teleport.PortalCreator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
//? if >1.21.4 {
import net.minecraft.world.entity.InsideBlockEffectApplier;
//?}
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin extends AbstractBlockMixin {
	@Shadow @Final public static EnumProperty<Direction.Axis> AXIS;

	/* The root hook for "throw a book in the portal" logic. */
    @Inject(at = @At("HEAD"), method = "entityInside")
    //? if >1.21.9 {
    /*private void throwBookInPortal(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean bl, CallbackInfo ci) {
    *///?} else if >1.21.5 {
    private void throwBookInPortal(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, CallbackInfo ci) {
        //?} else {
    	/*private void throwBookInPortal(BlockState state, Level world, BlockPos pos, Entity entity, CallbackInfo info) {
    *///?}
		if (world instanceof ServerLevel w && entity instanceof ItemEntity e) {
			PortalCreator.tryCreatePortalFromItem(w, pos, e);
		}
	}

	/* Allows infinity portals to consider themselves valid block configurations. */
	@Redirect(method="updateShape",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
	private boolean infinityPortalsAreValid(BlockState neighborState, Block block) {
		return (neighborState.getBlock() instanceof NetherPortalBlock);
	}

	/* Makes sure that newly lit portals in infdims become infinity portals. */
	@Override
	protected void injected_onBlockAdded(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
		if (state.is(Blocks.NETHER_PORTAL) &&
				(RandomProvider.rule("randomizeAllNetherPortals")
						|| InfinityMethods.isInfinity(world)))
			world.scheduleTick(pos, Blocks.NETHER_PORTAL, 2);
	}
	@Override
	protected void injected_scheduledTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random, CallbackInfo ci) {
		if (state.is(Blocks.NETHER_PORTAL)) {
			boolean bl = RandomProvider.rule("randomizeAllNetherPortals");
			if (!bl && !InfinityMethods.isInfinity(world)) return;
			world.setBlockAndUpdate(pos, ModBlocks.PORTAL.get().defaultBlockState().
					setValue(AXIS, state.getValue(AXIS)));
			if (world.getBlockEntity(pos) instanceof InfinityPortalBlockEntity ipbe) {
				if (bl) {
					long l = InfinityMethods.getRandomSeed(new java.util.Random(world.getGameTime()));
					ipbe.setDimension(l);
				}
				else {
					ipbe.setOpen(true);
				}
			}
		}
	}
}
