package net.lerariemann.infinity.iridescence;

import dev.architectury.core.block.ArchitecturyLiquidBlock;
import net.lerariemann.infinity.item.PortalDataHolder;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.util.core.RandomProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import java.util.function.Supplier;

public class IridescenceLiquidBlock extends ArchitecturyLiquidBlock {
    public IridescenceLiquidBlock(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        super.entityInside(state, world, pos, entity);
        if (world.getFluidState(pos).getAmount() > 3 && world instanceof ServerLevel w) switch (entity) {
            case Player player -> Iridescence.tryBeginJourney(player, RandomProvider.ruleInt("iridescenceContactLevel"), false);
            case Mob ent -> Iridescence.tryApplyEffect(ent);
            case ItemEntity item -> {
                if (!Iridescence.isIridescentItem(item.getItem()) && item.getOwner() instanceof LivingEntity le &&
                !Iridescence.getPhase(le).equals(Iridescence.Phase.INITIAL))
                    ModItemFunctions.checkCollisionRecipes(w, item, ModItemFunctions.IRIDESCENCE_CRAFTING_TYPE.get(),
                            i -> PortalDataHolder.getIridComponents(i, item));
            }
            default -> {}
        }
    }
}
