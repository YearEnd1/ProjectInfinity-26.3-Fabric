package net.lerariemann.infinity.item;

import net.lerariemann.infinity.block.custom.AltarBlock;
import net.lerariemann.infinity.block.entity.ChromaticBlockEntity;
import net.lerariemann.infinity.block.entity.InfinityPortalBlockEntity;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.var.ModTags;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.var.ColorLogic;
import net.minecraft.core.BlockPos;
//? if >1.21 {
import net.minecraft.core.component.DataComponentPatch;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChromaticItem extends Item implements PortalDataHolder {
    public ChromaticItem(Properties settings) {
        super(settings);
    }
    @Override
    public
    //? if >1.21 {
    DataComponentPatch
    //?} else {
    /*CompoundTag
    *///?}
    getPortalComponents(InfinityPortalBlockEntity ipbe) {
        return ofColor(ipbe.getPortalColor());
    }
    @Override
    public Optional<
            //? if >1.21 {
            DataComponentPatch
             //?} else {
            /*CompoundTag
            *///?}
            > getIridComponents(ItemEntity entity) {
        Vec3 pos = entity.position();
        int hue = (int)(360*InfinityMethods.sampler.getValue(pos.x, pos.y, pos.z));
        return Optional.of(ofHue(hue));
    }

    @Override
    public ItemStack getStack() {
        return getDefaultInstance();
    }

    public static void playDing(Player player, float pitch) {
        player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.5f, pitch);
    }

    //? if >1.21 {
    static DataComponentPatch ofColor(int color) {
        return DataComponentPatch.builder()
                .set(ModComponentTypes.COLOR.get(), color)
                .remove(ModComponentTypes.DYE_COLOR.get())
                .remove(ModComponentTypes.HUE.get())
                .build();
    }
    static DataComponentPatch ofHue(int hue) {
        return DataComponentPatch.builder()
                .set(ModComponentTypes.COLOR.get(), ColorLogic.getPureHue(hue/360f))
                .remove(ModComponentTypes.DYE_COLOR.get())
                .set(ModComponentTypes.HUE.get(), hue)
                .build();
    }
    static DataComponentPatch ofDye(DyeColor dyeColor) {
        return DataComponentPatch.builder()
                .set(ModComponentTypes.COLOR.get(), ColorLogic.getChromaticColor(dyeColor))
                .set(ModComponentTypes.DYE_COLOR.get(), dyeColor.getName())
                .remove(ModComponentTypes.HUE.get())
                .build();
    }
    //?} else {
    /*static CompoundTag ofColor(int color) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt(ModComponentTypes.COLOR, color);
        return compoundTag;
    }
    static CompoundTag ofHue(int hue) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt(ModComponentTypes.COLOR, ColorLogic.getPureHue(hue/360f));
        compoundTag.putInt(ModComponentTypes.HUE, hue);
        return compoundTag;
    }
    static CompoundTag ofDye(DyeColor dyeColor) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt(ModComponentTypes.COLOR, ColorLogic.getChromaticColor(dyeColor));
        compoundTag.putString(ModComponentTypes.DYE_COLOR, dyeColor.getName());
        return compoundTag;
    }
    *///?}

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return super.useOn(context);
        return useOnBlock(player, context.getHand(), context.getLevel(), context.getClickedPos(), context.getItemInHand())
                ? InteractionResult.SUCCESS : super.useOn(context);
    }

    public boolean useOnBlock(Player player, InteractionHand hand, Level world, BlockPos pos, ItemStack currStack) {
        BlockState oldState = world.getBlockState(pos);
        int currColor = VersionMethods.getOrDefaultInt(currStack, ModComponentTypes.COLOR, 0xFFFFFF);
        int currHue = VersionMethods.getOrDefaultInt(currStack, ModComponentTypes.HUE, -1);
        if (player.isShiftKeyDown()) { //copy color
            ItemStack newStack = currStack.copy();
            int i = -1;
            if (world.getBlockEntity(pos) instanceof ChromaticBlockEntity cbe) i = cbe.getTint();
            else if (oldState.is(ModBlocks.ALTAR.get())) {
                int altarState = oldState.getValue(AltarBlock.COLOR);
                if (altarState == 0) i = ColorLogic.getChromaticColor(DyeColor.LIGHT_GRAY);
                else {
                    int hue = (altarState - 1) * (360 / (AltarBlock.numColors - 1));
                    if (!ColorLogic.matchesPureHue(currColor, hue)) {
                        //? if >1.21 {
                        newStack.applyComponentsAndValidate(ofHue(hue));
                        //?} else {
                        /*newStack.setTag(ofHue(hue));
                        *///?}
                    } else return false;
                }
            }
            else { //copy color from vanilla blocks
                DyeColor dyeColor = ColorLogic.getColorByState(oldState);
                if (dyeColor != null &&
                        !dyeColor.getName().equals(VersionMethods.getOrDefaultString(newStack, ModComponentTypes.DYE_COLOR, "null"))) {
                    //? if >1.21 {
                    newStack.applyComponentsAndValidate(ofDye(dyeColor));
                    //?} else {
                    /*newStack.setTag(ofDye(dyeColor));
                    *///?}
                }
                else return false;
            }
            if (i > 0) {
                if (currHue > 0) {
                    if (ColorLogic.matchesPureHue(i, currHue)) return false;
                    //? if >1.21 {
                    newStack.applyComponentsAndValidate(ofColor(i));
                    //?} else {
                    /*newStack.setTag(ofColor(i));
                    *///?}
                }
                else if (i != currColor) {
                    //? if >1.21 {
                    newStack.applyComponentsAndValidate(ofColor(i));
                    //?} else {
                    /*newStack.setTag(ofColor(i));
                    *///?}
                } else return false;
            }
            player.setItemInHand(hand, newStack);
            playDing(player, 0.5f);
            return true;
        }
        else { //paste color to blocks
            if (oldState.is(ModTags.IRIDESCENT_BLOCKS)) return false;
            boolean bl = VersionMethods.contains(currStack, ModComponentTypes.DYE_COLOR);
            BlockState state = ColorLogic.recolor(bl ? VersionMethods.getOrNullString(currStack, ModComponentTypes.DYE_COLOR) : "infinity:chromatic", oldState);
            if (state == null) return false;
            world.setBlockAndUpdate(pos, state);
            AtomicBoolean cancel = new AtomicBoolean(false);
            if (!bl && world.getBlockEntity(pos) instanceof ChromaticBlockEntity cbe) {
                if (currHue > 0) cbe.setColor(currHue, 255, 255, cancel);
                else cbe.setColor(currColor, cancel);
            }
            if (!cancel.get()) {
                playDing(player, 1f);
                return true;
            }
        }
        return false;
    }
}
