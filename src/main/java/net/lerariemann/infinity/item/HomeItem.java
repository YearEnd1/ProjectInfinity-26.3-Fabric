package net.lerariemann.infinity.item;

import net.lerariemann.infinity.util.teleport.WarpLogic;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HomeItem extends Item {
    public HomeItem(Properties settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (user instanceof ServerPlayer player) {
            WarpLogic.respawnAlive(player);
        }
        return super.finishUsingItem(stack, world, user);
    }
}
