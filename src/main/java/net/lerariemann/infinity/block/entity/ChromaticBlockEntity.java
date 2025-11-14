package net.lerariemann.infinity.block.entity;

import net.lerariemann.infinity.block.custom.Boopable;
import net.lerariemann.infinity.registry.core.ModBlockEntities;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.lerariemann.infinity.util.var.ColorLogic;
import net.minecraft.core.BlockPos;
//? if >1.21 {
/*import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
*///?}
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.contents.NbtContents;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChromaticBlockEntity extends TintableBlockEntity {
    public short hue;
    public short saturation;
    public short brightness;
    public int color;
    public ChromaticBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHROMATIC.get(), pos, state);
        hue = 0;
        saturation = 0;
        brightness = 255;
    }

    public void setColor(int colorHex) {
        setColor(colorHex, null);
    }

    public void setColor(int hue, int saturation, int brightness, @Nullable AtomicBoolean cancel) {
        if (cancel != null && this.hue == hue && this.saturation == saturation && this.brightness == brightness) {
            cancel.set(true);
            return;
        }
        this.hue = (short)hue;
        this.saturation = (short)saturation;
        this.brightness = (short)brightness;
        updateColor();
        sync();
    }

    public void setColor(int colorHex, @Nullable AtomicBoolean cancel) {
        if (cancel != null && colorHex == color) {
            cancel.set(true);
            return;
        }
        Color c = (new Color(colorHex));
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        hue = (short)(hsb[0] * 360);
        saturation = (short)(hsb[1] * 255);
        brightness = (short)(hsb[2] * 255);
        color = colorHex;
        sync();
    }
    public void updateColor() {
        color = Color.HSBtoRGB(hue / 360f, saturation / 255f, brightness / 255f) & 0xFFFFFF;
    }

    //? if >1.21 {
    /*@Override
    protected void collectImplicitComponents(DataComponentMap.Builder componentMapBuilder) {
        super.collectImplicitComponents(componentMapBuilder);
        componentMapBuilder.set(ModComponentTypes.COLOR.get(), color);
    }
    @Override
    protected void applyImplicitComponents(DataComponentInput components) {
        super.applyImplicitComponents(components);
        setColor(components.getOrDefault(ModComponentTypes.COLOR.get(), 0xFFFFFF));
    }

    public static DataComponentMap asMap(int i) {
        return DataComponentMap.builder()
                .set(ModComponentTypes.COLOR.get(), i)
                .build();
    }
    public DataComponentMap asMap() {
        return asMap(getTint());
    }
    *///?} else {
    public CompoundTag asMap() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt(ModComponentTypes.COLOR, getTint());
        return compoundTag;
    }
    //?}
    public short offset(short orig, short amount, AtomicBoolean cancel) {
        if (amount < 0 ? orig == 0 : orig == 255) {
            cancel.set(true);
            return orig;
        }
        orig += amount;
        if (orig < 0) orig = 0;
        else if (orig > 255) orig = 255;
        return orig;
    }
    public boolean onUse(Level world, BlockPos pos, ItemStack stack) {
        SoundEvent event = SoundEvents.NOTE_BLOCK_GUITAR.value();
        float pitch = -1f;
        float volume = 1f;
        AtomicBoolean cancel = new AtomicBoolean(false);

        if (stack.is(Items.AMETHYST_SHARD)) {
            saturation = offset(saturation, (short) 16, cancel);
            pitch = saturation / 255f;
        }
        else if (stack.is(ModItems.FOOTPRINT.get())) {
            saturation = offset(saturation, (short) -16, cancel);
            pitch = saturation / 255f;
        }
        else if (stack.is(ModItems.WHITE_MATTER.get())) {
            brightness = offset(brightness, (short) 16, cancel);
            pitch = brightness / 255f;
        }
        else if (stack.is(ModItems.BLACK_MATTER.get())) {
            brightness = offset(brightness, (short) -16, cancel);
            pitch = brightness / 255f;
        }
        else if (stack.getItem() instanceof DyeItem dye) {
            setColor(ColorLogic.getChromaticColor(dye.getDyeColor()), cancel);
            event = SoundEvents.DYE_USE;
        }
        else return false;
        if (cancel.get()) return false; //block was already at extremal saturation / brightness, no need for side effects
        updateColor();
        pitch = pitch < 0 ? 1f : 0.5f + 1.5f * pitch; //scaling for Minecraft's sound system
        if (!world.isClientSide()) world.playSound(null, pos, event, SoundSource.BLOCKS, volume, pitch);
        sync();
        return true;
    }

    public void onIridStarUse(boolean reverse) {
        if (reverse) {
            hue -= 10;
            if (hue < 0) hue += 360;
        }
        else {
            hue += 10;
            if (hue > 360) hue -= 360;
        }
        updateColor();
        sync();
    }

    void sync() {
        setChanged();
        if (level != null) {
            Boopable.boop(level, worldPosition);
        }
    }

    @Override
    //? if >1.21 {
    /*public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
    *///?} else {
    public void load(CompoundTag nbt) {
        super.load(nbt);
        //?}
        if (nbt.getTagType("color") == Tag.TAG_INT)
            setColor(nbt.getInt("color"));
        else if (nbt.getTagType("color") == Tag.TAG_COMPOUND) {
            CompoundTag color = NbtUtils.getCompound(nbt, "color");
            hue = NbtUtils.getShort(color, "h");
            saturation = NbtUtils.getShort(color, "s");
            brightness = NbtUtils.getShort(color, "b");
            updateColor();
        }
    }
    @Override
    protected void saveAdditional(CompoundTag nbt
            //? if >1.21
            /*, HolderLookup.Provider registryLookup*/
    ) {
        super.saveAdditional(nbt
                //? if >1.21
                /*, registryLookup*/
        );
        CompoundTag color = new CompoundTag();
        color.putShort("h", hue);
        color.putShort("s", saturation);
        color.putShort("b", brightness);
        nbt.put("color", color);
    }

    public int getTint() {
        return color;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public CompoundTag getUpdateTag(
            //? if >1.21
            /*HolderLookup.Provider registryLookup*/
    ) {
        return saveWithoutMetadata(
                //? if >1.21
                /*registryLookup*/
        );
    }
}
