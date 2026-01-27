package net.lerariemann.infinity.mixin.options;

import net.lerariemann.infinity.options.InfinityOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
//? if >1.21.2 {
import net.minecraft.world.level.redstone.Orientation;
//?}
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin {
    @Shadow protected abstract void playNote(@Nullable Entity entity, BlockState state, Level level, BlockPos pos);

    @Inject(method = "neighborChanged", at = @At("HEAD"), cancellable = true)
    //? if >1.21.2 {
    void inj(BlockState state, Level level, BlockPos pos, Block neighborBlock, Orientation orientation, boolean movedByPiston, CallbackInfo ci) {
    //?} else {
    /*void inj(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving, CallbackInfo ci) {
    *///?}
        if (InfinityOptions.access(level).isHaunted()) {
            boolean bl = level.hasNeighborSignal(pos);
            if (bl != state.getValue(NoteBlock.POWERED)) {
                if (bl) {
                    playNote(null, state, level, pos);
                }

                level.setBlock(pos, state.setValue(NoteBlock.POWERED, bl), 0, 0);
                ci.cancel();
            }
        }
    }
}
