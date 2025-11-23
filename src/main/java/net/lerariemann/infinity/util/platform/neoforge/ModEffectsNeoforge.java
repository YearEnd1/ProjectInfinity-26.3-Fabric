package net.lerariemann.infinity.util.platform.neoforge;

//? neoforge {
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.iridescence.IridescentCooldownEffect;
import net.lerariemann.infinity.iridescence.IridescentEffect;
import net.lerariemann.infinity.registry.core.ModStatusEffects;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffectsNeoforge {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, InfinityMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        ModStatusEffects.IRIDESCENT_EFFECT = EFFECTS.register("iridescence",
                () -> new IridescentEffect(MobEffectCategory.NEUTRAL, 0xFF66FF)).getDelegate();
        ModStatusEffects.IRIDESCENT_SETUP = EFFECTS.register("iridescent_setup",
                () -> new IridescentEffect.Setup(MobEffectCategory.NEUTRAL, 0xFF00FF)).getDelegate();
        ModStatusEffects.IRIDESCENT_COOLDOWN = EFFECTS.register("iridescent_cooldown",
                () -> new IridescentCooldownEffect(MobEffectCategory.NEUTRAL, 0x884488)).getDelegate();
        ModStatusEffects.AFTERGLOW = EFFECTS.register("afterglow",
                ModStatusEffects::getAfterglowInstanceForReg).getDelegate();
        EFFECTS.register(eventBus);
    }
}
//?}
