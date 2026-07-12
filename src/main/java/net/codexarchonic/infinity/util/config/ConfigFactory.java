package net.codexarchonic.infinity.util.config;

import net.codexarchonic.infinity.util.core.ConfigType;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import java.util.function.BiFunction;
import java.util.function.Function;

/** Handles scanning of game content listed in a registry of choice.
 * <p>Filtering and formatting of content is delegated to the provided extractor function; these are defined in {@link ConfigGenerator}.
 * <p>Sorting and saving of content is delegated to {@link DataCollection}.
 * @param <S> type of content being scanned for: i.e. {@link Item}, {@link Biome}, etc. */
public class ConfigFactory<S> {
    Registry<S> reg;
    Function<ResourceKey<S>, CompoundTag> dataExtractor;
    ConfigFactory(Registry<S> reg, Function<ResourceKey<S>, CompoundTag> extractor) {
        this.reg = reg;
        this.dataExtractor = extractor;
    }

    static <S> ConfigFactory<S> of(Registry<S> r) {
        return new ConfigFactory<>(r, key -> new CompoundTag());
    }
    static <S> ConfigFactory<S> of(Registry<S> r, BiFunction<Registry<S>, ResourceKey<S>, CompoundTag> extractor) {
        return new ConfigFactory<>(r, key -> extractor.apply(r, key));
    }
    static <S> ConfigFactory<S> of(Registry<S> reg, Function<ResourceKey<S>, CompoundTag> extractor) {
        return new ConfigFactory<>(reg, extractor);
    }

    void generate(ConfigType type) {
        DataCollection.Logged collection = new DataCollection.Logged(type);
        reg.registryKeySet().forEach(key -> {
            try { //sonicraft crashes in here
                CompoundTag data = dataExtractor.apply(key);
                if (data != null) {
                    ResourceLocation id = key.location();
                    if (data.isEmpty()) collection.addIdentifier(id);
                    else collection.add(id.getNamespace(), id.toString(), data);
                }
            } catch (Exception ignored) {
            }
        });
        collection.save();
    }
}
