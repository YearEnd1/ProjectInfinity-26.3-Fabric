package net.lerariemann.infinity.registry.var;

import net.lerariemann.infinity.access.InfinityOptionsAccess;
import net.lerariemann.infinity.access.WorldRendererAccess;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.item.F4Item;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.config.SoundScanner;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.lerariemann.infinity.util.loading.DimensionGrabber;
import net.lerariemann.infinity.options.InfinityOptions;
import net.lerariemann.infinity.util.loading.ShaderLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import static net.lerariemann.infinity.util.InfinityMethods.getId;


//? if >1.21 {

import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.world.level.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//?} else {
/*import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
*///?}

import java.nio.file.Path;

public class ModPayloads {
    public static String oldShader = "";
    public interface FriendlyPacket extends
    //? if >1.21 {
    CustomPacketPayload {
     //?} else {
    /*FabricPacket {
        *///?}
    }

    public interface FriendlyPacketS2C extends FriendlyPacket {
        void receive(Minecraft client);

        default void send(ServerPlayer player) {
            InfinityPlatform.INSTANCE.sendS2CPayload(player, this);
        }
    }

    public interface FriendlyPacketC2S extends FriendlyPacket {
        void receive(ServerPlayer player);

        default void send() {
            InfinityPlatform.INSTANCE.sendC2SPayload(this);
        }
    }

    public record WorldAddS2CPayload(ResourceLocation id, CompoundTag data) implements FriendlyPacketS2C {
        static ResourceLocation sid = getId("add_world");
        //? if >1.21 {
        
