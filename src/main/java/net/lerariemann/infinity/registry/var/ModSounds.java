package net.lerariemann.infinity.registry.var;

import dev.architectury.registry.registries.DeferredRegister;
import net.lerariemann.infinity.util.InfinityMethods;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
//? if >1.21 {
/*import net.minecraft.sounds.SoundEvents;
*///?}

import static net.lerariemann.infinity.InfinityMod.MOD_ID;

public class ModSounds {
    public static final ResourceLocation IVORY_MUSIC_HOPE = InfinityMethods.getId("music.ivory.hope_instilled");
    public static final ResourceLocation IVORY_MUSIC_CHALLENGER = InfinityMethods.getId("music.ivory.challenger");
    public static final ResourceLocation CLASSIC_MUSIC = InfinityMethods.getId("music.c418.classic");
    public static SoundEvent IVORY_MUSIC_HOPE_EVENT = SoundEvent.createVariableRangeEvent(IVORY_MUSIC_HOPE);
    public static SoundEvent IVORY_MUSIC_CHALLENGER_EVENT = SoundEvent.createVariableRangeEvent(IVORY_MUSIC_CHALLENGER);
    public static SoundEvent CLASSIC_MUSIC_SOUND_EVENT = SoundEvent.createVariableRangeEvent(CLASSIC_MUSIC);

    //? if <1.21 {
    public static final ResourceLocation BACKPORT_VAULT = InfinityMethods.getId("backport.vault.open_shutter");
    public static SoundEvent BACKPORT_VAULT_EVENT = SoundEvent.createVariableRangeEvent(BACKPORT_VAULT);
    //?}

    public static SoundEvent portalFail =
            //? if >1.21 {
            /*SoundEvents.VAULT_REJECT_REWARDED_PLAYER;
             *///?} else {
            ModSounds.BACKPORT_VAULT_EVENT;
    //?}
    public static SoundEvent portalOpen =
            //? if >1.21 {
            /*SoundEvents.VAULT_OPEN_SHUTTER;
             *///?} else {
            ModSounds.BACKPORT_VAULT_EVENT;
    //?}

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(MOD_ID, Registries.SOUND_EVENT);

    public static void registerSounds() {
        SOUNDS.register(IVORY_MUSIC_HOPE, () -> IVORY_MUSIC_HOPE_EVENT);
        SOUNDS.register(IVORY_MUSIC_CHALLENGER, () -> IVORY_MUSIC_CHALLENGER_EVENT);
        SOUNDS.register(CLASSIC_MUSIC, () -> CLASSIC_MUSIC_SOUND_EVENT);
        //? if <1.21 {
        SOUNDS.register(BACKPORT_VAULT, () -> BACKPORT_VAULT_EVENT);
        //?}
        SOUNDS.register();
    }
}
