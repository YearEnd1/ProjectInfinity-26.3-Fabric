package net.lerariemann.infinity.util.loading;

import com.google.common.util.concurrent.AtomicDouble;
import net.lerariemann.infinity.registry.var.ModPayloads;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.CommonIO;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.packs.repository.PackRepository;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public interface ShaderLoader {
    String FILENAME = "current.json";
    AtomicInteger iridLevel = new AtomicInteger(-1);
    AtomicDouble iridProgress = new AtomicDouble(0.0);
    String shaders = VersionMethods.above212 ? "post_effect" : "shaders";

    static Path shaderDir(Minecraft client) {
        return client.getResourcePackDirectory().resolve("infinity/assets/infinity/" + shaders);
    }

    static void reloadShaders(Minecraft client, CompoundTag worldShader, boolean isIridescenceShaderPresent) {
        if (worldShader.isEmpty()) {
            reloadShaders(client, false, isIridescenceShaderPresent);
        }
        else {
            LogManager.getLogger().info("Payload received");
            CommonIO.write(worldShader, shaderDir(client), ShaderLoader.FILENAME);
            reloadShaders(client, true, isIridescenceShaderPresent);
            if (!Objects.equals(ModPayloads.oldShader, worldShader.toString())) {
                client.reloadResourcePacks();
                ModPayloads.oldShader = worldShader.toString();
            }
        }
    }

    static void reloadShaders(Minecraft client, boolean isWorldShaderPresent, boolean isIridescenceShaderPresent) {
        if (client.player == null) return;
        if (isIridescenceShaderPresent) {
            VersionMethods.loadPP(client, (VersionMethods.above212 ? "" : "post/") + "iridescence");
            return;
        }
        iridLevel.set(-1);
        iridProgress.set(0.0);
        try {
            updateWorldShaderFromDisk(client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(isWorldShaderPresent && shaderDir(client).resolve(FILENAME).toFile().exists()) {
            VersionMethods.loadPP(client, "current");
            return;
        }
        client.gameRenderer
        //? if >1.21.2 {
        .clearPostEffect();
        //?} else {
        /*.shutdownEffect();
        *///?}
    }

    static void updateWorldShaderFromDisk(Minecraft client) throws IOException {
        PackRepository m = client.getResourcePackRepository();
        Path resourcepackPath = client.getResourcePackDirectory().resolve("infinity");
        Files.createDirectories(shaderDir(client));
        if (!resourcepackPath.resolve("pack.mcmeta").toFile().exists())
            CommonIO.write(InfinityPlatform.packMcmeta("Shader container"), resourcepackPath.toString(), "pack.mcmeta");
        m.reload();
        m.addPack("file/" + resourcepackPath.getFileName().toString());
    }
}
