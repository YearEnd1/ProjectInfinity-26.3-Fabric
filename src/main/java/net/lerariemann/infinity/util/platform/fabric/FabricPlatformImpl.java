package net.lerariemann.infinity.util.platform.fabric;

//? fabric {
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//? if >1.21 {
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.lerariemann.infinity.registry.payload.ModPayloads;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.enchantment.ItemEnchantments;
//?}
//? if <1.21 {
/*import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.registries.BuiltInRegistries;
 *///?}
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.iridescence.IridescenceLiquidBlock;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.registry.payload.FriendlyPacket;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;

import java.nio.file.Path;
import java.util.List;

public class FabricPlatformImpl implements InfinityPlatform {

    @Override
    public boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    @Override
    public List<String> getModsLoaded() {
        return FabricLoader.getInstance().getAllMods().stream().map(c -> c.getMetadata().getId()).toList();
    }

    @Override
    public String loader() {
        return "fabric";
    }

    @Override
    public Path getConfigFolder() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public boolean isDevelopment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void registerPayloadsServer() {
        //? if >1.21
        ModPayloads.registerFabricServerside();
    }
    @Override
    public void registerPayloadsClient() {
        //? if >1.21
        ModPayloads.registerFabricClientside();
    }
    public void sendS2CPayload(ServerPlayer entity, FriendlyPacket payload) {
        ServerPlayNetworking.send(entity, payload);
    }
    public void sendC2SPayload(FriendlyPacket payload) {
        ClientPlayNetworking.send(payload);

    }

    /**
     * On NeoForge, this allows the world to be ticked.
     * On Fabric, this invokes a server world event in Fabric API.
     */
    public void onWorldLoad(Object mixin, ServerLevel world) {
        ServerWorldEvents.LOAD.invoker().onWorldLoad((MinecraftServer) mixin, world);
    }

    /**
     * Check if a block is in the Black Dyed Conventional Tag.
     */
    public boolean isInBlack(BlockState state) {
        //? if <1.21 {
        /*return BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().contains("black_");
         *///?} else {
        return state.is(ConventionalBlockTags.BLACK_DYED);
         //?}
    }

    /**
     * Check if a block is in the White Dyed Conventional Tag.
     */
    public boolean isInWhite(BlockState state) {
        //? if <1.21 {
        /*return BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().contains("white_");
         *///?} else {
        return state.is(ConventionalBlockTags.WHITE_DYED);
        //?}
    }

    /**
     * Get the still variant of Iridescence fluid.
     */
    public RegistrySupplier<? extends FlowingFluid> getIridescenceStill() {
        return ModFluidsFabric.IRIDESCENCE_STILL;
    }
    /**
     * Get the flowing variant of Iridescence fluid.
     */
    public RegistrySupplier<? extends FlowingFluid> getIridescenceFlowing() {
        return ModFluidsFabric.IRIDESCENCE_FLOWING;
    }

    /**
     * Get the registry supplier for Iridescence fluid.
     */
    public RegistrySupplier<LiquidBlock> getIridBlockForReg() {
        var properties = VersionMethods.copyBlockProperties(Blocks.WATER).mapColor(MapColor.COLOR_MAGENTA)
                //? if >1.21.2
                .setId(ModBlocks.registryKey("iridescence"))
                ;
        return ModBlocks.BLOCKS.register("iridescence", () ->
                new IridescenceLiquidBlock(InfinityPlatform.INSTANCE.getIridescenceStill(),
                        properties));
    }

    /**
     * Read from the config path inside the mod JAR.
     */
    @SuppressWarnings("for removal")
    public Path getRootPathInJar(String path) {
        ModContainer mc = FabricLoader.getInstance().getModContainer(InfinityMod.MOD_ID).orElse(null);
        assert mc != null;
        return mc.getRootPaths().get(0).resolve(path);
    }

    /**
     * Create an Item Tag.
     */
    public TagKey<Item> createItemTag(String id) {
        return TagKey.create(Registries.ITEM, InfinityMethods.getId(id));
    }
    public TagKey<Block> createBlockTag(String id) {
        return TagKey.create(Registries.BLOCK, InfinityMethods.getId(id));
    }

    /**
     * Register a Flammable nameToElement (how does Architectury API not have a helper for this)
     */
    public void registerFlammableBlock(RegistrySupplier<Block> block, int burn, int spread) {
        FlammableBlockRegistry.getDefaultInstance().add(block.get(), burn, spread);
    }

    /**
     * Neoforge-exclusive method of testing if a mob is located in iridescence as far as fluid types are concerned.
     * Used only in mixins, to fix the neoforge loader stripping mobs of ability to swim in non-water fluids.
     * On Fabric, returns false.
     */
    public boolean acidTest(Entity entity, boolean eyes) {
        return false;
    }
    public double acidHeightTest(Entity entity) {
        return -1;
    }

    //? if >1.21 {
    public Item.Properties deferredIntComponent(String chromaticMatter, RegistrySupplier<DataComponentType<Integer>> componentTypeSupplier, int i) {
        return new Item.Properties()
                //? if >1.21.4
                .setId(ModItems.registryKey(chromaticMatter))
                .component(componentTypeSupplier.get(), i);
    }
    public ItemEnchantments getEnchantments(ItemStack weapon, MinecraftServer s) {
        return weapon.getEnchantments();
    }
    //?}

    @Override
    public boolean isFabric() {
        return true;
    }

    @Override
    public Path getGameDir() {
        return FabricLoader.getInstance().getGameDir();
    }
}
