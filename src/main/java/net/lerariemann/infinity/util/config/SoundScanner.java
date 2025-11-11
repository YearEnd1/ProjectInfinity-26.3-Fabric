package net.lerariemann.infinity.util.config;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.access.MinecraftServerAccess;
import net.lerariemann.infinity.mixin.SoundListMixin;
import net.lerariemann.infinity.registry.var.ModPayloads;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.CommonIO;
import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.lerariemann.infinity.util.core.RandomProvider;
import net.lerariemann.infinity.util.loading.DimensionGrabber;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.storage.LevelResource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * Pipeline for generating custom sound events and jukebox song definitions for every music track in the game.
 */
public record SoundScanner(Map<ResourceLocation, Resource> soundIds) {
    /** Holds a map which allows to get the list of all sound IDs in existence and .ogg data for each.
     * Seeded by {@link SoundListMixin} on client launch. */
    public static SoundScanner instance;
    public static boolean isPreloaded() {
        return instance != null;
    }
    public static Stream<ResourceLocation> getMatchingLoadedIds() {
        if (isPreloaded()) return instance.soundIds.keySet().stream().filter(s -> s.getPath().contains("music") && !s.getPath().contains("record"));
        return Stream.of();
    }

    /**
     * <p>On player connect, the server sends a {@link ModPayloads.SoundPackS2CPayload} payload to the client.
     * <p>If the server already contains data upon which the client should create its resource pack, it holds this data.
     * <p>Otherwise, this payload is empty, and the server relies on the client to create it and send it back to the server for future use. */
    public static void unpackDownloadedPack(CompoundTag songIds, Minecraft cl) {
        //the client unpacks a non-empty payload only when needed, meaning only if it doesn't have necessary files yet
        if (!songIds.isEmpty() && !Files.exists(cl.getResourcePackDirectory().resolve("infinity/assets/infinity/sounds.json"))) {
            cl.execute(() -> saveResourcePack(cl, NbtUtils.getList(songIds, "entries", Tag.TAG_STRING).stream()
                    .map(Tag::getAsString).map(VersionMethods::id), false));
        }
        else if (isPreloaded()) {
            cl.execute(() -> {
                CompoundTag jukeboxes = saveResourcePack(cl, getMatchingLoadedIds(), true);
                CompoundTag res = new CompoundTag();
                ListTag songIdsList = new ListTag();
                getMatchingLoadedIds().forEach(id -> songIdsList.add(StringTag.valueOf(id.toString())));
                res.put("entries", songIdsList);
                res.put("jukeboxes", jukeboxes);
                ModPayloads.sendJukeboxesPayload(res);
            });
        }
    }
    /**
     * Generating and saving a resource pack from a stream of identifiers that correspond to music tracks.
     * @param sendJukeboxes if this is true, the method also generates and returns data that needs to be sent to the server
     * to seed it with corresponding jukebox song definitions.*/
    public static CompoundTag saveResourcePack(Minecraft client, Stream<ResourceLocation> songIds, boolean sendJukeboxes) {
        CompoundTag soundsForRP = new CompoundTag();
        CompoundTag subtitlesForRP = new CompoundTag();
        CompoundTag jukeboxes = new CompoundTag();
        songIds.forEach(id -> {
            String str = id.toString().replace(".ogg", "").replace("sounds/", "");
            List<String> arr = Arrays.stream(str.split("[:/]")).toList(); //preloading IDs
            String songID = "disc." + arr.getFirst() + "." + arr.getLast();
            String subtitleID = "infinity:subtitles." + songID;
            String subtitleData = InfinityMethods.formatAsTitleCase(arr.getFirst() + " - " + arr.getLast());

            ListTag soundForRPList = new ListTag();
            soundForRPList.add(StringTag.valueOf(str));
            CompoundTag soundForRP = new CompoundTag();
            soundForRP.put("sounds", soundForRPList);
            soundForRP.putString("subtitle", subtitleID);
            subtitlesForRP.putString(subtitleID, subtitleData);
            soundsForRP.put(songID, soundForRP);

            if (sendJukeboxes) {
                if (!isPreloaded()) return;
                double length;
                try {
                    length = calculateDuration(instance.soundIds.get(id).open().readAllBytes());
                } catch (IOException e) {
                    length = 600;
                }
                jukeboxes.put(arr.getLast(), getJukeboxDef(songID, subtitleID, length));
            }
        });
        CommonIO.write(soundsForRP, client.getResourcePackDirectory().resolve("infinity/assets/infinity"), "sounds.json");
        CommonIO.write(subtitlesForRP, client.getResourcePackDirectory().resolve("infinity/assets/infinity/lang"), "en_us.json");
        return jukeboxes;
    }

