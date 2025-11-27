package net.lerariemann.infinity.item.tint;

//? if >1.21.4 {
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record InfinityTintSource(int color) implements ItemTintSource {
    public static final MapCodec<InfinityTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(InfinityTintSource::color)).apply(instance, InfinityTintSource::new));

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        if (stack.has(ModComponentTypes.COLOR.get())) {
            return InfinityMethods.getItemColorFromComponents(stack, 1);
        } else {
            return VersionMethods.opaque(color);
        }
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }

}
//?}