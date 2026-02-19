package net.lerariemann.infinity.registry.payload.c2s;

//? if >1.21 {
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.lerariemann.infinity.util.InfinityMethods;
import org.jspecify.annotations.NonNull;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
*///?}
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.registry.payload.FriendlyPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import static net.lerariemann.infinity.util.InfinityMethods.getId;

public record F4UpdateC2SPayload(int slot, int width, int height) implements FriendlyPacket.C2S {
    public static ResourceLocation sid = getId("update_f4");
    //? if >1.21 {
    public static final Type<F4UpdateC2SPayload> ID = new Type<>(InfinityMethods.getId("receive_f4"));
    public static final StreamCodec<RegistryFriendlyByteBuf, F4UpdateC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, F4UpdateC2SPayload::slot,
            ByteBufCodecs.VAR_INT, F4UpdateC2SPayload::width,
            ByteBufCodecs.VAR_INT, F4UpdateC2SPayload::height,
            F4UpdateC2SPayload::new);
    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
    //?} else {
        /*@Override
        public void write(FriendlyByteBuf buf) {
            buf.writeInt(slot);
            buf.writeInt(width);
            buf.writeInt(height);
        }
        public static PacketType<F4UpdateC2SPayload> type =
                PacketType.create(sid, buf -> new F4UpdateC2SPayload(buf.readInt(), buf.readInt(), buf.readInt()));
        @Override
        public PacketType<?> getType() {
            return type;
        }
        public static void receive(F4UpdateC2SPayload packet, ServerPlayer player, PacketSender responseSender) {
            packet.receive(player);
        }
        *///?}
    public void receive(ServerPlayer player) {
        ItemStack st = player.getInventory().getItem(slot);
        if (st.is(ModItems.F4.get())) {
            ItemStack newStack = st.copy();
            //? if >1.21 {
            newStack.applyComponents(DataComponentMap.builder()
                    .set(ModComponentTypes.SIZE_X.get(), Math.clamp(width, 1, 21))
                    .set(ModComponentTypes.SIZE_Y.get(), Math.clamp(height, 1, 21))
                    .build());
            //?} else {
                /*CompoundTag nbt = st.getTag();
                if (nbt == null) nbt = new CompoundTag();
                nbt.putInt(ModComponentTypes.SIZE_X, width);
                nbt.putInt(ModComponentTypes.SIZE_Y, height);
                newStack.setTag(nbt);
                *///?}
            player.getInventory().setItem(slot, newStack);
        }
    }
}
