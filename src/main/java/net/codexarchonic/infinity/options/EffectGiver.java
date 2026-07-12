package net.codexarchonic.infinity.options;

import net.codexarchonic.infinity.util.VersionMethods;
import net.codexarchonic.infinity.util.core.NbtUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Optional;

public record EffectGiver(Holder<MobEffect> id, int duration, int amplifier, int cooldown) {
    public static Holder<MobEffect> effectOf(String id) {
        Optional<Holder.Reference<MobEffect>> entry = VersionMethods.getOptionalFromRegistry(BuiltInRegistries.MOB_EFFECT, ResourceKey.create(Registries.MOB_EFFECT, VersionMethods.id(id)));
        if (entry.isEmpty() || entry.get().value() instanceof InstantenousMobEffect) return null;
        return entry.get();
    }

    public static EffectGiver of(CompoundTag data) {
        if (data.contains("id")) return new EffectGiver(effectOf(NbtUtils.getString(data, "id")),
                NbtUtils.getInt(data, "duration", 300),
                NbtUtils.getInt(data, "amplifier", 0),
                Math.min(NbtUtils.getInt(data, "cooldown", 100), 100));
        return new EffectGiver(null, 0,0,200);
    }

    public void tryGiveEffect(ServerPlayer player) {
        if (!isEmpty() && player.level().getGameTime() % cooldown == 0) {
            player.addEffect(new MobEffectInstance(id
                    //? if <1.21
                    /*.value()*/
                    , duration, amplifier));
        }
    }

    public boolean isEmpty() {
        return id == null;
    }
}
