package net.lerariemann.infinity.mixin.core;

import com.mojang.authlib.GameProfile;
import net.lerariemann.infinity.access.Timebombable;
import net.lerariemann.infinity.access.ServerPlayerEntityAccess;
import net.lerariemann.infinity.options.InfinityOptions;
import net.lerariemann.infinity.util.teleport.WarpLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.lerariemann.infinity.registry.var.ModPayloads;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? if >1.21 {

import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.portal.TeleportTransition;
//?} else {
/*import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
*///?}

import java.util.Optional;
import java.util.Set;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player implements ServerPlayerEntityAccess {
    //? if >1.21 {
    @Shadow public abstract @Nullable Entity changeDimension(TeleportTransition teleportTarget);
    //?} else {
    /*@Shadow public ServerGamePacketListenerImpl connection;
    @Shadow private int lastSentExp;
    @Shadow private float lastSentHealth;
    @Shadow private int lastSentFood;
    *///?}
    @Shadow public abstract boolean hurt(DamageSource source, float amount);

    @Shadow
    public abstract Level level();

    @Unique private long infinity$ticksUntilWarp;
    @Unique private ResourceLocation infinity$idForWarp;

    public ServerPlayerEntityMixin(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    //? if >1.21 {
    
    @Inject(method="findRespawnAndUseSpawnBlock", at = @At("HEAD"), cancellable = true)
    private static void injected(ServerLevel world, BlockPos pos, float angle, boolean forced, boolean alive, CallbackInfoReturnable<Optional<Vec3>> cir) {
        if (InfinityMethods.isTimebombed(world)) cir.setReturnValue(Optional.empty());
    }

    @Inject(method = "changeDimension",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getPlayerList()Lnet/minecraft/server/players/PlayerList;"))
    private void injected3(TeleportTransition teleportTarget, CallbackInfoReturnable<Entity> cir) {
        ServerLevel targetWorld = teleportTarget.newLevel();
     //?} else {
    /*@Inject(method= "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V", at = @At(value="INVOKE", target ="Lnet/minecraft/server/players/PlayerList;sendPlayerPermissionLevel(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void injected5(ServerLevel targetWorld, double x, double y, double z, float yaw, float pitch, CallbackInfo ci) {
        this.connection.send(new ClientboundPlayerAbilitiesPacket(getAbilities()));
        for(MobEffectInstance effect: getActiveEffects())
            connection.send(new ClientboundUpdateMobEffectPacket(getId(), effect));
        lastSentExp = -1;
        lastSentHealth = -1.0F;
        lastSentFood = -1;
    *///?}
        ServerPlayer player = (ServerPlayer)(Object)this;
        ModPayloads.sendShaderPayload(player, targetWorld);
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
        ((Timebombable)level()).tickTimebombProgress(player);
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
