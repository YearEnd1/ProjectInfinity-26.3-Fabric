package net.lerariemann.infinity.mixin.options;

import com.llamalad7.mixinextras.sugar.Local;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.access.MinecraftServerAccess;
import net.lerariemann.infinity.util.config.ConfigGenInvocation;
import net.lerariemann.infinity.util.core.CommonIO;
import net.lerariemann.infinity.util.core.RandomProvider;
import net.lerariemann.infinity.util.teleport.WarpLogic;
import net.lerariemann.infinity.registry.var.ModPayloads;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
//? if >1.21
import net.minecraft.server.network.CommonListenerCookie;
//? if >1.21.9 {
/*import net.minecraft.server.notifications.NotificationService;
*///?}
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerManagerMixin {
    @Unique
    public boolean infinity$needsTpOut;

    @Inject(method = "<init>", at = @At("TAIL"))
    //? if >1.21.9 {
    /*private void injected(MinecraftServer server, LayeredRegistryAccess<RegistryLayer> registries, PlayerDataStorage playerIo, NotificationService notificationService, CallbackInfo ci) {
    *///?} else {
    private void injected(MinecraftServer server, LayeredRegistryAccess<RegistryLayer> registryManager, PlayerDataStorage saveHandler, int maxPlayers, CallbackInfo ci) {
    //?}
        infinity$needsTpOut = false;
    }

    //FIXME 1.21.10 - check new target
    //? if >1.21.9 {
    /*@Inject(method="placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;level()Lnet/minecraft/server/level/ServerLevel;"))
    *///?} else {
    @Inject(method="placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
    //?}
    //? if >1.21 {
    void checkNeedsTpOut(Connection connection, ServerPlayer player, CommonListenerCookie clientData, CallbackInfo ci) {
    //?} else {
    /*void inj(Connection connection, ServerPlayer player, CallbackInfo ci) {
    *///?}
        infinity$needsTpOut = true;
    }

    @Inject(method="placeNewPlayer", at = @At(value = "TAIL"))
    //? if >1.21 {
    void tpOut(Connection connection, ServerPlayer player, CommonListenerCookie clientData, CallbackInfo ci) {
    //?} else {
    /*void inj2(Connection connection, ServerPlayer player, CallbackInfo ci) {
    *///?}
        if (infinity$needsTpOut) {
            infinity$needsTpOut = false;
            WarpLogic.respawnAlive(player);
            WarpLogic.sendToMissingno(player);
        }
    }

    @Inject(method = "placeNewPlayer", at = @At(value="INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;sendPlayerPermissionLevel(Lnet/minecraft/server/level/ServerPlayer;)V"))
    //? if >1.21 {
    private void updatePlayer(Connection connection, ServerPlayer player, CommonListenerCookie clientData, CallbackInfo ci, @Local(ordinal=0) ServerLevel serverWorld2) {
    //?} else {
    /*private void injected(Connection connection, ServerPlayer player, CallbackInfo ci, @Local(ordinal=0) ServerLevel serverWorld2) {
    *///?}
        if (serverWorld2 == null) return;
        ModPayloads.sendShaderPayload(player, serverWorld2);
        ModPayloads.sendStarsPayload(player);
        MinecraftServerAccess acc = ((MinecraftServerAccess)(serverWorld2.getServer()));
        if (acc.infinity$needsInvocation()) {
            ConfigGenInvocation.invokeOn(player);
        }
        if (RandomProvider.rule("useSoundSyncPackets")) {
            //? if <1.21 {
            /*if (!acc.infinity$hasJukeboxes()) {
                new ModPayloads.SoundPackS2CPayload(new CompoundTag()).send(player);
                InfinityMod.LOGGER.info("Requesting sound pack from client");
                return;
            }
            *///?}
            InfinityMod.LOGGER.info("Sending sound pack to client");
            new ModPayloads.SoundPackS2CPayload(CommonIO.read(
                    player.level().getServer().getWorldPath(LevelResource.DATAPACK_DIR).resolve("client_sound_pack_data.json"))).send(player);
        }
    }

    @Inject(method="sendLevelInfo", at = @At("TAIL"))
    private void sendInfinityLevelInfo(ServerPlayer player, ServerLevel world, CallbackInfo ci) {
        ModPayloads.sendShaderPayload(player, world);
        ModPayloads.sendStarsPayload(player);
    }
}
