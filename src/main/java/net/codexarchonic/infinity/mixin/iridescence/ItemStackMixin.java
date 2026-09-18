package net.codexarchonic.infinity.mixin.iridescence;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.codexarchonic.infinity.iridescence.Iridescence;
import net.codexarchonic.infinity.registry.var.ModTags;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {


    //? if >1.21.2 {
    @WrapMethod(method="getStyledHoverName")
    Component styleIridescentItems(Operation<Component> original) {
        var component = original.call();
        var stack = (ItemStack) (Object) this;
        if (stack.is(ModTags.IRIDESCENT_ITEMS)) {
            return component.copy().withStyle(Style.EMPTY.withColor(Iridescence.getTimeBasedColor()));
        }
        return component;
    }
    //?} else {
    /*@ModifyExpressionValue(method="getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 0))
    MutableComponent styleIridescentItems(MutableComponent original) {
        if (is(ModTags.IRIDESCENT_ITEMS)) {
            return original.withStyle(Style.EMPTY.withColor(Iridescence.getTimeBasedColor()));
        }
        return original;
    }
    *///?}

}
