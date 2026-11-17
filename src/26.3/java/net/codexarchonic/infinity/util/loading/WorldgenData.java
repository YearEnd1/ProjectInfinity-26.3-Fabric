package net.codexarchonic.infinity.util.loading;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.codexarchonic.infinity.util.core.CommonIO;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;

/** Adapts legacy generated dimensions and user configuration at the decoding boundary. */
public final class WorldgenData {
    public static CompoundTag upgrade(CompoundTag input) {
        String json = CommonIO.compoundToString(input);
        try {
            return TagParser.parseCompoundFully(
                    upgrade(JsonParser.parseString(json)).toString());
        } catch (CommandSyntaxException e) {
            throw new IllegalArgumentException("Invalid generated worldgen data", e);
        }
    }
    public static JsonElement upgrade(JsonElement input) {
        return upgrade(input, 4, 8);
    }

    private static JsonElement upgrade(JsonElement input, int cellSizeXZ, int cellSizeY) {
        if (input.isJsonArray()) {
            JsonArray result = new JsonArray();
            input.getAsJsonArray().forEach(value -> result.add(upgrade(value, cellSizeXZ, cellSizeY)));
            return result;
        }
        if (!input.isJsonObject()) return input;
        JsonObject source = input.getAsJsonObject();
        JsonObject result = new JsonObject();
                int horizontal = cellSizeXZ;
        int vertical = cellSizeY;
        if (source.has("noise_router") && source.has("noise") && source.get("noise").isJsonObject()) {
            JsonObject noise = source.getAsJsonObject("noise");
            if (noise.has("size_horizontal")) horizontal = 4 * noise.get("size_horizontal").getAsInt();
            if (noise.has("size_vertical")) vertical = 4 * noise.get("size_vertical").getAsInt();
        }
        for (var entry : source.entrySet()) {
            result.add(entry.getKey(), upgrade(entry.getValue(), horizontal, vertical));
        }
        if (result.size() == 2 && result.has("type") && result.has("config") && result.get("config").isJsonObject()) {
            JsonObject config = result.remove("config").getAsJsonObject();
            config.entrySet().forEach(entry -> result.add(entry.getKey(), entry.getValue()));
        }
        if (result.has("firstOctave") && result.has("amplitudes")) {
            JsonArray values = result.getAsJsonArray("amplitudes");
            double[] amplitudes = new double[values.size()];
            for (int index = 0; index < values.size(); index++) amplitudes[index] = values.get(index).getAsDouble();
            var noise = net.minecraft.world.level.levelgen.synth.NormalNoise.createParity(result.get("firstOctave").getAsInt(), amplitudes);
            return net.minecraft.world.level.levelgen.synth.NormalNoise.DIRECT_CODEC.encodeStart(com.mojang.serialization.JsonOps.INSTANCE, noise).getOrThrow();
        }
        if (result.has("Name") && !result.has("id")) {
            result.add("id", result.remove("Name"));
            if (result.has("Properties")) result.add("properties", result.remove("Properties"));
        }
        if (result.has("properties") && result.get("properties").isJsonObject()) {
            JsonObject properties = result.getAsJsonObject("properties");
            for (var entry : properties.entrySet()) {
                if (entry.getValue().isJsonPrimitive()) entry.setValue(new com.google.gson.JsonPrimitive(entry.getValue().getAsString()));
            }
        }
        String legacyType = result.has("type") && result.get("type").isJsonPrimitive()
                ? result.get("type").getAsString().replaceFirst("^minecraft:", "") : "";
        if (legacyType.equals("weird_scaled_sampler")) return rarityScaledNoise(result);
        if (legacyType.equals("simple_state_provider") && result.has("state")) return result.get("state");
        String provider = switch (legacyType) {
            case "weighted_state_provider" -> "weighted";
            case "rotated_block_provider" -> "rotated";
            case "noise_provider" -> "noise";
            case "dual_noise_provider" -> "dual_noise";
            case "noise_threshold_provider" -> "noise_threshold";
            case "randomized_int_state_provider" -> "randomized_int";
            default -> null;
        };
        if (provider != null) result.addProperty("type", "minecraft:" + provider);
        if (result.has("argument1") && result.has("argument2")) {
            result.add("left", result.remove("argument1"));
            result.add("right", result.remove("argument2"));
        }
        switch (legacyType) {
            case "abs", "square", "cube", "half_negative", "quarter_negative", "squeeze",
                 "blend_density", "interpolated" -> {
                if (result.has("argument")) result.add("input", result.remove("argument"));
            }
            case "cache_2d", "cache_once", "cache_all_in_cell", "flat_cache" -> {
                result.addProperty("type", "minecraft:cache");
                if (result.has("argument")) result.add("input", result.remove("argument"));
            }
            case "end_islands" -> result.addProperty("type", "minecraft:end_outer_islands");
            case "forest_rock" -> {
                result.addProperty("type", "minecraft:block_blob");
                result.add("can_place_on", JsonParser.parseString("{\"type\":\"minecraft:matching_block_tag\",\"tag\":\"minecraft:forest_rock_can_place_on\"}"));
            }
            case "y_clamped_gradient" -> {
                result.addProperty("type", "minecraft:gradient");
                result.addProperty("axis", "y");
                result.add("from_coordinate", result.remove("from_y"));
                result.add("to_coordinate", result.remove("to_y"));
            }
            case "shifted_noise" -> result.addProperty("type", "minecraft:noise");
            default -> { }
        }
        if (legacyType.equals("interpolated")) {
            if (!result.has("cell_size_xz")) result.addProperty("cell_size_xz", cellSizeXZ);
            if (!result.has("cell_size_y")) result.addProperty("cell_size_y", cellSizeY);
        }
        if (result.has("type") && result.has("weight") && result.has("minCount") && result.has("maxCount")) {
            int minimum = result.get("minCount").getAsInt();
            int maximum = result.get("maxCount").getAsInt();
            if (!result.has("count")) {
                if (minimum == maximum) result.addProperty("count", minimum);
                else {
                    JsonObject count = new JsonObject();
                    count.addProperty("type", "minecraft:uniform");
                    count.addProperty("min_inclusive", minimum);
                    count.addProperty("max_inclusive", maximum);
                    result.add("count", count);
                }
            }
            result.remove("minCount");
            result.remove("maxCount");
        }
        if (result.has("spawners") && result.has("temperature")) {
            JsonObject attributes = result.has("attributes") ? result.getAsJsonObject("attributes") : new JsonObject();
            String naturalSpawns = "minecraft:gameplay/natural_mob_spawns";
            if (!attributes.has(naturalSpawns)) {
                JsonObject argument = new JsonObject();
                argument.add("spawns_by_category", result.get("spawners"));
                if (result.has("spawn_costs")) argument.add("spawn_costs", result.get("spawn_costs"));
                JsonObject overlay = new JsonObject();
                overlay.addProperty("modifier", "overlay");
                overlay.add("argument", argument);
                attributes.add(naturalSpawns, overlay);
            }
            result.add("attributes", attributes);
            result.remove("spawners");
            result.remove("spawn_costs");
        }
        if (result.has("noise_router") && result.get("noise_router").isJsonObject()) {
            if (!result.has("material_rule") && result.has("surface_rule")) {
                result.add("material_rule", result.remove("surface_rule"));
            }
            JsonObject router = result.getAsJsonObject("noise_router");
            if (result.has("spawn_target") && result.get("spawn_target").isJsonArray()) {
                JsonArray targets = new JsonArray();
                for (JsonElement entry : result.getAsJsonArray("spawn_target")) {
                    JsonObject target = entry.getAsJsonObject();
                    if (!target.has("temperature") && !target.has("continentalness")) {
                        targets.add(target);
                        continue;
                    }
                    JsonObject migrated = new JsonObject();
                    addSpawnTarget(migrated, target, router, "temperature", "temperature", "temperature");
                    addSpawnTarget(migrated, target, router, "humidity", "vegetation", "vegetation");
                    addSpawnTarget(migrated, target, router, "continentalness", "continents", "continents");
                    addSpawnTarget(migrated, target, router, "erosion", "erosion", "erosion");
                    addSpawnTarget(migrated, target, router, "weirdness", "ridges", "ridges");
                    targets.add(migrated);
                }
                result.add("spawn_target", targets);
            }
            if (!router.has("chunk_surface_level")) {
                // Legacy settings did not store a surface-height function. The nether's
                // constant fallback also supports fully solid, custom-material worlds.
                router.addProperty("chunk_surface_level", 0.0);
            }
        }
        String type = result.has("type") && result.get("type").isJsonPrimitive()
                ? result.get("type").getAsString() : "";
        if (type.equals("lake") || type.equals("minecraft:lake")) {
            if (!result.has("can_place_feature")) {
                result.add("can_place_feature", JsonParser.parseString("{\"type\":\"minecraft:true\"}"));
            }
            if (!result.has("can_replace_with_air_or_fluid")) {
                result.add("can_replace_with_air_or_fluid", JsonParser.parseString(
                        "{\"type\":\"minecraft:not\",\"predicate\":{\"type\":\"minecraft:matching_block_tag\",\"tag\":\"minecraft:features_cannot_replace\"}}"));
            }
            if (!result.has("can_replace_with_barrier")) {
                result.add("can_replace_with_barrier", JsonParser.parseString(
                        "{\"type\":\"minecraft:not\",\"predicate\":{\"type\":\"minecraft:matching_block_tag\",\"tag\":\"minecraft:lava_pool_stone_cannot_replace\"}}"));
            }
        }
        if (type.equals("delta_feature") || type.equals("minecraft:delta_feature")) {
            // Size plus rim displacement must fit inside the neighboring chunk radius.
            capDeltaSize(result, "size");
            capDeltaSize(result, "rim_size");
        }
        EnvironmentData.upgrade(result);
        PlacedFeatureData.upgrade(result);
        return result;
    }
    private static void capDeltaSize(JsonObject feature, String field) {
        if (!feature.has(field)) return;
        JsonElement provider = feature.get(field);
        if (provider.isJsonPrimitive()) {
            feature.addProperty(field, Math.clamp(provider.getAsInt(), 0, 8));
            return;
        }
        JsonObject object = provider.getAsJsonObject();
        if (object.has("min_inclusive") && object.has("max_inclusive")
                && object.get("min_inclusive").getAsInt() >= 0 && object.get("max_inclusive").getAsInt() <= 8) return;
        JsonObject clamped = new JsonObject();
        clamped.addProperty("type", "minecraft:clamped");
        clamped.addProperty("min_inclusive", 0);
        clamped.addProperty("max_inclusive", 8);
        clamped.add("source", provider);
        feature.add(field, clamped);
    }
    private static void addSpawnTarget(JsonObject output, JsonObject target, JsonObject router,
                                       String legacyKey, String routerKey, String fallbackKey) {
        if (!target.has(legacyKey)) return;
        String density = "minecraft:overworld/" + fallbackKey;
        if (router.has(routerKey) && router.get(routerKey).isJsonPrimitive()
                && router.get(routerKey).getAsJsonPrimitive().isString()) {
            density = router.get(routerKey).getAsString();
        }
        output.add(density, target.get(legacyKey));
    }
    private static JsonObject rarityScaledNoise(JsonObject source) {
        boolean caves2d = source.get("rarity_value_mapper").getAsString().equals("type_2");
        double[] boundaries = caves2d ? new double[]{-0.75, -0.5, 0.5, 0.75} : new double[]{-0.5, 0, 0.5};
        double[] rarities = caves2d ? new double[]{0.5, 0.75, 1, 2, 3} : new double[]{0.75, 1, 1.5, 2};
        JsonObject selector = new JsonObject();
        selector.addProperty("type", "minecraft:interval_select");
        selector.add("input", source.get("input"));
        JsonArray thresholds = new JsonArray();
        for (double boundary : boundaries) thresholds.add(boundary);
        selector.add("thresholds", thresholds);
        JsonArray functions = new JsonArray();
        for (double rarity : rarities) {
            JsonObject noise = new JsonObject();
            noise.addProperty("type", "minecraft:noise");
            noise.add("noise", source.get("noise"));
            noise.addProperty("xz_scale", 1 / rarity);
            noise.addProperty("y_scale", 1 / rarity);
            JsonObject absolute = new JsonObject();
            absolute.addProperty("type", "minecraft:abs");
            absolute.add("input", noise);
            JsonObject scaled = new JsonObject();
            scaled.addProperty("type", "minecraft:mul");
            scaled.add("left", absolute);
            scaled.addProperty("right", rarity);
            functions.add(scaled);
        }
        selector.add("functions", functions);
        return selector;
    }}
