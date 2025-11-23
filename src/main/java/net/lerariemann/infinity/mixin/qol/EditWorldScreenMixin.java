package net.lerariemann.infinity.mixin.qol;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.lerariemann.infinity.access.ScreenAccess;
import net.lerariemann.infinity.util.InfinityMethods;
//? if >1.21 {
import net.minecraft.client.Minecraft;
//?}
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EditWorldScreen.class)
public class EditWorldScreenMixin {

    @Shadow @Final private BooleanConsumer callback;

    @Inject(method = "<init>", at = @At(value = "RETURN", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
            void addButton(
            //? if >1.21 {
            Minecraft client, LevelStorageSource.LevelStorageAccess session, String levelName, BooleanConsumer callback
            //?} else {
    /*BooleanConsumer booleanConsumer, LevelStorageSource.LevelStorageAccess session
    *///?}
    , CallbackInfo ci) {
        var screen = (EditWorldScreen) (Object) this;
        ((ScreenAccess) screen).infinity$addWidget(Button.builder(Component.translatable("screen.infinity.delete_datapacks"), button -> {
            boolean bl = InfinityMethods.deleteLevel(session);
            this.callback.accept(!bl);
        }).width(200).tooltip(Tooltip.create(Component.translatable("screen.infinity.delete_datapacks.tooltip"), null)).build());
    }
}
