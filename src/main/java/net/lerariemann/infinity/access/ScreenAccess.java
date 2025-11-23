package net.lerariemann.infinity.access;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

public interface ScreenAccess {
    <T extends GuiEventListener & Renderable & NarratableEntry> T infinity$addWidget(T widget);
}
