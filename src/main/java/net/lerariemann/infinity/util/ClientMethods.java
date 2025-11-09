package net.lerariemann.infinity.util;

import net.lerariemann.infinity.util.screen.F4Screen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

/** Mod methods that should only be loaded on the client (trying to load these on the server crashes it, don't do that).
 * @see InfinityMethods */
public class ClientMethods {
    public static void setF4Screen(Player player) {
        if (player instanceof LocalPlayer clientPlayer) {
            Minecraft.getInstance().setScreen(F4Screen.of(clientPlayer));
        }
    }
}
