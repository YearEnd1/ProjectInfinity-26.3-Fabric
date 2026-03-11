package net.lerariemann.infinity.item;

import dev.architectury.core.item.ArchitecturyBucketItem;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class IridescenceBucketItem extends ArchitecturyBucketItem {
	public IridescenceBucketItem(Item.Properties properties) {
		super(InfinityPlatform.INSTANCE.getIridescenceStill(), properties.craftRemainder(Items.BUCKET).stacksTo(1));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos blockPos = context.getClickedPos();
		BlockState blockState = level.getBlockState(blockPos);

		Player player = context.getPlayer();

		if (!blockState.is(Blocks.CAULDRON) || player == null)
			return InteractionResult.PASS;

		this.playEmptySound(player, level, blockPos);

		ItemStack itemStack = context.getItemInHand();
		ItemStack itemStack2 = ItemUtils.createFilledResult(itemStack, player, Items.BUCKET.getDefaultInstance());

		player.setItemInHand(context.getHand(), itemStack2);
		level.setBlockAndUpdate(blockPos, ModBlocks.IRIDESCENCE_CAULDRON.get().defaultBlockState());

		if (player instanceof ServerPlayer serverPlayer)
			CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);

		return VersionMethods.sidedSuccess(level.isClientSide());
	}
}
