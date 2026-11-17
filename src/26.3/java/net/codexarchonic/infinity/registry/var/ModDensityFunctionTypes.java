package net.codexarchonic.infinity.registry.var;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.registry.registries.DeferredRegister;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.*;
import net.minecraft.world.level.levelgen.synth.Noise;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.jspecify.annotations.NullMarked;

import static net.codexarchonic.infinity.InfinityMod.MOD_ID;

@NullMarked
public class ModDensityFunctionTypes {
    @FunctionalInterface
    interface PointSampler extends DensitySampler {
        @Override
        default void sampleVolume(SamplerContext context, DensityBuffer output, DensityVolume volume) {
            DensitySampler.sampleVolumeNaive(context, output, volume, this);
        }
    }

    interface SpatialFunction extends DensityFunction {
        double compute(int blockX, int blockY, int blockZ);
        double minValue();
        double maxValue();

        @Override
        default DensitySampler compileSampler(CompileContext context) {
            return (PointSampler) (sampler, x, y, z) -> (float) compute(x, y, z);
        }

        @Override
        default DensityFunction rewriteChildren(DfRewriteRule rule) { return this; }

        @Override
        default int domainAxes() { return ALL_AXES; }

        @Override
        default Interval range() { return Interval.encapsulating((float) minValue(), (float) maxValue()); }
    }

    record NonbinaryOperation(Type type, DensityFunction input) implements DensityFunction {
        @Override
        public DensitySampler compileSampler(CompileContext context) {
            DensitySampler source = input.compileSampler(context);
            return (PointSampler) (sampler, x, y, z) -> (float) apply(type, source.sampleValue(sampler, x, y, z));
        }

        @Override
        public DensityFunction rewriteChildren(DfRewriteRule rule) { return new NonbinaryOperation(type, rule.rewrite(input)); }

        @Override
        public int domainAxes() { return input.domainAxes(); }

        @Override
        public Interval range() {
            return switch (type) {
                case SIN, COS -> Interval.ofSymmetric(1);
                case EXP -> Interval.mapMonotonic(input.range(), value -> (float) Math.exp(value));
                case LN -> Interval.log(input.range());
                default -> Interval.INFINITE;
            };
        }

        private static double apply(Type type, double density) {
            return switch (type) {
                case SIN -> Math.sin(density);
                case COS -> Math.cos(density);
                case SQRT -> Math.sqrt(density);
                case INVERT -> (Math.abs(density) < 0.001 ? 1000*Math.signum(density) : 1.0 / density);
                case EXP -> (Math.exp(density));
                case LN -> Math.log(density);
            };
        }

        @Override
        public MapCodec<? extends DensityFunction> codec() {
            return this.type.codecHolder;
        }

        enum Type
        {
            SIN("sin"),
            COS("cos"),
            SQRT("sqrt"),
            INVERT("invert"),
            LN("ln"),
            EXP("exp");

            private final String name;
            final MapCodec<NonbinaryOperation> codecHolder = DensityFunction.CODEC.fieldOf("argument").xmap(input -> new NonbinaryOperation(this, input), NonbinaryOperation::input);

            Type(String name) {
                this.name = name;
            }
        }
    }

