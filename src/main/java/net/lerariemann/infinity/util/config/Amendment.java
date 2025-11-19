package net.lerariemann.infinity.util.config;

import dev.architectury.platform.Platform;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.CommonIO;
import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public record Amendment(ConfigType area, ModSelector modSelector, Selector selector, Results results) {
    public static Amendment of(CompoundTag data) {
        String areaName = NbtUtils.getString(data, "area", "");
        ConfigType area = ConfigType.byName(areaName);
        if (area == null) {
            InfinityMod.LOGGER.warn("Unknown amendment area: {}", areaName);
            return null;
        }

        String mod = NbtUtils.getString(data, "mod", "");
        ModSelector modSelector;
        if (mod.equals("all")) {
            modSelector = new UniversalModSelector();
        }
        else if (!Platform.isModLoaded(mod)) return null;
        else modSelector = new MatchingModSelector(mod);

        String selectorType = NbtUtils.getString(data,"selector", "");
        Selector selector = switch(selectorType) {
            case "all" -> new UniversalSelector();
            case "containing" -> new ContainingSelector(NbtUtils.getString(data, "containing"));
            case "matching" -> new MatchingSelector(NbtUtils.getString(data, "matching"));
            case "matching_block_tag" -> new MatchingBlockTagSelector(NbtUtils.getString(data, "matching_block_tag"));
            case "matching_any" -> new MatchingAnySelector(NbtUtils.getList(data, "matching_any", Tag.TAG_STRING)
                    .stream().map(e->(StringTag)e).map(NbtUtils::getAsString).toList());
            default -> {
                InfinityMod.LOGGER.warn("Unknown amendment selector type: {}", selectorType);
                yield null;
            }
        };

        String resultType = NbtUtils.getString(data, "results", "");
        Results results = switch (resultType) {
            case "set_value" -> new SetValue(NbtUtils.getInt(data, "value"));
            case "erase" -> new SetValue(0);
            case "set_field" -> new SetField(NbtUtils.getString(data, "field_name"), data.get("field"));
            default -> {
                InfinityMod.LOGGER.warn("Unknown amendment result type: {}", resultType);
                yield null;
            }
        };

        if (selector == null || results == null) return null;
        return new Amendment(area, modSelector, selector, results);
    }

    public static Map<ConfigType, List<Amendment>> getAmendmentList() {
        Map<ConfigType, List<Amendment>> data = new HashMap<>();
        CompoundTag rawData = CommonIO.read(InfinityMod.amendmentPath);
        AtomicInteger i = new AtomicInteger();
        for (Tag e : NbtUtils.getList(rawData,"elements", Tag.TAG_COMPOUND)) {
            Amendment amd = Amendment.of((CompoundTag)e);
            if (amd == null) continue;
            i.getAndIncrement();
            ConfigType type = amd.area;
            if (!data.containsKey(type)) data.put(type, new ArrayList<>());
            data.get(type).add(amd);
        }
        InfinityMod.LOGGER.info("Registered {} valid amendments", i.get());
        return data;
    }

    public boolean applies(String modId, String key) {
        return modSelector.applies(modId) && selector.applies(key);
    }
    public double apply() {
        return results.apply();
    }
    public CompoundTag apply(CompoundTag data) {
        return results.apply(data);
    }

    public interface ModSelector {
        boolean applies(String modId);
    }
    public static class UniversalModSelector implements ModSelector {
        @Override
        public boolean applies(String modId) {
            return true;
        }
    }
    public record MatchingModSelector(String mod) implements ModSelector {
        @Override
        public boolean applies(String modId) {
            return modId.equals(this.mod);
        }
    }

    public interface Selector {
        boolean applies(String key);
    }
    public static class UniversalSelector implements Selector {
        public boolean applies(String key) {
            return true;
        }
    }
    public record MatchingSelector(String key) implements Selector {
        public boolean applies(String key) {
            return key.equals(this.key);
        }
    }
    public record MatchingBlockTagSelector(TagKey<Block> tag) implements Selector {
        public MatchingBlockTagSelector(String key) {
            this(TagKey.create(Registries.BLOCK, VersionMethods.id(key)));
        }

        public boolean applies(String key) {
            Block b = VersionMethods.getBlock((key));
            if (b != null) {
                return b.defaultBlockState().is(this.tag);
            }
            return false;
        }
    }
    public record MatchingAnySelector(List<String> lst) implements Selector {
        public boolean applies(String key) {
            return this.lst.contains(key);
        }
    }
    public record ContainingSelector(String key) implements Selector {
        public boolean applies(String key) {
            return key.contains(this.key);
        }
    }

    public interface Results{
        default double apply() {
            return 1.0;
        }
        default CompoundTag apply(CompoundTag data) {
            return data;
        }
    }
    public record SetValue(double value) implements Results {
        @Override
        public double apply() {
            return value;
        }
    }
    public record SetField(String key, Tag value) implements Results {
        @Override
        public CompoundTag apply(CompoundTag data) {
            if (data == null) data = new CompoundTag();
            data.put(key, value);
            return data;
        }
    }
}
