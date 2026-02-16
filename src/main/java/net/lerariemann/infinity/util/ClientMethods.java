package net.lerariemann.infinity.util;

import net.lerariemann.infinity.registry.core.ModStatusEffects;
import net.lerariemann.infinity.util.screen.F4Screen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

/** Mod methods that should only be loaded on the client (trying to load these on the server crashes it, don't do that).
 * @see InfinityMethods */
public interface ClientMethods {
    static void setF4Screen(Player player) {
        if (player.isLocalPlayer()) {
            Minecraft.getInstance().setScreen(F4Screen.of(player));
        }
    }

	static void tryApplySpecial(ModStatusEffects.SpecialEffect eff, int duration, int amplifier) {
		eff.tryApplySpecial(Minecraft.getInstance().player, duration, amplifier);
	}
}
