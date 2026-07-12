package net.codexarchonic.infinity.util.platform.forge;

//? forge {
/*import net.codexarchonic.infinity.iridescence.IridescenceLiquidBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class IridescenceLiquidBlockForge extends IridescenceLiquidBlock implements IClientBlockExtensions {
    public IridescenceLiquidBlockForge(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public @Nullable BlockPathTypes getBlockPathType(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @Nullable Mob mob) {
        return BlockPathTypes.WATER;
    }
}
*///?}
