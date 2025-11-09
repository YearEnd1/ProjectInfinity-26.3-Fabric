package net.lerariemann.infinity.item;

import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.core.ModStatusEffects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import java.util.List;

public class IridescentPotionItem extends Item {
    public IridescentPotionItem(Properties settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        int level = stack.getOrDefault(ModComponentTypes.CHARGE.get(), 0);
        if (!world.isClientSide) Iridescence.tryBeginJourney(user, level, true);
        if (user instanceof Player player) {
            if (user instanceof ServerPlayer spe) {
                CriteriaTriggers.CONSUME_ITEM.trigger(spe, stack);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            stack.consume(1, player);

            if (!player.hasInfiniteMaterials())
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
        }
        else if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        user.gameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(world, user, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        int level = stack.getOrDefault(ModComponentTypes.CHARGE.get(), 0);
        List<MobEffectInstance> effects = List.of(new MobEffectInstance(ModStatusEffects.IRIDESCENT_EFFECT,
                Iridescence.getFullEffectLength(level), level));
        PotionContents.addPotionTooltip(effects, tooltip::add, 1.0F, context.tickRate());
    }
}
