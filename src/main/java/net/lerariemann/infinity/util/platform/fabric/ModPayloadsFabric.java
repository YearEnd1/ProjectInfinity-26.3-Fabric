package net.lerariemann.infinity.util.platform.fabric;

//? if >1.21 && fabric {
/*import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lerariemann.infinity.registry.var.ModPayloads;
import net.lerariemann.infinity.util.config.SoundScanner;
import net.minecraft.client.Minecraft;

import static net.lerariemann.infinity.registry.var.ModPayloads.*;

public class ModPayloadsFabric {
    public static Minecraft client(Object context) {
        ClientPlayNetworking.Context clientContext = (ClientPlayNetworking.Context) context;
        return clientContext.client();
    }

    public static void registerPayloadsServer() {
        PayloadTypeRegistry.playS2C().register(WorldAddS2CPayload.ID, WorldAddS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(BiomeAddS2CPayload.ID, BiomeAddS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ShaderS2CPayload.ID, ShaderS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(StarsS2CPayload.ID, StarsS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SoundPackS2CPayload.ID, SoundPackS2CPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(F4UpdateC2SPayload.ID, F4UpdateC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(F4DeployC2SPayload.ID, F4DeployC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(JukeboxesC2SPayload.ID, JukeboxesC2SPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(F4UpdateC2SPayload.ID, (payload, context) ->
                payload.receive(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(F4DeployC2SPayload.ID, (payload, context) ->
                payload.receive(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(JukeboxesC2SPayload.ID, (payload, context) ->
                SoundScanner.unpackUploadedJukeboxes(context.server(), payload.data()));
    }

    public static void registerPayloadsClient() {
        ClientPlayNetworking.registerGlobalReceiver(WorldAddS2CPayload.ID, (payload, context) ->
                payload.receive(client(context)));
        ClientPlayNetworking.registerGlobalReceiver(BiomeAddS2CPayload.ID, (payload, context) ->
                payload.receive(client(context)));
        ClientPlayNetworking.registerGlobalReceiver(ShaderS2CPayload.ID, (payload, context) ->
                payload.receive(client(context)));
        ClientPlayNetworking.registerGlobalReceiver(StarsS2CPayload.ID, (payload, context) ->
                payload.receive(client(context)));
        ClientPlayNetworking.registerGlobalReceiver(SoundPackS2CPayload.ID, (payload, context) ->
                SoundScanner.unpackDownloadedPack(payload.songIds(), client(context)));
    }
}
*///?}
