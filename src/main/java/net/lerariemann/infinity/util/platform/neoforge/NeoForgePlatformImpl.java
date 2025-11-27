package net.lerariemann.infinity.util.platform.neoforge;

//? neoforge {
import dev.architectury.registry.registries.RegistrySupplier;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.registry.var.ModPayloads;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
//? if >1.21.4 {
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
//?}
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.nio.file.Path;
import java.util.function.Supplier;

public class NeoForgePlatformImpl implements InfinityPlatform {
    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @Override
    public String loader() {
        return "neoforge";
    }

    @Override
    public Path getConfigFolder() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean isDevelopment() {
        //? if >1.21.9 {
        /*return !FMLEnvironment.isProduction();
        *///?} else {
        return !FMLEnvironment.production;
        //?}

    }
    @Override
    public void registerPayloadsServer() {};
    @Override
    public void registerPayloadsClient() {}

    @Override
    public void sendS2CPayload(ServerPlayer entity, ModPayloads.FriendlyPacket payload) {
        PacketDistributor.sendToPlayer(entity, payload);
    }

    @Override
    public void sendC2SPayload(ModPayloads.FriendlyPacket payload) {
        //? <1.21.2 {
        /*PacketDistributor.sendToServer(payload);
        *///?} else {
        ClientPacketDistributor.sendToServer(payload);
         //?}
    }

    /**
     * On NeoForge, this allows the world to be ticked.
     * On Fabric, this invokes a server world event in Fabric API.
     */
    @Override
    public void onWorldLoad(Object mixin, ServerLevel world) {
        MinecraftServer server = world.getServer();
        server.forgeGetWorldMap().put(world.dimension(),world);
        server.markWorldsDirty();
        NeoForge.EVENT_BUS.post(new LevelEvent.Load(world));
    }

    /**
     * Check if a block is in the Black Dyed Conventional Tag.
     */
    @Override
    public boolean isInBlack(BlockState state) {
        return state.is(Tags.Blocks.DYED_BLACK);
    }

    /**
     * Check if a block is in the White Dyed Conventional Tag.
     */
    @Override
    public boolean isInWhite(BlockState state) {
        return state.is(Tags.Blocks.DYED_WHITE);
    }

    /**
     * Get the still variant of Iridescence fluid.
     */
    @Override
    public RegistrySupplier<? extends FlowingFluid> getIridescenceStill() {
        return ModFluidsNeoforge.IRIDESCENCE_STILL;
    }
    /**
     * Get the flowing variant of Iridescence fluid.
     */
    @Override
    public RegistrySupplier<? extends FlowingFluid> getIridescenceFlowing() {
        return ModFluidsNeoforge.IRIDESCENCE_FLOWING;
    }

    /**
     * Get the registry supplier for Iridescence fluid.
     */
    @Override
    public RegistrySupplier<LiquidBlock> getIridBlockForReg() {
        var properties = VersionMethods.copyBlockProperties(Blocks.WATER).mapColor(MapColor.COLOR_MAGENTA)
                //? if >1.21.2
                .setId(ModBlocks.registryKey("iridescence"))
                ;
        return ModBlocks.BLOCKS.register("iridescence", () ->
                new IridescenceLiquidBlockNeoforge(InfinityPlatform.INSTANCE.getIridescenceStill(), properties));
    }

    /**
     * Read from the config path inside the mod JAR.
     */
    @Override
    @SuppressWarnings("for removal")
    public Path getRootPathInJar(String path) {
        var file = ModLoadingContext.get().getActiveContainer().getModInfo().getOwningFile().getFile();
        // FIXME 1.21.10 - this does not work in production. standard paths do not work on neoforge 1.21.10, will either need to drop neo or make this even more platform specific
        //? if >1.21.9 {
        /*var contents = file.getContents();
        Path returnPath = null;
        for (Path contentRoot : contents.getContentRoots()) {
            if (contentRoot.resolve(path).toFile().exists()) {
                returnPath = contentRoot.resolve(path);
            }
        }
        return returnPath;
        *///?} else {
        return file.findResource(path);
        //?}
    }

    /**
     * Create an Item Tag.
     */
    @Override
    public TagKey<Item> createItemTag(String id) {
        return ItemTags.create(InfinityMethods.getId(id));
    }
    @Override
    public TagKey<Block> createBlockTag(String id) {
        return BlockTags.create(InfinityMethods.getId(id));
    }

    /**
     * Register a Flammable nameToElement (how does Architectury API not have a helper for this)
     */
    @Override
    public void registerFlammableBlock(RegistrySupplier<Block> block, int burn, int spread) {
        FireBlock fireblock = (FireBlock)Blocks.FIRE;
        fireblock.setFlammable(block.get(), burn, spread);
    }

    /**
     * Neoforge-exclusive method of testing if a mob is located in iridescence as far as fluid types are concerned.
     * Used only in mixins, to fix the neoforge loader stripping mobs of ability to swim in non-water fluids.
     * On Fabric, returns false.
     */
    @Override
    public boolean acidTest(Entity entity, boolean eyes) {
        if (entity instanceof Player) return false;
        if (eyes) return entity.isEyeInFluidType(FluidTypes.IRIDESCENCE_TYPE.get());
        return entity.isInFluidType(FluidTypes.IRIDESCENCE_TYPE.get());
    }
    @Override
    public double acidHeightTest(Entity entity) {
        if (entity instanceof Player) return -1;
        return entity.getFluidTypeHeight(FluidTypes.IRIDESCENCE_TYPE.get());
    }

    @Override
    public Item.Properties deferredIntComponent(String id, RegistrySupplier<DataComponentType<Integer>> componentTypeSupplier, int i) {
        return new Item.Properties().component(componentTypeSupplier, i)
                //? if >1.21.4
                .setId(ModItems.registryKey(id))
                ;
    }
    @Override
    public ItemEnchantments getEnchantments(ItemStack weapon, MinecraftServer s) {
        return weapon.getAllEnchantments(s.registryAccess()
                //? if >1.21.4 {
                .lookupOrThrow(Registries.ENCHANTMENT));
                 //?} else {
                /*.lookupOrThrow(Registries.ENCHANTMENT));
        *///?}
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
//?}