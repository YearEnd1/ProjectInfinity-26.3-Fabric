package net.lerariemann.infinity.util.platform.forge;

//? forge {
/*import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
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
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
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
                    .adjacentPathType(BlockPathTypes.WATER_BORDER)) {

                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {

                        @Override
                        public @NonNull ResourceLocation getStillTexture() {
                            return Iridescence.TEXTURE;
                        }

                        @Override
                        public @NonNull ResourceLocation getFlowingTexture() {
                            return Iridescence.FLOWING_TEXTURE;
                        }

                        @Override
                        public int getTintColor(@NonNull FluidState state, @NonNull BlockAndTintGetter getter, @NonNull BlockPos pos) {
                            return VersionMethods.opaque(Iridescence.getPosBasedColor(pos));
                        }
                    });
                }
            });

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
        public @Nullable BlockPathTypes getBlockPathType(@NonNull FluidState state, @NonNull BlockGetter level, @NonNull BlockPos pos,
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
