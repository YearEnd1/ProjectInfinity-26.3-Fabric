package net.codexarchonic.infinity.access;

import java.util.Set;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface BlockEntityTypeAccess<T extends BlockEntity> {
    BlockEntityType.BlockEntitySupplier<? extends T> infinity$getFactory();
    Set<Block> infinity$getBlocks();
}
