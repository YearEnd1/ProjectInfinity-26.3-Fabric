package net.lerariemann.infinity.util.loading;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.lerariemann.infinity.util.core.CommonIO;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.file.Files.walk;

public class JsonGrabber<E> {
    Codec<E> decoder;
    WritableRegistry<E> registry;
    RegistryOps.RegistryInfoLookup registryInfoGetter;

    JsonGrabber(RegistryOps.RegistryInfoLookup get, Codec<E> dec, WritableRegistry<E> reg) {
        decoder = dec;
        registry = reg;
        registryInfoGetter = get;
    }

    public void grabAll(Path rootdir) {
        grabAll(rootdir, false);
    }

    void grabAll(Path rootdir, boolean bl) {
        if(!rootdir.toFile().exists()) return;
        try (Stream<Path> files = walk(rootdir)) {
            files.forEach(a -> grab(a, bl));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void grab(Path path, boolean bl) {
        String path1 = path.toString();
        if (path1.endsWith(".json")) {
            String fullname = path1.substring(path1.lastIndexOf("/") + 1, path1.length() - 5);
            int i = fullname.lastIndexOf("\\");
            if (i>=0) fullname = fullname.substring(i + 1);
            ResourceKey<E> key = ResourceKey.create(registry.key(), InfinityMethods.getId(fullname));
            grab(path1, key, bl);
        }
    }

    void grab(ResourceLocation id, CompoundTag compound, boolean bl) {
        grab(ResourceKey.create(registry.key(), id), JsonParser.parseString(CommonIO.compoundToString(compound)), bl);
    }

    void grab(ResourceKey<E> key, JsonElement jsonElement, boolean bl) {
        RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, registryInfoGetter);
        try {
            DataResult<E> dataResult = decoder.parse(registryOps, jsonElement);
            if(dataResult.result().isPresent()) {
                E object = dataResult.result().get();
                if (bl || !registry.containsKey(key)) registry.register(key, object, RegistrationInfo.BUILT_IN);
            }
            else {
                LogManager.getLogger().info(jsonElement);
            }
        }
        catch (Exception e) {
            LogManager.getLogger().info(jsonElement);
            throw new RuntimeException(e.getMessage() + " Element affected: " + jsonElement.toString());
        }
    }

    void grab(String path, ResourceKey<E> registryKey, boolean bl) {
        File file = new File(path);
        String content;
        try {
            content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonElement jsonElement = JsonParser.parseString(content);
        grab(registryKey, jsonElement, bl);
    }

    E grabWithReturn(String rootdir, String i, boolean register) {
        String path = rootdir + "/" + i + ".json";
        ResourceKey<E> key = ResourceKey.create(registry.key(), InfinityMethods.getId(i));
        File file = new File(path);
        String content;
        try {
            content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonElement jsonElement = JsonParser.parseString(content);
        RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, registryInfoGetter);
        DataResult<E> dataResult = decoder.parse(registryOps, jsonElement);
        E object = dataResult.getOrThrow((error) -> null);
        if (register) registry.register(key, object, RegistrationInfo.BUILT_IN);
        return object;
    }
}
