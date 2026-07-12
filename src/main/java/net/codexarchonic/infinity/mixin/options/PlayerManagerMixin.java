package net.codexarchonic.infinity.mixin.options;

import com.llamalad7.mixinextras.sugar.Local;
import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.access.MinecraftServerAccess;
import net.codexarchonic.infinity.registry.payload.s2c.ShaderS2CPayload;
import net.codexarchonic.infinity.registry.payload.s2c.SoundPackS2CPayload;
import net.codexarchonic.infinity.registry.payload.s2c.StarsS2CPayload;
import net.codexarchonic.infinity.util.config.ConfigGenInvocation;
import net.codexarchonic.infinity.util.core.CommonIO;
import net.codexarchonic.infinity.util.core.RandomProvider;
import net.codexarchonic.infinity.util.teleport.WarpLogic;
import net.minecraft.core.LayeredRegistryAccess;
//? if <1.21 {
/*import net.minecraft.nbt.CompoundTag;
*///?}
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
    //~ if >1.21.9 'int maxPlayers'->'NotificationService notificationService' {
    private void injected(MinecraftServer server, LayeredRegistryAccess<RegistryLayer> registryManager, PlayerDataStorage saveHandler, int maxPlayers, CallbackInfo ci) {
    //~}
        infinity$needsTpOut = false;
    }

    //FIXME 1.21.10 - check new target
    //~ if >1.21.9 'Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;'->'Lnet/minecraft/server/level/ServerPlayer;level()Lnet/minecraft/server/level/ServerLevel;' {
    @Inject(method="placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;overworld()Lnet/minecraft/server/level/ServerLevel;"))
    //~}
    void checkNeedsTpOut(Connection connection, ServerPlayer player,
                         //? if >1.21 {
                         CommonListenerCookie clientData,
                         //?}
                         CallbackInfo ci) {
        infinity$needsTpOut = true;
    }

    @Inject(method="placeNewPlayer", at = @At(value = "TAIL"))
    void tpOut(Connection connection, ServerPlayer player,
               //? if >1.21 {
               CommonListenerCookie clientData,
               //?}
               CallbackInfo ci) {
        if (infinity$needsTpOut) {
            infinity$needsTpOut = false;
            WarpLogic.respawnAlive(player);
            WarpLogic.sendToMissingno(player);
        }
    }

    @Inject(method = "placeNewPlayer", at = @At(value="INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;sendPlayerPermissionLevel(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void updatePlayer(Connection connection, ServerPlayer player,
                              //? if >1.21 {
                              CommonListenerCookie clientData,
                              //?}
                              CallbackInfo ci, @Local(ordinal=0) ServerLevel serverWorld2) {
        if (serverWorld2 == null) return;
        ShaderS2CPayload.sendTo(player, serverWorld2);
        StarsS2CPayload.sendTo(player);
        MinecraftServerAccess acc = ((MinecraftServerAccess)(serverWorld2.getServer()));
        if (acc.infinity$needsInvocation()) {
            ConfigGenInvocation.invokeOn(player);
        }
        if (RandomProvider.rule("useSoundSyncPackets")) {
            //? if <1.21 {
            /*if (!acc.infinity$hasJukeboxes()) {
                new SoundPackS2CPayload(new CompoundTag()).send(player);
                InfinityMod.LOGGER.info("Requesting sound pack from client");
                return;
            }
            *///?}
            InfinityMod.LOGGER.info("Sending sound pack to client");
            new SoundPackS2CPayload(CommonIO.read(
                    player.level().getServer().getWorldPath(LevelResource.DATAPACK_DIR).resolve("client_sound_pack_data.json"))).send(player);
        }
    }

    @Inject(method="sendLevelInfo", at = @At("TAIL"))
    private void sendInfinityLevelInfo(ServerPlayer player, ServerLevel world, CallbackInfo ci) {
        ShaderS2CPayload.sendTo(player, world);
        StarsS2CPayload.sendTo(player);
    }
}
