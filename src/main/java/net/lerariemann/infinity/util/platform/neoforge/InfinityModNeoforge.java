package net.lerariemann.infinity.util.platform.neoforge;

//? neoforge {
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.compat.CreateCompat;
//? if >1.21.4 {
import net.lerariemann.infinity.compat.eiv.EivIntegration;
//?}
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.core.ModEntities;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.registry.var.ModPayloads;
import net.lerariemann.infinity.registry.var.ModStats;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.config.SoundScanner;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;
import static net.lerariemann.infinity.registry.var.ModPayloads.*;

@Mod(MOD_ID)
@EventBusSubscriber(modid = MOD_ID)
public class InfinityModNeoforge {
    public InfinityModNeoforge(IEventBus eventBus, ModContainer container) {
        // Run our common setup.
        InfinityMod.init();
        // Run our client setup.
        //? if >1.21.9 {
        /*if (FMLEnvironment.getDist() == Dist.CLIENT) {
        *///?} else {
        if (FMLEnvironment.dist == Dist.CLIENT) {
        //?}
            InfinityModNeoForgeClient.initializeClient(eventBus);
        }
        // Run any remaining tasks that require waiting for the registry to freeze on NeoForge.
        FluidTypes.registerFluidTypes(eventBus);
        ModFluidsNeoforge.registerModFluids();
        ModEffectsNeoforge.register(eventBus);
        //? if <1.21.2 {
        /*if (InfinityPlatform.INSTANCE.isModLoaded("create"))
            CreateCompat.register();
        *///?}
    }

    @SubscribeEvent
    private static void initCommonResources(AddPackFindersEvent event) {
        event.addPackFinders(
                InfinityMethods.getId("resourcepacks/" + InfinityPlatform.version),
                PackType.SERVER_DATA,
                Component.literal("Infinite Dimensions - Multiversioned Resources"),
                PackSource.BUILT_IN,
                true,
                Pack.Position.TOP
        );
        event.addPackFinders(
                InfinityMethods.getId("resourcepacks/" + InfinityPlatform.version),
                PackType.CLIENT_RESOURCES,
                Component.literal("Infinite Dimensions - Multiversioned Resources"),
                PackSource.BUILT_IN,
                true,
                Pack.Position.TOP
        );
    }

    @SubscribeEvent
    public static void registerSpawns(RegisterSpawnPlacementsEvent event) {
        ModEntities.registerSpawnRestrictions(event);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        ModStats.load();
        ModBlocks.registerFlammableBlocks();
        ModItemFunctions.registerDispenserBehaviour();
        // this is less than ideal but i cannot figure out why the proper solution isn't working
        //? if >1.21.4 {
        if (InfinityPlatform.INSTANCE.isModLoaded("eiv"))
            EivIntegration.initialize();
        //?}
    }

    public static Minecraft client() {
        return Minecraft.getInstance();
    }
    public static ServerPlayer getPlayer(IPayloadContext context) {
        return ((ServerPlayer)context.player());
    }

    public static MinecraftServer getServerFromPlayer(IPayloadContext context) {
        ServerPlayer player = getPlayer(context);
        //? if >1.21.9 {
        /*return player.level().getServer();
        *///?} else {
        return player.getServer();
        //?}
    }

    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                ModPayloads.WorldAddS2CPayload.ID,
                ModPayloads.WorldAddS2CPayload.CODEC,
                (payload, context) -> payload.receive(client())
        );
        registrar.playToClient(
                ModPayloads.BiomeAddS2CPayload.ID,
                ModPayloads.BiomeAddS2CPayload.CODEC,
                (payload, context) -> payload.receive(client())
        );
        registrar.playToClient(
                ModPayloads.ShaderS2CPayload.ID,
                ModPayloads.ShaderS2CPayload.CODEC,
                (payload, context) -> payload.receive(client())
        );
        registrar.playToClient(
                ModPayloads.StarsS2CPayload.ID,
                ModPayloads.StarsS2CPayload.CODEC,
                (payload, context) -> payload.receive(client())
        );
        registrar.playToClient(
                ModPayloads.SoundPackS2CPayload.ID,
                ModPayloads.SoundPackS2CPayload.CODEC,
                (payload, context) -> SoundScanner.unpackDownloadedPack(payload.songIds(), client())
        );
        registrar.playToServer(
                ModPayloads.F4UpdateC2SPayload.ID,
                ModPayloads.F4UpdateC2SPayload.CODEC,
                (payload, context) -> payload.receive(getPlayer(context))
        );
        registrar.playToServer(
                ModPayloads.F4DeployC2SPayload.ID,
                ModPayloads.F4DeployC2SPayload.CODEC,
                (payload, context) -> payload.receive(getPlayer(context))
        );
        registrar.playToServer(
                ModPayloads.JukeboxesC2SPayload.ID,
                ModPayloads.JukeboxesC2SPayload.CODEC,
                (payload, context) -> SoundScanner.unpackUploadedJukeboxes(getServerFromPlayer(context), payload.data())
        );
    }
}
//?}