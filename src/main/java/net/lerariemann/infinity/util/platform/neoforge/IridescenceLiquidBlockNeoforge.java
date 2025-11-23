package net.lerariemann.infinity.util.platform.neoforge;

//? neoforge {
import net.lerariemann.infinity.iridescence.IridescenceLiquidBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class IridescenceLiquidBlockNeoforge extends IridescenceLiquidBlock implements IBlockExtension {
    public IridescenceLiquidBlockNeoforge(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public PathType getBlockPathType(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @Nullable Mob mob) {
        return PathType.WATER;
    }
}
//?}
