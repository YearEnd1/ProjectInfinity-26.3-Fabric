package net.lerariemann.infinity.mixin.core;

import com.mojang.serialization.Lifecycle;
import net.lerariemann.infinity.access.RegistryAccess;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Needed for runtime dimension generation to inject generated content into the game.
 * Based on DynReg by BasiqueEvangelist.
 * */
@Mixin(MappedRegistry.class)
public abstract class SimpleRegistryMixin<T> implements RegistryAccess, Registry<T> {
    @Shadow
    private boolean frozen;
    @Shadow
    @Nullable
    private Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders;
    @Mutable
    @Shadow @Final
    private Map<TagKey<T>, HolderSet.Named<T>> frozenTags;
    @Shadow private MappedRegistry.TagSet<T> allTags;
    @Unique
    private boolean infinity$intrusive;

    @Inject(method = "<init>(Lnet/minecraft/resources/ResourceKey;Lcom/mojang/serialization/Lifecycle;Z)V", at = @At("TAIL"))
    private void saveIntrusiveness(ResourceKey<?> key, Lifecycle lifecycle, boolean intrusive, CallbackInfo ci) {
        infinity$intrusive = intrusive;
    }

    @Override
    public void infinity$unfreeze() {
        frozen = false;
        allTags = MappedRegistry.TagSet.unbound();
        if (infinity$intrusive) this.unregisteredIntrusiveHolders = new IdentityHashMap<>();
    }
}
