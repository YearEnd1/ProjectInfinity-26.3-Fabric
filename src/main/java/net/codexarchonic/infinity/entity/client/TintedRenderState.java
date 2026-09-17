package net.codexarchonic.infinity.entity.client;

//? if >1.21.2 {
//? if >26
//import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public interface TintedRenderState {
    int getColour();
    ResourceLocation getTexture();
    default float getAlpha() {
        return 1f;
    }

    class Slime extends SlimeRenderState implements TintedRenderState {
        //? if >26 {
        /*public final BlockModelRenderState coreModel = new BlockModelRenderState();
        *///?}
        public BlockState core;

        public int colour;

        @Override
        public int getColour() {
            return colour;
        }

        @Override
        public ResourceLocation getTexture() {
            return ChaosSlimeRenderer.TEXTURE;
        }
    }

    class Creeper extends CreeperRenderState implements TintedRenderState {
        public int colour;

        @Override
        public int getColour() {
            return colour;
        }

        @Override
        public ResourceLocation getTexture() {
            return ChaosCreeperRenderer.TEXTURE;
        }
    }

    class Skeleton extends SkeletonRenderState implements TintedRenderState {
        public int colour;

        @Override
        public int getColour() {
            return colour;
        }

        @Override
        public ResourceLocation getTexture() {
            return ChaosSkeletonRenderer.TEXTURE;
        }
    }
}
//?}
