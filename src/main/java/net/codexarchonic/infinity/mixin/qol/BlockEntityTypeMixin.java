package net.codexarchonic.infinity.mixin.qol;

import net.codexarchonic.infinity.access.BlockEntityTypeAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin<T extends BlockEntity> implements BlockEntityTypeAccess<T> {
    @Shadow @Final private BlockEntityType.BlockEntitySupplier<? extends T> factory;

    @Shadow @Final private Set<Block> validBlocks;

    @Override
    public BlockEntityType.BlockEntitySupplier<? extends T> infinity$getFactory() {
        return factory;
    }

    @Override
    public Set<Block> infinity$getBlocks() {
        return validBlocks;
    }
}
