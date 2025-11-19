package net.lerariemann.infinity.access;

import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.RandomProvider;
import net.lerariemann.infinity.util.teleport.WarpLogic;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.lerariemann.infinity.registry.var.ModCriteria;

public interface Timebombable {
    void infinity$timebomb();
    boolean infinity$tryRestore();

    boolean infinity$isTimebombed();
    int infinity$getTimebombProgress();

    static int getCooldownTicks() {
        int i = RandomProvider.ruleInt("resetChargeCooldown");
        if (i <= 0) i = Integer.MAX_VALUE;
        return i;
    }

    default void tickTimebombProgress(ServerPlayer player) {
        int i = infinity$getTimebombProgress();
        if (i > 3540) {
            WarpLogic.respawnAlive(player);
        }
        else if (i > 3500) {
            ModCriteria.WHO_REMAINS.get().trigger(player);
        }
        else if (i > 200) {
            if (i%4 == 0) {
                Registry<DamageType> r = player.level().getServer().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
                Holder<DamageType> entry = r.wrapAsHolder(VersionMethods.getFromRegistry(r, InfinityMethods.getId("world_ceased")));
                player.hurt(new DamageSource(entry), i > 400 ? 2.0f : 1.0f);
            }
        }
    }
}
