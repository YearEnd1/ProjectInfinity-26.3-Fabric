package net.lerariemann.infinity.entity.client;

//? if >1.21.2 {
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
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
        public BlockState core;
        public int colour;
        public float squish;
        public int size = 1;

        @Override
        public int getColour() {
            return colour;
        }

        @Override
        public ResourceLocation getTexture() {
            return InfinityMethods.getId("textures/entity/slime.png");
        }
    }

    class Creeper extends SlimeRenderState implements TintedRenderState {
        public int colour;

        @Override
        public int getColour() {
            return colour;
        }

        @Override
        public ResourceLocation getTexture() {
            return InfinityMethods.getId("textures/entity/creeper.png");
        }
    }

    class Skeleton extends SlimeRenderState implements TintedRenderState {
        public int colour;

        @Override
        public int getColour() {
            return colour;
        }

        @Override
        public ResourceLocation getTexture() {
            return VersionMethods.id("textures/entity/skeleton/skeleton.png");
        }
    }
}
//?}
