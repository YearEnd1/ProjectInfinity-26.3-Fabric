package net.lerariemann.infinity.registry.payload.s2c;


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
import net.lerariemann.infinity.registry.payload.FriendlyPacket;
import net.lerariemann.infinity.util.loading.DimensionGrabber;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;

import static net.lerariemann.infinity.util.InfinityMethods.getId;

public record WorldAddS2CPayload(ResourceLocation id, CompoundTag data) implements FriendlyPacket.S2C {
    public static ResourceLocation sid = getId("add_world");
    //? if >1.21 {
    public static final Type<WorldAddS2CPayload> ID = new Type<>(sid);
    public static final StreamCodec<RegistryFriendlyByteBuf, WorldAddS2CPayload> CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, WorldAddS2CPayload::id,
            ByteBufCodecs.COMPOUND_TAG, WorldAddS2CPayload::data,
            WorldAddS2CPayload::new);
    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
    //?} else {
        /*public void write(FriendlyByteBuf buf) {
            buf.writeResourceLocation(id);
            buf.writeNbt(data);
        }
        public static PacketType<WorldAddS2CPayload> type =
                PacketType.create(sid, buf -> new WorldAddS2CPayload(buf.readResourceLocation(), buf.readNbt()));
        @Override
        public PacketType<?> getType() {
            return type;
        }
        public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
            type.read(buf).receive(client);
        }
        *///?}
    public void receive(Minecraft client) {
        client.execute(() -> DimensionGrabber.grabObjectForClient(client, DimensionType.DIRECT_CODEC, Registries.DIMENSION_TYPE, id, data));
    }
}
