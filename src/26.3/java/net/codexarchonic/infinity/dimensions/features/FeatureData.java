package net.codexarchonic.infinity.dimensions.features;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

/** Converts block states and providers emitted by shared dimension generators. */
final class FeatureData {
    static CompoundTag create(String type, CompoundTag config) {
        CompoundTag feature = config.copy();
        feature.putString("type", type);
        return convertCompound(feature);
    }

    private static Tag convert(Tag tag) {
        if (tag instanceof CompoundTag compound) return convertCompound(compound);
        if (tag instanceof ListTag list) {
            ListTag result = new ListTag();
            list.forEach(entry -> result.add(convert(entry)));
            return result;
        }
        return tag;
    }

    private static CompoundTag convertCompound(CompoundTag source) {
        CompoundTag result = new CompoundTag();
        if (source.contains("Name")) {
            result.put("id", source.get("Name"));
            if (source.contains("Properties")) result.put("properties", source.get("Properties").copy());
            return result;
        }
        String type = source.getStringOr("type", "");
        String vanilla = type.startsWith("minecraft:") ? type.substring(10) : type;
        if (vanilla.equals("simple_state_provider") && source.get("state") instanceof CompoundTag state) {
            return convertCompound(state);
        }
        for (String key : source.keySet()) {
            if (vanilla.equals("tree") && key.equals("force_dirt")) continue;
            String target = vanilla.equals("tree") && key.equals("dirt_provider") ? "below_trunk_provider" : key;
            result.put(target, convert(source.get(key)));
        }
        String provider = switch (vanilla) {
            case "weighted_state_provider" -> "weighted";
            case "rotated_block_provider" -> "rotated";
            case "noise_provider" -> "noise";
            case "dual_noise_provider" -> "dual_noise";
            case "noise_threshold_provider" -> "noise_threshold";
            case "randomized_int_state_provider" -> "randomized_int";
            default -> null;
        };
        if (provider != null) result.putString("type", "minecraft:" + provider);
        return result;
    }
}
