package net.lerariemann.infinity.util.platform.neoforge;

//? neoforge {
/*import dev.architectury.platform.Platform;
import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.compat.CreateCompat;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.registry.core.ModEntities;
import net.lerariemann.infinity.registry.core.ModItemFunctions;
import net.lerariemann.infinity.registry.var.ModPayloads;
import net.lerariemann.infinity.registry.var.ModStats;
import net.lerariemann.infinity.util.config.SoundScanner;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
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
        if (FMLEnvironment.dist == Dist.CLIENT) InfinityModNeoForgeClient.initializeClient(eventBus);
        // Run any remaining tasks that require waiting for the registry to freeze on NeoForge.
        FluidTypes.registerFluidTypes(eventBus);
        ModFluidsNeoforge.registerModFluids();
        ModEffectsNeoforge.register(eventBus);
        if (Platform.isModLoaded("create"))
            CreateCompat.register();
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
        if (Platform.isModLoaded("create")) {
            //CreateCompat.register();
        }
    }

    public static Minecraft client() {
        return Minecraft.getInstance();
    }
    public static ServerPlayer getPlayer(IPayloadContext context) {
        return ((ServerPlayer)context.player());
    }

    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                ModPayloads.WorldAddS2CPayload.ID,
                ModPayloads.WorldAddS2CPayload.CODEC,
                (payload, context) -> receiveWorldAddPayload(client(), payload.id(), payload.world_data())
        );
        registrar.playToClient(
                ModPayloads.BiomeAddS2CPayload.ID,
                ModPayloads.BiomeAddS2CPayload.CODEC,
                (payload, context) -> receiveBiomeAddPayload(client(), payload.biome_id(), payload.biome_data())
        );
        registrar.playToClient(
                ModPayloads.ShaderS2CPayload.ID,
                ModPayloads.ShaderS2CPayload.CODEC,
                (payload, context) -> receiveShaderPayload(client(), payload.shader_data(), payload.iridescence())
        );
        registrar.playToClient(
                ModPayloads.StarsS2CPayload.ID,
                ModPayloads.StarsS2CPayload.CODEC,
                (payload, context) -> receiveStarsPayload(client())
        );
        registrar.playToClient(
                ModPayloads.SoundPackS2CPayload.ID,
                ModPayloads.SoundPackS2CPayload.CODEC,
                (payload, context) -> SoundScanner.unpackDownloadedPack(payload.songIds(), client())
        );
        registrar.playToServer(
                ModPayloads.F4UpdateC2SPayload.ID,
                ModPayloads.F4UpdateC2SPayload.CODEC,
                (payload, context) -> receiveF4UpdatePayload(getPlayer(context), payload.slot(), payload.width(), payload.height())
        );
        registrar.playToServer(
                ModPayloads.F4DeployC2SPayload.ID,
                ModPayloads.F4DeployC2SPayload.CODEC,
                (payload, context) -> receiveF4DeployPayload(getPlayer(context))
        );
        registrar.playToServer(
                ModPayloads.JukeboxesC2SPayload.ID,
                ModPayloads.JukeboxesC2SPayload.CODEC,
                (payload, context) -> SoundScanner.unpackUploadedJukeboxes(getPlayer(context).server, payload.data())
        );
    }
}
*///?}