package net.codexarchonic.infinity.util.loading;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/** Migrates removed aquatic features to the 26.3 placement and feature codecs. */
final class PlacedFeatureData {
    static void upgrade(JsonObject placed) {
        // These wrappers are invoked at the position already scattered and height-mapped by
        // RandomVegetation. A second count_on_every_layer scatters another 0..15 blocks,
        // allowing large trees to read two chunks away from the generating chunk.
        if (isTreeWrapper(placed) && placed.has("placement") && placed.get("placement").isJsonArray()) {
            for (JsonElement entry : placed.getAsJsonArray("placement")) {
                if (entry.isJsonObject()) {
                    JsonObject modifier = entry.getAsJsonObject();
                    if (modifier.has("type") && (modifier.get("type").getAsString().equals("minecraft:count_on_every_layer")
                            || modifier.get("type").getAsString().equals("count_on_every_layer"))) {
                        modifier.addProperty("type", "minecraft:count");
                    }
                }
            }
        }
        if (!placed.has("placement") || !placed.has("feature") || !placed.get("feature").isJsonObject()) return;
        JsonObject feature = placed.getAsJsonObject("feature");
        String type = feature.has("type") ? feature.get("type").getAsString() : "";
        JsonArray placement = placed.getAsJsonArray("placement");
        switch (type) {
            case "sea_pickle", "minecraft:sea_pickle" -> {
                JsonObject count = object("{\"type\":\"minecraft:count\"}");
                count.add("count", feature.get("count"));
                placement.add(count);
                addUnderwaterPlacement(placement);
                placed.add("feature", object("""
                        {"type":"minecraft:simple_block","to_place":{
                          "type":"minecraft:randomized_int","property":"pickles",
                          "source":{"id":"minecraft:sea_pickle","properties":{"waterlogged":"true"}},
                          "values":{"type":"minecraft:uniform","min_inclusive":1,"max_inclusive":4}}}
                        """));
            }
            case "seagrass", "minecraft:seagrass" -> {
                JsonObject selector = object("{\"type\":\"minecraft:random_selector\"}");
                selector.add("default", placedBlock("minecraft:seagrass"));
                JsonObject tall = object("{\"chance\":0}");
                tall.add("chance", feature.get("probability"));
                JsonObject tallPlaced = placedBlock("minecraft:tall_seagrass");
                tallPlaced.getAsJsonArray("placement").add(object("""
                        {"type":"minecraft:block_predicate_filter","predicate":{
                          "type":"minecraft:matching_blocks","blocks":"minecraft:water","offset":[0,1,0]}}
                        """));
                tall.add("feature", tallPlaced);
                JsonArray features = new JsonArray();
                features.add(tall);
                selector.add("features", features);
                placed.add("feature", selector);
                addUnderwaterPlacement(placement);
            }
            case "random_patch", "minecraft:random_patch", "flower", "minecraft:flower" -> {
                JsonObject inner = feature.getAsJsonObject("feature");
                placed.add("feature", inner.get("feature"));
                JsonObject count = object("{\"type\":\"minecraft:count\"}");
                count.add("count", feature.get("tries"));
                placement.add(count);
                JsonObject offset = object("{\"type\":\"minecraft:offset\"}");
                offset.add("x", spread(feature.get("xz_spread").getAsInt()));
                offset.add("z", spread(feature.get("xz_spread").getAsInt()));
                offset.add("y", spread(feature.get("y_spread").getAsInt()));
                placement.add(offset);
                if (inner.has("placement")) inner.getAsJsonArray("placement").forEach(placement::add);
            }
            default -> { }
        }
    }

    private static boolean isTreeWrapper(JsonObject placed) {
        if (!placed.has("feature")) return false;
        JsonElement feature = placed.get("feature");
        if (feature.isJsonPrimitive()) return true; // Named trees copied by CorePack.treePlacement.
        if (!feature.isJsonObject() || !feature.getAsJsonObject().has("type")) return false;
        String type = feature.getAsJsonObject().get("type").getAsString().replace("minecraft:", "");
        return type.equals("tree") || type.equals("huge_fungus")
                || type.equals("infinity:random_flat_mushroom") || type.equals("infinity:random_round_mushroom")
                || type.equals("huge_brown_mushroom") || type.equals("huge_red_mushroom");
    }

    private static JsonObject spread(int radius) {
        JsonObject provider = object("{\"type\":\"minecraft:trapezoid\",\"plateau\":0}");
        provider.addProperty("min", -radius);
        provider.addProperty("max", radius);
        return provider;
    }

    private static JsonObject placedBlock(String block) {
        JsonObject result = object("{\"feature\":{\"type\":\"minecraft:simple_block\"},\"placement\":[]}");
        JsonObject state = new JsonObject();
        state.addProperty("id", block);
        result.getAsJsonObject("feature").add("to_place", state);
        return result;
    }

    private static void addUnderwaterPlacement(JsonArray placement) {
        placement.add(object("""
                {"type":"minecraft:offset",
                  "x":{"type":"minecraft:trapezoid","min":-7,"max":7,"plateau":0},
                  "y":0,
                  "z":{"type":"minecraft:trapezoid","min":-7,"max":7,"plateau":0}}
                """));
        placement.add(object("{\"type\":\"minecraft:heightmap\",\"heightmap\":\"OCEAN_FLOOR_WG\"}"));
        placement.add(object("""
                {"type":"minecraft:block_predicate_filter","predicate":{
                  "type":"minecraft:matching_blocks","blocks":"minecraft:water"}}
                """));
    }

    private static JsonObject object(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }
}
