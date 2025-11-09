package net.lerariemann.infinity.options;

import net.lerariemann.infinity.util.Methods;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public record EffectGiver(Holder<MobEffect> id, int duration, int amplifier, int cooldown) {
    public static Holder<MobEffect> effectOf(String id) {
        Holder<MobEffect> entry = BuiltInRegistries.MOB_EFFECT.getHolderOrThrow(ResourceKey.create(Registries.MOB_EFFECT, Methods.id(id)));
        if (entry == null || entry.value() instanceof InstantenousMobEffect) return null;
        return entry;
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
            player.addEffect(new MobEffectInstance(id, duration, amplifier));
        }
    }

    public boolean isEmpty() {
        return id == null;
    }
}
