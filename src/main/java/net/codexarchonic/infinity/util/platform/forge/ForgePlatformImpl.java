package net.codexarchonic.infinity.util.platform.forge;

//? forge {
/*import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.codexarchonic.infinity.registry.core.ModBlocks;
import net.codexarchonic.infinity.registry.payload.FriendlyPacket;
import net.codexarchonic.infinity.util.InfinityMethods;
import net.codexarchonic.infinity.util.VersionMethods;
import net.codexarchonic.infinity.util.platform.InfinityPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.language.IModInfo;

import java.nio.file.Path;
import java.util.List;

public class ForgePlatformImpl implements InfinityPlatform {
    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @Override
    public List<String> getModsLoaded() {
        return ModList.get().getMods().stream().map(IModInfo::getModId).toList();
    }

    @Override
    public String loader() {
        return "forge";
    }

    @Override
    public Path getConfigFolder() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean isDevelopment() {
        return !FMLEnvironment.production;
    }
    @Override
    public void registerPayloadsServer() {};
    @Override
    public void registerPayloadsClient() {};
    public void sendS2CPayload(ServerPlayer entity, FriendlyPacket payload) {
        ServerPlayNetworking.send(entity, payload);
    }

    public void sendC2SPayload(FriendlyPacket payload) {
        ClientPlayNetworking.send(payload);
    }

    /^*
     * On NeoForge, this allows the world to be ticked.
     * On Fabric, this invokes a server world event in Fabric API.
     ^/
    public void onWorldLoad(Object mixin, ServerLevel world) {
        MinecraftServer server = world.getServer();
        server.forgeGetWorldMap().put(world.dimension(),world);
        server.markWorldsDirty();
        MinecraftForge.EVENT_BUS.post(new LevelEvent.Load(world));
    }

    /^*
     * Check if a block is in the Black Dyed Conventional Tag.
     ^/
    public boolean isInBlack(BlockState state) {
        return BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().contains("black_");
    }

    /^*
     * Check if a block is in the White Dyed Conventional Tag.
     ^/
    public boolean isInWhite(BlockState state) {
        return BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().contains("white_");
    }

    /^*
     * Get the still variant of Iridescence fluid.
     ^/
    public RegistrySupplier<? extends FlowingFluid> getIridescenceStill() {
        return ModFluidsForge.IRIDESCENCE_STILL;
    }
    /^*
     * Get the flowing variant of Iridescence fluid.
     ^/
    public RegistrySupplier<? extends FlowingFluid> getIridescenceFlowing() {
        return ModFluidsForge.IRIDESCENCE_FLOWING;
    }

    /^*
     * Get the registry supplier for Iridescence fluid.
     ^/
    public RegistrySupplier<LiquidBlock> getIridBlockForReg() {
        var properties = VersionMethods.copyBlockProperties(Blocks.WATER).mapColor(MapColor.COLOR_MAGENTA);
        return ModBlocks.BLOCKS.register("iridescence", () ->
                    new IridescenceLiquidBlockForge(InfinityPlatform.INSTANCE.getIridescenceStill(), properties));
    }

    /^*
     * Read from the config path inside the mod JAR.
     ^/
    @SuppressWarnings("for removal")
    public Path getRootPathInJar(String path) {
        return ModLoadingContext.get().getActiveContainer().getModInfo().getOwningFile().getFile()
                    .findResource(path);
    }

    /^*
     * Create an Item Tag.
     ^/
    public TagKey<Item> createItemTag(String id) {
        return TagKey.create(Registries.ITEM, InfinityMethods.getId(id));
    }
    public TagKey<Block> createBlockTag(String id) {
        return TagKey.create(Registries.BLOCK, InfinityMethods.getId(id));
    }

    /^*
     * Register a Flammable nameToElement (how does Architectury API not have a helper for this)
     ^/
    public void registerFlammableBlock(RegistrySupplier<Block> block, int burn, int spread) {
//        FireBlock fireblock = (FireBlock)Blocks.FIRE;
//        fireblock.setFlammable(block.get(), burn, spread);
    }

    /^*
     * Neoforge-exclusive method of testing if a mob is located in iridescence as far as fluid types are concerned.
     * Used only in mixins, to fix the neoforge loader stripping mobs of ability to swim in non-water fluids.
     * On Fabric, returns false.
     ^/
    public boolean acidTest(Entity entity, boolean eyes) {
        if (entity instanceof Player) return false;
        if (eyes) return entity.isEyeInFluidType(FluidTypes.IRIDESCENCE_TYPE.get());
        return entity.isInFluidType(FluidTypes.IRIDESCENCE_TYPE.get());
    }

    public double acidHeightTest(Entity entity) {
        if (entity instanceof Player) return -1;
        return entity.getFluidTypeHeight(FluidTypes.IRIDESCENCE_TYPE.get());
    }

    @Override
    public boolean isFabric() {
        return false;
    }

    @Override
    public Path getGameDir() {
        return FMLPaths.GAMEDIR.get();
    }


}
*///?}