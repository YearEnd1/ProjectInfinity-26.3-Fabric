package net.codexarchonic.infinity.util.core;

import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.dimensions.RandomDimension;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.codexarchonic.infinity.util.VersionMethods;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.file.Files.walk;


public class Easterizer {
    public Map<String, ResourceLocation> destinationMap = new HashMap<>();
    public Map<String, String> aliasMap = new HashMap<>();
    public Map<String, CompoundTag> map = new HashMap<>();
    public Map<String, String> typeMap = new HashMap<>();
    public Map<String, CompoundTag> optionmap = new HashMap<>();

    public Easterizer() {
        try (Stream<Path> files = walk(InfinityMod.configPath.resolve("easter"))) {
            files.filter(p -> p.toFile().isFile() && !p.toString().endsWith("_type.json")).forEach(p -> {
                String name = p.getFileName().toString().replace(".json", "");
                CompoundTag compound = CommonIO.read(p.toFile());
                if (compound.contains("name"))
                    name = NbtUtils.getString(compound, "name");
                if (NbtUtils.compoundHasList(compound, "aliases")) {
                    String finalName = name;
                    NbtUtils.getList(compound, "aliases", Tag.TAG_STRING)
                            .stream()
                            .map(e -> (StringTag)e)
                            .map(NbtUtils::getAsString)
                            .forEach(alias -> aliasMap.put(alias, finalName));
                }
                else if (NbtUtils.compoundHasString(compound, "aliases"))
                    aliasMap.put(NbtUtils.getString(compound, "aliases"), name);
                if (compound.contains("destination")) destinationMap.put(name, VersionMethods.id(NbtUtils.getString(compound, "destination")));
                else {
                    if (compound.contains("type"))
                        typeMap.put(name, NbtUtils.getString(compound, "type"));
                    if (compound.contains("options")) {
                        optionmap.put(name, NbtUtils.getCompound(compound, "options"));
                    }
                    map.put(name, NbtUtils.getCompound(compound, "generator"));
                }
            });
        } catch (IOException e) {
            InfinityMod.LOGGER.error("Error reading easterizer.json file!", e);
        }
    }

    public boolean easterize(RandomDimension d) {
        String name = d.getName();
        if (!isEaster(d.getName())) return false;
        String type = typeMap.getOrDefault(name, "default");
        if (!type.contains(":")) type = InfinityMod.MOD_ID + ":" + type;
        d.data.putString("type", type);
        d.data.put("generator", map.get(name));
        return true;
    }

    public static boolean isDisabled(String name) {
        return InfinityMod.provider.disabledDimensions.contains(name);
    }

    public ResourceLocation getAsEaster(String name) {
        if (aliasMap.containsKey(name)) name = aliasMap.get(name);
        return destinationMap.getOrDefault(name, isEaster(name) ? InfinityMethods.getId(name) : null);
    }

    public boolean isEaster(String name) {
        return map.containsKey(name) && !isDisabled(name);
    }
}
