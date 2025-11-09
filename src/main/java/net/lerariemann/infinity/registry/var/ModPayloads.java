package net.lerariemann.infinity.registry.var;

import dev.architectury.platform.Platform;
import net.lerariemann.infinity.access.InfinityOptionsAccess;
import net.lerariemann.infinity.access.WorldRendererAccess;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.item.F4Item;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.platform.PlatformMethods;
import net.lerariemann.infinity.util.loading.DimensionGrabber;
import net.lerariemann.infinity.options.InfinityOptions;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.loading.ShaderLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import java.nio.file.Path;

public class ModPayloads {
    public record WorldAddS2CPayload(ResourceLocation world_id, CompoundTag world_data) implements CustomPacketPayload {
        public static final Type<WorldAddS2CPayload> ID = new Type<>(InfinityMethods.getId("add_world"));
        public static final StreamCodec<RegistryFriendlyByteBuf, WorldAddS2CPayload> CODEC = StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, WorldAddS2CPayload::world_id,
                ByteBufCodecs.COMPOUND_TAG, WorldAddS2CPayload::world_data,
                WorldAddS2CPayload::new);
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }
    public static void sendWorldAddPayload(ServerPlayer player, ResourceLocation id, CompoundTag data) {
        PlatformMethods.sendS2CPayload(player, new WorldAddS2CPayload(id, data));
    }
    public static void receiveWorldAddPayload(Minecraft client, ResourceLocation id, CompoundTag data) {
        client.execute(() -> DimensionGrabber.grabObjectForClient(client, DimensionType.DIRECT_CODEC, Registries.DIMENSION_TYPE, id, data));
    }

    public record BiomeAddS2CPayload(ResourceLocation biome_id, CompoundTag biome_data) implements CustomPacketPayload {
        public static final Type<BiomeAddS2CPayload> ID = new Type<>(InfinityMethods.getId("add_biome"));
        public static final StreamCodec<RegistryFriendlyByteBuf, BiomeAddS2CPayload> CODEC = StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, BiomeAddS2CPayload::biome_id,
                ByteBufCodecs.COMPOUND_TAG, BiomeAddS2CPayload::biome_data,
                BiomeAddS2CPayload::new);
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }
    public static void sendBiomeAddPayload(ServerPlayer player, ResourceLocation id, CompoundTag data) {
        PlatformMethods.sendS2CPayload(player, new BiomeAddS2CPayload(id, data));
    }
    public static void receiveBiomeAddPayload(Minecraft client, ResourceLocation id, CompoundTag data) {
        client.execute(() -> DimensionGrabber.grabObjectForClient(client, Biome.DIRECT_CODEC, Registries.BIOME, id, data));
    }

    public record ShaderS2CPayload(CompoundTag shader_data, boolean iridescence) implements CustomPacketPayload {
        public static final Type<ShaderS2CPayload> ID = new Type<>(InfinityMethods.getId("reload_shader"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ShaderS2CPayload> CODEC = StreamCodec.composite(
                ByteBufCodecs.COMPOUND_TAG, ShaderS2CPayload::shader_data,
                ByteBufCodecs.BOOL, ShaderS2CPayload::iridescence,
                ShaderS2CPayload::new);
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }
    public static void sendShaderPayload(ServerPlayer player) {
        sendShaderPayload(player, player.serverLevel(), Iridescence.shouldApplyShader(player));
    }
    public static void sendShaderPayload(ServerPlayer player, ServerLevel world) {
        sendShaderPayload(player, world, Iridescence.shouldApplyShader(player));
    }
    public static void sendShaderPayload(ServerPlayer player, ServerLevel world, boolean iridescence) {
        PlatformMethods.sendS2CPayload(player, new ShaderS2CPayload(
                world == null ? new CompoundTag() : InfinityOptions.access(world).data(),
                iridescence));
    }
    public static void receiveShaderPayload(Minecraft client, CompoundTag data, boolean iridescence) {
        InfinityOptions options = new InfinityOptions(data);
        ((InfinityOptionsAccess)client).infinity$setOptions(options);
        client.execute(() -> ShaderLoader.reloadShaders(client, options.getShader(), iridescence));
    }
    public static boolean resourcesReloaded = Path.of(Platform.getGameFolder() + "/resourcepacks/infinity/assets/infinity/shaders").toFile().exists();

    public record StarsS2CPayload() implements CustomPacketPayload {
        public static final StarsS2CPayload INSTANCE = new StarsS2CPayload();
        public static final Type<StarsS2CPayload> ID = new Type<>(InfinityMethods.getId("reload_stars"));
        public static final StreamCodec<RegistryFriendlyByteBuf, StarsS2CPayload> CODEC = StreamCodec.unit(INSTANCE);
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }
    public static void sendStarsPayload(ServerPlayer player) {
        PlatformMethods.sendS2CPayload(player, StarsS2CPayload.INSTANCE);
    }
    public static void receiveStarsPayload(Minecraft client) {
        ((WorldRendererAccess)(client.levelRenderer)).infinity$setNeedsStars(true);
    }

    public record F4UpdateC2SPayload(int slot, int width, int height) implements CustomPacketPayload {
        public static final Type<F4UpdateC2SPayload> ID = new Type<>(InfinityMethods.getId("receive_f4"));
        public static final StreamCodec<RegistryFriendlyByteBuf, F4UpdateC2SPayload> CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, F4UpdateC2SPayload::slot,
                ByteBufCodecs.VAR_INT, F4UpdateC2SPayload::width,
                ByteBufCodecs.VAR_INT, F4UpdateC2SPayload::height,
                F4UpdateC2SPayload::new);
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }
    public static void sendF4UpdatePayload(int slot, int width, int height) {
        PlatformMethods.sendC2SPayload(new F4UpdateC2SPayload(slot, width, height));
    }
    public static void receiveF4UpdatePayload(ServerPlayer player, int slot, int width, int height) {
        ItemStack st = player.getInventory().getItem(slot);
        if (st.is(ModItems.F4.get())) {
            ItemStack newStack = st.copy();
            newStack.applyComponents(DataComponentMap.builder()
                    .set(ModComponentTypes.SIZE_X.get(), Math.clamp(width, 1, 21))
                    .set(ModComponentTypes.SIZE_Y.get(), Math.clamp(height, 1, 21))
                    .build());
            player.getInventory().setItem(slot, newStack);
        }
    }

    public record F4DeployC2SPayload() implements CustomPacketPayload {
        public static final F4DeployC2SPayload INSTANCE = new F4DeployC2SPayload();
        public static final Type<F4DeployC2SPayload> ID = new Type<>(InfinityMethods.getId("deploy_f4"));
        public static final StreamCodec<RegistryFriendlyByteBuf, F4DeployC2SPayload> CODEC = StreamCodec.unit(INSTANCE);
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }
    public static void sendF4DeployPayload() {
        PlatformMethods.sendC2SPayload(F4DeployC2SPayload.INSTANCE);
    }
    public static void receiveF4DeployPayload(ServerPlayer player) {
        ItemStack st = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (st.is(ModItems.F4.get())) {
            InteractionResultHolder<ItemStack> result = F4Item.deploy(player.serverLevel(), player, InteractionHand.MAIN_HAND);
            player.setItemInHand(InteractionHand.MAIN_HAND, result.getObject());
        }
    }

    public record SoundPackS2CPayload(CompoundTag songIds) implements CustomPacketPayload {
        public static final Type<SoundPackS2CPayload> ID = new Type<>(InfinityMethods.getId("download_sound_pack"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SoundPackS2CPayload> CODEC = StreamCodec.composite(
                ByteBufCodecs.COMPOUND_TAG, SoundPackS2CPayload::songIds, SoundPackS2CPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }
    public static void sendSoundPackPayload(ServerPlayer player, CompoundTag data) {
        PlatformMethods.sendS2CPayload(player, new SoundPackS2CPayload(data));
    }

    public record JukeboxesC2SPayload(CompoundTag data) implements CustomPacketPayload {
        public static final Type<JukeboxesC2SPayload> ID = new Type<>(InfinityMethods.getId("upload_jukeboxes"));
        public static final StreamCodec<RegistryFriendlyByteBuf, JukeboxesC2SPayload> CODEC = StreamCodec.composite(
                ByteBufCodecs.COMPOUND_TAG, JukeboxesC2SPayload::data, JukeboxesC2SPayload::new);
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }
    public static void sendJukeboxesPayload(CompoundTag data) {
        PlatformMethods.sendC2SPayload(new JukeboxesC2SPayload(data));
    }
}