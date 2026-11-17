package net.codexarchonic.infinity.util.loading;

import com.google.gson.JsonObject;
import java.util.Locale;

/** Converts pre-26.3 visual settings into environment attributes and world-clock references. */
public final class EnvironmentData {
    public static void upgrade(JsonObject data) {
        if (data.has("infiniburn") && data.has("height") && data.has("min_y")
                && (data.has("effects") || (!data.has("timelines") && !data.has("default_clock")))) dimension(data);
        if (data.has("temperature") && data.has("effects") && data.get("effects").isJsonObject()) {
            JsonObject attributes = attributes(data), effects = data.getAsJsonObject("effects");
            color(attributes, effects, "sky_color", "minecraft:visual/sky_color");
            color(attributes, effects, "fog_color", "minecraft:visual/fog_color");
            color(attributes, effects, "water_fog_color", "minecraft:visual/water_fog_color");
        }
    }

    private static void dimension(JsonObject data) {
        String effects = data.has("effects") ? data.get("effects").getAsString() : "minecraft:overworld";
        boolean nether = effects.equals("minecraft:the_nether"), end = effects.equals("minecraft:the_end");
        JsonObject attributes = attributes(data);
        if (!data.has("skybox")) data.addProperty("skybox", nether ? "none" : end ? "end" : "overworld");
        if (!data.has("default_clock")) data.addProperty("default_clock", "minecraft:overworld");
        if (nether) {
            put(attributes, "minecraft:visual/sky_light_factor", 0.0);
            put(attributes, "minecraft:visual/sky_light_color", "#7a7aff");
            if (!data.has("cardinal_light")) data.addProperty("cardinal_light", "nether");
        } else if (end) {
            put(attributes, "minecraft:visual/sky_color", "#000000");
            put(attributes, "minecraft:visual/fog_color", "#181318");
            put(attributes, "minecraft:visual/sky_light_factor", 0.0);
            put(attributes, "minecraft:visual/sky_light_color", "#ac60cd");
        } else {
            put(attributes, "minecraft:visual/sky_color", "#78a7ff");
            put(attributes, "minecraft:visual/fog_color", "#c0d8ff");
            put(attributes, "minecraft:visual/cloud_color", "#ccffffff");
            put(attributes, "minecraft:visual/cloud_height", 192.33);
        }
        if (data.has("fixed_time")) {
            // Keep deliberately frozen dimensions frozen. Their old fixed_time field is no longer decoded.
            if (!data.has("has_fixed_time")) data.addProperty("has_fixed_time", true);
            if (!data.has("timelines")) data.addProperty("timelines", "#minecraft:universal");
            double phase = Math.floorMod(data.get("fixed_time").getAsLong(), 24000) / 24000.0 - 0.25;
            phase -= Math.floor(phase);
            double angle = (2 * phase + (1 - Math.cos(phase * Math.PI)) / 2) / 3 * 360;
            put(attributes, "minecraft:visual/sun_angle", angle);
            put(attributes, "minecraft:visual/moon_angle", angle);
            put(attributes, "minecraft:visual/star_angle", angle);
            if (!nether && !end) {
                double daylight = Math.clamp(Math.cos(Math.toRadians(angle)) * 2 + 0.5, 0.24, 1.0);
                put(attributes, "minecraft:visual/sky_light_factor", daylight);
                put(attributes, "minecraft:gameplay/sky_light_level", daylight);
                put(attributes, "minecraft:visual/star_brightness", (1 - daylight) * 0.65);
            }
        } else if (!data.has("timelines")) {
            data.addProperty("timelines", nether ? "#minecraft:in_nether" : end ? "#minecraft:in_end" : "#minecraft:in_overworld");
        }
    }

    private static JsonObject attributes(JsonObject data) {
        if (!data.has("attributes")) data.add("attributes", new JsonObject());
        return data.getAsJsonObject("attributes");
    }
    private static void color(JsonObject target, JsonObject source, String old, String key) {
        if (source.has(old) && !target.has(key))
            target.addProperty(key, String.format(Locale.ROOT, "#%06x", source.get(old).getAsInt() & 0xffffff));
    }
    private static void put(JsonObject target, String key, String value) {
        if (!target.has(key)) target.addProperty(key, value);
    }
    private static void put(JsonObject target, String key, double value) {
        if (!target.has(key)) target.addProperty(key, value);
    }
}
