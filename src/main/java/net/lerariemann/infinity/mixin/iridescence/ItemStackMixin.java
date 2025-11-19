package net.lerariemann.infinity.mixin.iridescence;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.registry.var.ModTags;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract boolean is(TagKey<Item> tag);

    //? if >1.21.2 {
    @WrapMethod(method="getStyledHoverName")
    Component inj(Operation<Component> original) {
        var component = original.call();
        if (is(ModTags.IRIDESCENT_ITEMS)) {
            return component.copy().withStyle(Style.EMPTY.withColor(Iridescence.getTimeBasedColor()));
        }
        return component;
    }
    //?} else {
    /*@ModifyExpressionValue(method="getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 0))
        MutableComponent inj(MutableComponent original) {
        if (is(ModTags.IRIDESCENT_ITEMS)) {
            return original.withStyle(Style.EMPTY.withColor(Iridescence.getTimeBasedColor()));
        }
        return original;
    }
    *///?}

}
