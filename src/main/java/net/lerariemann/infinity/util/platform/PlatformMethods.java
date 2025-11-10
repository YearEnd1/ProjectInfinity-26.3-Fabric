package net.lerariemann.infinity.util.platform;

import dev.architectury.registry.registries.RegistrySupplier;

import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import java.nio.file.Path;
import java.util.function.Supplier;

//? fabric {
/*import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.iridescence.IridescenceLiquidBlock;
import net.lerariemann.infinity.item.StarOfLangItem;
import net.lerariemann.infinity.util.platform.fabric.ModFluidsFabric;
import net.minecraft.core.registries.Registries;
*///?}
//? neoforge {
import io.netty.buffer.Unpooled;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.lerariemann.infinity.util.platform.neoforge.FluidTypes;
import net.lerariemann.infinity.util.platform.neoforge.IridescenceLiquidBlockNeoforge;
import net.lerariemann.infinity.util.platform.neoforge.ModFluidsNeoforge;
//?}
/**
 * Methods that require different implementations to work on Fabric vs. NeoForge.
 * @see InfinityMethods
 */
public class PlatformMethods {
    /**
     * Creates an empty PacketByteBuf - intended to replace the method
     * in Fabric API.
     */
    public static FriendlyByteBuf createPacketByteBufs() {
        //? fabric {
        /*return PacketByteBufs.create();
        *///?}
        //? neoforge {
        return new FriendlyByteBuf(Unpooled.buffer());
        //?}
    }

    public static void sendS2CPayload(ServerPlayer entity, CustomPacketPayload payload) {
        //? fabric {
        /*ServerPlayNetworking.send(entity, payload);
        *///?}
        //? neoforge {
        PacketDistributor.sendToPlayer(entity, payload);
        //?}
    }
    public static void sendC2SPayload(CustomPacketPayload payload) {
        //? fabric {
        /*ClientPlayNetworking.send(payload);
        *///?}
        //? neoforge {
        PacketDistributor.sendToServer(payload);
        //?}
    }

    /**
     * On NeoForge, this allows the world to be ticked.
     * On Fabric, this invokes a server world event in Fabric API.
     */
    public static void onWorldLoad(Object mixin, ServerLevel world) {
        //? fabric {
        /*ServerWorldEvents.LOAD.invoker().onWorldLoad((MinecraftServer)mixin, world);
        *///?}
        //? neoforge {
        MinecraftServer server = world.getServer();
        server.forgeGetWorldMap().put(world.dimension(),world);
        server.markWorldsDirty();
        NeoForge.EVENT_BUS.post(new LevelEvent.Load(world));
        //?}
    }

    /**
     * Fabric-exclusive method of adding an item to an Item Group, which uses Fabric API.
     * On NeoForge, does nothing.
     */
    public static <T extends Item> void addAfter(RegistrySupplier<T> supplier, ResourceKey<CreativeModeTab> group, Item item) {
        //? fabric {
        /*ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.addAfter(item, supplier.get()));
        *///?}
    }
    public static <T extends Item> void addEnd(RegistrySupplier<T> supplier, ResourceKey<CreativeModeTab> group) {
        //? fabric {
        /*ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.accept(supplier.get()));
        *///?}
    }

    /**
     * Check if a block is in the Black Dyed Conventional Tag.
     */
    public static boolean isInBlack(BlockState state) {
        //? fabric {
        /*return state.is(ConventionalBlockTags.BLACK_DYED);
        *///?}
        //? neoforge {
        return state.is(Tags.Blocks.DYED_BLACK);
        //?}
    }

    /**
     * Check if a block is in the White Dyed Conventional Tag.
     */
    public static boolean isInWhite(BlockState state) {
        //? fabric {
        /*return state.is(ConventionalBlockTags.WHITE_DYED);
        *///?}
        //? neoforge {
        return state.is(Tags.Blocks.DYED_WHITE);
        //?}
    }

