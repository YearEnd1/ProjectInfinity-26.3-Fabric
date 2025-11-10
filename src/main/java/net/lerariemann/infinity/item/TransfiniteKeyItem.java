package net.lerariemann.infinity.item;

import net.lerariemann.infinity.block.entity.InfinityPortalBlockEntity;
import net.lerariemann.infinity.options.PortalColorApplier;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.util.BackportMethods;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.Methods;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
//? if >1.21 {
import net.minecraft.core.component.DataComponentPatch;
//?}
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TransfiniteKeyItem extends Item implements PortalDataHolder.Destinable {
    public TransfiniteKeyItem(Properties settings) {
        super(settings);
    }

    @Override
    public DataComponentPatch getPortalComponents(InfinityPortalBlockEntity ipbe) {
        ResourceLocation id = ipbe.getDimension();
        int color = (ipbe.getLevel() instanceof ServerLevel w) ?
                PortalColorApplier.of(id, w.getServer()).apply(BlockPos.ZERO) :
                (int)InfinityMethods.getNumericFromId(id);
        return DataComponentPatch.builder()
                .set(ModComponentTypes.DESTINATION.get(), id)
                .set(ModComponentTypes.COLOR.get(), color & 0xFFFFFF)
                .build();
    }

    @Override
    public Optional<DataComponentPatch> getIridComponents(ItemEntity entity) {
        return Optional.of(DataComponentPatch.builder()
                .remove(ModComponentTypes.DESTINATION.get())
                .remove(ModComponentTypes.COLOR.get())
                .build());
    }

    @NotNull
    @Override
    public ResourceLocation getDestination(ItemStack stack) {
        return Objects.requireNonNullElse(BackportMethods.getDimensionIdentifier(stack),
                Methods.id(InfinityMethods.ofRandomDim)); // no destination component -> randomize
    }

    @Override
    public ItemStack getStack() {
        return getDefaultInstance();
    }

    @Override
    public void appendHoverText(ItemStack stack,
                                //? if >1.21 {
                                TooltipContext
                                //?} else {
                                /*Level
                                *///?}
                                        context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        ResourceLocation dimension = BackportMethods.getDimensionIdentifier(stack);
        MutableComponent mutableText = (dimension != null)
                ? InfinityMethods.getDimensionNameAsText(dimension)
                : Component.translatable("tooltip.infinity.key.randomise");
        tooltip.add(mutableText.withStyle(ChatFormatting.GRAY));
    }
}
