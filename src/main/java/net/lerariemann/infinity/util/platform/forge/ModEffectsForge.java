package net.lerariemann.infinity.util.platform.forge;

//? forge {
/*import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.iridescence.IridescentCooldownEffect;
import net.lerariemann.infinity.iridescence.IridescentEffect;
import net.lerariemann.infinity.registry.core.ModStatusEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffectsForge {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, InfinityMod.MOD_ID);

    public static RegistryObject<IridescentEffect> IRIDESCENT_EFFECT;
    public static RegistryObject<IridescentEffect.Setup> IRIDESCENT_SETUP;
    public static RegistryObject<IridescentCooldownEffect> IRIDESCENT_COOLDOWN;
    public static RegistryObject<MobEffect> AFTERGLOW;


    public static void register(IEventBus eventBus) {
        IRIDESCENT_EFFECT = EFFECTS.register("iridescence",
                () -> new IridescentEffect(MobEffectCategory.NEUTRAL, 0xFF66FF));
        IRIDESCENT_SETUP = EFFECTS.register("iridescent_setup",
                () -> new IridescentEffect.Setup(MobEffectCategory.NEUTRAL, 0xFF00FF));
        IRIDESCENT_COOLDOWN = EFFECTS.register("iridescent_cooldown",
                () -> new IridescentCooldownEffect(MobEffectCategory.NEUTRAL, 0x884488));
        AFTERGLOW = EFFECTS.register("afterglow",
                ModStatusEffects::getAfterglowInstanceForReg);
        EFFECTS.register(eventBus);
    }
}
*///?}
