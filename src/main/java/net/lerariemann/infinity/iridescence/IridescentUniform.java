package net.lerariemann.infinity.iridescence;

//?if >1.21.2 {
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import net.lerariemann.infinity.util.loading.ShaderLoader;
import net.minecraft.client.Minecraft;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.time.LocalTime;

public class IridescentUniform implements AutoCloseable {
    public static final int UBO_SIZE = new Std140SizeCalculator().putFloat().putFloat().putFloat().putFloat().get();
    public final GpuBuffer buffer = RenderSystem.getDevice().createBuffer(() -> "Iridescence Settings", 136, UBO_SIZE);
    public static final String NAME = "IridescentUniform";

    public boolean isActive() {
        return ShaderLoader.iridLevel.get() >= 0;
    }
    public void trySet(RenderPass renderPass) {
        if (isActive() && buffer != null) renderPass.setUniform(NAME, buffer);
    }

    public void tryUpdate() {
        if (isActive()) try (MemoryStack memorystack = MemoryStack.stackPush()) {
            ByteBuffer bytebuffer = Std140Builder.onStack(memorystack, UBO_SIZE)
                    .putFloat(((int)(LocalTime.now().toNanoOfDay() / 1000000)) / 1000.0f)
                    .putFloat(ShaderLoader.iridLevel.get())
                    .putFloat((float)ShaderLoader.iridProgress.get())
                    .putFloat(Minecraft.getInstance().options.screenEffectScale().get().floatValue())
                    .get();
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.buffer.slice(), bytebuffer);
        }
    }

    @Override
    public void close() {
        this.buffer.close();
    }
}
//?}
