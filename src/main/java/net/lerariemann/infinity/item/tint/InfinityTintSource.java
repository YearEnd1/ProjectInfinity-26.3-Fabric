package net.lerariemann.infinity.item.tint;

//? if >1.21.4 {
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record InfinityTintSource(int defaultColour, boolean invert) implements ItemTintSource {
    public static final MapCodec<InfinityTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(InfinityTintSource::defaultColour),
                    Codec.BOOL.fieldOf("invert").orElse(false).forGetter(InfinityTintSource::invert)
            ).apply(instance, InfinityTintSource::new));

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        int res = (stack.has(ModComponentTypes.COLOR.get())) ?
            InfinityMethods.getItemColorFromComponents(stack, 1) :
            VersionMethods.opaque(defaultColour);
        return invert ? 0xFFFFFF ^ res : res;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }

}
//?}