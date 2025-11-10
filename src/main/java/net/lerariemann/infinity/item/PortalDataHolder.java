package net.lerariemann.infinity.item;

import net.lerariemann.infinity.block.entity.InfinityPortalBlockEntity;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.util.BackportMethods;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface PortalDataHolder {
    @Nullable
    default ResourceLocation getDestination(ItemStack stack) {
        return BackportMethods.getDimensionIdentifier(stack);
    }

    static boolean isDestinationRandom(ResourceLocation id) {
        return (id != null && id.toString().equals(InfinityMethods.ofRandomDim));
    }

    default ResourceLocation getDestinationParsed(ItemStack stack, Level world) {
        ResourceLocation id = getDestination(stack);
        return (isDestinationRandom(id)) ? InfinityMethods.getRandomId(world.random) : id;
    }

    static Optional<DataComponentPatch> addPortalComponents(Item item, InfinityPortalBlockEntity ipbe) {
        if (item instanceof PortalDataHolder pdh)
            return Optional.of(pdh.getPortalComponents(ipbe));
        return Optional.empty();
    }
    default DataComponentPatch getPortalComponents(InfinityPortalBlockEntity ipbe) {
        return DataComponentPatch.builder()
                .set(ModComponentTypes.COLOR.get(), ipbe.getPortalColor())
                .build();
    }

    static Optional<DataComponentPatch> getIridComponents(Item item, ItemEntity entity) {
        if (item instanceof PortalDataHolder pdh)
            return pdh.getIridComponents(entity);
        return Optional.empty();
    }
    default Optional<DataComponentPatch> getIridComponents(ItemEntity entity) {
        return Optional.empty();
    }

    default ItemStack withPortalData(InfinityPortalBlockEntity ipbe) {
        ItemStack stack = getStack();
        stack.applyComponentsAndValidate(getPortalComponents(ipbe));
        return stack;
    }

    ItemStack getStack();

    interface Destinable extends PortalDataHolder {
        @Override
        default DataComponentPatch getPortalComponents(InfinityPortalBlockEntity ipbe) {
            return DataComponentPatch.builder()
                    .set(ModComponentTypes.DESTINATION.get(), ipbe.getDimension())
                    .set(ModComponentTypes.COLOR.get(), ipbe.getPortalColor())
                    .build();
        }
    }
}
