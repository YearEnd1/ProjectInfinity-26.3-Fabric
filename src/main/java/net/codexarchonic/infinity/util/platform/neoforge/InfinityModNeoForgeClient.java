package net.codexarchonic.infinity.util.platform.neoforge;

//? neoforge {
/*import net.codexarchonic.infinity.InfinityMod;
import net.codexarchonic.infinity.InfinityModClient;
import net.codexarchonic.infinity.compat.ModCompat;
import net.codexarchonic.infinity.registry.payload.ModPayloads;
import net.codexarchonic.infinity.registry.var.ModParticles;
import net.codexarchonic.infinity.registry.var.ModScreenHandlers;
import net.codexarchonic.infinity.util.VersionMethods;
import net.codexarchonic.infinity.util.config.SoundScanner;
import net.codexarchonic.infinity.util.platform.InfinityPlatform;
import net.codexarchonic.infinity.registry.core.ModBlocks;
import net.codexarchonic.infinity.iridescence.Iridescence;
import net.codexarchonic.infinity.registry.core.ModItemFunctions;
import net.codexarchonic.infinity.registry.core.ModItems;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.codexarchonic.infinity.util.screen.F4Screen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
//? if >1.21.4 {
/^import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.codexarchonic.infinity.item.tint.InfinityTintSource;

^///?}
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jspecify.annotations.NonNull;

import static net.codexarchonic.infinity.InfinityMod.MOD_ID;

@Mod(value = MOD_ID, dist =  Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = MOD_ID)
public class InfinityModNeoForgeClient {

    public InfinityModNeoForgeClient(IEventBus eventBus, ModContainer container) {
        // Run our client setup.
        InfinityModClient.initializeClient();
        InfinityModNeoForgeClient.registerModsPage();
    }

    //Integrate Cloth Config screen (if mod present) with NeoForge mod menu.
    public static void registerModsPage() {
        if (ModCompat.CLOTH_CONFIG)
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
        event.register((blockState, blockAndTintGetter, blockPos, i) -> Iridescence.getColor(blockAndTintGetter, blockPos),
                ModBlocks.IRIDESCENCE_CAULDRON.get()
        );
    }
    //? if <1.21.4 {
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
    //?} else {
    /^@SubscribeEvent
    public static void registerItemColorHandlers(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(InfinityMethods.getId("color"), InfinityTintSource.MAP_CODEC);
    }
    ^///?}
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
        //? if >1.21.4 {
        /^ItemBlockRenderTypes.setRenderLayer(InfinityPlatform.INSTANCE.getIridescenceStill().get(), ChunkSectionLayer.TRANSLUCENT);
        ItemBlockRenderTypes.setRenderLayer(InfinityPlatform.INSTANCE.getIridescenceFlowing().get(), ChunkSectionLayer.TRANSLUCENT);
        ^///?} else {
        ItemBlockRenderTypes.setRenderLayer(InfinityPlatform.INSTANCE.getIridescenceStill().get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(InfinityPlatform.INSTANCE.getIridescenceFlowing().get(), RenderType.translucent());
        //?}
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        ModParticles.registerClientNeoforge(event);
    }

    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public @NonNull ResourceLocation getStillTexture() {
                return Iridescence.TEXTURE;
            }
            @Override
            public @NonNull ResourceLocation getFlowingTexture() {
                return Iridescence.FLOWING_TEXTURE;
            }
            @Override
            public @NonNull ResourceLocation getOverlayTexture() {
                return Iridescence.OVERLAY_TEXTURE;
            }

            @Override
            public int getTintColor() {
                return VersionMethods.opaque(Iridescence.getTimeBasedColor());
            }
            @Override
            public int getTintColor(@NonNull FluidState state, @NonNull BlockAndTintGetter getter, @NonNull BlockPos pos) {
                return VersionMethods.opaque(Iridescence.getPosBasedColor(pos));
            }

        }, FluidTypes.IRIDESCENCE_TYPE.value());
    }
}
*///?}