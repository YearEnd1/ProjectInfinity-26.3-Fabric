package net.codexarchonic.infinity;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.codexarchonic.infinity.item.F4Item;
import net.codexarchonic.infinity.registry.core.ModEntities;
import net.codexarchonic.infinity.registry.core.ModItems;
import net.codexarchonic.infinity.registry.payload.ModPayloads;
import net.codexarchonic.infinity.registry.payload.c2s.F4DeployC2SPayload;
import net.codexarchonic.infinity.registry.var.ModScreenHandlers;
import net.codexarchonic.infinity.util.VersionMethods;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

//? if >1.21.9 {
/*import static net.minecraft.client.KeyMapping.Category.MISC;
*///?}

public class InfinityModClient {

    //? if <1.21.9 {
    public static String MISC = "key.categories.misc";
    //?}

    public static KeyMapping f4ConfigKey = new KeyMapping("key.infinity.f4",
            //~ if >=26.3 'KEYSYM'->'KEYBOARD'
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_F4,
            MISC);
    public static KeyMapping postProcessorConfigKey = new KeyMapping("key.infinity.postprocessor",
            //~ if >=26.3 'KEYSYM'->'KEYBOARD'
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_F12,
            MISC);

    public static void initializeClient() {
        ModEntities.registerEntityRenderers();
        ModScreenHandlers.register();
        KeyMappingRegistry.register(f4ConfigKey);
        ModPayloads.registerS2CPacketsReceivers();
        ClientTickEvent.CLIENT_POST.register(client -> {
            while (f4ConfigKey.consumeClick()) if (client.player != null
                    && client.player.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.F4.get())) {
                F4DeployC2SPayload.INSTANCE.send();
                var result = F4Item.deploy(client.level, client.player, InteractionHand.MAIN_HAND);
                ItemStack interactionHolderResult = VersionMethods.getInteractionHolderResult(result);
                if (interactionHolderResult != null)
                    client.player.setItemInHand(InteractionHand.MAIN_HAND, interactionHolderResult);
            }
            //~ if >=26.3 'togglePostEffect'->'toggleSpectatorPostEffect'
            while (postProcessorConfigKey.consumeClick()) client.gameRenderer.togglePostEffect();
        });
    }
}