        public static final Type<WorldAddS2CPayload> ID = new Type<>(sid);
        public static final StreamCodec<RegistryFriendlyByteBuf, WorldAddS2CPayload> CODEC = StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, WorldAddS2CPayload::id,
                ByteBufCodecs.COMPOUND_TAG, WorldAddS2CPayload::data,
                WorldAddS2CPayload::new);
        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
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
        public void receive() {
            receive(Minecraft.getInstance());
        }
        public void receive(Minecraft client) {
            client.execute(() -> DimensionGrabber.grabObjectForClient(client, DimensionType.DIRECT_CODEC, Registries.DIMENSION_TYPE, id, data));
        }
    }

    public record BiomeAddS2CPayload(ResourceLocation id, CompoundTag data) implements FriendlyPacketS2C {
        static ResourceLocation sid = getId("add_biome");
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
        public void receive() {
            receive(Minecraft.getInstance());
        }
        public void receive(Minecraft client) {
            client.execute(() -> DimensionGrabber.grabObjectForClient(client, Biome.DIRECT_CODEC, Registries.BIOME, id, data));
        }
    }

    public record ShaderS2CPayload(boolean iridescence, CompoundTag data) implements FriendlyPacketS2C {
        static ResourceLocation sid = getId("reload_shader");
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
        public void receive() {
            receive(Minecraft.getInstance());
        }
        @Override
        public void receive(Minecraft client) {
            InfinityOptions options = new InfinityOptions(data);
            ((InfinityOptionsAccess)client).infinity$setOptions(options);
            client.execute(() -> ShaderLoader.reloadShaders(client, options.getShader(), iridescence));
        }
    }

    public record StarsS2CPayload() implements FriendlyPacketS2C {
        static ResourceLocation sid = getId("reload_stars");
        public static final StarsS2CPayload INSTANCE = new StarsS2CPayload();
        //? if >1.21 {
        
        public static final Type<StarsS2CPayload> ID = new Type<>(sid);
        public static final StreamCodec<RegistryFriendlyByteBuf, StarsS2CPayload> CODEC = StreamCodec.unit(INSTANCE);
        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
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
        public void receive() {
            receive(Minecraft.getInstance());
        }
        @Override
        public void receive(Minecraft client) {
            client.execute(() -> ((WorldRendererAccess)(client.levelRenderer)).infinity$setNeedsStars(true));
        }
    }

    public record SoundPackS2CPayload(CompoundTag songIds) implements FriendlyPacketS2C {
        static ResourceLocation sid = getId("download_sound_pack");
        //? if >1.21 {
        
        public static final Type<SoundPackS2CPayload> ID = new Type<>(sid);
        public static final StreamCodec<RegistryFriendlyByteBuf, SoundPackS2CPayload> CODEC = StreamCodec.composite(
                ByteBufCodecs.COMPOUND_TAG, SoundPackS2CPayload::songIds, SoundPackS2CPayload::new);
        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
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
        public void receive() {
            receive(Minecraft.getInstance());
        }
        @Override
        public void receive(Minecraft client) {
            SoundScanner.unpackDownloadedPack(songIds, client);
        }
    }

    public record F4DeployC2SPayload() implements FriendlyPacketC2S {
        static ResourceLocation sid = getId("deploy_f4");
        public static final F4DeployC2SPayload INSTANCE = new F4DeployC2SPayload();
        //? if >1.21 {
        
        public static final Type<F4DeployC2SPayload> ID = new Type<>(sid);
        public static final StreamCodec<RegistryFriendlyByteBuf, F4DeployC2SPayload> CODEC = StreamCodec.unit(INSTANCE);
        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
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
        public static void receive(F4DeployC2SPayload packet, ServerPlayer player, PacketSender responceSenser) {
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

    public record F4UpdateC2SPayload(int slot, int width, int height) implements FriendlyPacketC2S {
        static ResourceLocation sid = getId("update_f4");
        //? if >1.21 {
        public static final Type<F4UpdateC2SPayload> ID = new Type<>(InfinityMethods.getId("receive_f4"));
        public static final StreamCodec<RegistryFriendlyByteBuf, F4UpdateC2SPayload> CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, F4UpdateC2SPayload::slot,
                ByteBufCodecs.VAR_INT, F4UpdateC2SPayload::width,
                ByteBufCodecs.VAR_INT, F4UpdateC2SPayload::height,
                F4UpdateC2SPayload::new);
        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
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
        public static void receive(F4UpdateC2SPayload packet, ServerPlayer player, PacketSender responceSenser) {
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

    public record JukeboxesC2SPayload(CompoundTag data) implements FriendlyPacketC2S {
        static ResourceLocation sid = getId("upload_jukeboxes");
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
        public static void receive(JukeboxesC2SPayload packet, ServerPlayer player, PacketSender responceSenser) {
            packet.receive(player);
        }
        *///?}
        @Override
        public void receive(ServerPlayer player) {
            SoundScanner.unpackUploadedJukeboxes(player.level().getServer(), data);
        }
    }

    public static void registerC2SPacketsReceivers() {
        //? if >1.21 {
        net.lerariemann.infinity.util.platform.InfinityPlatform.INSTANCE.registerPayloadsServer();
        //?} else {
        /*ServerPlayNetworking.registerGlobalReceiver(F4UpdateC2SPayload.type, F4UpdateC2SPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(F4DeployC2SPayload.type, F4DeployC2SPayload::receive);
        *///?}
    }

    public static void registerS2CPacketsReceivers() {
        //? if >1.21 {
        net.lerariemann.infinity.util.platform.InfinityPlatform.INSTANCE.registerPayloadsClient();
        //?} else {
        /*ClientPlayNetworking.registerGlobalReceiver(WorldAddS2CPayload.sid, WorldAddS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(BiomeAddS2CPayload.sid, BiomeAddS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(ShaderS2CPayload.sid, ShaderS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(StarsS2CPayload.sid, StarsS2CPayload::receive);
        ClientPlayNetworking.registerGlobalReceiver(SoundPackS2CPayload.sid, SoundPackS2CPayload::receive);
        *///?}
    }

    public static void sendShaderPayload(ServerPlayer player) {
        sendShaderPayload(player,
                //? if >1.21.2 {
                player.level()
                //?} else {
                /*player.serverLevel()
                *///?}
                , Iridescence.shouldApplyShader(player));
    }
    public static void sendShaderPayload(ServerPlayer player, ServerLevel world) {
        sendShaderPayload(player, world, Iridescence.shouldApplyShader(player));
    }
    public static void sendShaderPayload(ServerPlayer player, ServerLevel world, boolean iridescence) {
        new ShaderS2CPayload(iridescence,
                world == null ? new CompoundTag() : InfinityOptions.access(world).data()).send(player);
    }
    public static void sendStarsPayload(ServerPlayer player) {
        StarsS2CPayload.INSTANCE.send(player);
    }
}