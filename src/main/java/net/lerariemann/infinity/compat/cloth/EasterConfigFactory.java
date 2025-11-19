package net.lerariemann.infinity.compat.cloth;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.CommonIO;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import static net.lerariemann.infinity.compat.cloth.ClothConfigFactory.*;


public class EasterConfigFactory {
    public static void build(ConfigBuilder builder, JsonObject infinityJson) {
        ConfigCategory easterCategory = builder.getOrCreateCategory(Component.translatable("config.infinity.title.easter"));
        JsonArray currentConfig = infinityJson.getAsJsonArray("disabledDimensions");
        JsonArray defaultConfig = readJson(configPath()+("/.infinity-default.json")).getAsJsonObject().getAsJsonArray("disabledDimensions");
        Stream<String> sorted = InfinityMod.provider.easterizer.map.keySet().stream().sorted();
        for (String name : sorted.toList()) {
            var defaultsDisabled = isEasterEggDisabled(defaultConfig, name);
            easterCategory.addEntry(builder.entryBuilder().startBooleanToggle(
                            Component.nullToEmpty(InfinityMethods.formatAsTitleCase(name)), isEasterEggDisabled(currentConfig, name))
                    .setSaveConsumer(newValue -> easterSetter(name, newValue))
                    .setTooltip(easterTooltip(defaultsDisabled))
                    .setDefaultValue(defaultsDisabled).build());
        }
    }

    static void easterSetter(String name, boolean newValue) {
        CompoundTag rootConfig = readRootConfigNbt();
        // If a dimension should be enabled...
        if (newValue) {
            // and it is currently disabled (in the list of disabled dimensions)
            if (NbtUtils.getList(rootConfig, "disabledDimensions", Tag.TAG_STRING).contains(StringTag.valueOf(name))) {
                // remove it from the list of disabled dimensions.
                NbtUtils.getList(rootConfig,"disabledDimensions", Tag.TAG_STRING).remove(StringTag.valueOf(name));

                CommonIO.write(rootConfig, configPath(), "infinity.json");
            }
        }
        // If a dimension should be disabled...
        else {
            if (!NbtUtils.getList(rootConfig, "disabledDimensions", Tag.TAG_STRING).contains(StringTag.valueOf(name))) {
                // remove it from the list of disabled dimensions.
                NbtUtils.getList(rootConfig, "disabledDimensions", Tag.TAG_STRING).add(StringTag.valueOf(name));

                CommonIO.write(rootConfig, configPath(), "infinity.json");
            }
        }
    }

    static boolean isEasterEggDisabled(JsonArray array, String name) {
        boolean result = false;
        for (JsonElement disabled : array) {
            if (Objects.equals(disabled.getAsString(), name)) {
                result = true;
            }
        }
        return !result;
    }

    static Optional<Component[]> easterTooltip(boolean enabled) {
        if (!enabled)
            return Optional.of(createTooltip("config.infinity.easter.disabled.description").toArray(new Component[0]));
        else return Optional.empty();
    }
}
