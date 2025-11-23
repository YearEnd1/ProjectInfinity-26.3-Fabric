package net.lerariemann.infinity.item;

import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.registry.core.ModStatusEffects;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
//? if >1.21.4 {

import net.minecraft.world.item.component.TooltipDisplay;
//?} else {
/*import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.UseAnim;
*///?}
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import java.util.function.Consumer;
//? if >1.21 {
import net.minecraft.world.item.alchemy.PotionContents;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
//?} else {
/*import net.minecraft.world.item.alchemy.PotionUtils;
*///?}
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import java.util.List;

public class IridescentPotionItem extends Item {
    int level;

    public IridescentPotionItem(Properties settings, int level) {
        super(settings);
        this.level = level;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (!world.isClientSide()) Iridescence.tryBeginJourney(user, level, true);
        if (user instanceof Player player) {
            if (user instanceof ServerPlayer spe) {
                CriteriaTriggers.CONSUME_ITEM.trigger(spe, stack);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            VersionMethods.consumeWithRemainder(player, stack, Items.GLASS_BOTTLE.getDefaultInstance());
        }
        else if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        user.gameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack
            //? if >1.21
            , LivingEntity user
    ) {
        return 32;
    }

    //? if >1.21.4 {
    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }
    //?} else {
    /*@Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
    *///?}


    @Override
    public
    //? if >1.21.4 {
    InteractionResult
    //?} else {
    /*InteractionResultHolder<ItemStack>
    *///?}
    use(Level world, Player user, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack,
                                //? if >1.21 {
                                TooltipContext
                                //?} else {
                                /*Level
                                *///?}
                                        context,
                                //? if >1.21.4 {
                                TooltipDisplay display, Consumer<Component>tooltipAdder
                                //?} else {
                                /*List<Component> tooltip
                                *///?}
                                , TooltipFlag type) {
        int level = VersionMethods.getOrDefaultInt(stack, ModComponentTypes.CHARGE, 0);
        List<MobEffectInstance> effects = List.of(new MobEffectInstance(ModStatusEffects.IRIDESCENT_EFFECT
                //? if <1.21
                /*.value()*/
                ,
                Iridescence.getFullEffectLength(level), level));
        //? if <1.21.4 {
        /*Consumer<Component> tooltipAdder = tooltip::add;
        *///?}
        //? if >1.21 {
        PotionContents.addPotionTooltip(effects, tooltipAdder, 1.0F, context.tickRate());
        //?} else {
        /*PotionUtils.addPotionTooltip(effects, tooltip, 1.0F);
        *///?}
    }
}
