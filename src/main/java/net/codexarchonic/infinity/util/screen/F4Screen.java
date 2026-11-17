package net.codexarchonic.infinity.util.screen;

import net.codexarchonic.infinity.util.InfinityMethods;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
//? if >1.21.4 {
import net.minecraft.client.renderer.RenderPipelines;
//?}
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import java.util.concurrent.atomic.AtomicInteger;

public class F4Screen extends AbstractContainerScreen<F4ScreenHandler> {
    EditBox widthValue;
    EditBox heightValue;

    public static F4Screen of(Player player) {
        F4ScreenHandler.Factory factory = new F4ScreenHandler.Factory(player);
        F4ScreenHandler handler = (F4ScreenHandler)factory.createMenu(0, player.getInventory(), player);
        return new F4Screen(handler, player.getInventory(), factory.getDisplayName());
    }

    public F4Screen(F4ScreenHandler screenHandler, Inventory playerInventory, Component text) {
        super(screenHandler, playerInventory, text);
        menu.addSlotListener(new ContainerListener() {
                    public void slotChanged(AbstractContainerMenu handlerx, int slotId, ItemStack stack) {
                    }

                    public void dataChanged(AbstractContainerMenu handlerx, int property, int value) {
                        if (property == 0) menu.width.set(value);
                        else menu.height.set(value);
                    }
                });
    }

    @Override
    protected void init() {
        super.init();
        widthValue = new EditBox(this.font, leftPos + 82, topPos + 30, 12, 8, Component.translatable("screen.infinity.f4.width"));
        heightValue = new EditBox(this.font, leftPos + 82, topPos + 57, 12, 8, Component.translatable("screen.infinity.f4.height"));
        setupValue(widthValue, menu.width);
        setupValue(heightValue, menu.height);
    }

    public void setupValue(EditBox value, AtomicInteger valueReference) {
        value.setTextColor(-1);
        value.setTextColorUneditable(-1);
        value.setBordered(false);
        value.setMaxLength(2);
        value.setValue(String.valueOf(valueReference.get()));
        //? if <26 {
        value.setFilter(string -> (string.isEmpty() ||
                (string.matches("[0-9]*")
                        && Integer.parseInt(string) > 0
                        && Integer.parseInt(string) < 22)));
        //?}
        //FIXME 26.1
        value.setX(leftPos + (valueReference.get() < 10 ? 85 : 82));
        value.setResponder(string -> {
            int v = string.isEmpty() ? 3 : Integer.parseInt(string);
            valueReference.set(v);
            value.setX(leftPos + ((string.length() < 2) ? 85 : 82));
        });
        addWidget(value);
    }

    //~ if >26 'render('->'extractRenderState(' {
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.drawString(this.font, Component.translatable("screen.infinity.f4.width"), leftPos + 59, topPos + 18, 4210752, false);
        guiGraphics.drawString(this.font, Component.translatable("screen.infinity.f4.height"), leftPos + 56, topPos + 45, 4210752, false);
        this.widthValue.render(guiGraphics, mouseX, mouseY, delta);
        this.heightValue.render(guiGraphics, mouseX, mouseY, delta);
        //~ if >26 'renderTooltip'->'extractTooltip'
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
    //~}

    @Override
    //~ if >26 'renderBg(GuiGraphics context, float delta, int mouseX, int mouseY)'->'extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta)'
    public void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        context.blit(
                //? if >1.21.4 {
                RenderPipelines.GUI_TEXTURED,
                //?}
                InfinityMethods.getId("textures/gui/f4.png"),
                leftPos, topPos,
                0, 0,
                this.imageWidth, this.imageHeight
                //? if >1.21.4 {
                , 256, 256
                //?}
        );
    }
}
