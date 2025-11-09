package net.lerariemann.infinity.block.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BookBoxBlock extends Block {
    public BookBoxBlock(Properties settings) {
        super(settings);
    }

    public String title(BlockPos pos) {
        return pos.getX() + "." + pos.getY() + "." + pos.getZ();
    }

    public String text(BlockPos pos) {
        Random r = new Random(pos.getX() + 30000000L*(pos.getZ() + 30000000L*pos.getY()));
        String s = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890.^&*№+-_~@$%()!?";
        StringBuilder res = new StringBuilder();
        for (int j = 0; j<256; j++) {
            res.append(s.charAt(r.nextInt(s.length())));
        }
        return res.toString();
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.isClientSide) {
            ItemStack itemStack1 = Items.WRITTEN_BOOK.getDefaultInstance();
            List<Filterable<Component>> pages = new ArrayList<>();
            pages.add(Filterable.passThrough(Component.nullToEmpty(text(pos))));
            WrittenBookContent component = new WrittenBookContent(Filterable.passThrough(title(pos)), "§kLeraRiemann", 3, pages, false);
            itemStack1.set(DataComponents.WRITTEN_BOOK_CONTENT, component);
            player.getInventory().add(itemStack1);
        }
        return InteractionResult.SUCCESS;
    }
}
