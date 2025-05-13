package me.zyouime.eventnotifier.mixin;

import me.zyouime.eventnotifier.EventNotifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        if (!EventNotifier.initialized) {
            EventNotifier.initialized = true;
            EventNotifier.getInstance().sendRegisterMsg(MinecraftClient.getInstance().getSession().getUsername());
        }
    }
}