    record Coordinate(double scale, int axis) implements SpatialFunction {
        public static final MapCodec<Coordinate> CODEC_HOLDER = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.DOUBLE.fieldOf("scale").orElse(1.0).forGetter(a -> a.scale),
                Codec.INT.fieldOf("axis").forGetter(a -> a.axis)).apply(
                instance, Coordinate::new));

        @Override
        public double compute(int blockX, int blockY, int blockZ) {
            if (axis == -1 || scale == 0.0) return apply(blockX, blockY, blockZ);
            double d = apply(blockX, blockY, blockZ) * scale;
            d -= Math.floor(d);
            return (d - 0.5)*2*Math.PI;
        }

        @Override
        public double minValue() {
            return scale == 0 && axis != -1 ? Double.NEGATIVE_INFINITY : -Math.PI;
        }

        @Override
        public double maxValue() {
            return scale == 0 && axis != -1 ? Double.POSITIVE_INFINITY : Math.PI;
        }

        @Override
        public MapCodec<? extends DensityFunction> codec() {
            return CODEC_HOLDER;
        }

        public double apply(int blockX, int blockY, int blockZ) {
            return switch(axis) {
                case 1 -> blockX; //x
                case 2 -> blockY; //y
                case 3 -> blockZ; //z
                case 0 -> r(blockX, blockZ); //r
                case -1 -> Math.acos(blockX / r(blockX, blockZ))*(blockZ < 0 ? -1 : 1); //phi
                default -> 0.0;
            };
        }

        static double r(int blockX, int blockZ) {
            double res = Math.sqrt((double)blockX*blockX + (double)blockZ*blockZ);
            return Math.max(res, 0.01);
        }
    }

    record Menger(double scale, int max_y) implements SpatialFunction {
        public static final MapCodec<Menger> CODEC_HOLDER = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.DOUBLE.fieldOf("scale").orElse(1.0).forGetter(a -> a.scale),
                Codec.INT.fieldOf("max_y").orElse(0).forGetter(a -> a.max_y)).apply(
                instance, Menger::new));

        @Override
        public double compute(int blockX, int blockY, int blockZ) {
            int x = blockX;
            int y = blockY;
            int z = blockZ;
            return (y > max_y || check(x, z) || check(x, y) || check(y, z)) ? -scale : scale;
        }

        @Override
        public double minValue() {
            return -scale;
        }

        @Override
        public double maxValue() {
            return scale;
        }

        @Override
        public MapCodec<? extends DensityFunction> codec() {
            return CODEC_HOLDER;
        }

        public boolean check(int a, int b) {
            int a1 = Math.abs(a);
            int b1 = Math.abs(b);
            while (a1 > 0 && b1 > 0) {
                if (a1 % 3 == 1 && b1 % 3 == 1) return true;
                a1 /= 3;
                b1 /= 3;
            }
            return false;
        }
    }

    record Skygrid(double scale, int size, int separation) implements SpatialFunction {
        public static final MapCodec<Skygrid> CODEC_HOLDER = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.DOUBLE.fieldOf("scale").orElse(1.0).forGetter(a -> a.scale),
                Codec.INT.fieldOf("size").orElse(1).forGetter(a -> a.size),
                Codec.INT.fieldOf("separation").orElse(3).forGetter(a -> a.separation)).apply(
                instance, Skygrid::new));

        @Override
        public double compute(int blockX, int blockY, int blockZ) {
            int n = separation + size;
            int x = Math.abs(blockX);
            int y = Math.abs(blockY);
            int z = Math.abs(blockZ);
            return (x % n < size && y % n < size && z % n < size) ? scale : -scale;
        }

        @Override
        public double minValue() {
            return -scale;
        }

        @Override
        public double maxValue() {
            return scale;
        }

        @Override
        public MapCodec<? extends DensityFunction> codec() {
            return CODEC_HOLDER;
        }
    }

    interface TileableStructure extends SpatialFunction {
        int size(int axis);
        boolean testBlock(int x, int y, int z);

        default int normalize(int x, int axis) {
            int a = Math.abs(x < 0 ? x+1 : x) % size(axis);
            return (x < 0) ? size(axis) - 1 - a : a;
        }

        @Override
        default double compute(int blockX, int blockY, int blockZ) {
            int x = normalize(blockX,0);
            int y = normalize(blockY,1);
            int z = normalize(blockZ,2);
            return testBlock(x, y, z) ? 1 : -1;
        }
        @Override
        default double minValue() {
            return -1;
        }
        @Override
        default double maxValue() {
            return 1;
        }
    }

    public enum Library implements TileableStructure {
        INSTANCE;
        static final MapCodec<Library> CODEC_HOLDER = MapCodec.unit(INSTANCE);

        @Override
        public int size(int axis) {
            return (axis == 1) ? 16 : 15;
        }

        @Override
        public boolean testBlock(int x, int y, int z) {
            int max_xz = Math.max(Math.abs(7 - x), Math.abs(7 - z));
            int min_xz = Math.min(Math.abs(7 - x), Math.abs(7 - z));
            if (max_xz == 7) {
                return y >= 3 || y == 0 || min_xz > 1; //walls
            }
            if (max_xz < 2) {
                return true; // central column 3*3
            }
            if (max_xz == 2 && min_xz == 1) return true; //ladders
            return y == 0; // floor
        }

        @Override
        public MapCodec<? extends DensityFunction> codec() {
            return CODEC_HOLDER;
        }
    }

    static Noise sampler, sampler2;

    public record Classic(int sealevel) implements SpatialFunction {
        public static final MapCodec<Classic> CODEC_HOLDER = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.INT.fieldOf("sealevel").orElse(64).forGetter(a -> a.sealevel)).apply(
                instance, Classic::new));

        @Override
        public double compute(int blockX, int blockY, int blockZ) {
            return blockY < sample(blockX, blockZ) ? 1 : -1;
        }

        int sample(int x, int z) {
            double heightLow = sampler.get(x * 1.3, 0, z * 1.3)*6 - 6;
            double heightHigh = sampler.get(x * 1.3, 200, z * 1.3)*7.2 + 6;
            if (sampler2.get(x, 0, z) > 0.0) {
                heightHigh = heightLow;
            }
            double heightResult = Math.max(heightLow, heightHigh) / 2;

            if (heightResult < 0.0) {
                heightResult *= 0.8;
            }

            return sealevel + (int)heightResult;
        }

        @Override
        public double minValue() {
            return -1;
        }
        @Override
        public double maxValue() { return 1; }
        @Override
        public MapCodec<? extends DensityFunction> codec() { return CODEC_HOLDER; }
    }

    public static final DeferredRegister<MapCodec<? extends DensityFunction>> DENSITY_FUNCTION_TYPES =
            DeferredRegister.create(MOD_ID, Registries.DENSITY_FUNCTION_TYPE);

    public static <T extends DensityFunction> void register(String name, MapCodec<T> codec) {
        DENSITY_FUNCTION_TYPES.register(name, () -> codec);
    }

    public static void registerFunctions() {
        sampler = NormalNoise.createParity(-5, InfinityMethods.genOctaves(8)).create(new LegacyRandomSource(0L));
        sampler2 = NormalNoise.createParity(-6, InfinityMethods.genOctaves(8)).create(new LegacyRandomSource(0L));
        for (NonbinaryOperation.Type enum_ : NonbinaryOperation.Type.values()) {
            register(enum_.name, enum_.codecHolder);
        }
        register("coordinate", Coordinate.CODEC_HOLDER);
        register("menger", Menger.CODEC_HOLDER);
        register("skygrid", Skygrid.CODEC_HOLDER);
        register("library", Library.CODEC_HOLDER);
        register("classic", Classic.CODEC_HOLDER);
        DENSITY_FUNCTION_TYPES.register();
    }
}
