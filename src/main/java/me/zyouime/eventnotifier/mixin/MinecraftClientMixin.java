package me.zyouime.eventnotifier.mixin;

import me.zyouime.eventnotifier.render.font.FontRenderers;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.io.IOException;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        try {
            FontRenderers.mainFont = FontRenderers.create(16f);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
