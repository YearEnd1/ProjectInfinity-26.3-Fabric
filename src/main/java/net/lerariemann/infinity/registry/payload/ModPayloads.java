package net.lerariemann.infinity.registry.payload;

import net.lerariemann.infinity.registry.payload.c2s.*;
import net.lerariemann.infinity.registry.payload.s2c.*;

//? if <1.21 || fabric {
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
//?}
//? if >1.21 && fabric {
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
//?}
//? neoforge {
/*import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
*///?}

public class ModPayloads {
    public static String oldShader = "";

    public static void registerC2SPacketsReceivers() {
        //? if >1.21 {
        net.lerariemann.infinity.util.platform.InfinityPlatform.INSTANCE.registerPayloadsServer();
        //?} else {
        /*ServerPlayNetworking.registerGlobalReceiver(F4UpdateC2SPayload.type, F4UpdateC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(F4DeployC2SPayload.type, F4DeployC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(JukeboxesC2SPayload.type, JukeboxesC2SPayload::receive);
        *///?}
    }

    public static void registerS2CPacketsReceivers() {
        //? if >1.21 {
        net.lerariemann.infinity.util.platform.InfinityPlatform.INSTANCE.registerPayloadsClient();
        //?} else {
        /*ClientPlayNetworking.registerGlobalReceiver(WorldAddS2CPayload.sid, WorldAddS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(BiomeAddS2CPayload.sid, BiomeAddS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(ShaderS2CPayload.sid, ShaderS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(StarsS2CPayload.sid, StarsS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(SoundPackS2CPayload.sid, SoundPackS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(PlayJukeboxS2CPayload.sid, PlayJukeboxS2CPayload::receive);
        *///?}
    }

    //? if >1.21 && fabric {
    public static Minecraft client(Object context) {
        ClientPlayNetworking.Context clientContext = (ClientPlayNetworking.Context) context;
        return clientContext.client();
    }

    public static void registerFabricServerside() {
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
                payload.receive(context.player()));
    }

    public static void registerFabricClientside() {
        ClientPlayNetworking.registerGlobalReceiver(WorldAddS2CPayload.ID, (payload, context) ->
                payload.receive(client(context)));
        ClientPlayNetworking.registerGlobalReceiver(BiomeAddS2CPayload.ID, (payload, context) ->
                payload.receive(client(context)));
        ClientPlayNetworking.registerGlobalReceiver(ShaderS2CPayload.ID, (payload, context) ->
                payload.receive(client(context)));
        ClientPlayNetworking.registerGlobalReceiver(StarsS2CPayload.ID, (payload, context) ->
                payload.receive(client(context)));
        ClientPlayNetworking.registerGlobalReceiver(SoundPackS2CPayload.ID, (payload, context) ->
                payload.receive(client(context)));
    }
    //?}
    //? neoforge {
    /*public static ServerPlayer getPlayer(IPayloadContext context) {
        return ((ServerPlayer)context.player());
    }

    public static void registerNeoforge(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                F4UpdateC2SPayload.ID,
                F4UpdateC2SPayload.CODEC,
                (payload, context) -> payload.receive(getPlayer(context))
        );
        registrar.playToServer(
                F4DeployC2SPayload.ID,
                F4DeployC2SPayload.CODEC,
                (payload, context) -> payload.receive(getPlayer(context))
        );
        registrar.playToServer(
                JukeboxesC2SPayload.ID,
                JukeboxesC2SPayload.CODEC,
                (payload, context) -> payload.receive(getPlayer(context))
        );
        registrar.playToClient(
                WorldAddS2CPayload.ID,
                WorldAddS2CPayload.CODEC,
                (payload, context) -> payload.receive()
        );
        registrar.playToClient(
                BiomeAddS2CPayload.ID,
                BiomeAddS2CPayload.CODEC,
                (payload, context) -> payload.receive()
        );
        registrar.playToClient(
                ShaderS2CPayload.ID,
                ShaderS2CPayload.CODEC,
                (payload, context) -> payload.receive()
        );
        registrar.playToClient(
                StarsS2CPayload.ID,
                StarsS2CPayload.CODEC,
                (payload, context) -> payload.receive()
        );
        registrar.playToClient(
                SoundPackS2CPayload.ID,
                SoundPackS2CPayload.CODEC,
                (payload, context) -> payload.receive()
        );
    }
    *///?}
}