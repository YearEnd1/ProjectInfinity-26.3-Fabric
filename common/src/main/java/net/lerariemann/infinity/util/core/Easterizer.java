package net.lerariemann.infinity.util.core;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.dimensions.RandomDimension;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.file.Files.walk;


public class Easterizer {
    public Map<String, Identifier> destinationMap = new HashMap<>();
    public Map<String, String> aliasMap = new HashMap<>();
    public Map<String, NbtCompound> map = new HashMap<>();
    public Map<String, String> typeMap = new HashMap<>();
    public Map<String, NbtCompound> optionmap = new HashMap<>();

    public Easterizer() {
        try (Stream<Path> files = walk(InfinityMod.configPath.resolve("easter"))) {
            files.filter(p -> p.toFile().isFile() && !p.toString().endsWith("_type.json")).forEach(p -> {
                String name = p.getFileName().toString().replace(".json", "");
                NbtCompound compound = CommonIO.read(p.toFile());
                if (compound.contains("name"))
                    name = NbtUtils.getString(compound, "name");
                if (compound.getType("aliases") == NbtElement.LIST_TYPE) {
                    String finalName = name;
                    NbtUtils.getList(compound, "aliases", NbtElement.STRING_TYPE)
                            .stream()
                            .map(e -> (NbtString)e)
                            .map(NbtString::asString)
                            .forEach(alias -> aliasMap.put(alias, finalName));
                }
                else if (compound.getType("aliases") == NbtElement.STRING_TYPE)
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
            throw new RuntimeException(e);
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

    public Identifier getAsEaster(String name) {
        if (aliasMap.containsKey(name)) name = aliasMap.get(name);
        return destinationMap.getOrDefault(name, isEaster(name) ? InfinityMethods.getId(name) : null);
    }

    public boolean isEaster(String name) {
        return map.containsKey(name) && !isDisabled(name);
    }
}
