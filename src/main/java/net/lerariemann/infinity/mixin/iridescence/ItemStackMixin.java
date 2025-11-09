package net.lerariemann.infinity.mixin.iridescence;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.registry.var.ModTags;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract boolean is(TagKey<Item> tag);

    @ModifyExpressionValue(method="getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 0))
    MutableComponent inj(MutableComponent original) {
        if (is(ModTags.IRIDESCENT_ITEMS)) {
            return original.withColor(Iridescence.getTimeBasedColor());
        }
        return original;
    }
}
