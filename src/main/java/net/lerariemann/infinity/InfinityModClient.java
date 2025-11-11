package net.lerariemann.infinity;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.lerariemann.infinity.item.F4Item;
import net.lerariemann.infinity.registry.core.ModEntities;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.registry.var.ModPayloads;
import net.lerariemann.infinity.registry.var.ModScreenHandlers;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class InfinityModClient {
    public static KeyMapping f4ConfigKey = new KeyMapping("key.infinity.f4",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F4,
            "key.categories.misc");
    public static KeyMapping postProcessorConfigKey = new KeyMapping("key.infinity.postprocessor",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F12,
            "key.categories.misc");

    public static void initializeClient() {
        ModEntities.registerEntityRenderers();
        ModScreenHandlers.register();
        KeyMappingRegistry.register(f4ConfigKey);
        ModPayloads.registerS2CPacketsReceivers();
        ClientTickEvent.CLIENT_POST.register(client -> {
            while (f4ConfigKey.consumeClick()) if (client.player != null
                    && client.player.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.F4.get())) {
                ModPayloads.sendF4DeployPayload();
                InteractionResultHolder<ItemStack> result = F4Item.deploy(client.level, client.player, InteractionHand.MAIN_HAND);
                client.player.setItemInHand(InteractionHand.MAIN_HAND, result.getObject());
            }
            while (postProcessorConfigKey.consumeClick()) client.gameRenderer.togglePostEffect();
        });
    }
}
