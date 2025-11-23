package net.lerariemann.infinity.util.screen;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.lerariemann.infinity.item.F4Item;
import net.lerariemann.infinity.registry.core.ModComponentTypes;
import net.lerariemann.infinity.registry.var.ModPayloads;
import net.lerariemann.infinity.registry.var.ModScreenHandlers;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

//? if >1.21 {
import net.minecraft.core.component.DataComponentMap;
//?}
//? if neoforge {
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
//?}
@MethodsReturnNonnullByDefault
public class F4ScreenHandler extends AbstractContainerMenu {
    public final Inventory playerInventory;
    public final ItemStack stack;
    public final int slot;
    public AtomicInteger width;
    public AtomicInteger height;

    public F4ScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf packet) {
        this(syncId, playerInventory, packet.readUtf(), packet.readVarInt());
    }
    public F4ScreenHandler(int syncId, Inventory playerInventory, String destination, int slot) {
        super(ModScreenHandlers.F4.get(), syncId);
        this.playerInventory = playerInventory;
        this.slot = slot;
        stack = playerInventory.getItem(slot);
        width = new AtomicInteger(VersionMethods.getOrDefaultInt(stack, ModComponentTypes.SIZE_X, 3));
        height = new AtomicInteger(VersionMethods.getOrDefaultInt(stack, ModComponentTypes.SIZE_Y, 3));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void removed(Player player) {
        ItemStack st = stack.copy();
        //? if >1.21 {
        st.applyComponents(DataComponentMap.builder()
                .set(ModComponentTypes.SIZE_X.get(), Math.clamp(width.get(), 1, 21))
                .set(ModComponentTypes.SIZE_Y.get(), Math.clamp(height.get(), 1, 21))
                .build());
         //?} else {
        /*CompoundTag nbtCompound = new CompoundTag();
        nbtCompound.putInt(ModComponentTypes.SIZE_X, Mth.clamp(width.get(), 1, 21));
        nbtCompound.putInt(ModComponentTypes.SIZE_Y, Mth.clamp(height.get(), 1, 21));
        stack.setTag(nbtCompound);
        *///?}
        playerInventory.setItem(slot, st);
        super.removed(player);
        if (player instanceof LocalPlayer) {
            new ModPayloads.F4UpdateC2SPayload(slot, width.get(), height.get()).send();
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }
    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    public static class Factory implements ExtendedMenuProvider {
        ItemStack f4;
        int slot;
        String destination;

        public Factory(Player player) {
            f4 = player.getItemInHand(InteractionHand.MAIN_HAND);
            slot = player.getInventory().
            //? if >1.21.4 {
            getSelectedSlot();
            //?} else {
            /*selected;
            *///?}
            ResourceLocation id = VersionMethods.getDimensionIdentifier(f4);
            destination = id == null ? "" : id.toString();
        }

        @Override
        public void saveExtraData(FriendlyByteBuf packetByteBuf) {
            packetByteBuf.writeUtf(destination);
            packetByteBuf.writeVarInt(slot);
        }

        @Override
        public Component getDisplayName() {
            return F4Item.getDimensionTooltip(destination.isEmpty() ? null : VersionMethods.id(destination));
        }

        @Override
        public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
            return new F4ScreenHandler(syncId, playerInventory, destination, slot);
        }
    }
}
