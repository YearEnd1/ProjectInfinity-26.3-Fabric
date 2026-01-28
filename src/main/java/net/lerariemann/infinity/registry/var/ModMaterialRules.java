package net.lerariemann.infinity.registry.var;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.registry.registries.DeferredRegister;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.ConfigType;
import net.lerariemann.infinity.util.core.RandomProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.jetbrains.annotations.Nullable;

//? if neoforge
/*import javax.annotation.ParametersAreNonnullByDefault;*/

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

@MethodsReturnNonnullByDefault
//? if neoforge
/*@ParametersAreNonnullByDefault*/
public class ModMaterialRules {
    static int normalize(int x, int size) {
        int a = Math.abs(x < 0 ? x+1 : x) % size;
        return (x < 0) ? size - 1 - a : a;
    }

    public record RandomBlockStateRule(RandomProvider prov) implements SurfaceRules.SurfaceRule
    {
        @Override
        public BlockState tryApply(int i, int j, int k) {
            long seed = Mth.getSeed(i, j, k);
            double d = (seed & 0xFFFL) / (double)0xFFFL;
            d = d - Math.floor(d);
            BlockState st = VersionMethods.getBlock(VersionMethods.id(prov.randomName(d, ConfigType.FULL_BLOCKS_WG))).defaultBlockState();
            if(st.hasProperty(BlockStateProperties.PERSISTENT)) st = st.setValue(BlockStateProperties.PERSISTENT, Boolean.TRUE);
            return st;
        }
    }

    public enum RandomBlockMaterialRule implements SurfaceRules.RuleSource
    {
        INSTANCE;
        static final KeyDispatchDataCodec<RandomBlockMaterialRule> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
        static RandomProvider PROVIDER;
        public static void setProvider(RandomProvider p) {
            PROVIDER = p;
        }

        @Override
        public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
            return CODEC;
        }

