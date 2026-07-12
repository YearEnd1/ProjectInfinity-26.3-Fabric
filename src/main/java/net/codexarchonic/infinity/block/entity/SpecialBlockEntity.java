package net.codexarchonic.infinity.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SpecialBlockEntity extends BlockEntity {
    public SpecialBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public abstract int getTint();

    public abstract static class Boopable extends SpecialBlockEntity implements net.codexarchonic.infinity.block.custom.Boopable {
        public Boopable(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }

        public void sync() {
            setChanged();
            if (level != null) {
                net.codexarchonic.infinity.block.custom.Boopable.boop(level, worldPosition);
            }
        }
    }
}
