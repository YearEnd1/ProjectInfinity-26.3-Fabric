package net.lerariemann.infinity.util.platform.neoforge;

//? neoforge {
import dev.architectury.platform.Platform;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.InfinityModClient;
import net.lerariemann.infinity.registry.var.ModScreenHandlers;
import net.lerariemann.infinity.util.platform.PlatformMethods;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.iridescence.Iridescence;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.screen.F4Screen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jetbrains.annotations.NotNull;

public class InfinityModNeoForgeClient {
    public static void initializeClient(IEventBus eventBus) {
        InfinityModClient.initializeClient();
        InfinityModNeoForgeClient.registerModsPage();
        eventBus.addListener(InfinityModNeoForgeClient::registerBlockColorHandlers);
        eventBus.addListener(InfinityModNeoForgeClient::registerItemColorHandlers);
        eventBus.addListener(InfinityModNeoForgeClient::registerFluidRenderLayers);
        eventBus.addListener(InfinityModNeoForgeClient::registerModelPredicates);
        eventBus.addListener(InfinityModNeoForgeClient::registerMenuScreens);
    }

    //Integrate Cloth Config screen (if mod present) with NeoForge mod menu.
    public static void registerModsPage() {
        if (Platform.isModLoaded("cloth_config"))
            ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, ModConfigFactory::new);
    }

    // Apply colour handlers to tint Neither Portals and Book Boxes.
    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register(InfinityMethods::getBlockEntityColor, ModBlocks.PORTAL.get(),
                ModBlocks.BIOME_BOTTLE.get(),
                ModBlocks.CHROMATIC_WOOL.get(),
                ModBlocks.CHROMATIC_CARPET.get());
        event.register(InfinityMethods::getBookBoxColor, ModBlocks.BOOK_BOX.get());
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
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModScreenHandlers.F4.get(), F4Screen::new);
    }
    @SubscribeEvent
    public static void registerModelPredicates(FMLClientSetupEvent event) {
        ModItemFunctions.registerModelPredicates();
    }

    @SubscribeEvent
    public static void registerFluidRenderLayers(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(PlatformMethods.getIridescenceStill().get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(PlatformMethods.getIridescenceFlowing().get(), RenderType.translucent());
    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = InfinityMod.MOD_ID)
    public static class FluidClientHandler {
        @SubscribeEvent
        static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
            event.registerFluidType(new IClientFluidTypeExtensions() {
                @Override
                public @NotNull ResourceLocation getStillTexture() {
                    return Iridescence.TEXTURE;
                }
                @Override
                public @NotNull ResourceLocation getFlowingTexture() {
                    return Iridescence.FLOWING_TEXTURE;
                }
                @Override
                public @NotNull ResourceLocation getOverlayTexture() {
                    return Iridescence.OVERLAY_TEXTURE;
                }

                @Override
                public int getTintColor() {
                    return FastColor.ARGB32.opaque(Iridescence.getTimeBasedColor());
                }
                @Override
                public int getTintColor(@NotNull FluidState state, @NotNull BlockAndTintGetter getter, @NotNull BlockPos pos) {
                    return FastColor.ARGB32.opaque(Iridescence.getPosBasedColor(pos));
                }

            }, FluidTypes.IRIDESCENCE_TYPE.value());
        }
    }
}
//?}