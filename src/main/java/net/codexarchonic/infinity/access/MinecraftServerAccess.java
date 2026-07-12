package net.codexarchonic.infinity.access;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;

public interface MinecraftServerAccess {

    void infinity$addWorld(ResourceKey<Level> key, LevelStem options);

    boolean infinity$hasToAdd(ResourceKey<Level> key);

    boolean infinity$needsInvocation();
    void infinity$onInvocation();

    void infinity$setDimensionProvider();

    //? if <1.21 {
    /*void infinity$setJukeboxDataMap(CompoundTag data);
    boolean infinity$hasJukeboxes();

    String infinity$getRandomJukeboxId();
    String infinity$getJukeboxSubtitle(String id);
    int infinity$getJukeboxDuration(String id);
    *///?}
}
