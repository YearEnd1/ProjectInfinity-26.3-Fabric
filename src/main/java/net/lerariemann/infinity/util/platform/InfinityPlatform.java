package net.lerariemann.infinity.util.platform;

//? fabric {
/*import net.lerariemann.infinity.util.platform.fabric.FabricPlatformImpl;
*///?}
//? neoforge {
import net.lerariemann.infinity.util.platform.neoforge.NeoForgePlatformImpl;
//?}
import dev.architectury.registry.registries.RegistrySupplier;
import net.lerariemann.infinity.registry.var.ModPayloads;
//? if >1.21 {
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
//?}
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
//? forge {
/*import net.lerariemann.infinity.util.platform.forge.ForgePlatformImpl;
 *///?}

import java.nio.file.Path;

public interface InfinityPlatform {
    int version =
            //? if >1.21.6 {
            22;
             //?} else if >1.21 {
            /*21;
            *///?} else {
            /*20;
             *///?}
    int mcmeta =
    //? if >1.21.6 {
    81;
    //?} else if >1.21 {
    /*48;
     *///?} else {
    /*15;
     *///?}

    static CompoundTag packMcmeta(String description) {
        CompoundTag res = new CompoundTag();
        CompoundTag pack = new CompoundTag();
        pack.putInt("pack_format", InfinityPlatform.mcmeta);
        pack.putString("description", description);
        res.put("pack", pack);
        return res;
    }

    //? fabric {
    /*InfinityPlatform INSTANCE = new FabricPlatformImpl();
     *///?}
    //? neoforge {
    InfinityPlatform INSTANCE = new NeoForgePlatformImpl();
    //?}
    //? forge {
    /*InfinityPlatform INSTANCE = new ForgePlatformImpl();
     *///?}

    boolean isModLoaded(String modid);
    String loader();
    Path getConfigFolder();
    boolean isDevelopment();
    void registerPayloadsServer();
    void registerPayloadsClient();
    void sendS2CPayload(ServerPlayer entity, ModPayloads.FriendlyPacket payload);
    void sendC2SPayload(ModPayloads.FriendlyPacket payload);

    /**
     * On NeoForge, this allows the world to be ticked.
     * On Fabric, this invokes a server world event in Fabric API.
     */
    void onWorldLoad(Object mixin, ServerLevel world);

    /**
     * Check if a block is in the Black Dyed Conventional Tag.
     */
    boolean isInBlack(BlockState state);

    /**
     * Check if a block is in the White Dyed Conventional Tag.
     */
    boolean isInWhite(BlockState state);

    /**
     * Get the still variant of Iridescence fluid.
     */
    RegistrySupplier<? extends FlowingFluid> getIridescenceStill();

    /**
     * Get the flowing variant of Iridescence fluid.
     */
    RegistrySupplier<? extends FlowingFluid> getIridescenceFlowing();

    /**
     * Get the registry supplier for Iridescence fluid.
     */
    RegistrySupplier<LiquidBlock> getIridBlockForReg();

    /**
     * Read from the config path inside the mod JAR.
     */
    Path getRootPathInJar(String path);

    /**
     * Create an Item Tag.
     */
    TagKey<Item> createItemTag(String id);

    TagKey<Block> createBlockTag(String id);

    /**
     * Register a Flammable nameToElement (how does Architectury API not have a helper for this)
     */
    void registerFlammableBlock(RegistrySupplier<Block> block, int burn, int spread);

    /**
     * Neoforge-exclusive method of testing if a mob is located in iridescence as far as fluid types are concerned.
     * Used only in mixins, to fix the neoforge loader stripping mobs of ability to swim in non-water fluids.
     * On Fabric, returns false.
     */
    boolean acidTest(Entity entity, boolean eyes);

    double acidHeightTest(Entity entity);

    //? if >1.21 {
    Item.Properties deferredIntComponent(String id, RegistrySupplier<DataComponentType<Integer>> componentTypeSupplier, int i);
    ItemEnchantments getEnchantments(ItemStack weapon, MinecraftServer s);

    //?}


    boolean isFabric();

    Path getGameDir();

}