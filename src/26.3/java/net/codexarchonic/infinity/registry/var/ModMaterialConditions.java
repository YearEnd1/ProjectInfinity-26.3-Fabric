package net.codexarchonic.infinity.registry.var;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.registry.registries.DeferredRegister;
import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.util.var.TextData;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;
import net.minecraft.world.level.levelgen.material.condition.MaterialCondition;
import net.minecraft.world.level.levelgen.material.condition.ConditionEvaluator;
import org.apache.commons.io.FileUtils;
import org.jspecify.annotations.NullMarked;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static net.codexarchonic.infinity.InfinityMod.MOD_ID;

@NullMarked
public class ModMaterialConditions {
    public record LinearCondition(double k_x, double k_y, double k_z, double min, double max, int separation) implements MaterialCondition
    {
        public static final MapCodec<LinearCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                (Codec.DOUBLE.fieldOf("k_x").orElse(1.0).forGetter(a -> a.k_x)),
                (Codec.DOUBLE.fieldOf("k_y").orElse(0.0).forGetter(a -> a.k_y)),
                (Codec.DOUBLE.fieldOf("k_z").orElse(1.0).forGetter(a -> a.k_z)),
                (Codec.DOUBLE.fieldOf("min").orElse(0.0).forGetter(a -> a.min)),
                (Codec.DOUBLE.fieldOf("max").orElse(0.5).forGetter(a -> a.max)),
                (Codec.INT.fieldOf("separation").orElse(16).forGetter(a -> a.separation))
        ).apply(instance, LinearCondition::new));

        @Override
        public MapCodec<? extends MaterialCondition> codec() {
            return CODEC;
        }

        @Override
        public ConditionEvaluator compile(MaterialRuleContext materialRuleContext) {
            class LinearPredicate implements ConditionEvaluator {

                double scale() {
                    return Math.sqrt(k_x*k_x + k_y*k_y + k_z*k_z);
                }
                public boolean test() {
                    int x = materialRuleContext.blockX();
                    int y = materialRuleContext.blockY();
                    int z = materialRuleContext.blockZ();
                    double res = (k_x * x + k_y * y + k_z * z)/(separation*scale());
                    res = res - Math.floor(res);
                    return (res > min) && (res < max);
                }
            }
            return new LinearPredicate();
        }
    }

    public record CheckerboardCondition(int size) implements MaterialCondition
    {
        public static final MapCodec<CheckerboardCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                (Codec.INT.fieldOf("size").orElse(1).forGetter(a -> a.size))
        ).apply(instance, CheckerboardCondition::new));

        @Override
        public MapCodec<? extends MaterialCondition> codec() {
            return CODEC;
        }

        @Override
        public ConditionEvaluator compile(MaterialRuleContext materialRuleContext) {
            class CheckerboardPredicate implements ConditionEvaluator {

                public int sign(int coord) {
                    if (coord < 0) return -sign(-1-coord);
                    return ((coord / size) % 2)*2 - 1;
                }
                public boolean test() {
                    int x = sign(materialRuleContext.blockX());
                    int z = sign(materialRuleContext.blockZ());
                    return (x > 0) ^ (z > 0);
                }
            }

            return new CheckerboardPredicate();
        }
    }

    public record TextCondition(int font_size, int char_spacing, int line_spacing, int max_width, boolean read_from_file,
                                String text, TextData data) implements MaterialCondition
    {
        public static final MapCodec<TextCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                (Codec.INT.fieldOf("font_size").orElse(1).forGetter(a -> a.font_size)),
                (Codec.INT.fieldOf("char_spacing").orElse(1).forGetter(a -> a.char_spacing)),
                (Codec.INT.fieldOf("line_spacing").orElse(1).forGetter(a -> a.line_spacing)),
                (Codec.INT.fieldOf("max_width").orElse(Integer.MAX_VALUE).forGetter(a -> a.max_width)),
                (Codec.BOOL.fieldOf("read_from_file").orElse(false).forGetter(a -> a.read_from_file)),
                (Codec.STRING.fieldOf("text").forGetter(a -> a.text))
        ).apply(instance, TextCondition::of));

        static TextCondition of(int font_size, int char_spacing, int line_spacing, int max_width, boolean read_from_file, String text) {
            String txt = text;
            if (read_from_file) {
                File file = InfinityMod.configPath.resolve(text).toFile();
                if (file.exists()) {
                    try {
                        txt = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                        txt = txt.replace("\n", "$n");
                    } catch (IOException ignored) {
                    }
                }
            }
            return new TextCondition(font_size, char_spacing, line_spacing, max_width, read_from_file, txt,
                    TextData.genData(char_spacing, max_width, txt));
        }

        public int find(int x, int line_num) {
            int char_num = Collections.binarySearch(data.offsetMap().get(line_num), x);
            if (char_num < 0) char_num = -char_num - 2;
            return char_num;
        }

        @Override
        public MapCodec<? extends MaterialCondition> codec() {
            return CODEC;
        }

        @Override
        public ConditionEvaluator compile(MaterialRuleContext materialRuleContext) {
            class CheckerboardPredicate implements ConditionEvaluator {
                final List<List<Integer>> textmap;
                final List<List<Character>> charmap;
                final int longest_line;

                CheckerboardPredicate() {
                    textmap = data.offsetMap();
                    charmap = data.charMap();
                    longest_line = data.longest_line();
                }

                public boolean test() {
                    int x = materialRuleContext.blockX() / font_size;
                    int z = materialRuleContext.blockZ() / font_size;
                    int factor = 8 + line_spacing;
                    if (x < 0 || z < 0 || x > longest_line || z >= factor * textmap.size()) return false;
                    int line_num = z / factor;
                    int char_num = find(x, line_num);
                    int char_z = z % factor;
                    if (char_z >= 8 || char_num < 0) return false;
                    int char_x = x - textmap.get(line_num).get(char_num);
                    return TextData.check(char_x, char_z, charmap.get(line_num).get(char_num));
                }
            }
            return new CheckerboardPredicate();
        }
    }

    public static final DeferredRegister<
            MapCodec
            <? extends MaterialCondition>> MATERIAL_CONDITIONS = DeferredRegister.create(MOD_ID, Registries.MATERIAL_CONDITION_TYPE);


    public static void registerConditions() {
        MATERIAL_CONDITIONS.register("linear", () -> LinearCondition.CODEC);
        MATERIAL_CONDITIONS.register("checkerboard", () -> CheckerboardCondition.CODEC);
        MATERIAL_CONDITIONS.register("text", () -> TextCondition.CODEC);
        MATERIAL_CONDITIONS.register();
    }
}
