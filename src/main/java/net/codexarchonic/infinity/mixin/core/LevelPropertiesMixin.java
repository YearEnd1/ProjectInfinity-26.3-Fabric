package net.codexarchonic.infinity.mixin.core;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;
import java.util.function.Consumer;

/** Removes infinite dimensions from level.dat to allow the mod to change and erase them easily without trailing data. */
@Mixin(PrimaryLevelData.class)
public class LevelPropertiesMixin {
    @WrapOperation(method = "setTagData", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"))
    void removeDimensionsFromLevelDat(Optional<Tag> instance, Consumer<? super Tag> action, Operation<Void> original) {
        if (instance.isEmpty()) return;
        CompoundTag c = ((CompoundTag) instance.get());
        if (c.contains("dimensions")) {
            CompoundTag d = NbtUtils.getCompound(c, "dimensions");
            CompoundTag newD = new CompoundTag();
            NbtUtils.keys(d).stream().filter(s -> !s.startsWith(InfinityMod.MOD_ID + ":")).forEach(key -> newD.put(key, d.get(key)));
            c.put("dimensions", newD);
        }
        action.accept(c);
    }
}
