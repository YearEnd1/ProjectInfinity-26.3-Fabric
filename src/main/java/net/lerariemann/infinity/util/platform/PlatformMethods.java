package net.lerariemann.infinity.util.platform;

import dev.architectury.registry.registries.RegistrySupplier;

import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.var.ModPayloads;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.VersionMethods;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import java.nio.file.Path;

//? if >1.21 {
import net.minecraft.core.component.DataComponentType;
import java.util.function.Supplier;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.ItemStack;
//?}
//? if >1.21 && fabric {
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
//?}
//? if <1.21 && fabric {
/*import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
*///?}

//? fabric {
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.iridescence.IridescenceLiquidBlock;
import net.lerariemann.infinity.util.platform.fabric.ModFluidsFabric;
//?}
//? neoforge {
/*import io.netty.buffer.Unpooled;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.lerariemann.infinity.util.platform.neoforge.FluidTypes;
import net.lerariemann.infinity.util.platform.neoforge.IridescenceLiquidBlockNeoforge;
import net.lerariemann.infinity.util.platform.neoforge.ModFluidsNeoforge;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.entity.player.Player;
*///?}
//? forge {
/*import net.minecraft.world.entity.player.Player;
import net.lerariemann.infinity.util.platform.forge.ModFluidsForge;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.lerariemann.infinity.util.platform.forge.IridescenceLiquidBlockForge;
import net.lerariemann.infinity.util.platform.forge.FluidTypes;
*///?}
/**
 * Methods that require different implementations to work on Fabric vs. NeoForge.
 * @see InfinityMethods
 */
public interface PlatformMethods {
    /**
     * Creates an empty PacketByteBuf - intended to replace the method
     * in Fabric API.
     */
    static FriendlyByteBuf createPacketByteBufs() {
        //? fabric {
        return PacketByteBufs.create();
        //?}
        //? neoforge || forge {
        /*return new FriendlyByteBuf(Unpooled.buffer());
        *///?}
    }

    static void sendS2CPayload(ServerPlayer entity, ModPayloads.FriendlyPacket payload) {
        //? fabric || forge {
        ServerPlayNetworking.send(entity, payload);
        //?}
        //? neoforge {
        /*PacketDistributor.sendToPlayer(entity, payload);
        *///?}
    }
    static void sendC2SPayload(ModPayloads.FriendlyPacket payload) {
        //? fabric || forge {
        ClientPlayNetworking.send(payload);
        //?}
        //? neoforge {
        /*PacketDistributor.sendToServer(payload);
        *///?}
    }

    /**
     * On NeoForge, this allows the world to be ticked.
     * On Fabric, this invokes a server world event in Fabric API.
     */
    static void onWorldLoad(Object mixin, ServerLevel world) {
        //? if >1.21 && fabric {
        ServerWorldEvents.LOAD.invoker().onWorldLoad((MinecraftServer)mixin, world);
        //?}
        //? if <1.21 && fabric {
        /*ServerWorldEvents.LOAD.invoker().onWorldLoad((MinecraftServer) mixin, world);
        *///?}
        //? neoforge {
        /*MinecraftServer server = world.getServer();
        server.forgeGetWorldMap().put(world.dimension(),world);
        server.markWorldsDirty();
        NeoForge.EVENT_BUS.post(new LevelEvent.Load(world));
        *///?}
        //? forge {
        /*MinecraftServer server = world.getServer();
        server.forgeGetWorldMap().put(world.dimension(),world);
        server.markWorldsDirty();
        MinecraftForge.EVENT_BUS.post(new LevelEvent.Load(world));
        *///?}
    }

