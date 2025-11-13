package net.lerariemann.infinity.util;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import dev.architectury.platform.Platform;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.access.Timebombable;
import net.lerariemann.infinity.block.entity.TintableBlockEntity;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.util.core.RandomProvider;
import net.lerariemann.infinity.util.platform.PlatformMethods;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
//? if <1.21 {
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.core.registries.Registries;
import net.lerariemann.infinity.registry.core.ModItems;
//?}
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.commons.io.FileUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

/** Common mod methods that work identically on Fabric and NeoForge.
 * @see PlatformMethods */
@SuppressWarnings("unused")
public interface InfinityMethods {
    String ofRandomDim = "infinity:random";
    NormalNoise sampler =
            NormalNoise.create(new LegacyRandomSource(0L), -5, genOctaves(2));

    static double[] genOctaves(int octaves){
        double[] a = new double[octaves];
        Arrays.fill(a, 1);
        return a;
    }
    static double sample(BlockPos pos) {
        return sampler.getValue(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Converts a string to an identifier in the Infinite Dimensions namespace.
     */
    static ResourceLocation getId(String value){
		return VersionMethods.id(MOD_ID, value);
	}

    /**
     * Converts a dimension's long value to an identifier in the Infinite Dimensions namespace.
     */
    static ResourceLocation getDimId(long value){
		return getId("generated_" + value);
	}

    /**
     * Checks if a dimension is an Infinite Dimension.
     */
    static boolean isInfinity(Level w) {
        return isInfinity(w.dimension());
    }
    static boolean isInfinity(ResourceKey<Level> key) {
        return key.location().getNamespace().equals(MOD_ID);
    }
    static boolean isBiomeInfinity(LevelAccessor world, BlockPos pos) {
        //? if >1.21 {
        /*return world.getBiome(pos).getRegisteredName().contains("infinity");
        *///?} else {
        return world.registryAccess().registryOrThrow(Registries.BIOME).getKey(world.getBiome(pos).value()).getNamespace().contains("infinity");
        //?}

    }
    static boolean isBiomeInfinity(Holder<Biome> b) {
        //? if >1.21 {
        /*return b.getRegisteredName().contains("infinity");
        *///?} else {
        return false;
        //?}
    }
    static boolean isBiomeInfinity(BlockGetter world, BlockPos pos) {
        if (world instanceof LevelAccessor acc) {
            return InfinityMethods.isBiomeInfinity(acc, pos);
        }
        //? if fabric {
        else return Platform.isFabric()
                && world.hasBiomes()
                && world.getBiomeFabric(pos) != null
                && InfinityMethods.isBiomeInfinity(world.getBiomeFabric(pos));
        //?} else {
        /*return false;
        *///?}
    }

    /**
     * Checks if a mod designed for Fabric (using - instead of _) is loaded.
     * This is most commonly used for FFAPI modules, but also applies to mods
     * like Item Descriptions or Cloth Config.
     */
    static boolean isFabricApiLoaded(String modID) {
        if (Platform.isFabric()) return Platform.isModLoaded(modID.replace("_", "-"));
        else return Platform.isModLoaded(modID.replace("-", "_"));
    }

    static int properMod(int a, int b) {
        int res = a%b;
        return (res >= 0) ? res : b + res;
    }

    static void sendUnexpectedError(ServerPlayer player, String type) {
        player.sendSystemMessage(Component.translatable("error.infinity." + type + ".unexpected"));
    }

    static String dimTextPreprocess(String text) {
        if (text.isEmpty()) return "missingno";
        if (RandomProvider.rule("forceLowercase")) text = text.toLowerCase();
        text = text.replaceAll("\n", " ");
        return text;
    }

    /**
     * Convert a provided string into a dimension ID.
     * This also checks if it matches an Easter Egg dimension.
     */
    static ResourceLocation dimTextToId(String text) {
        ResourceLocation easterId = InfinityMod.provider.easterizer.getAsEaster(text);
        if (easterId != null)
            return easterId;
        return InfinityMethods.getDimId(getDimensionSeed(text));
    }

    static boolean isTimebombed(ServerLevel world) {
        return ((Timebombable)world).infinity$isTimebombed();
    }

    /**
     * Check if a dimension exists and has not been timebombed.
     */
    static boolean dimExists(ServerLevel world) {
        return (world != null && !isTimebombed(world));
    }

    /**
     * Hashes text into dimension ID.
     */
    static long getDimensionSeed(String text) {
        HashCode f = Hashing.sha256().hashString(text + InfinityMod.provider.salt, StandardCharsets.UTF_8);
        return InfinityMethods.longArithmeticEnabled() ? f.asLong() & Long.MAX_VALUE : f.asInt() & Integer.MAX_VALUE;
    }

    static long getNumericFromId(ResourceLocation id) {
        String dimensionName = id.getPath();
        String numericId = dimensionName.substring(dimensionName.lastIndexOf("_") + 1);
        long i;
        try {
            i = Long.parseLong(numericId);
        } catch (Exception e) {
            /* Simply hash the name if it isn't of "generated_..." format. */
            i = getDimensionSeed(numericId);
        }
        return i;
    }

    private static float bookBoxSample(BlockPos pos, int offset) {
        return Mth.clamp(0.5f * (1f + (float)sampler.getValue(4*pos.getX(), 4*(pos.getY() + offset), 4*pos.getZ())), 0f, 1f);
    }
    static int getBookBoxColor(BlockState state, BlockAndTintGetter world, BlockPos pos, int tintIndex) {
        if (pos != null) {
            float r = bookBoxSample(pos, -1000);
            float g = bookBoxSample(pos, 0);
            float b = bookBoxSample(pos, 1000);
            return Mth.color(r, g, b);
        }
        return 16777215;
    }

    static int getOverlayColorFromComponents(ItemStack stack, int layer) {
        //? if >1.21 {
        /*int color = stack.getComponents().getOrDefault(ModComponentTypes.COLOR.get(), 0xFFFFFF);
        if (layer == 1) {
            return FastColor.ARGB32.opaque(color);
        }
        return FastColor.ARGB32.opaque(0xFFFFFF);
        *///?} else {
        if (stack.getTag() != null) {
            if (layer == 1) {
                if (stack.is(ModItems.TRANSFINITE_KEY.get()))
                    return stack.getTag().getInt(ModComponentTypes.COLOR);
                else if (stack.is(ModItems.BIOME_BOTTLE_ITEM.get())) {
                    return stack.getTag().getCompound("BlockEntityTag").getInt("Color");
                }
                else if (stack.is(ModItems.F4.get())) {
                    return VersionMethods.getOrDefaultInt(stack, ModComponentTypes.COLOR, 10879231);
                }
            }
            else if (stack.is(ModItems.CHROMATIC_MATTER.get()) || stack.is(ModItems.CHROMATIC_WOOL.get()) || stack.is(ModItems.CHROMATIC_CARPET.get()))
                return stack.getTag().getInt(ModComponentTypes.COLOR);
        }
        return 0xFFFFFF;
        //?}
    }
    static int getItemColorFromComponents(ItemStack stack, int layer) {
        int color = VersionMethods.getOrDefaultInt(stack, ModComponentTypes.COLOR, 0xFFFFFF);
        return VersionMethods.opaque(color);
    }
    static int getDiscColorFromComponents(ItemStack stack, int layer) {
        int color = getItemColorFromComponents(stack, layer);
        return layer == 0 ? color : 0xFFFFFF ^ color;
    }

    /**
     * For use in color providers with blocks which the block entity sets color for.
     */
    static int getBlockEntityColor(BlockState state, BlockAndTintGetter world, BlockPos pos, int tintIndex) {
        if (world != null && pos != null) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof TintableBlockEntity be) {
                return be.getTint() & 0xFFFFFF;
            }
        }
        return 0xFFFFFF;
    }

