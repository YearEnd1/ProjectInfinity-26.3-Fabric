package net.lerariemann.infinity.util.platform.fabric;

//? if >1.21 && fabric {
import net.lerariemann.infinity.registry.payload.ModPayloads;

public class ModPayloadsFabric {
    public static void registerPayloadsServer() {
        ModPayloads.registerFabricServerside();
    }

    public static void registerPayloadsClient() {
        ModPayloads.registerFabricClientside();
    }
}
//?}
