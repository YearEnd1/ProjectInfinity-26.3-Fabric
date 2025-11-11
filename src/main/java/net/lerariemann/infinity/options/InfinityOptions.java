package net.lerariemann.infinity.options;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.access.InfinityOptionsAccess;
import net.lerariemann.infinity.util.VersionMethods;
import net.lerariemann.infinity.util.core.CommonIO;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import org.joml.Vector3f;

import java.io.File;
import java.util.function.Function;

public class InfinityOptions {
    public CompoundTag data;
    public PitchShifter shifter;
    public EffectGiver effect;

    private final double mavity;
    private final double timeScale;
    private final boolean haunted;

    public InfinityOptions(CompoundTag data) {
        this.data = data;
        this.shifter = PitchShifter.decode(NbtUtils.getCompound(data, "pitch_shift", new CompoundTag()));
        this.effect = EffectGiver.of(NbtUtils.getCompound(data, "effect", new CompoundTag()));
        this.mavity = NbtUtils.getDouble(data, "mavity", 1.0);
        this.timeScale = NbtUtils.getDouble(data, "time_scale", 1.0);
        this.haunted = NbtUtils.getBoolean(data, "haunted", false);
    }

    public CompoundTag data() {
        return data;
    }

    public static InfinityOptions empty() {
        return new InfinityOptions(new CompoundTag());
    }

    public static CompoundTag readData(MinecraftServer server, ResourceLocation worldId) {
        if (worldId.getNamespace().equals(InfinityMod.MOD_ID)) {
            String name = worldId.getPath();
            File f = server.getWorldPath(LevelResource.DATAPACK_DIR).resolve(name + "/data/infinity/options.json").toFile();
            if (f.exists()) {
                return CommonIO.read(f);
            }
        }
        return new CompoundTag();
    }
    public static InfinityOptions generate(MinecraftServer server, ResourceLocation worldId) {
        return new InfinityOptions(readData(server, worldId));
    }

    public static InfinityOptions access(Level world) {
        return ((InfinityOptionsAccess)world).infinity$getOptions();
    }

    public static InfinityOptions ofClient() {
        return ofClient(Minecraft.getInstance());
    }

    public static InfinityOptions ofClient(Minecraft client) {
        return ((InfinityOptionsAccess)client).infinity$getOptions();
    }

    public static InfinityOptions nullSafe(InfinityOptions options) {
        return (options != null) ? options : InfinityOptions.empty();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public CompoundTag getShader() {
        return NbtUtils.getCompound(data, "shader", new CompoundTag());
    }

    public double getTimeScale() {
        return timeScale;
    }
    public double getMavity() {
        return mavity;
    }
    public Function<Float, Float> getSoundPitch() {
        return shifter.applier();
    }
    public boolean isHaunted() {
        return haunted;
    }
    public int getHauntingTicks(RandomSource random) {
        if (!isHaunted()) return -2;
        if (data.contains("haunting_ticks")) return NbtUtils.getInt(data, "haunting_ticks");
        return random.nextIntBetweenInclusive(NbtUtils.getInt(data, "min_haunting_ticks", 20), NbtUtils.getInt(data, "max_haunting_ticks", 200));
    }

    //sky - common
    public String getSkyType() {
        return NbtUtils.getString(data, "sky_type", "empty");
    }
    public float getHorizonShadingRatio() {
        return NbtUtils.getFloat(data, "horizon_shading_ratio", 1.0f);
    }
    public boolean endSkyLike() {
        return NbtUtils.getBoolean(data, "end_sky_like", false);
    }
    public boolean hasDawn() {
        return NbtUtils.getBoolean(data, "dawn", !getSkyType().equals("rainbow"));
    }

    //sun
    public float getSolarSize() {
        return NbtUtils.getFloat(data, "solar_size", 30.0f);
    }
    public float getSolarTilt() {
        return NbtUtils.getFloat(data, "solar_tilt", -90.0f);
    }
    public Vector3f getSolarTint() {
        int color = NbtUtils.getInt(data, "solar_tint",16777215);
        return new Vector3f((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f);
    }
    public ResourceLocation getSolarTexture() {
        return VersionMethods.id(NbtUtils.getString(data, "solar_texture", "textures/environment/sun.png"));
    }

    //stars
    public int getNumStars() {
        return NbtUtils.getInt(data, "num_stars", 1500);
    }
    public float getStarSizeBase() {
        return NbtUtils.getFloat(data, "star_size_base", 0.15f);
    }
    public float getStarSizeModifier() {
        return NbtUtils.getFloat(data, "star_size_modifier", 0.1f);
    }
    public float getStellarTiltY() {
        return NbtUtils.getFloat(data, "stellar_tilt_y", -90.0f);
    }
    public float getStellarTiltZ() {
        return NbtUtils.getFloat(data, "stellar_tilt_z", 0.0f);
    }
    public float getStellarVelocity() {
        return NbtUtils.getFloat(data, "stellar_velocity", 1.0f);
    }
    public float getDayStarBrightness() {
        return NbtUtils.getFloat(data, "star_brightness_day", 0.0f);
    }
    public float getNightStarBrightness() {
        return NbtUtils.getFloat(data, "star_brightness_night", 0.5f);
    }
    public Vector3f getStellarColor() {
        int color = NbtUtils.getInt(data, "stellar_color",16777215);
        return new Vector3f((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f);
    }

    //moons
    public boolean isMoonCustom() {
        return data.contains("lunar_texture");
    }
    public int getNumMoons() {
        return data.contains("moons") ? NbtUtils.getList(data,"moons", Tag.TAG_COMPOUND).size() : 1;
    }
    public boolean lunarTest(String key, int i) {
        return data.contains("moons") && ((CompoundTag)(NbtUtils.getList(data, "moons", Tag.TAG_COMPOUND).get(i))).contains(key);
    }
    public float fullLunarTest(String key, int i, float def) {
        return lunarTest(key, i) ? ((CompoundTag)(NbtUtils.getList(data, "moons", Tag.TAG_COMPOUND).get(i))).getFloat(key) : def;
    }
    public float getLunarSize(int i) {
        return fullLunarTest("lunar_size", i, 20.0f);
    }
    public float getLunarTiltY(int i) {
        return fullLunarTest("lunar_tilt_y", i, -90.0f);
    }
    public float getLunarTiltZ(int i) {
        return fullLunarTest("lunar_tilt_z", i, 0.0f);
    }
    public Vector3f getLunarTint(int i) {
        int color = lunarTest("lunar_tint", i) ? ((CompoundTag)(NbtUtils.getList(data, "moons", Tag.TAG_COMPOUND).get(i))).getInt("lunar_tint") : 16777215;
        return new Vector3f((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f);
    }
    public ResourceLocation getLunarTexture(int i) {
        if (lunarTest("lunar_texture", i))
            return VersionMethods.id(NbtUtils.getString(((CompoundTag) (NbtUtils.getList(data, "moons", Tag.TAG_COMPOUND).get(i))), "lunar_texture"));
        return VersionMethods.id("textures/environment/moon_phases.png");
    }
    public float getLunarVelocity(int i) {
        return fullLunarTest("lunar_velocity", i, 1.0f);
    }
    public float getLunarOffset(int i) {
        return fullLunarTest("lunar_offset", i, 0.0f);
    }
}
