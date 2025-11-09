package net.lerariemann.infinity.mixin.core;

import com.mojang.authlib.GameProfile;
import net.lerariemann.infinity.access.Timebombable;
import net.lerariemann.infinity.access.ServerPlayerEntityAccess;
import net.lerariemann.infinity.options.InfinityOptions;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.teleport.WarpLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.lerariemann.infinity.registry.var.ModPayloads;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.Set;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player implements ServerPlayerEntityAccess {
    public ServerPlayerEntityMixin(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow public abstract ServerLevel serverLevel();
    @Shadow public abstract boolean teleportTo(ServerLevel world, double destX, double destY, double destZ, Set<RelativeMovement> flags, float yaw, float pitch);
    @Shadow public abstract boolean hurt(DamageSource source, float amount);
    @Shadow public abstract @Nullable Entity changeDimension(DimensionTransition teleportTarget);
    @Unique private long infinity$ticksUntilWarp;
    @Unique private ResourceLocation infinity$idForWarp;

    @Inject(method="findRespawnAndUseSpawnBlock", at = @At("HEAD"), cancellable = true)
    private static void injected(ServerLevel world, BlockPos pos, float angle, boolean forced, boolean alive, CallbackInfoReturnable<Optional<Vec3>> cir) {
        if (InfinityMethods.isTimebombed(world)) cir.setReturnValue(Optional.empty());
    }

    @Inject(method = "changeDimension",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getPlayerList()Lnet/minecraft/server/players/PlayerList;"))
    private void injected3(DimensionTransition teleportTarget, CallbackInfoReturnable<Entity> cir) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        ModPayloads.sendShaderPayload(player, teleportTarget.newLevel());
        ModPayloads.sendStarsPayload(player);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        /* Handle infinity options */
        InfinityOptions.access(level()).effect.tryGiveEffect(player);
        /* Handle the warp command */
        if (--this.infinity$ticksUntilWarp == 0L)
            WarpLogic.performWarp(player, infinity$idForWarp);
        /* Handle effects from dimension deletion */
        ((Timebombable)serverLevel()).tickTimebombProgress(player);
    }

    @Inject(method = "setGameMode", at = @At("RETURN"))
    private void injected4(GameType gameMode, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            ModPayloads.sendShaderPayload((ServerPlayer)(Object)this);
        }
    }

    @Override
    public void infinity$setWarpTimer(long ticks, ResourceLocation dim) {
        this.infinity$ticksUntilWarp = ticks;
        this.infinity$idForWarp = dim;
    }
}
