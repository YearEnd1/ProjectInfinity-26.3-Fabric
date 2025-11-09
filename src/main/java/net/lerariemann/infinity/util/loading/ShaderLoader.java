package net.lerariemann.infinity.util.loading;

import com.google.common.util.concurrent.AtomicDouble;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lerariemann.infinity.access.GameRendererAccess;
import net.lerariemann.infinity.registry.var.ModPayloads;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.core.CommonIO;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.packs.repository.PackRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

@Environment(EnvType.CLIENT)
public interface ShaderLoader {
    String FILENAME = "current.json";
    AtomicInteger iridLevel = new AtomicInteger(-1);
    AtomicDouble iridProgress = new AtomicDouble(0.0);

    static Path shaderDir(Minecraft client) {
        return client.getResourcePackDirectory().resolve("infinity/assets/infinity/shaders");
    }

    static void reloadShaders(Minecraft client, CompoundTag worldShader, boolean isIridescenceShaderPresent) {
        if (worldShader.isEmpty()) {
            reloadShaders(client, false, isIridescenceShaderPresent);
        }
        else {
            CommonIO.write(worldShader, shaderDir(client), ShaderLoader.FILENAME);
            reloadShaders(client, true, isIridescenceShaderPresent);
            if (!ModPayloads.resourcesReloaded) {
                client.reloadResourcePacks();
                ModPayloads.resourcesReloaded = true;
            }
        }
    }

    static void reloadShaders(Minecraft client, boolean isWorldShaderPresent, boolean isIridescenceShaderPresent) {
        if (client.player == null) return;
        if (isIridescenceShaderPresent) {
            ((GameRendererAccess)(client.gameRenderer)).infinity$loadPP(InfinityMethods.getId("shaders/post/iridescence.json"));
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
            ((GameRendererAccess)(client.gameRenderer)).infinity$loadPP(InfinityMethods.getId("shaders/" + FILENAME));
            return;
        }
        client.gameRenderer.shutdownEffect();
    }

    static void updateWorldShaderFromDisk(Minecraft client) throws IOException {
        PackRepository m = client.getResourcePackRepository();
        Path resourcepackPath = client.getResourcePackDirectory().resolve("infinity");
        Files.createDirectories(resourcepackPath.resolve("assets/infinity/shaders"));
        if (!resourcepackPath.resolve("pack.mcmeta").toFile().exists())
            CommonIO.write(packMcmeta(), resourcepackPath.toString(), "pack.mcmeta");
        m.reload();
        m.addPack("file/" + resourcepackPath.getFileName().toString());
    }

    static CompoundTag packMcmeta() {
        CompoundTag res = new CompoundTag();
        CompoundTag pack = new CompoundTag();
        pack.putInt("pack_format", 34);
        pack.putString("description", "Shader container");
        res.put("pack", pack);
        return res;
    }
}
