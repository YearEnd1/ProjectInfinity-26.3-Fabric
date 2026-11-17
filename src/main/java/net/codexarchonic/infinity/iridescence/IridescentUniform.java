package net.codexarchonic.infinity.iridescence;

//? if >1.21.2 {
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import net.codexarchonic.infinity.util.loading.ShaderLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MappableRingBuffer;

import java.time.LocalTime;

public class IridescentUniform implements AutoCloseable {
    public static final int UBO_SIZE = new Std140SizeCalculator().putFloat().putFloat().putFloat().putFloat().get();
    public final MappableRingBuffer buffer = new MappableRingBuffer(() -> "Iridescence Settings",
            GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_MAP_WRITE, UBO_SIZE);
    public static final String NAME = "IridescentUniform";
    public static GpuBuffer uniformInstance;

    public static boolean isActive() {
        return ShaderLoader.iridLevel.get() >= 0;
    }
    public static void trySet(RenderPass renderPass) {
        if (isActive() && uniformInstance != null) {
            renderPass.setUniform(NAME, uniformInstance);
        }
    }

    public void tryUpdate() {
        if (isActive()) {
            this.buffer.rotate();
            try (var view = this.buffer.currentBuffer().map(false, true)) {
                Std140Builder.intoBuffer(view.data())
                        .putFloat(((int)(LocalTime.now().toNanoOfDay() / 1000000)) / 1000.0f)
                        .putFloat(ShaderLoader.iridLevel.get())
                        .putFloat((float)ShaderLoader.iridProgress.get())
                        .putFloat(Minecraft.getInstance().options.screenEffectScale().get().floatValue());
            }
            uniformInstance = this.buffer.currentBuffer();
        }
    }

    @Override
    public void close() {
        this.buffer.close();
    }
}
//?}
