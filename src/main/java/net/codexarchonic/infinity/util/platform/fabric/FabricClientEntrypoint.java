package net.codexarchonic.infinity.util.platform.fabric;

//? fabric {

//? if >26 {
/*import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
*///?}

//? if >1.21.2 {
//? if <26 {
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
//?}
import net.codexarchonic.infinity.item.tint.InfinityTintSource;
import net.minecraft.client.color.item.ItemTintSources;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
//?} else {
/*import net.minecraft.client.renderer.RenderType;
import net.codexarchonic.infinity.registry.core.ModItems;
*///?}
import net.codexarchonic.infinity.iridescence.Iridescence;
//? if <26 {
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
//?}
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.api.ClientModInitializer;
import net.codexarchonic.infinity.InfinityModClient;
import net.codexarchonic.infinity.registry.core.ModBlocks;
import net.codexarchonic.infinity.registry.var.ModParticles;
import net.codexarchonic.infinity.registry.core.ModItemFunctions;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.codexarchonic.infinity.util.platform.InfinityPlatform;

public class FabricClientEntrypoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Colour handlers
        //~ if >26 'ColorProviderRegistry.BLOCK'->'BlockColorRegistry' {
        ColorProviderRegistry.BLOCK.register(InfinityMethods::getBookBoxColor,
                ModBlocks.BOOK_BOX.get());
        ColorProviderRegistry.BLOCK.register(InfinityMethods::getBlockEntityColor,
                ModBlocks.PORTAL.get(),
                ModBlocks.BIOME_BOTTLE.get(),
                ModBlocks.CHROMATIC_WOOL.get(),
                ModBlocks.CHROMATIC_CARPET.get());
        ColorProviderRegistry.BLOCK.register((blockState, blockAndTintGetter, blockPos, i) -> Iridescence.getColor(blockAndTintGetter, blockPos),
                ModBlocks.IRIDESCENCE_CAULDRON.get()
        );
        //~}
        //? if <1.21.4 {
        /*ColorProviderRegistry.ITEM.register(InfinityMethods::getOverlayColorFromComponents,
                ModItems.TRANSFINITE_KEY.get(),
                ModItems.BIOME_BOTTLE_ITEM.get(),
                ModItems.F4.get());
        ColorProviderRegistry.ITEM.register(InfinityMethods::getItemColorFromComponents,
                ModItems.CHROMATIC_WOOL.get(),
                ModItems.CHROMATIC_CARPET.get(),
                ModItems.CHROMATIC_MATTER.get(),
                ModItems.PORTAL_ITEM.get());
        ColorProviderRegistry.ITEM.register(InfinityMethods::getDiscColorFromComponents,
                ModItems.DISC.get());
        *///?} else {
        ItemTintSources.ID_MAPPER.put(InfinityMethods.getId("color"), InfinityTintSource.MAP_CODEC);
        //?}

        // Render layer maps
        //? if <26 {
        //? if >1.21.2 {
        var translucent = ChunkSectionLayer.TRANSLUCENT;
        var cutout = ChunkSectionLayer.CUTOUT_MIPPED;
        //?} else {
        /*var BlockRenderLayerMap = net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap.INSTANCE;
        var cutout = RenderType.cutoutMipped();
        var translucent = RenderType.translucent();
         *///?}
        BlockRenderLayerMap.putBlocks(cutout,
                ModBlocks.BOOK_BOX.get(),
                ModBlocks.IRIDESCENT_KELP.get(),
                ModBlocks.IRIDESCENT_KELP_PLANT.get());
        BlockRenderLayerMap.putBlocks(translucent,
                ModBlocks.IRIDESCENCE_CAULDRON.get(),
                ModBlocks.TIME_BOMB.get(),
                ModBlocks.BIOME_BOTTLE.get(),
                ModBlocks.CHROMATIC_WOOL.get(),
                ModBlocks.CHROMATIC_CARPET.get());
        BlockRenderLayerMap.putFluids(translucent,
                InfinityPlatform.INSTANCE.getIridescenceStill().get(),
                InfinityPlatform.INSTANCE.getIridescenceFlowing().get());
        //?}

        // Common client setup tasks
        InfinityModClient.initializeClient();
        ModItemFunctions.registerModelPredicates();
        ModParticles.registerClientFabric();

        //Fluid stuff
        FluidVariantAttributes.register(InfinityPlatform.INSTANCE.getIridescenceStill().get(), new ModFluidsFabric.IridescenceVariantAttributeHandler());
        FluidVariantAttributes.register(InfinityPlatform.INSTANCE.getIridescenceFlowing().get(), new ModFluidsFabric.IridescenceVariantAttributeHandler());
        FluidVariantRendering.register(InfinityPlatform.INSTANCE.getIridescenceStill().get(), new ModFluidsFabric.IridescenceVariantRenderHandler());
        FluidVariantRendering.register(InfinityPlatform.INSTANCE.getIridescenceFlowing().get(), new ModFluidsFabric.IridescenceVariantRenderHandler());
        //? if >26 {
        /*FluidRenderingRegistry.register(InfinityPlatform.INSTANCE.getIridescenceStill().get(), new FluidModel.Unbaked(new Material(Iridescence.TEXTURE), new Material(Iridescence.FLOWING_TEXTURE), null, (b)->Iridescence.getTimeBasedColor()));
        *///?} else {
        FluidRenderHandlerRegistry.INSTANCE.register(InfinityPlatform.INSTANCE.getIridescenceStill().get(), new ModFluidsFabric.IridescenceRenderHandler());
        FluidRenderHandlerRegistry.INSTANCE.register(InfinityPlatform.INSTANCE.getIridescenceFlowing().get(), new ModFluidsFabric.IridescenceRenderHandler());
        //?}
    }
}
//?}