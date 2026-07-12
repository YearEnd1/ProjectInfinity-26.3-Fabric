package net.codexarchonic.infinity.registry.payload.s2c;

//? if >1.21 {
import org.jspecify.annotations.NonNull;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//?} else {
/*import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
*///?}
import net.codexarchonic.infinity.registry.payload.FriendlyPacket;
import net.codexarchonic.infinity.util.config.SoundScanner;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import static net.codexarchonic.infinity.util.InfinityMethods.getId;

public record SoundPackS2CPayload(CompoundTag songIds) implements FriendlyPacket.S2C {
    public static ResourceLocation sid = getId("download_sound_pack");
    //? if >1.21 {

    public static final Type<SoundPackS2CPayload> ID = new Type<>(sid);
    public static final StreamCodec<RegistryFriendlyByteBuf, SoundPackS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, SoundPackS2CPayload::songIds, SoundPackS2CPayload::new);
    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
    //?} else {
        /*@Override
        public void write(FriendlyByteBuf buf) {
            buf.writeNbt(songIds);
        }
        public static PacketType<SoundPackS2CPayload> type =
                PacketType.create(sid, buf -> new SoundPackS2CPayload(buf.readNbt()));
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
        SoundScanner.unpackDownloadedPack(songIds, client);
    }
}