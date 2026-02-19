package net.lerariemann.infinity.util.platform.neoforge;

//? neoforge {
/*import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class FluidTypes {
    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, InfinityMod.MOD_ID);

    public static final DeferredHolder<FluidType, FluidType> IRIDESCENCE_TYPE = FLUID_TYPES.register("iridescence",
            () -> new IridescentFluidType(FluidType.Properties.create()
            .descriptionId("fluid.infinity.iridescence")
                    .fallDistanceModifier(0F)
                    .canExtinguish(true)
                    .canConvertToSource(true)
                    .supportsBoating(true)
                    .canSwim(true)
                    .canHydrate(true)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                    .pathType(PathType.WATER)
                    .adjacentPathType(PathType.WATER_BORDER)));

    public static FluidInteractionRegistry.InteractionInformation getIridescentInteraction(FluidType type) {
        return new FluidInteractionRegistry.InteractionInformation(
                (level, currentPos, relativePos, currentState) -> level.getFluidState(relativePos).getFluidType() == type,
                (level, currentPos, relativePos, currentState) -> {
            level.setBlockAndUpdate(currentPos, EventHooks.fireFluidPlaceBlockEvent(level, currentPos, currentPos,
                    currentState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() :
                            Iridescence.getRandomColorBlock(level,"glazed_terracotta").defaultBlockState()));
            level.levelEvent(1501, currentPos, 0);
        });
    }

    public static void registerFluidTypes(IEventBus bus) {
        FLUID_TYPES.register(bus);
    }
    public static void registerFluidInteractions(FMLCommonSetupEvent event) {
        FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), getIridescentInteraction(IRIDESCENCE_TYPE.value()));
    }

    public static class IridescentFluidType extends FluidType {
        public IridescentFluidType(Properties properties) {
            super(properties);
        }
        @Override
        public PathType getBlockPathType(@NonNull FluidState state, @NonNull BlockGetter level, @NonNull BlockPos pos,
                                             @Nullable Mob mob, boolean canFluidLog) {
            return canFluidLog ? super.getBlockPathType(state, level, pos, mob, true) : null;
        }
        @Override
        public boolean canConvertToSource(@NonNull FluidState state, @NonNull LevelReader reader, @NonNull BlockPos pos) {
            if (reader instanceof Level level) {
                return Iridescence.isInfinite(level);
            }
            //Best guess fallback to default (true)
            return super.canConvertToSource(state, reader, pos);
        }
        @Override
        public boolean isVaporizedOnPlacement(@NonNull Level w, @NonNull BlockPos pos, @NonNull FluidStack stack) {
            return false;
        }
    }
}
*///?}