    /** Receiver for a C2S {@link ModPayloads.JukeboxesC2SPayload} payload, which holds data to send to clients in the future for them to
     * generate custom sound resource packs, as well as jukebox song definitions corresponding to this data. */
    public static void unpackUploadedJukeboxes(MinecraftServer server, CompoundTag data) {
        if (!RandomProvider.rule("useSoundSyncPackets")) return;
        if (!data.contains("jukeboxes") || !data.contains("entries")) return;
        if (Files.exists(server.getWorldPath(LevelResource.DATAPACK_DIR).resolve("client_sound_pack_data.json"))) return;

        CompoundTag allJukeboxes = NbtUtils.getCompound(data, "jukeboxes");
        Path pathJukeboxes = server.getWorldPath(LevelResource.DATAPACK_DIR).resolve("infinity/data/infinity/jukebox_song");
        for (String key: allJukeboxes.getAllKeys()) {
            if (allJukeboxes.get(key) instanceof CompoundTag jukebox) {
                CommonIO.write(jukebox, pathJukeboxes, key + ".json");
            }
        }
        grabJukeboxes(server);

        data.remove("jukeboxes");
        CompoundTag packData = new CompoundTag();
        packData.put("entries", NbtUtils.getList(data, "entries", new ListTag(), Tag.TAG_STRING));
        CommonIO.write(packData, server.getWorldPath(LevelResource.DATAPACK_DIR), "client_sound_pack_data.json");
    }
    /** Injects freshly received jukebox song definitions into the server's registries, config files and {@link RandomProvider}. */
    public static void grabJukeboxes(MinecraftServer server) {
        Path pathJukeboxes = server.getWorldPath(LevelResource.DATAPACK_DIR).resolve("infinity/data/infinity/jukebox_song");
        InfinityMod.LOGGER.info("grabbing jukeboxes");
        DimensionGrabber.readCategoryFromDisk(server, JukeboxSong.DIRECT_CODEC, Registries.JUKEBOX_SONG, pathJukeboxes);
        if (!((MinecraftServerAccess)server).infinity$needsInvocation()) {
            ConfigFactory.of(server.registryAccess().registryOrThrow(Registries.JUKEBOX_SONG)).generate(ConfigType.JUKEBOXES);
            InfinityMod.updateProvider(server);
        }
    }

    /**
     Getting a duration of an OGG file from its byte data; <a href="https://stackoverflow.com/a/44407355">implementation from StackOverflow</a>.
     */
    public static double calculateDuration(byte[] track) throws IOException {
        int rate = -1;
        int length = -1;
        int size = track.length;

        for (int i = size-1-8-2-4; i>=0 && length<0; i--) { //4 bytes for "OggS", 2 unused bytes, 8 bytes for length
            // Looking for length (value after last "OggS")
            if (
                    track[i]==(byte)'O'
                            && track[i+1]==(byte)'g'
                            && track[i+2]==(byte)'g'
                            && track[i+3]==(byte)'S'
            ) {
                byte[] byteArray = new byte[]{track[i+6],track[i+7],track[i+8],track[i+9],track[i+10],track[i+11],track[i+12],track[i+13]};
                ByteBuffer bb = ByteBuffer.wrap(byteArray);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                length = bb.getInt(0);
            }
        }
        for (int i = 0; i<size-8-2-4 && rate<0; i++) {
            // Looking for rate (first value after "vorbis")
            if (
                    track[i]==(byte)'v'
                            && track[i+1]==(byte)'o'
                            && track[i+2]==(byte)'r'
                            && track[i+3]==(byte)'b'
                            && track[i+4]==(byte)'i'
                            && track[i+5]==(byte)'s'
            ) {
                byte[] byteArray = new byte[]{track[i+11],track[i+12],track[i+13],track[i+14]};
                ByteBuffer bb = ByteBuffer.wrap(byteArray);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                rate = bb.getInt(0);
            }
        }
        return length / (double) rate;
    }

    private static @NotNull CompoundTag getJukeboxDef(String songID, String subtitleID, double length) {
        CompoundTag jukebox_def = new CompoundTag();
        CompoundTag sound_event = new CompoundTag();
        sound_event.putString("sound_id", "infinity:" + songID);
        jukebox_def.put("sound_event", sound_event);
        CompoundTag description = new CompoundTag();
        description.putString("translate", subtitleID);
        jukebox_def.put("description", description);
        jukebox_def.putFloat("length_in_seconds", (float)length);
        jukebox_def.putInt("comparator_output", 15);
        return jukebox_def;
    }
}
