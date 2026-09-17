package net.codexarchonic.infinity.registry.var;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.codexarchonic.infinity.block.custom.AltarBlock;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
//~ if >26 'ParticleFactoryRegistry'->'ParticleProviderRegistry' {
//? if fabric {
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
//?}
//? if neoforge {
/*import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
*///?}
//? if forge {
/*import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
*///?}

//? if <1.21 {
/*import net.minecraft.client.particle.EnchantmentTableParticle;
*///?} else {
import net.minecraft.client.particle.FlyTowardsPositionParticle;
//?}

import java.util.ArrayList;
import java.util.List;

import static net.codexarchonic.infinity.InfinityMod.MOD_ID;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(MOD_ID, Registries.PARTICLE_TYPE);
    public static final List<RegistrySupplier<SimpleParticleType>> altarParticles = new ArrayList<>();

    static class Type extends SimpleParticleType {
        protected Type() {
            super(false);
        }
    }
    private static void register(String key) {
        RegistrySupplier<SimpleParticleType> v = PARTICLES.register(key, Type::new);
        altarParticles.add(v);
    }

    public static void registerServer() {
        for (int i = 0; i < AltarBlock.numColors; i++) register("altar_" + i);
        PARTICLES.register();
    }

    //? if fabric {
    public static void registerClientFabric() {
        for (RegistrySupplier<SimpleParticleType> p : altarParticles) {
            ParticleFactoryRegistry.getInstance().register(
    //?}
    //? if neoforge {
    /*public static void registerClientNeoforge(RegisterParticleProvidersEvent event) {
        for (RegistrySupplier<SimpleParticleType> p : altarParticles) {
            event.registerSpriteSet(
    *///?}
    //? if forge {
    /*public static void registerClientForge(RegisterParticleProvidersEvent event) {
        for (RegistrySupplier<SimpleParticleType> p : altarParticles) {
            event.registerSpriteSet(
    *///?}
            p.get(),
            //? if <1.21 {
            /*EnchantmentTableParticle.Provider::new
            *///?} else {
            FlyTowardsPositionParticle.EnchantProvider::new
             //?}
            );
        }
    }
}
//~}