package net.lerariemann.infinity.util.platform.forge;

//? forge {
/*import net.lerariemann.infinity.iridescence.IridescenceLiquidBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class IridescenceLiquidBlockForge extends IridescenceLiquidBlock implements IClientBlockExtensions {
    public IridescenceLiquidBlockForge(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public @Nullable BlockPathTypes getBlockPathType(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @Nullable Mob mob) {
        return BlockPathTypes.WATER;
    }
}
*///?}
