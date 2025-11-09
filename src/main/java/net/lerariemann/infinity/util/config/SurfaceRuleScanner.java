package net.lerariemann.infinity.util.config;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.dimensions.RandomNoisePreset;
import net.lerariemann.infinity.util.core.CommonIO;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface SurfaceRuleScanner {
    static void scan(MinecraftServer server) {
        Map<String, CompoundTag> map = new HashMap<>();
        Registry<NoiseGeneratorSettings> registry = server.registryAccess().registryOrThrow(Registries.NOISE_SETTINGS);
        registry.registryKeySet().forEach(key -> {
            if (!key.location().getNamespace().contains("infinity")) {
                Optional<NoiseGeneratorSettings> o = registry.getOptional(key);
                o.ifPresent(settings -> {
                    Optional<Tag> c = NoiseGeneratorSettings.DIRECT_CODEC.encodeStart(NbtOps.INSTANCE, settings).result();
                    c.ifPresent(e -> {
                        Tree t = new Tree(NbtUtils.getCompound(((CompoundTag) e), "surface_rule"));
                        t.biomeLocations.keySet().forEach(biome -> {
                            if (!map.containsKey(biome)) map.put(biome, t.wrappedRule(biome));
                        });
                    });
                });
            }
        });
        map.forEach((biome, value) -> {
            String biomename = biome.substring(biome.lastIndexOf(":") + 1) + ".json";
            String modname = biome.substring(0, biome.lastIndexOf(":"));
            String path = "config/infinity/modular/" + modname + "/surface_rule";
            CommonIO.writeSurfaceRule(value, path, biomename);
        });
        DataCollection.loggerOutput(map.size(), "surface rules");
    }

    class Tree{
        ArrayList<TreeLeaf> registry;
        HashMap<String, ArrayList<Integer>> biomeLocations;

        Tree(CompoundTag surfaceRule) {
            registry = new ArrayList<>();
            biomeLocations = new HashMap<>();
            TreeLeaf root = new TreeLeaf(new CompoundTag(), -1, null, false);
            add(surfaceRule, root);
        }

        static CompoundTag conditionCase(CompoundTag if_true, CompoundTag then_run) {
            CompoundTag c = new CompoundTag();
            c.put("if_true", if_true);
            c.put("then_run", then_run);
            c.putString("type", "minecraft:condition");
            return c;
        }

        void addBiomeLoc(String s, Integer i) {
            if (!biomeLocations.containsKey(s)) biomeLocations.put(s, new ArrayList<>());
            biomeLocations.get(s).add(i);
        }

        TreeLeaf addOfRule(CompoundTag rule, TreeLeaf where, boolean terminal) {
            TreeLeaf l = new TreeLeaf(rule, registry.size(), where, terminal);
            registry.add(l);
            return l;
        }

        void add(CompoundTag rule, TreeLeaf where) {
            switch(NbtUtils.getString(rule,"type", "")) {
                case "condition", "minecraft:condition" -> {
                    CompoundTag next = NbtUtils.getCompound(rule,"then_run");
                    CompoundTag c = NbtUtils.getCompound(rule, "if_true");
                    if (NbtUtils.getString(c, "type").contains("above_preliminary_surface")) {
                        add(next, where);
                    }
                    else if (!NbtUtils.getString(c, "type").contains("biome")) {
                        TreeLeaf l = addOfRule(c, where, false);
                        add(next, l);
                    }
                    else {
                        TreeLeaf l = addOfRule(next, where, true);
                        if(Objects.requireNonNull(c.get("biome_is")).getType().equals(ListTag.TYPE)) {
                            NbtUtils.getList(c, "biome_is", Tag.TAG_STRING).forEach(e -> addBiomeLoc(e.getAsString(), l.i));
                        }
                    }
                }
                case "sequence", "minecraft:sequence" -> {
                    ListTag sq = NbtUtils.getList(rule, "sequence", Tag.TAG_COMPOUND);
                    sq.forEach(e -> add((CompoundTag)e, where));
                }
                default -> {
                    if (!checkUnneededParts(rule)) {
                        TreeLeaf l = addOfRule(rule, where, true);
                        addBiomeLoc("minecraft:default", l.i);
                    }
                }
            }
        }

        static boolean checkUnneededParts(CompoundTag rule) {
            return NbtUtils.getString(rule, "type", "").contains("block")
                    && (rule.toString().contains("minecraft:bedrock")
                    || rule.toString().contains("minecraft:deepslate"));
        }

        public CompoundTag wrappedRule(String biome) {
            CompoundTag c = new CompoundTag();
            ListTag l = new ListTag();
            l.add(StringTag.valueOf(biome));
            c.put("biomes", l);
            c.put("rule", extractRule(biome));
            return c;
        }

        public CompoundTag extractRule(String biome) {
            if (!biomeLocations.containsKey(biome)) return null;
            else {
                CompoundTag comp = RandomNoisePreset.startingRule("sequence");
                ListTag l = new ListTag();
                try {
                    biomeLocations.get(biome).forEach(i -> l.add(extractRule(i)));
                    if (biomeLocations.containsKey("minecraft:default"))
                        biomeLocations.get("minecraft:default").forEach(i -> l.add(extractRule(i)));
                    else {
                        InfinityMod.LOGGER.warn("Default locations unexpectedly missing when processing surface rules for biome {}", biome);
                    }
                    comp.put("sequence", l);
                } catch (Exception e) {
                    throw new RuntimeException("Encountered an unexpected exception when processing surface rules for biome " + biome + "\n" + e.getMessage());
                }
                biomeLocations.get(biome).forEach(i -> l.add(extractRule(i)));
                if (biomeLocations.containsKey("minecraft:default"))
                    biomeLocations.get("minecraft:default").forEach(i -> l.add(extractRule(i)));
                comp.put("sequence", l);
                return comp;
            }
        }

        TreeLeaf getParent(TreeLeaf l) {
            if (l.i_parent == -1) return l;
            return registry.get(l.i_parent);
        }

        CompoundTag extractRule(int i) {
            TreeLeaf l = registry.get(i);
            assert l.is_terminal;
            if (l.i_parent == -1) return l.data;
            return extractRule(getParent(l), l.data);
        }

        CompoundTag extractRule(TreeLeaf l, CompoundTag data) {
            CompoundTag newdata = conditionCase(l.data, data);
            if (l.i_parent == -1) return newdata;
            return extractRule(getParent(l), newdata);
        }

        static class TreeLeaf{
            CompoundTag data;
            int i;
            int i_parent;
            boolean is_terminal;

            TreeLeaf(CompoundTag c, int num, @Nullable TreeLeaf where, boolean e) {
                data = c;
                i = num;
                if (where == null) {
                    i_parent = -1;
                }
                else {
                    i_parent = where.i;
                }
                is_terminal = e;
            }
        }
    }
}
