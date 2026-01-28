package net.lerariemann.infinity.registry.payload;


//? if >1.21 {
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//?} else {
/*import net.fabricmc.fabric.api.networking.v1.FabricPacket;
*///?}
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;

public interface FriendlyPacket extends
        //? if >1.21 {
        CustomPacketPayload {
    //?} else {
    /*FabricPacket {
     *///?}
    interface S2C extends FriendlyPacket {
        void receive(Minecraft client);

        default void send(ServerPlayer player) {
            InfinityPlatform.INSTANCE.sendS2CPayload(player, this);
        }
        // neoforge doesn't provide the client context
        //? if neoforge {
        /*default void receive() {
            receive(Minecraft.getInstance());
        }
        *///?}
    }

    interface C2S extends FriendlyPacket {
        void receive(ServerPlayer player);

        default void send() {
            InfinityPlatform.INSTANCE.sendC2SPayload(this);
        }
    }
}