    /**
     * Get the still variant of Iridescence fluid.
     */
    public static RegistrySupplier<? extends FlowingFluid> getIridescenceStill() {
        //? fabric {
        /*return ModFluidsFabric.IRIDESCENCE_STILL;
        *///?}
        //? neoforge {
        return ModFluidsNeoforge.IRIDESCENCE_STILL;
        //?}
    }
    /**
     * Get the flowing variant of Iridescence fluid.
     */
    public static RegistrySupplier<? extends FlowingFluid> getIridescenceFlowing() {
        //? fabric {
        /*return ModFluidsFabric.IRIDESCENCE_FLOWING;
        *///?}
        //? neoforge {
        return ModFluidsNeoforge.IRIDESCENCE_FLOWING;
         //?}
    }

    /**
     * Get the registry supplier for Iridescence fluid.
     */
    public static RegistrySupplier<LiquidBlock> getIridBlockForReg() {
        //? fabric {
        /*return ModBlocks.BLOCKS.register("iridescence", () ->
                new IridescenceLiquidBlock(PlatformMethods.getIridescenceStill(),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).mapColor(MapColor.COLOR_MAGENTA)));
        *///?}
        //? neoforge {
        return ModBlocks.BLOCKS.register("iridescence", () ->
                new IridescenceLiquidBlockNeoforge(PlatformMethods.getIridescenceStill(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)
                                .mapColor(MapColor.COLOR_MAGENTA)));
        //?}
    }

    /**
     * Read from the config path inside the mod JAR.
     */
    public static Path getRootConfigPath() {
        //? fabric {
        /*ModContainer mc = FabricLoader.getInstance().getModContainer(InfinityMod.MOD_ID).orElse(null);
        assert mc != null;
        return mc.getRootPaths().getFirst().resolve("config");
        *///?}
        //? neoforge {
        
        return ModLoadingContext.get().getActiveContainer().getModInfo().getOwningFile().getFile().findResource("config");
        //?}
    }

    /**
     * Create an Item Tag.
     */
    public static TagKey<Item> createItemTag(String id) {
        //? fabric {
        /*return TagKey.create(Registries.ITEM, InfinityMethods.getId(id));
        *///?}
        //? neoforge {
        
        return ItemTags.create(InfinityMethods.getId(id));
         //?}
    }
    public static TagKey<Block> createBlockTag(String id) {
        //? fabric {
        /*return TagKey.create(Registries.BLOCK, InfinityMethods.getId(id));
        *///?}
        //? neoforge {
        
        return BlockTags.create(InfinityMethods.getId(id));
         //?}
    }

    /**
     * Register a Flammable nameToElement (how does Architectury API not have a helper for this)
     */
    public static void registerFlammableBlock(RegistrySupplier<Block> block, int burn, int spread) {
        //? fabric {
        /*FlammableBlockRegistry.getDefaultInstance().add(block.get(), burn, spread);
         *///?}
        //? neoforge {
        FireBlock fireblock = (FireBlock)Blocks.FIRE;
        fireblock.setFlammable(block.get(), burn, spread);
        //?}
    }

    /**
     * Neoforge-exclusive method of testing if a mob is located in iridescence as far as fluid types are concerned.
     * Used only in mixins, to fix the neoforge loader stripping mobs of ability to swim in non-water fluids.
     * On Fabric, returns false.
     */
    public static boolean acidTest(Entity entity, boolean eyes) {
        //? fabric {
        /*return false;
        *///?}
        //? neoforge {
        
        if (entity instanceof Player) return false;
        if (eyes) return entity.isEyeInFluidType(FluidTypes.IRIDESCENCE_TYPE.value());
        return entity.isInFluidType(FluidTypes.IRIDESCENCE_TYPE.value());
         //?}
    }
    public static double acidHeightTest(Entity entity) {
        //? fabric {
        /*return -1;
        *///?}
        //? neoforge {
        
        if (entity instanceof Player) return -1;
        return entity.getFluidTypeHeight(FluidTypes.IRIDESCENCE_TYPE.value());
         //?}
    }

    public static Item.Properties deferredIntComponent(Supplier<DataComponentType<Integer>> componentTypeSupplier, int i) {
        //? fabric {
        /*return new Item.Properties().component(componentTypeSupplier.get(), i);
        *///?}
        //? neoforge {
        return new Item.Properties().component(componentTypeSupplier, i);
        //?}
    }
}