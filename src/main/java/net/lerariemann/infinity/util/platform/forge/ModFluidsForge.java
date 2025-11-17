package net.lerariemann.infinity.util.platform.forge;

//? forge {
/*import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.function.Consumer;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

public class ModFluidsForge {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(MOD_ID, Registries.FLUID);

    public static final RegistrySupplier<ForgeFlowingFluid.Flowing> IRIDESCENCE_FLOWING =
            FLUIDS.register("flowing_iridescence", () -> new ForgeFlowingFluid.Flowing(iridProp()));
    public static final RegistrySupplier<ForgeFlowingFluid.Source> IRIDESCENCE_STILL =
            FLUIDS.register("iridescence", () -> new ForgeFlowingFluid.Source(iridProp()));

    public static ForgeFlowingFluid.Properties iridProp() {
        return (new ForgeFlowingFluid.Properties(FluidTypes.IRIDESCENCE_TYPE,
                IRIDESCENCE_STILL, IRIDESCENCE_FLOWING))
                .bucket(ModItems.IRIDESCENCE_BUCKET)
                .block(ModBlocks.IRIDESCENCE);
    }

    public static void registerModFluids(IEventBus bus) {
        FLUIDS.register();
    }
}
*///?}
