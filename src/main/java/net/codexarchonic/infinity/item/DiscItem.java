package net.codexarchonic.infinity.item;

//? if <1.21 {
/*import net.codexarchonic.infinity.access.MinecraftServerAccess;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.NonNull;
*///?} else {
import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.util.core.ConfigType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
//?}
import net.codexarchonic.infinity.registry.core.ModComponentTypes;
import net.codexarchonic.infinity.registry.core.ModItems;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.codexarchonic.infinity.util.VersionMethods;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
//? if <1.21.5 {
/*import net.minecraft.world.InteractionResultHolder;
import java.util.List;
*///?} else {
import net.minecraft.world.item.component.TooltipDisplay;
//?}
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class DiscItem
//? if <1.21 {
/*extends RecordItem
 *///?} else {
extends Item
//?}
{
    public static String emptySubtitle = "subtitles.music.missing";
    //? if <1.21 {

    /*public static int defaultDuration = 185;

    public DiscItem(Properties builder) {
        super(15, SoundEvents.MUSIC_DISC_CAT, builder, 185);
    }

    public static ItemStack getRandom(MinecraftServer server) {
        MinecraftServerAccess acc = (MinecraftServerAccess)server;
        ItemStack res = ModItems.DISC.get().getDefaultInstance();
        String id = acc.infinity$getRandomJukeboxId();
        CompoundTag t = res.getOrCreateTag();
        t.putString("sound_id", id);
        t.putString("sound_sub", acc.infinity$getJukeboxSubtitle(id));
        t.putInt("sound_duration", acc.infinity$getJukeboxDuration(id));
        t.putInt(ModComponentTypes.COLOR, (int)InfinityMethods.getDimensionSeed(id));
        res.setTag(t);
        return res;
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(VersionMethods.getOrDefaultString(stack, "sound_sub", emptySubtitle)).withStyle(ChatFormatting.GRAY));
    }

    /^* our discs are stackable which mojang did not foresee ^/
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (blockstate.is(Blocks.JUKEBOX) && !(Boolean)blockstate.getValue(JukeboxBlock.HAS_RECORD)) {
            ItemStack itemstack = context.getItemInHand();
            if (isBlank(itemstack)) return InteractionResult.PASS;
            if (!level.isClientSide) {
                Player player = context.getPlayer();
                BlockEntity blockentity = level.getBlockEntity(blockpos);
                if (blockentity instanceof JukeboxBlockEntity) {
                    JukeboxBlockEntity jukeboxblockentity = (JukeboxBlockEntity)blockentity;
                    jukeboxblockentity.setFirstItem(itemstack.copyWithCount(1));
                    level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, blockstate));
                }

                itemstack.shrink(1);
                if (player != null) {
                    player.awardStat(Stats.PLAY_RECORD);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
*///?} else {
    public DiscItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                //? if >1.21.2 {
                                TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder
                                //?} else {
            /*List<Component> tooltip
             *///?}
            , TooltipFlag type) {
        //? if >1.21.2 {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, type);
        //?} else {
        /*super.appendHoverText(stack, context, tooltip, type);
        Consumer<Component> tooltipAdder = tooltip::add;
        *///?}
        if (isBlank(stack)) {
            MutableComponent mutableText = Component.translatable(emptySubtitle).withStyle(ChatFormatting.GRAY);
            tooltipAdder.accept(mutableText);
        }
    }

    public static ItemStack getRandom(MinecraftServer server) {
        ItemStack stack = ModItems.DISC.get().getDefaultInstance();
        ResourceLocation song = VersionMethods.id(InfinityMod.provider.randomName(InfinityMod.random, ConfigType.JUKEBOXES));
        ResourceKey<JukeboxSong> jukeboxSongKey = ResourceKey.create(Registries.JUKEBOX_SONG, song);
        stack.applyComponents(DataComponentMap.builder()
                //? if >26 {
                /*.set(DataComponents.JUKEBOX_PLAYABLE, new JukeboxPlayable(server.registryAccess().getOrThrow(jukeboxSongKey)))
                *///?} else {
                .set(DataComponents.JUKEBOX_PLAYABLE, new JukeboxPlayable(new EitherHolder<>(
                        jukeboxSongKey)
                        //? if <1.21.2
                        //, true
                ))
                //?}
                .set(ModComponentTypes.COLOR.get(), (int) InfinityMethods.getDimensionSeed(song.getPath())).build());
        return stack;
    }
//?}
    public static boolean isBlank(ItemStack st) {
        //? if <1.21 {
        /*return !st.getOrCreateTag().contains("sound_id");
         *///?} else {
        return !st.getComponents().has(DataComponents.JUKEBOX_PLAYABLE);
        //?}
    }

    @Override
    public
        //? if <1.21.5 {
        /*InteractionResultHolder<ItemStack>
         *///?} else {
    InteractionResult
    //?}
    use(Level world, Player player, InteractionHand hand) {
        ItemStack st = player.getItemInHand(hand);
        if (isBlank(st)) {
            player.setItemInHand(hand, st.copyWithCount(st.getCount() - 1));
            if (!world.isClientSide()) {
                player.getInventory().add(getRandom(world.getServer()));
            }
            return VersionMethods.success(player.getItemInHand(hand));
        }
        return VersionMethods.pass(st);
    }
}
