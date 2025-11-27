package net.lerariemann.infinity.mixin.iridescence;

import com.llamalad7.mixinextras.sugar.Local;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
//? if <1.21.2 {
/*import net.minecraft.world.InteractionResultHolder;
*///?}
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BottleItem.class)
public abstract class GlassBottleItemMixin {
    @Shadow protected abstract ItemStack turnBottleIntoItem(ItemStack stack, Player player, ItemStack outputStack);

    //? if >1.21 {
    @Inject(method="use", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V",
            shift = At.Shift.AFTER), cancellable = true)
    //?} else {
    /*@Inject(method="use", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/core/BlockPos;)V",
            shift = At.Shift.AFTER), cancellable = true)
    *///?}
    void bottleIridescence(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<
            //? if >1.21.2 {
            InteractionResult
            //?} else {
            /*InteractionResultHolder<ItemStack>
            *///?}
            > cir, @Local ItemStack itemStack, @Local BlockPos pos) {
        if (Iridescence.isIridescence(world, pos))
            cir.setReturnValue(VersionMethods.sidedSuccess(turnBottleIntoItem(itemStack, user,
                    ModItems.IRIDESCENT_POTION.get().getDefaultInstance()), world.isClientSide()));
    }
}
