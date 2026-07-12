package net.codexarchonic.infinity.util.platform.fabric.compat;

//? fabric {
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.codexarchonic.infinity.compat.ModCompat;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        //Display Cloth Config screen if mod present, else error.
        if (ModCompat.CLOTH_CONFIG) return new ModConfigFactory();
        else {
            return parent -> null;
        }
    }
}
//?}
