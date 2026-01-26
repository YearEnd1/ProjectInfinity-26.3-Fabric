package net.lerariemann.infinity.registry.var;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.lerariemann.infinity.util.screen.F4Screen;
import net.lerariemann.infinity.util.screen.F4ScreenHandler;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

public class ModScreenHandlers {
    private static final DeferredRegister<MenuType<?>> SCREEN_HANDLERS =
            DeferredRegister.create(MOD_ID, Registries.MENU);

    public static final RegistrySupplier<MenuType<F4ScreenHandler>> F4 =
            SCREEN_HANDLERS.register("f4", () -> MenuRegistry.ofExtended(F4ScreenHandler::new));

    public static void register() {
        SCREEN_HANDLERS.register();
        //? if fabric {
        MenuScreens.register(F4.get(), F4Screen::new);
        //?}
    }
}
