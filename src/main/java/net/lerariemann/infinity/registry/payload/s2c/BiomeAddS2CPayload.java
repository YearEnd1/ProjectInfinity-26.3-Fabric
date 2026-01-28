package net.lerariemann.infinity.registry.payload.s2c;

//? if >1.21 {
import org.jetbrains.annotations.NotNull;
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
import net.minecraft.world.level.biome.Biome;

import static net.lerariemann.infinity.util.InfinityMethods.getId;

public record BiomeAddS2CPayload(ResourceLocation id, CompoundTag data) implements FriendlyPacket.S2C {
    public static ResourceLocation sid = getId("add_biome");
    //? if >1.21 {
    public static final Type<BiomeAddS2CPayload> ID = new Type<>(sid);
    public static final StreamCodec<RegistryFriendlyByteBuf, BiomeAddS2CPayload> CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, BiomeAddS2CPayload::id,
            ByteBufCodecs.COMPOUND_TAG, BiomeAddS2CPayload::data,
            BiomeAddS2CPayload::new);
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
    //?} else {
        /*public void write(FriendlyByteBuf buf) {
            buf.writeResourceLocation(id);
            buf.writeNbt(data);
        }
        public static PacketType<BiomeAddS2CPayload> type =
                PacketType.create(sid, buf -> new BiomeAddS2CPayload(buf.readResourceLocation(), buf.readNbt()));
        @Override
        public PacketType<?> getType() {
            return type;
        }
        public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
            type.read(buf).receive(client);
        }
        *///?}
    public void receive(Minecraft client) {
        client.execute(() -> DimensionGrabber.grabObjectForClient(client, Biome.DIRECT_CODEC, Registries.BIOME, id, data));
    }
}
