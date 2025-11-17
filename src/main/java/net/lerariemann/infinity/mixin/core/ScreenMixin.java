package net.lerariemann.infinity.mixin.core;

import net.lerariemann.infinity.access.ScreenAccess;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Screen.class)
public abstract class ScreenMixin implements ScreenAccess {
    @Shadow
    protected abstract <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget);

    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry> T addWidget(T widget) {
        return addRenderableWidget(widget);
    }
}
