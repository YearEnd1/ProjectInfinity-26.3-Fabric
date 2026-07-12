package net.codexarchonic.infinity.registry.payload.c2s;
//? if >1.21 {
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;
//?} else {
/*import net.minecraft.network.FriendlyByteBuf;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
*///?}
import net.codexarchonic.infinity.item.F4Item;
import net.codexarchonic.infinity.registry.core.ModItems;
import net.codexarchonic.infinity.registry.payload.FriendlyPacket;
import net.codexarchonic.infinity.util.VersionMethods;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import static net.codexarchonic.infinity.util.InfinityMethods.getId;

public record F4DeployC2SPayload() implements FriendlyPacket.C2S {
    public static ResourceLocation sid = getId("deploy_f4");
    public static final F4DeployC2SPayload INSTANCE = new F4DeployC2SPayload();
    //? if >1.21 {

    public static final Type<F4DeployC2SPayload> ID = new Type<>(sid);
    public static final StreamCodec<RegistryFriendlyByteBuf, F4DeployC2SPayload> CODEC = StreamCodec.unit(INSTANCE);
    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
    //?} else {
        /*@Override
        public void write(FriendlyByteBuf buf) {
        }
        public static PacketType<F4DeployC2SPayload> type = PacketType.create(sid, buf -> INSTANCE);
        @Override
        public PacketType<?> getType() {
            return type;
        }
        public static void receive(F4DeployC2SPayload packet, ServerPlayer player, PacketSender responseSender) {
            packet.receive(player);
        }
        *///?}
    @Override
    public void receive(ServerPlayer player) {
        ItemStack st = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (st.is(ModItems.F4.get())) {
            var result = F4Item.deploy(player.level(), player, InteractionHand.MAIN_HAND);
            var resultItem = VersionMethods.getInteractionHolderResult(result);
            if (resultItem != null)
                player.setItemInHand(InteractionHand.MAIN_HAND, resultItem);
        }
    }
}
