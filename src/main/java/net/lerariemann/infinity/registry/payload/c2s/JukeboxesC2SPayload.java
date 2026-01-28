package net.lerariemann.infinity.registry.payload.c2s;

//? if >1.21 {
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.lerariemann.infinity.util.InfinityMethods;
import org.jetbrains.annotations.NotNull;
//?} else {
/*import net.minecraft.network.FriendlyByteBuf;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
*///?}
import net.minecraft.nbt.CompoundTag;
import net.lerariemann.infinity.registry.payload.FriendlyPacket;
import net.lerariemann.infinity.util.config.SoundScanner;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import static net.lerariemann.infinity.util.InfinityMethods.getId;

public record JukeboxesC2SPayload(CompoundTag data) implements FriendlyPacket.C2S {
    public static ResourceLocation sid = getId("upload_jukeboxes");
    //? if >1.21 {

    public static final Type<JukeboxesC2SPayload> ID = new Type<>(InfinityMethods.getId("upload_jukeboxes"));
    public static final StreamCodec<RegistryFriendlyByteBuf, JukeboxesC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, JukeboxesC2SPayload::data, JukeboxesC2SPayload::new);
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
    //?} else {
        /*@Override
        public void write(FriendlyByteBuf buf) {
            buf.writeNbt(data);
        }
        public static PacketType<JukeboxesC2SPayload> type =
                PacketType.create(sid, buf -> new JukeboxesC2SPayload(buf.readNbt()));
        @Override
        public PacketType<?> getType() {
            return type;
        }
        public static void receive(JukeboxesC2SPayload packet, ServerPlayer player, PacketSender responseSender) {
            packet.receive(player);
        }
        *///?}
    @Override
    public void receive(ServerPlayer player) {
        SoundScanner.unpackUploadedJukeboxes(player.level().getServer(), data);
    }
}
