package net.codexarchonic.infinity.util.loading;

import com.google.common.util.concurrent.AtomicDouble;
import net.codexarchonic.infinity.registry.payload.ModPayloads;
import net.codexarchonic.infinity.util.VersionMethods;
import net.codexarchonic.infinity.util.core.CommonIO;
import net.codexarchonic.infinity.util.platform.InfinityPlatform;
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

    static Path shaderDir(Minecraft client) {
        return client.getResourcePackDirectory().resolve("infinity/assets/infinity/" + (InfinityPlatform.version > 21 ? "post_effect" : "shaders"));
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
            VersionMethods.loadPP(client, (InfinityPlatform.version > 21 ? "" : "post/") + "iridescence");
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
        //? if >1.21.2 {
        if (client.player != null) client.player.setActivePostEffects(java.util.List.of());
        //?} else {
        /*client.gameRenderer.shutdownEffect();
        *///?}
    }

    static void updateWorldShaderFromDisk(Minecraft client) throws IOException {
        PackRepository m = client.getResourcePackRepository();
        Path resourcepackPath = client.getResourcePackDirectory().resolve("infinity");
        Files.createDirectories(shaderDir(client));
        if (!resourcepackPath.resolve("pack.mcmeta").toFile().exists())
            CommonIO.write(InfinityPlatform.resourcePackMcmeta("Shader container"), resourcepackPath.toString(), "pack.mcmeta");
        m.reload();
        m.addPack("file/" + resourcepackPath.getFileName().toString());
    }
}