        @Override
        public SurfaceRules.SurfaceRule apply(SurfaceRules.Context materialRuleContext) {
            return new RandomBlockStateRule(PROVIDER);
        }
    }

    public record RandomColoredBlock(String str) implements SurfaceRules.SurfaceRule
    {
        @Override
        public BlockState tryApply(int i, int j, int k) {
            double d = InfinityMethods.sampler.getValue(i, j, k);
            d = d - Math.floor(d);
            BlockState st = Iridescence.getRandomColorBlock(d, str).defaultBlockState();
            if(st.hasProperty(BlockStateProperties.PERSISTENT)) st = st.setValue(BlockStateProperties.PERSISTENT, Boolean.TRUE);
            return st;
        }

        record Rule(String str) implements SurfaceRules.RuleSource
        {
            static final KeyDispatchDataCodec<Rule> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.fieldOf("block_type").orElse("concrete").forGetter(a -> a.str)).apply(instance, Rule::new)));

            @Override
            public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
                return CODEC;
            }

            @Override
            public SurfaceRules.SurfaceRule apply(SurfaceRules.Context materialRuleContext) {
                return new RandomColoredBlock(str);
            }
        }
    }

    public static class Notes implements SurfaceRules.SurfaceRule {
        @Override
        public @Nullable BlockState tryApply(int i, int j, int k) {
            NoteBlockInstrument instr = InfinityMethods.getRandomInstrument();
            BlockState bs = Blocks.NOTE_BLOCK.defaultBlockState().setValue(NoteBlock.INSTRUMENT, instr);
            if (instr.isTunable()) bs = bs.setValue(NoteBlock.NOTE, InfinityMod.random.nextInt(24));
            return bs;
        }

        enum Rule implements SurfaceRules.RuleSource {
            INSTANCE;
            static final KeyDispatchDataCodec<Rule> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
            @Override
            public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
                return CODEC;
            }
            @Override
            public SurfaceRules.SurfaceRule apply(SurfaceRules.Context materialRuleContext) {
                return new Notes();
            }
        }
    }

    public static class Lamps implements SurfaceRules.SurfaceRule {
        @Override
        public @Nullable BlockState tryApply(int i, int j, int k) {
            return Blocks.REDSTONE_LAMP.defaultBlockState().setValue(RedstoneLampBlock.LIT, InfinityMod.random.nextBoolean());
        }

        enum Rule implements SurfaceRules.RuleSource {
            INSTANCE;
            static final KeyDispatchDataCodec<Rule> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
            @Override
            public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
                return CODEC;
            }
            @Override
            public SurfaceRules.SurfaceRule apply(SurfaceRules.Context materialRuleContext) {
                return new Lamps();
            }
        }
    }


    public static class Library implements SurfaceRules.SurfaceRule
    {
        static final BlockState floor = Blocks.OAK_SLAB.defaultBlockState();
        static final BlockState wall = ModBlocks.BOOK_BOX.get().defaultBlockState();
        static final BlockState decor = Blocks.GLOWSTONE.defaultBlockState();
        static final BlockState glass = Blocks.OAK_TRAPDOOR.defaultBlockState();
        static final BlockState column = Blocks.OAK_PLANKS.defaultBlockState();
        static final BlockState air = Blocks.AIR.defaultBlockState();
        @Override
        public BlockState tryApply(int i, int j, int k) {
            int x = normalize(i, 15);
            int y = normalize(j, 16);
            int z = normalize(k, 15);
            int max_xz = Math.max(Math.abs(7 - x), Math.abs(7 - z));
            int min_xz = Math.min(Math.abs(7 - x), Math.abs(7 - z));
            if (max_xz == 7) {
                if (min_xz < 2) {
                    if (y == 0) return floor;
                    if (y < 4) return air; //corridors
                }
                return wall; //walls
            }
            if (max_xz < 2) {
                return ((x + z) % 2 == 1) ? wall : column; //central column
            }
            if (max_xz == 2 && min_xz == 1) {
                if (j == 0) return floor;
                Direction d = (x == 5 ? Direction.WEST : x == 9 ? Direction.EAST : z == 5 ? Direction.NORTH : Direction.SOUTH);
                return Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, d); //ladders
            }
            if (y == 0) {
                if (max_xz == 4) {
                    if (min_xz == 4) return decor;
                    if (min_xz == 0 && j > 0) return glass; //lighting
                }
                return floor; //floor
            }
            return air;
        }

        enum Rule implements SurfaceRules.RuleSource {
            INSTANCE;
            static final KeyDispatchDataCodec<Rule> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
            @Override
            public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
                return CODEC;
            }
            @Override
            public SurfaceRules.SurfaceRule apply(SurfaceRules.Context materialRuleContext) {
                return new Library();
            }
        }
    }

    public static class Backrooms implements SurfaceRules.SurfaceRule {
        static final BlockState floor = Blocks.MUSHROOM_STEM.defaultBlockState();
        static final BlockState wall = Blocks.SMOOTH_SANDSTONE.defaultBlockState();
        static final BlockState light = Blocks.OCHRE_FROGLIGHT.defaultBlockState();
        static final BlockState ceiling = Blocks.SMOOTH_SANDSTONE.defaultBlockState();
        static final BlockState air = Blocks.AIR.defaultBlockState();
        static final BlockState filler = Blocks.OBSIDIAN.defaultBlockState();
        static int anti_normalize(int x, int size) {
            return Math.abs(x < 0 ? x+1 : x) / size;
        }
        @Override
        public BlockState tryApply(int i, int j, int k) {
            int size_xz = 15;
            int halfsize_xz = 7;
            int x = normalize(i, size_xz);
            int y = normalize(j-1, 16);
            int z = normalize(k, size_xz);
            int xrel = Math.abs(halfsize_xz - x);
            int zrel = Math.abs(halfsize_xz - z);
            int max_xz = Math.max(xrel, zrel);
            int min_xz = Math.min(xrel, zrel);
            boolean isXMax = max_xz == xrel;
            boolean isOpen = (max_xz >= 3) && (min_xz <= 3) && (y <= 6) && ((double) (Mth.getSeed(
                    2*anti_normalize(i, size_xz) - Mth.sign(i)*Mth.sign(halfsize_xz - x)*(isXMax ? 1 : 0),
                    anti_normalize(j-1, 16),
                    2*anti_normalize(k, size_xz) - Mth.sign(k)*Mth.sign(halfsize_xz - z)*(isXMax ? 0 : 1))
                    & 0xFL) / 15.0 > 0.3);
            if (isOpen || (min_xz <=3 && y <= 6 && (i==0 || k==0))) {
                if (min_xz == 3) return wall;
                if (y == 0) return floor;
                if (y == 6) {
                    if (min_xz == 0 && max_xz%3 == 0) return light;
                    return ceiling;
                }
                return air;
            }
            else if (max_xz <= 3 && y <= 6) {
                if (max_xz == 3) return wall;
                if (y == 0) return floor;
                if (y == 6) {
                    if (max_xz == 0) return light;
                    return ceiling;
                }
                return air;
            }
            return filler;
        }

        enum Rule implements SurfaceRules.RuleSource {
            INSTANCE;
            static final KeyDispatchDataCodec<Rule> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
            @Override
            public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
                return CODEC;
            }
            @Override
            public SurfaceRules.SurfaceRule apply(SurfaceRules.Context materialRuleContext) {
                return new Backrooms();
            }
        }
    }

    public static class Nexus implements SurfaceRules.SurfaceRule
    {
        static final BlockState floor = Blocks.SMOOTH_STONE.defaultBlockState();
        static final BlockState wall = Blocks.OAK_PLANKS.defaultBlockState();
        static final BlockState column1 = Blocks.OAK_LOG.defaultBlockState();
        static final BlockState column2 = Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X);
        static final BlockState stair1 = Blocks.OAK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
        static final BlockState stair2 = Blocks.OAK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
        static final BlockState stair3 = Blocks.OAK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(BlockStateProperties.HALF, Half.TOP);
        static final BlockState stair4 = Blocks.OAK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).setValue(BlockStateProperties.HALF, Half.TOP);
        static final BlockState light1 = Blocks.JACK_O_LANTERN.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
        static final BlockState light2 = Blocks.JACK_O_LANTERN.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
        static final BlockState light3 = Blocks.GLOWSTONE.defaultBlockState();
        static final BlockState air = Blocks.AIR.defaultBlockState();
        @Override
        public BlockState tryApply(int i, int j, int k) {
            int x = normalize(i, 8);
            int y = j - 50;
            int z = normalize(k, 16);
            if (y==-2) return Blocks.BEDROCK.defaultBlockState();
            switch (y) {
                case -1, 9 -> {
                    if ((z == 10 || z == 6) && (x == 0 || x == 4)) return light3;
                    return (y == -1) ? floor : wall;
                }
                case 0 -> {
                    return switch (z) {
                        case 0, 1, 2, 14, 15 -> wall;
                        case 3 -> stair1;
                        case 13 -> stair2;
                        default -> air;
                    };
                }
                case 1, 2, 3, 4, 5 -> {
                    if (z == 0) return wall;
                    if (z == 1 || z == 15) {
                        if (x == 5 || x == 7) return column1;
                        if (x==6) {
                            if (y != 3) return wall;
                            return (z == 1) ? light1 : light2;
                        }
                    }
                    return air;
                }
                case 6 -> {
                    if (z == 0) return wall;
                    return (z == 1 || z == 15) ? column2 : air;
                }
                case 7 -> {
                    return switch (z) {
                        case 2 -> stair3;
                        case 14 -> stair4;
                        case 0, 1, 15 -> wall;
                        default -> air;
                    };
                }
                case 8 -> {
                    return switch (z) {
                        case 0, 1, 2, 3, 13, 14, 15 -> wall;
                        case 4 -> stair3;
                        case 12 -> stair4;
                        default -> air;
                    };
                }
                default -> {
                    return air;
                }
            }
        }

        enum Rule implements SurfaceRules.RuleSource {
            INSTANCE;
            static final KeyDispatchDataCodec<Rule> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
            @Override
            public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
                return CODEC;
            }
            @Override
            public SurfaceRules.SurfaceRule apply(SurfaceRules.Context materialRuleContext) {
                return new Nexus();
            }
        }
    }

    public static class Perfection implements SurfaceRules.SurfaceRule
    {
        static final BlockState cobblestone = Blocks.COBBLESTONE.defaultBlockState();
        static final BlockState lightNorth = Blocks.WALL_TORCH.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
        static final BlockState lightSouth = Blocks.WALL_TORCH.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
        static final BlockState lightEast = Blocks.WALL_TORCH.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST);
        static final BlockState lightWest = Blocks.WALL_TORCH.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST);
        static final BlockState glass = Blocks.GLASS.defaultBlockState();
        static final BlockState air = Blocks.AIR.defaultBlockState();
        @Override
        public BlockState tryApply(int i, int j, int k) {
            int x = normalize(i, 10);
            int y = j - 50;
            int z = normalize(k, 10);
            if (y==-2) return Blocks.BEDROCK.defaultBlockState();
            switch (y) {
                case -1 -> {
                    return cobblestone;
                }
                case 4 -> {
                    //Skylights
                    if ((z == 7 || z == 6 || z == 0 || z == 9) && (x == 0 || x == 9 || x == 2 || x == 3)) return glass;
                    return cobblestone;
                }
                case 3 -> {
                    // Crossroad overhang, North/South
                    if (z == 2 || z == 3 || z == 4) {
                        return cobblestone;
                    }
                    //Crossroad torch - South (North facing)
                    else if (z == 1) {
                        if (x == 1) {
                            return lightNorth;
                        }
                    }
                    //Crossroad torch - North (South facing)
                    else if (z == 5) {
                        if (x == 1) {
                            return lightSouth;
                        }
                    }
                    // Crossroad overhang, East/West
                    if (x == 7 || x == 6 || x == 5) {
                        return cobblestone;
                    }
                    //Crossroad torch - West (East facing)
                    else if (x == 8) {
                        if (z == 8) {
                            return lightEast;
                        }
                    }
                    //Crossroad torch - East (West facing)
                    else if (x == 4) {
                        if (z == 8) {
                            return lightWest;
                        }
                    }
                     return air;
                }
                case 0, 1, 2 -> {
                    //Crossroad walls, East/West
                    if (x == 7 || x == 6 || x == 5) {
                        if (z == 7 || z == 8 || z == 9) {
                            return air;
                        }
                        return cobblestone;
                    }
                    //Crossroad walls, North/South
                    if (z == 2 || z == 3 || z == 4 || z == 12 || z == 13 || z == 14) {
                        if (x == 0 || x == 2 || x == 1) {
                            return air;
                        }
                        return cobblestone;
                    }

                    return air;
                }
                default -> {
                    return air;
                }
            }
        }

        enum Rule implements SurfaceRules.RuleSource {
            INSTANCE;
            static final KeyDispatchDataCodec<Rule> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
            @Override
            public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
                return CODEC;
            }
            @Override
            public SurfaceRules.SurfaceRule apply(SurfaceRules.Context materialRuleContext) {
                return new Perfection();
            }
        }
    }

    public static final DeferredRegister<
            //? if >1.21 {
            MapCodec
            //?} else {
            /*Codec
            *///?}
                    <? extends SurfaceRules.RuleSource>> MATERIAL_RULES =
            DeferredRegister.create(MOD_ID, Registries.MATERIAL_RULE);

    public static <R extends SurfaceRules.RuleSource, T extends KeyDispatchDataCodec<R>> void register(String name, T holder) {
        MATERIAL_RULES.register(name, holder::codec);
    }

    public static void registerRules() {
        register("chaos", RandomBlockMaterialRule.CODEC);
        register("colored_chaos", RandomColoredBlock.Rule.CODEC);
        register("notes", Notes.Rule.CODEC);
        register("lamps", Lamps.Rule.CODEC);
        register("library", Library.Rule.CODEC);
        register("backrooms", Backrooms.Rule.CODEC);
        register("nexus", Nexus.Rule.CODEC);
        register("perfection", Perfection.Rule.CODEC);
        MATERIAL_RULES.register();
    }
}
