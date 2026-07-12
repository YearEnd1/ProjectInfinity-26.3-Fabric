package net.codexarchonic.infinity.util.platform.fabric.compat;

//? fabric {
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.codexarchonic.infinity.compat.cloth.ClothConfigFactory;
import net.minecraft.client.gui.screens.Screen;

public class ModConfigFactory implements ConfigScreenFactory<Screen> {
    @Override
    public Screen create(Screen parent) {
        return ClothConfigFactory.create(parent);
    }
}
//?}
