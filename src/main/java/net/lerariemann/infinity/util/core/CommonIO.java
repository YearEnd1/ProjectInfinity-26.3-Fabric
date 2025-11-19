package net.lerariemann.infinity.util.core;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.platform.Platform;
import net.minecraft.nbt.*;
import net.minecraft.util.Mth;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static net.lerariemann.infinity.InfinityMod.LOGGER;
import static net.lerariemann.infinity.InfinityMod.configPath;

public interface CommonIO {
    static void write(CompoundTag base, String path, String filename) {
        write(compoundToString(base), Paths.get(path), filename);
    }

    static void write(CompoundTag base, Path dir, String filename) {
        write(compoundToString(base), dir, filename);
    }

    static void write(String source, Path dir, String filename) {
        List<String> lines = Collections.singletonList(source);
        filename = filename.replace("/", "_").replace("\\", "_");
        try {
            Files.createDirectories(dir);
            Path file = dir.resolve(filename);
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void writeSurfaceRule(CompoundTag base, String path, String filename) {
        String source = compoundToString(base);
        for (int j: (new Integer[]{31, 62})) for (int i = -5; i < 6; i++) {
            String z = Integer.toString(j+i);
            source = source.replace("\"absolute\": "+z, "\"absolute\": "+(i==0 ? "%SL%" : "%SL" + (i>0 ? "+" : "") + i + "%"));
        }
        write(source, Paths.get(path), filename);
    }

    static int getVersion(File file) {
        return getStaistic(file, "infinity_version");
    }
    static int getAmendmentVersion(File file) {
        return getStaistic(file, "amendment_version");
    }

    static int getStaistic(File file, String statname) {
        String content;
        try {
            content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            if (!content.contains(statname)) return 0;
            int i = content.indexOf(statname);
            int end = content.indexOf(",", i);
            if (end == -1) {
                end = content.indexOf("\n", i);
            }
            return Integer.parseInt(content.substring(content.indexOf(" ", i)+1, end).trim());
        } catch (IOException e) {
            LOGGER.warn("No file found: {}", file);
            return 0;
        }
    }

    static CompoundTag read(String path) {
        return read(new File(path));
    }
    static CompoundTag read(Path path) {
        return read(path.toFile());
    }
    static CompoundTag read(File file) {
        String content;
        try {
            if (file.exists()) {
                content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                CompoundTag c = SaferStringReader.parse(content);
                c.remove("infinity_version");
                return c;
            }
            return new CompoundTag();
        } catch (IOException | CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    static CompoundTag readAndFormat(String path, Object... args) {
        File file = new File(path);
        try {
            String content = String.valueOf((new Formatter(Locale.US)).format(FileUtils.readFileToString(file, StandardCharsets.UTF_8), args));
            //? if >1.21.2 {
            CompoundTag c = TagParser.parseCompoundFully(content);
            //?} else {
            /*CompoundTag c = TagParser.parseTag(content);
            *///?}
            c.remove("infinity_version");
            return c;
        } catch (IOException | CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    static CompoundTag readAndAddCompound(String path, CompoundTag block) {
        return readAndFormat(path, compoundToString(block));
    }

    static CompoundTag readSurfaceRule(File file, int seaLevel) {
        try {
            String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            CompoundTag c = formatSurfaceRule(content, seaLevel);
            c.remove("infinity_version");
            return c;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static CompoundTag formatSurfaceRule(String content, int seaLevel) {
        int i = content.lastIndexOf("%");
        if (i == -1) {
            try {
                //? if >1.21.2 {
                return TagParser.parseCompoundFully(content);
                //?} else {
                /*return TagParser.parseTag(content);
                *///?}
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        else if (content.contains("%SL%")) return formatSurfaceRule(content.replace("%SL%", String.valueOf(seaLevel)), seaLevel);
        else {
            int j = content.lastIndexOf("%SL");
            String num = content.substring(j+3, i);
            return formatSurfaceRule(content.replace("%SL" + num + "%", String.valueOf(seaLevel + Integer.parseInt(num))), seaLevel);
        }
    }

    private static boolean _checkIfModLoaded(File path1) {
        if (!RandomProvider.rule("enforceModLoadedChecks")) return true;
        String modname = path1.toPath().getName(path1.toPath().getNameCount() - 1).toString();
        return Platform.isModLoaded(modname);
    }

    static List<CompoundTag> readCategory(ConfigType type) {
        Path path = configPath.resolve("modular");
        String fullname = type.getKey() + ".json";
        List<CompoundTag> compounds = new ArrayList<>();
        for (File modDir: Objects.requireNonNull(path.toFile().listFiles(File::isDirectory))) if (_checkIfModLoaded(modDir)) {
            File file = modDir.toPath().resolve(fullname).toFile();
            if (file.exists()) {
                CompoundTag base = read(file);
                NbtUtils.getList(base,"elements", Tag.TAG_COMPOUND).stream().map(e -> (CompoundTag)e).forEach(compounds::add);
            }
        }
        return compounds;
    }

    static String appendTabs(String parent, int t) {
        return parent + "\t".repeat(Math.max(0, t));
    }

    static String elementToString(Tag base, int t) {
        if (base == null) {
            return "!!NULL!!";
        } else if (base instanceof CompoundTag nbtCompound) {
            return compoundToString(nbtCompound, t + 1);
        } else if (base instanceof ListTag nbtElements) {
            return listToString(nbtElements, t + 1);
        //? if >1.21.2 {
        } else if (base instanceof ByteTag nbtByte) {
            return (nbtByte.byteValue() != 0) ? "true" : "false";
        } else if (base instanceof DoubleTag nbtDouble) {
            return String.valueOf(boundsCheck(nbtDouble.floatValue()));
        } else if (base instanceof FloatTag nbtFloat) {
            return String.valueOf(boundsCheck(nbtFloat.floatValue()));
        } else if (base instanceof LongTag nbtLong) {
            return String.valueOf(boundsCheck(nbtLong.longValue()));
        } else if (base instanceof IntTag nbtInt) {
            return String.valueOf(boundsCheck(nbtInt.intValue()));
        } else if (base instanceof ShortTag nbtShort) {
            return String.valueOf(boundsCheck(nbtShort.shortValue()));
        } else if (base instanceof StringTag nbtString) {
            return "\"" + nbtString.toString().replace("\"", "\\\"") + "\"";
        }
        //?} else {
        /*
        } else if (base instanceof ByteTag nbtByte) {
            return (nbtByte.getAsByte() != 0) ? "true" : "false";
        } else if (base instanceof DoubleTag nbtDouble) {
            return String.valueOf(boundsCheck(nbtDouble.getAsFloat()));
        } else if (base instanceof FloatTag nbtFloat) {
            return String.valueOf(boundsCheck(nbtFloat.getAsFloat()));
        } else if (base instanceof LongTag nbtLong) {
            return String.valueOf(boundsCheck(nbtLong.getAsLong()));
        } else if (base instanceof IntTag nbtInt) {
            return String.valueOf(boundsCheck(nbtInt.getAsInt()));
        } else if (base instanceof ShortTag nbtShort) {
            return String.valueOf(boundsCheck(nbtShort.getAsShort()));
        } else if (base instanceof StringTag nbtString) {
            return "\"" + nbtString.getAsString().replace("\"", "\\\"") + "\"";
        }
        *///?}
        return base.toString();
    }

    int maxBound = 30000000;
    static float boundsCheck(float base) {
        return Mth.clamp(base, -maxBound, maxBound);
    }
    static int boundsCheck(long base) {
        return (int) Mth.clamp(base, -maxBound, maxBound);
    }
    static int boundsCheck(int base) {
        return Mth.clamp(base, -maxBound, maxBound);
    }

    static String compoundToString(CompoundTag base) {
        return compoundToString(base, 0);
    }
    static String compoundToString(CompoundTag base, int t) {
        String res = "{\n";
        int i = base.size() - 1;
        for (String key: base.
                //? if <1.21.2 {
                /*getAllKeys()
                *///?} else {
                keySet()
                //?}
        ) {
            Tag elem = base.get(key);
            res = appendTabs(res, t+1) + "\"" + key + "\": " + elementToString(elem, t);
            if (i!=0) res += ",";
            res += "\n";
            i--;
        }
        res = appendTabs(res, t) + "}";
        return res;
    }
    static String listToString(ListTag base, int t) {
        String res = "[\n";
        for (int i=0; i<base.size(); i++) {
            Tag elem = base.get(i);
            res = appendTabs(res, t+1) + elementToString(elem, t);
            if (i!=(base.size()-1)) res += ",";
            res += "\n";
        }
        res = appendTabs(res, t) + "]";
        return res;
    }
}

