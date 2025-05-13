package me.zyouime.eventnotifier.mixin;

import me.zyouime.eventnotifier.EventNotifier;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Session.class)
public abstract class SessionMixin {

    @Shadow @Final private String username;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo info) {
        if (EventNotifier.initialized) {
            EventNotifier.getInstance().sendRegisterMsg(this.username);
        }
    }
}
