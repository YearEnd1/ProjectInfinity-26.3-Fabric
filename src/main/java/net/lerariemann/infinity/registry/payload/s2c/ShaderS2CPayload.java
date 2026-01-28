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
import net.lerariemann.infinity.access.InfinityOptionsAccess;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.options.InfinityOptions;
import net.lerariemann.infinity.registry.payload.FriendlyPacket;
import net.lerariemann.infinity.util.loading.ShaderLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import static net.lerariemann.infinity.util.InfinityMethods.getId;

public record ShaderS2CPayload(boolean iridescence, CompoundTag data) implements FriendlyPacket.S2C {
    public static ResourceLocation sid = getId("reload_shader");
    //? if >1.21 {

    public static final Type<ShaderS2CPayload> ID = new Type<>(sid);
    public static final StreamCodec<RegistryFriendlyByteBuf, ShaderS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ShaderS2CPayload::iridescence,
            ByteBufCodecs.COMPOUND_TAG, ShaderS2CPayload::data,
            ShaderS2CPayload::new);
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
    //?} else {
        /*public void write(FriendlyByteBuf buf) {
            buf.writeBoolean(iridescence);
            buf.writeNbt(data);
        }
        public static PacketType<ShaderS2CPayload> type =
                PacketType.create(sid, buf -> new ShaderS2CPayload(buf.readBoolean(), buf.readNbt()));
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
        InfinityOptions options = new InfinityOptions(data);
        ((InfinityOptionsAccess)client).infinity$setOptions(options);
        client.execute(() -> ShaderLoader.reloadShaders(client, options.getShader(), iridescence));
    }

    public static void sendTo(ServerPlayer player) {
        sendTo(player,
                //? if >1.21.2 {
                player.level()
                //?} else {
                /*player.serverLevel()
                 *///?}
                , Iridescence.shouldApplyShader(player));
    }
    public static void sendTo(ServerPlayer player, ServerLevel world) {
        sendTo(player, world, Iridescence.shouldApplyShader(player));
    }
    public static void sendTo(ServerPlayer player, ServerLevel world, boolean iridescence) {
        new ShaderS2CPayload(iridescence,
                world == null ? new CompoundTag() : InfinityOptions.access(world).data()).send(player);
    }
}
