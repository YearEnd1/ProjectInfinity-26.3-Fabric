package net.codexarchonic.infinity.registry.payload.s2c;

//? if >1.21 {
import org.jspecify.annotations.NonNull;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//?} else {
/*import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
*///?}
import net.codexarchonic.infinity.access.WorldRendererAccess;
import net.codexarchonic.infinity.registry.payload.FriendlyPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import static net.codexarchonic.infinity.util.InfinityMethods.getId;

public record StarsS2CPayload() implements FriendlyPacket.S2C {
    public static ResourceLocation sid = getId("reload_stars");
    public static final StarsS2CPayload INSTANCE = new StarsS2CPayload();
    //? if >1.21 {

    public static final Type<StarsS2CPayload> ID = new Type<>(sid);
    public static final StreamCodec<RegistryFriendlyByteBuf, StarsS2CPayload> CODEC = StreamCodec.unit(INSTANCE);
    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
    //?} else {
        /*@Override
        public void write(FriendlyByteBuf buf) {
        }
        public static PacketType<StarsS2CPayload> type =
                PacketType.create(sid, buf -> INSTANCE);
        @Override
        public PacketType<?> getType() {
            return type;
        }
        public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
            type.read(buf).receive(client);
        }
        *///?}
    @Override
    public void receive(Minecraft client) {
        client.execute(() -> ((WorldRendererAccess)(client.levelRenderer)).infinity$setNeedsStars(true));
    }

    public static void sendTo(ServerPlayer player) {
        StarsS2CPayload.INSTANCE.send(player);
    }
}