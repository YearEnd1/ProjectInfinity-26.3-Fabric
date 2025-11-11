package net.lerariemann.infinity.util.platform.fabric;

//? fabric {
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRenderHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.registry.core.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

public class ModFluidsFabric {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(MOD_ID, Registries.FLUID);
    public static final RegistrySupplier<IridescenceFlowing> IRIDESCENCE_FLOWING =
            FLUIDS.register("flowing_iridescence", IridescenceFlowing::new);
    public static final RegistrySupplier<IridescenceStill> IRIDESCENCE_STILL =
            FLUIDS.register("iridescence", IridescenceStill::new);

    public static void registerModFluids() {
        FLUIDS.register();
    }

    public static abstract class IridescenceFabric extends FlowingFluid {
        @Override
        public Fluid getFlowing() {
            return ModFluidsFabric.IRIDESCENCE_FLOWING.get();
        }
        @Override
        public Fluid getSource() {
            return ModFluidsFabric.IRIDESCENCE_STILL.get();
        }
        @Override
        public Item getBucket() {
            return ModItems.IRIDESCENCE_BUCKET.get();
        }

        @Override
        protected BlockState createLegacyBlock(FluidState state) {
            return ModBlocks.IRIDESCENCE.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
        }
        @Override
        protected boolean canConvertToSource(Level world) {
            return Iridescence.isInfinite(world);
        }
        @Override
        protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {
            BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
            Block.dropResources(state, world, pos, blockEntity);
        }
        @Override
        protected int getSlopeFindDistance(LevelReader world) {
            return 4;
        }
        @Override
        protected int getDropOff(LevelReader world) {
            return 1;
        }
        @Override
        protected boolean canBeReplacedWith(FluidState state, BlockGetter world, BlockPos pos, Fluid fluid, Direction direction) {
            return direction == Direction.DOWN && !this.isSame(fluid);
        }
        @Override
        public int getTickDelay(LevelReader world) {
            return 5;
        }
        @Override
        protected float getExplosionResistance() {
            return 100.0F;
        }
        @Override
        public boolean isSame(Fluid fluid) {
            return fluid == getSource() || fluid == getFlowing();
        }
    }
    public static class IridescenceStill extends IridescenceFabric {
        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
        @Override
        public int getAmount(FluidState state) {
            return 8;
        }
    }
    public static class IridescenceFlowing extends IridescenceFabric {
        public IridescenceFlowing() {
            super();
            this.registerDefaultState(this.getStateDefinition().any().setValue(LEVEL, 7));
        }
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }
        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }
        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class IridescenceVariantAttributeHandler implements FluidVariantAttributeHandler {
        @Override
        public Component getName(FluidVariant fluidVariant) {
            return Component.translatable("block.infinity.iridescence").withStyle(Style.EMPTY.withColor(Iridescence.getTimeBasedColor()));
        }
    }
    public static class IridescenceVariantRenderHandler implements FluidVariantRenderHandler {
        @Override
        public int getColor(FluidVariant fluidVariant, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
            return (view != null && pos != null) ? Iridescence.getPosBasedColor(pos) : Iridescence.getTimeBasedColor();
        }
    }
    public static class IridescenceRenderHandler implements FluidRenderHandler {
        @Override
        public TextureAtlasSprite[] getFluidSprites(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
            Function<ResourceLocation, TextureAtlasSprite> atlas = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS);
            TextureAtlasSprite overlaySprite = atlas.apply(Iridescence.OVERLAY_TEXTURE);
            TextureAtlasSprite[] sprites = new TextureAtlasSprite[overlaySprite == null ? 2 : 3];
            sprites[0] = atlas.apply(Iridescence.TEXTURE);
            sprites[1] = atlas.apply(Iridescence.FLOWING_TEXTURE);
            if (overlaySprite != null) sprites[2] = overlaySprite;
            return sprites;
        }

        @Override
        public int getFluidColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
            return (view != null && pos != null) ? Iridescence.getPosBasedColor(pos) : Iridescence.getTimeBasedColor();
        }
    }
}
//?}
