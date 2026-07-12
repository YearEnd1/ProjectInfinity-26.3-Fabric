package net.codexarchonic.infinity.registry.payload.s2c;

//? if <1.21 {
/*import net.codexarchonic.infinity.access.WorldRendererAccess;
import net.codexarchonic.infinity.registry.payload.FriendlyPacket;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import static net.codexarchonic.infinity.util.InfinityMethods.getId;

public record PlayJukeboxS2CPayload(BlockPos pos, String soundEvent, String caption) implements FriendlyPacket.S2C {
    public static ResourceLocation sid = getId("play_jukebox");
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(soundEvent);
        buf.writeUtf(caption);
    }
    public static PacketType<PlayJukeboxS2CPayload> type =
            PacketType.create(sid, buf -> new PlayJukeboxS2CPayload(buf.readBlockPos(), buf.readUtf(), buf.readUtf()));
    @Override
    public PacketType<?> getType() {
        return type;
    }
    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        type.read(buf).receive(client);
    }
    @Override
    public void receive(Minecraft client) {
        ((WorldRendererAccess)client.levelRenderer).infinity$playMusic(
                pos, SoundEvent.createVariableRangeEvent(InfinityMethods.getId(soundEvent)), caption);
    }
}
*///?}