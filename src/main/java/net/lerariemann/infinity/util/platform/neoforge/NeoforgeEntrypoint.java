package net.lerariemann.infinity.util.platform.neoforge;

//? neoforge {

import net.lerariemann.infinity.InfinityMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(InfinityMod.MOD_ID)
@EventBusSubscriber // sample_content
public class NeoforgeEntrypoint {

    public NeoforgeEntrypoint() {
        InfinityMod.init();
    }

    @EventBusSubscriber(modid = InfinityMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientSetup(final FMLClientSetupEvent event) {
            InfinityMod.LOG.info("Initializing {} Client", InfinityMod.MOD_ID);
        }
    }

}
//?}