package net.lerariemann.infinity.registry.core;

//? if >1.21 {
import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.lerariemann.infinity.InfinityMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import java.util.function.UnaryOperator;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;
//?}

public class ModComponentTypes {
    //? if >1.21 {
    public static final DeferredRegister<DataComponentType<?>> COMPONENT_TYPES =
            DeferredRegister.create(MOD_ID, Registries.DATA_COMPONENT_TYPE);

    public static RegistrySupplier<DataComponentType<ResourceLocation>> DESTINATION = registerComponentType("destination",
            (builder) -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC));
    public static RegistrySupplier<DataComponentType<ResourceLocation>> BIOME_CONTENTS = registerComponentType("biome_contents",
            (builder) -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC));
    public static RegistrySupplier<DataComponentType<Integer>> COLOR = registerComponentType("color",
            (builder) -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static RegistrySupplier<DataComponentType<Integer>> HUE = registerComponentType("hue",
            (builder) -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static RegistrySupplier<DataComponentType<String>> DYE_COLOR = registerComponentType("dye_color",
            (builder) -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));
    public static RegistrySupplier<DataComponentType<Integer>> CHARGE = registerComponentType("charge",
            (builder) -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static RegistrySupplier<DataComponentType<Integer>> SIZE_X = registerComponentType("size_x",
            (builder) -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static RegistrySupplier<DataComponentType<Integer>> SIZE_Y = registerComponentType("size_y",
            (builder) -> builder.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    private static <T> RegistrySupplier<DataComponentType<T>> registerComponentType(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return COMPONENT_TYPES.register(id, () -> (builderOperator.apply(DataComponentType.builder())).build());
    }

    public static void registerComponentTypes() {
        InfinityMod.LOGGER.debug("Registering component types for " + MOD_ID);
        COMPONENT_TYPES.register();
    }

    //?} else {
    /*public static String DESTINATION = "key_destination";
    public static String BIOME_CONTENTS = "biome_contents";
    public static String COLOR = "key_color";
    public static String HUE = "hue";
    public static String DYE_COLOR = "dye_color";
    public static String CHARGE = "f4_charge";
    public static String SIZE_X = "f4_size_x";
    public static String SIZE_Y = "f4_size_y";
    *///?}
}