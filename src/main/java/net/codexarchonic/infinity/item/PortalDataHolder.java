package net.codexarchonic.infinity.item;

import net.codexarchonic.infinity.block.entity.InfinityPortalBlockEntity;
import net.codexarchonic.infinity.registry.core.ModComponentTypes;
import net.codexarchonic.infinity.util.VersionMethods;
import net.codexarchonic.infinity.util.InfinityMethods;
//? if >1.21 {
import net.minecraft.core.component.DataComponentPatch;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public interface PortalDataHolder {
    @Nullable
    default ResourceLocation getDestination(ItemStack stack) {
        return VersionMethods.getDimensionResourceLocation(stack);
    }

    static boolean isDestinationRandom(@Nullable ResourceLocation id) {
        return (id != null && id.toString().equals(InfinityMethods.ofRandomDim));
    }

    default ResourceLocation getDestinationParsed(ItemStack stack, Level world) {
        ResourceLocation id = getDestination(stack);
        //~ if >26 'random'->'getRandom()'
        return (isDestinationRandom(id)) ? InfinityMethods.getRandomId(world.random) : id;
    }

    //? if >1.21 {
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
    //?} else {
    /*static Optional<CompoundTag> addPortalComponents(Item item, InfinityPortalBlockEntity ipbe) {
        if (item instanceof PortalDataHolder pdh)
            return Optional.of(pdh.getPortalComponents(ipbe));
        return Optional.empty();
    }
    default CompoundTag getPortalComponents(InfinityPortalBlockEntity ipbe) {
        var compound = new CompoundTag();
        compound.putInt(ModComponentTypes.COLOR, ipbe.getPortalColor());
        return compound;
    }

    static Optional<CompoundTag> getIridComponents(Item item, ItemEntity entity) {
        if (item instanceof PortalDataHolder pdh)
            return pdh.getIridComponents(entity);
        return Optional.empty();
    }
    default Optional<CompoundTag> getIridComponents(ItemEntity entity) {
        return Optional.empty();
    }

    default ItemStack withPortalData(InfinityPortalBlockEntity ipbe) {
        ItemStack stack = getStack();
        CompoundTag tag = stack.getOrCreateTag();
        var portalComponents = getPortalComponents(ipbe);
        if (portalComponents.contains(ModComponentTypes.COLOR))
            tag.putInt(ModComponentTypes.COLOR, portalComponents.getInt(ModComponentTypes.COLOR));
        if (portalComponents.contains(ModComponentTypes.DESTINATION))
            tag.putString(ModComponentTypes.DESTINATION, portalComponents.getString(ModComponentTypes.DESTINATION));
        return stack;
    }
    *///?}

    ItemStack getStack();

    interface Destinable extends PortalDataHolder {
        //? if >1.21 {
        @Override
        default DataComponentPatch getPortalComponents(InfinityPortalBlockEntity ipbe) {
            return DataComponentPatch.builder()
                    .set(ModComponentTypes.DESTINATION.get(), ipbe.getDimension())
                    .set(ModComponentTypes.COLOR.get(), ipbe.getPortalColor())
                    .build();
        }
        //?} else {
        /*@Override
        default CompoundTag getPortalComponents(InfinityPortalBlockEntity ipbe) {
            var compound = new CompoundTag();
            compound.putInt(ModComponentTypes.COLOR, ipbe.getPortalColor());
            compound.putString(ModComponentTypes.DESTINATION, ipbe.getDimension().toString());
            return compound;
        }
        *///?}
    }
}
