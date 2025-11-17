package net.lerariemann.infinity.util.platform.forge;

//? forge {
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
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FluidTypes {
    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, InfinityMod.MOD_ID);

    public static final Supplier<FluidType> IRIDESCENCE_TYPE = FLUID_TYPES.register("iridescence",
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
                    .pathType(BlockPathTypes.WATER)
                    .adjacentPathType(BlockPathTypes.WATER_BORDER)));

    public static FluidInteractionRegistry.InteractionInformation getIridescentInteraction(FluidType type) {
        return new FluidInteractionRegistry.InteractionInformation(
                (level, currentPos, relativePos, currentState) -> level.getFluidState(relativePos).getFluidType() == type,
                (level, currentPos, relativePos, currentState) -> {
            level.setBlockAndUpdate(currentPos, ForgeEventFactory.fireFluidPlaceBlockEvent(level, currentPos, currentPos,
                    currentState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() :
                            Iridescence.getRandomColorBlock(level,"glazed_terracotta").defaultBlockState()));
            level.levelEvent(1501, currentPos, 0);
        });
    }

    public static void registerFluidTypes(IEventBus bus) {
        FLUID_TYPES.register(bus);
    }
    public static void registerFluidInteractions(FMLCommonSetupEvent event) {
        FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), getIridescentInteraction(IRIDESCENCE_TYPE.get()));
    }

    public static class IridescentFluidType extends FluidType {
        public IridescentFluidType(Properties properties) {
            super(properties);
        }
        @Override
        public @Nullable BlockPathTypes getBlockPathType(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos,
                                                         @Nullable Mob mob, boolean canFluidLog) {
            return canFluidLog ? super.getBlockPathType(state, level, pos, mob, true) : null;
        }
        @Override
        public boolean canConvertToSource(@NotNull FluidState state, @NotNull LevelReader reader, @NotNull BlockPos pos) {
            if (reader instanceof Level level) {
                return Iridescence.isInfinite(level);
            }
            //Best guess fallback to default (true)
            return super.canConvertToSource(state, reader, pos);
        }
        @Override
        public boolean isVaporizedOnPlacement(@NotNull Level w, @NotNull BlockPos pos, @NotNull FluidStack stack) {
            return false;
        }
    }
}
*///?}
