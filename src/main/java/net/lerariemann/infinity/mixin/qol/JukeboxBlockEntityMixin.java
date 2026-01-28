package net.lerariemann.infinity.mixin.qol;

//? if <1.21 {
/*import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.item.DiscItem;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.registry.var.ModPayloads;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
*///?}
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.ticks.ContainerSingleItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin extends BlockEntity implements ContainerSingleItem {
    public JukeboxBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    //? if <1.21 {
    /*@Shadow private long recordStartedTick;

    @Shadow private long tickCount;

    @Shadow private boolean isPlaying;

    @Inject(method = "startPlaying", at = @At("HEAD"), cancellable = true)
    void inj(CallbackInfo ci) {
        ItemStack disc = getFirstItem();
        if (!disc.is(ModItems.DISC.get())) return;
        String soundEffect = VersionMethods.getOrDefaultString(disc, "sound_id", "empty");
        InfinityMod.LOGGER.info("Started playing {}", soundEffect);
        recordStartedTick = tickCount;
        isPlaying = true;
        this.level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
        if (level instanceof ServerLevel level1) {
            ModPayloads.PlayJukeboxS2CPayload payload =
                    new ModPayloads.PlayJukeboxS2CPayload(getBlockPos(), soundEffect,
                            VersionMethods.getOrDefaultString(disc, "sound_sub", "empty"));
            PlayerList L = level1.getServer().getPlayerList();
            for(int i = 0; i < L.getPlayers().size(); ++i) {
                ServerPlayer serverPlayer = L.getPlayers().get(i);
                if (serverPlayer.level().dimension() == level1.dimension()) {
                    if (this.getBlockPos().closerToCenterThan(serverPlayer.position(), 64.0)) {
                        payload.send(serverPlayer);
                        InfinityMod.LOGGER.info("Payload sent");
                    }
                }
            }
        }
        this.setChanged();
        ci.cancel();
    }

    @Inject(method = "shouldRecordStopPlaying", at = @At("HEAD"), cancellable = true)
    void inj(RecordItem _record, CallbackInfoReturnable<Boolean> cir) {
        if (_record == ModItems.DISC.get()) {
            boolean res = this.tickCount >= this.recordStartedTick +
                    20L * VersionMethods.getOrDefaultInt(getFirstItem(), "sound_duration", DiscItem.defaultDuration)
                    + 20L;
            cir.setReturnValue(res);
        }
    }
    *///?}
}