    static long getRandomSeed(java.util.Random random) {
        return longArithmeticEnabled() ? random.nextLong() : random.nextInt();
    }

    static long getRandomSeed(RandomSource random) {
        return longArithmeticEnabled() ? random.nextLong() : random.nextInt();
    }

    static ResourceLocation getRandomId(java.util.Random random) {
        return getDimId(getRandomSeed(random));
    }
    
    static ResourceLocation getRandomId(RandomSource random) {
        return getDimId(getRandomSeed(random));
    }

    static MutableComponent getDimensionNameAsText(ResourceLocation dimension) {
        String name = dimension.toString();
        // Randomly generated dimensions.
        if (name.contains("infinity:generated_"))
            return Component.translatable("tooltip.infinity.key.generated")
                    .append(name.replace("infinity:generated_", ""));
        if (name.equals(InfinityMethods.ofRandomDim))
            return Component.translatable("tooltip.infinity.key.randomise");
        // All other dimensions.
        return Component.translatableWithFallback(
                Util.makeDescriptionId("dimension", dimension),
                InfinityMethods.formatAsTitleCase(dimension.getPath()));
    }

    /**
    * Creates a fallback for texts without translation by replacing underscores
     * with spaces and formatting the text as Title Case.
     */
    static String formatAsTitleCase(String text) {
        text = text.replaceAll("[_./]", " ");
        //i am sure java has a smarter way to do title case, but this works too
        StringBuilder newText = new StringBuilder();
        int i = 0;
        for (Character c : text.toCharArray()) {
            if (i == 0) {
                c = c.toString().toUpperCase().charAt(0);
            }
            newText.append(c);
            i++;
            if (c == ' ') {
                i = 0;
            }
        }
        return newText.toString();
    }

    static boolean chaosMobsEnabled() {
        return RandomProvider.rule("chaosMobsEnabled");
    }

    static boolean longArithmeticEnabled() {
        return RandomProvider.rule("longArithmeticEnabled");
    }

    static boolean deleteLevel(LevelStorageSource.LevelStorageAccess session) {
        return FileUtils.deleteQuietly(
                //? if >1.21 {
                /*session.getLevelDirectory().path().resolve("datapacks")
                *///?} else {
                session.getLevelPath(LevelResource.DATAPACK_DIR)
                //?}
                .toFile());
    }
}
