package net.lerariemann.infinity.util.config;

import net.lerariemann.infinity.access.MinecraftServerAccess;
import net.lerariemann.infinity.mixin.SoundListMixin;
import net.lerariemann.infinity.registry.payload.c2s.JukeboxesC2SPayload;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.CommonIO;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.lerariemann.infinity.util.core.RandomProvider;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
//? if >1.21 {
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.core.registries.Registries;
import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.util.loading.DimensionGrabber;
import java.nio.file.Path;
 //?}
import net.minecraft.world.level.storage.LevelResource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

/**
 * Pipeline for generating custom sound events and jukebox song definitions for every music track in the game.
 */
public record SoundScanner(Map<ResourceLocation, Resource> soundIds) {
    /** Holds a map which allows to get the list of all sound IDs in existence and .ogg data for each.
     * Seeded by {@link SoundListMixin} on client launch. */
    public static SoundScanner instance;
    public static final String lockFile = (InfinityPlatform.version > 20 ? "client" : "server") + "_sound_pack_data.json";
    public static boolean isPreloaded() {
        return instance != null;
    }
    public static Stream<ResourceLocation> getMatchingLoadedIds() {
        if (isPreloaded()) return instance.soundIds.keySet().stream().filter(s -> s.getPath().contains("music") && !s.getPath().contains("record"));
        return Stream.of();
    }

    /**
     * <p>On player connect, the server sends a {@link net.lerariemann.infinity.registry.payload.s2c.SoundPackS2CPayload} payload to the client.
     * <p>If the server already contains data upon which the client should create its resource pack, it holds this data.
     * <p>Otherwise, this payload is empty, and the server relies on the client to create it and send it back to the server for future use. */
    public static void unpackDownloadedPack(CompoundTag songIds, Minecraft cl) {

        //the client unpacks a non-empty payload only when needed, meaning only if it doesn't have necessary files yet
        if (!songIds.isEmpty() && !Files.exists(cl.getResourcePackDirectory().resolve("infinity/assets/infinity/sounds.json"))) {
            cl.execute(() -> saveResourcePack(cl, NbtUtils.getList(songIds, "entries", Tag.TAG_STRING).stream()
                    .map(NbtUtils::getAsString).map(VersionMethods::id), false));
        }
        else if (isPreloaded()) {
            cl.execute(() -> {
                CompoundTag jukeboxes = saveResourcePack(cl, getMatchingLoadedIds(), true);
                CompoundTag res = new CompoundTag();
                ListTag songIdsList = new ListTag();
                getMatchingLoadedIds().forEach(id -> songIdsList.add(StringTag.valueOf(id.toString())));
                res.put("entries", songIdsList);
                res.put("jukeboxes", jukeboxes);
                new JukeboxesC2SPayload(res).send();
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
            String songID = "disc." + arr.get(0) + "." + arr.get(arr.size()-1);
            String subtitleID = "infinity:subtitles." + songID;
            String subtitleData = InfinityMethods.formatAsTitleCase(arr.get(0) + " - " + arr.get(arr.size()-1));

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
                jukeboxes.put(arr.get(arr.size()-1), getJukeboxDef(songID, subtitleID, length));
            }
        });
        CommonIO.write(soundsForRP, client.getResourcePackDirectory().resolve("infinity/assets/infinity"), "sounds.json");
        CommonIO.write(subtitlesForRP, client.getResourcePackDirectory().resolve("infinity/assets/infinity/lang"), "en_us.json");
        return jukeboxes;
    }

    /** Receiver for a C2S {@link JukeboxesC2SPayload} payload, which holds data to send to clients in the future for them to
     * generate custom sound resource packs, as well as jukebox song definitions corresponding to this data. */
    public static void unpackUploadedJukeboxes(MinecraftServer server, CompoundTag data) {
        if (!RandomProvider.rule("useSoundSyncPackets")) return;
        if (!data.contains("jukeboxes") || !data.contains("entries")) return;
        if (Files.exists(server.getWorldPath(LevelResource.DATAPACK_DIR).resolve(lockFile))) return;

        //? if >1.21 {
        CompoundTag allJukeboxes = NbtUtils.getCompound(data, "jukeboxes");
        Path pathJukeboxes = server.getWorldPath(LevelResource.DATAPACK_DIR).resolve("infinity/data/infinity/jukebox_song");
        for (String key : NbtUtils.keys(allJukeboxes)) {
            if (allJukeboxes.get(key) instanceof CompoundTag jukebox) {
                CommonIO.write(jukebox, pathJukeboxes, key + ".json");
            }
        }
        grabJukeboxes(server);
        //?} else {
        /*((MinecraftServerAccess)server).infinity$setJukeboxDataMap(data.getCompound("jukeboxes"));
        CommonIO.write(data.getCompound("jukeboxes"), server.getWorldPath(LevelResource.DATAPACK_DIR), "server_sound_pack_data.json");
        *///?}

        data.remove("jukeboxes");
        CompoundTag packData = new CompoundTag();
        packData.put("entries", NbtUtils.getList(data, "entries", Tag.TAG_STRING));
        CommonIO.write(packData, server.getWorldPath(LevelResource.DATAPACK_DIR), "client_sound_pack_data.json");
    }

    /** Injects freshly received jukebox song definitions into the server's registries, config files and {@link RandomProvider}. */
    //? if >1.21 {
    public static void grabJukeboxes(MinecraftServer server) {
        Path pathJukeboxes = server.getWorldPath(LevelResource.DATAPACK_DIR).resolve("infinity/data/infinity/jukebox_song");
        InfinityMod.LOGGER.info("grabbing jukeboxes");
        DimensionGrabber.readCategoryFromDisk(server, JukeboxSong.DIRECT_CODEC, Registries.JUKEBOX_SONG, pathJukeboxes);
        if (!((MinecraftServerAccess)server).infinity$needsInvocation()) {
            ConfigFactory.of(VersionMethods.getRegistry(server.registryAccess(), Registries.JUKEBOX_SONG)).generate(ConfigType.JUKEBOXES);
            InfinityMod.updateProvider(server);
        }
    }
    //?}

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
        //? if >1.21 {
        CompoundTag sound_event = new CompoundTag();
        sound_event.putString("sound_id", "infinity:" + songID);
        jukebox_def.put("sound_event", sound_event);
        CompoundTag description = new CompoundTag();
        description.putString("translate", subtitleID);
        jukebox_def.put("description", description);
        jukebox_def.putFloat("length_in_seconds", (float)length);
        jukebox_def.putInt("comparator_output", 15);
        //?} else {
        /*jukebox_def.putString("sound_id", songID);
        jukebox_def.putString("sound_sub", subtitleID);
        jukebox_def.putInt("sound_duration", (int)(20*length));
        *///?}
        return jukebox_def;
    }
}
