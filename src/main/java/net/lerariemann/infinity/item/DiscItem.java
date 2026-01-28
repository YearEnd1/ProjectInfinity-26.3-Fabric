package net.lerariemann.infinity.item;

//? if <1.21 {
/*import net.lerariemann.infinity.access.MinecraftServerAccess;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
*///?}

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.ConfigType;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;

public class DiscItem
//? if <1.21 {
/*extends RecordItem
 *///?}
{
//? if <1.21 {
/*
    public static String emptySubtitle = "infinity.subtitle.missing";
    public static int defaultDuration = 185;

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
    public void appendHoverText(@NotNull ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
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
    public static ItemStack getRandom(MinecraftServer server) {
        ItemStack stack = ModItems.DISC.get().getDefaultInstance();
        ResourceLocation song = VersionMethods.id(InfinityMod.provider.randomName(InfinityMod.random, ConfigType.JUKEBOXES));
        stack.applyComponents(DataComponentMap.builder()
                .set(DataComponents.JUKEBOX_PLAYABLE, new JukeboxPlayable(new EitherHolder<>(
                        ResourceKey.create(Registries.JUKEBOX_SONG, song))
                        //? if <1.21.2
                        /*, true*/
                ))
                .set(ModComponentTypes.COLOR.get(), (int) InfinityMethods.getNumericFromId(song)).build());
        return stack;
    }
//?}
}
