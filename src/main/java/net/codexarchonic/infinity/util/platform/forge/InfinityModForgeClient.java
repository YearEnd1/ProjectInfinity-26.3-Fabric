package net.codexarchonic.infinity.util.platform.forge;

//? forge {

/*import net.codexarchonic.infinity.InfinityModClient;
import net.codexarchonic.infinity.compat.ModCompat;
import net.codexarchonic.infinity.iridescence.Iridescence;
import net.codexarchonic.infinity.registry.var.ModParticles;
import net.codexarchonic.infinity.util.platform.InfinityPlatform;
import net.codexarchonic.infinity.registry.core.ModBlocks;
import net.codexarchonic.infinity.registry.core.ModItemFunctions;
import net.codexarchonic.infinity.registry.core.ModItems;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.codexarchonic.infinity.util.screen.F4Screen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static net.codexarchonic.infinity.InfinityMod.MOD_ID;
import static net.codexarchonic.infinity.registry.var.ModScreenHandlers.F4;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class InfinityModForgeClient {
    public static void initializeClient(FMLJavaModLoadingContext context) {
        InfinityModClient.initializeClient();
        InfinityModForgeClient.registerModsPage(context);
    }

    //Integrate Cloth Config screen (if mod present) with NeoForge mod menu.
    public static void registerModsPage(FMLJavaModLoadingContext context) {
        if (ModCompat.CLOTH_CONFIG)
            context.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, ()-> new ConfigScreenHandler.ConfigScreenFactory(ModConfigFactory::createScreen));
    }

    // Apply colour handlers to tint Neither Portals and Book Boxes.
    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register(InfinityMethods::getBlockEntityColor, ModBlocks.PORTAL.get(),
                ModBlocks.BIOME_BOTTLE.get(),
                ModBlocks.CHROMATIC_WOOL.get(),
                ModBlocks.CHROMATIC_CARPET.get());
        event.register(InfinityMethods::getBookBoxColor, ModBlocks.BOOK_BOX.get());
        event.register((blockState, blockAndTintGetter, blockPos, i) -> Iridescence.getColor(blockAndTintGetter, blockPos),
                ModBlocks.IRIDESCENCE_CAULDRON.get());
    }
    @SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register(InfinityMethods::getOverlayColorFromComponents,
                ModItems.TRANSFINITE_KEY.get(),
                ModItems.BIOME_BOTTLE_ITEM.get(),
                ModItems.F4.get());
        event.register(InfinityMethods::getItemColorFromComponents,
                ModItems.CHROMATIC_WOOL.get(),
                ModItems.CHROMATIC_CARPET.get(),
                ModItems.CHROMATIC_MATTER.get(),
                ModItems.PORTAL_ITEM.get());
        event.register(InfinityMethods::getDiscColorFromComponents,
                ModItems.DISC.get());
    }
    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        ModParticles.registerClientForge(event);
    }

    @SubscribeEvent
    public static void registerModelPredicates(FMLClientSetupEvent event) {
        ModItemFunctions.registerModelPredicates();
        MenuScreens.register(F4.get(), F4Screen::new);
    }

    @SubscribeEvent
    public static void registerFluidRenderLayers(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(InfinityPlatform.INSTANCE.getIridescenceStill().get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(InfinityPlatform.INSTANCE.getIridescenceFlowing().get(), RenderType.translucent());
    }

    public static Minecraft client() {
        return Minecraft.getInstance();
    }
}
*///?}