    /**
     * Fabric-exclusive method of adding an item to an Item Group, which uses Fabric API.
     * On NeoForge, does nothing.
     */
    static <T extends Item> void addAfter(RegistrySupplier<T> supplier, ResourceKey<CreativeModeTab> group, Item item) {
        //? fabric {
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.addAfter(item, supplier.get()));
        //?}
    }
    static <T extends Item> void addEnd(RegistrySupplier<T> supplier, ResourceKey<CreativeModeTab> group) {
        //? fabric {
        ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.accept(supplier.get()));
        //?}
    }

    /**
     * Check if a block is in the Black Dyed Conventional Tag.
     */
    static boolean isInBlack(BlockState state) {
        //? if <1.21 {
        /*return BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().contains("black_");
        *///?}
        //? if >1.21 && fabric {
        return state.is(ConventionalBlockTags.BLACK_DYED);
        //?}
        //? neoforge {
        /*return state.is(Tags.Blocks.DYED_BLACK);
        *///?}
    }

    /**
     * Check if a block is in the White Dyed Conventional Tag.
     */
    static boolean isInWhite(BlockState state) {
        //? if <1.21 {
        /*return BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath().contains("white_");
        *///?}
        //? if >1.21 && fabric {
        return state.is(ConventionalBlockTags.WHITE_DYED);
        //?}
        //? neoforge {
        /*return state.is(Tags.Blocks.DYED_WHITE);
        *///?}
    }

    /**
     * Get the still variant of Iridescence fluid.
     */
    static RegistrySupplier<? extends FlowingFluid> getIridescenceStill() {
        //? fabric {
        return ModFluidsFabric.IRIDESCENCE_STILL;
        //?}
        //? neoforge {
        /*return ModFluidsNeoforge.IRIDESCENCE_STILL;
        *///?}
        //? forge {
        /*return ModFluidsForge.IRIDESCENCE_STILL;
         *///?}
    }
    /**
     * Get the flowing variant of Iridescence fluid.
     */
    static RegistrySupplier<? extends FlowingFluid> getIridescenceFlowing() {
        //? fabric {
        return ModFluidsFabric.IRIDESCENCE_FLOWING;
        //?}
        //? neoforge {
        /*return ModFluidsNeoforge.IRIDESCENCE_FLOWING;
         *///?}
        //? forge {
        /*return ModFluidsForge.IRIDESCENCE_FLOWING;
         *///?}
    }

    /**
     * Get the registry supplier for Iridescence fluid.
     */
    static RegistrySupplier<LiquidBlock> getIridBlockForReg() {
        var properties = VersionMethods.copyBlockProperties(Blocks.WATER).mapColor(MapColor.COLOR_MAGENTA)
                //? if >1.21.2
                .setId(ModBlocks.keyOfBlock("iridescence")
                );
        //? fabric {
        return ModBlocks.BLOCKS.register("iridescence", () ->
                new IridescenceLiquidBlock(PlatformMethods.getIridescenceStill(),
                        properties));
        //?}
        //? neoforge {
        /*return ModBlocks.BLOCKS.register("iridescence", () ->
                new IridescenceLiquidBlockNeoforge(PlatformMethods.getIridescenceStill(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)
                                .mapColor(MapColor.COLOR_MAGENTA)));
        *///?}
        //? forge {
        /*return ModBlocks.BLOCKS.register("iridescence", () ->
                new IridescenceLiquidBlockForge(PlatformMethods.getIridescenceStill(), BlockBehaviour.Properties.copy(Blocks.WATER)
                                .mapColor(MapColor.COLOR_MAGENTA)));
        *///?}
    }

    /**
     * Read from the config path inside the mod JAR.
     */
    @SuppressWarnings("for removal")
    static Path getRootPathInJar() {
        //? fabric {
        ModContainer mc = FabricLoader.getInstance().getModContainer(InfinityMod.MOD_ID).orElse(null);
        assert mc != null;
        return mc.getRootPaths().getFirst();
        //?}
        //? neoforge || forge {
        /*return ModLoadingContext.get().getActiveContainer().getModInfo().getOwningFile().getFile().getFilePath();
        *///?}
    }

    /**
     * Read from the config path inside the mod JAR.
     */
    @SuppressWarnings("for removal")
    static Path getRootPathInJar(String path) {
        //? fabric {
        ModContainer mc = FabricLoader.getInstance().getModContainer(InfinityMod.MOD_ID).orElse(null);
        assert mc != null;
        return mc.getRootPaths().getFirst().resolve(path);
        //?}
        //? neoforge || forge {

        /*return ModLoadingContext.get().getActiveContainer().getModInfo().getOwningFile().getFile()
                .findResource(path);
        *///?}
    }

    /**
     * Create an Item Tag.
     */
    static TagKey<Item> createItemTag(String id) {
        //? fabric || forge {
        return TagKey.create(Registries.ITEM, InfinityMethods.getId(id));
        //?}
        //? neoforge {
        
        /*return ItemTags.create(InfinityMethods.getId(id));
         *///?}
    }
    static TagKey<Block> createBlockTag(String id) {
        //? fabric || forge {
        return TagKey.create(Registries.BLOCK, InfinityMethods.getId(id));
        //?}
        //? neoforge {
        
        /*return BlockTags.create(InfinityMethods.getId(id));
         *///?}
    }

    /**
     * Register a Flammable nameToElement (how does Architectury API not have a helper for this)
     */
    static void registerFlammableBlock(RegistrySupplier<Block> block, int burn, int spread) {
        //? fabric {
        FlammableBlockRegistry.getDefaultInstance().add(block.get(), burn, spread);
         //?}
        //? neoforge {
        /*FireBlock fireblock = (FireBlock)Blocks.FIRE;
        fireblock.setFlammable(block.get(), burn, spread);
        *///?}
    }

    /**
     * Neoforge-exclusive method of testing if a mob is located in iridescence as far as fluid types are concerned.
     * Used only in mixins, to fix the neoforge loader stripping mobs of ability to swim in non-water fluids.
     * On Fabric, returns false.
     */
    static boolean acidTest(Entity entity, boolean eyes) {
        //? if fabric {
        return false;
        //?} else if neoforge || forge {
        
        /*if (entity instanceof Player) return false;
        if (eyes) return entity.isEyeInFluidType(FluidTypes.IRIDESCENCE_TYPE.get());
        return entity.isInFluidType(FluidTypes.IRIDESCENCE_TYPE.get());
         *///?}
    }
    static double acidHeightTest(Entity entity) {
        //? fabric {
        return -1;
        //?}
        //? neoforge || forge {
        
        /*if (entity instanceof Player) return -1;
        return entity.getFluidTypeHeight(FluidTypes.IRIDESCENCE_TYPE.get());
         *///?}
    }

    //? if >1.21 && fabric {
    static Item.Properties deferredIntComponent(Supplier<DataComponentType<Integer>> componentTypeSupplier, int i) {
        return new Item.Properties().component(componentTypeSupplier.get(), i);
    }
    static ItemEnchantments getEnchantments(ItemStack weapon, MinecraftServer s) {
        return weapon.getEnchantments();
    }
    //?}

    //? if >1.21 && neoforge {
    /*static Item.Properties deferredIntComponent(Supplier<DataComponentType<Integer>> componentTypeSupplier, int i) {
        return new Item.Properties().component(componentTypeSupplier, i);
    }
    static ItemEnchantments getEnchantments(ItemStack weapon, MinecraftServer s) {
        return weapon.getAllEnchantments(s.registryAccess().lookupOrThrow(Registries.ENCHANTMENT));
    }
    *///?}
}