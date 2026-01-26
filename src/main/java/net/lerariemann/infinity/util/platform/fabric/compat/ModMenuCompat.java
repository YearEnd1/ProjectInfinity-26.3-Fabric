package net.lerariemann.infinity.util.platform.fabric.compat;

//? fabric {
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.lerariemann.infinity.compat.ModCompat;
import net.lerariemann.infinity.util.platform.InfinityPlatform;

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
