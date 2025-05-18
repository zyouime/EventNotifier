package me.zyouime.eventnotifier.mixin;

import me.zyouime.eventnotifier.EventNotifier;
import me.zyouime.eventnotifier.config.ConfigData;
import me.zyouime.eventnotifier.config.ModConfig;
import me.zyouime.eventnotifier.render.hud.EventList;
import me.zyouime.eventnotifier.screen.ModScreen;
import me.zyouime.eventnotifier.setting.Setting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChatScreen.class, priority = 500)
public abstract class ChatScreenMixin extends Screen {

    @Unique
    private float scaledCenterX;
    @Unique
    private float scaledCenterY;
    @Unique
    private float scaledWidth;
    @Unique
    private float scaledHeight;
    @Unique
    private float offsetX;
    @Unique
    private float offsetY;
    @Unique
    private boolean isDragging;
    @Unique
    private Window window;
    @Unique
    private EventList eventList;


    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        window = this.client.getWindow();
        scaledWidth = window.getScaledWidth();
        scaledHeight = window.getScaledHeight();
        scaledCenterX = scaledWidth / 2f;
        scaledCenterY = scaledHeight / 2f;
        eventList = EventNotifier.getInstance().eventList;
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        eventList.render(context);
    }

//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        return super.mouseClicked(mouseX, mouseY, button);
//    }

    @Unique
    private float getEventX() {
        return Math.max(0, Math.min(scaledCenterX + eventList.getX(), scaledWidth - eventList.getWidth()));
    }

    @Unique
    private float getEventY() {
        return Math.max(0, Math.min(scaledCenterY + eventList.getY(), scaledHeight - eventList.getHeight()));
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        float eventX = this.getEventX();
        float eventY = this.getEventY();
        if (mouseX >= eventX + eventList.getWidth() - 14 && mouseX <= eventX + eventList.getWidth() - 5 && mouseY >= eventY + 5 && mouseY <= eventY + 14) {
            this.client.setScreen(new ModScreen(client.currentScreen));
            cir.setReturnValue(true);
        } else if (mouseX >= eventX && mouseX <= eventX + eventList.getWidth() && mouseY >= eventY && mouseY <= eventY + eventList.getHeight()) {
            isDragging = true;
            offsetX = (float) mouseX - eventX;
            offsetY = (float) mouseY - eventY;
            cir.setReturnValue(true);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging) {
            float x = ((float) mouseX - scaledCenterX - offsetX);
            float y = ((float) mouseY - scaledCenterY - offsetY);
            float minX = -scaledCenterX;
            float minY = -scaledCenterY;
            float maxX = scaledCenterX - eventList.getWidth();
            float maxY = scaledCenterY - eventList.getHeight();
            x = Math.max(minX, Math.min(x, maxX));
            y = Math.max(minY, Math.min(y, maxY));
            eventList.setX(x);
            eventList.setY(y);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }



    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        isDragging = false;
        if (eventList != null) {
            EventNotifier.Settings settings = EventNotifier.getInstance().settings;
            settings.x.setValue(eventList.getX());
            settings.y.setValue(eventList.getY());
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }


    @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
    private void mouseScrolled(double mouseX, double mouseY, double amount, CallbackInfoReturnable<Boolean> cir) {
        float eventX = this.getEventX();
        float eventY = this.getEventY();
        if (mouseX >= eventX + 2 && mouseX <= eventX + eventList.getWidth() - 2 && mouseY >= eventY + 26 && mouseY <= eventY + eventList.getHeight() - 8) {
            cir.setReturnValue(eventList.mouseScrolled(amount));
        }
    }

    @Override
    public void close() {
        EventNotifier.getInstance().settings.settingsList.forEach(Setting::save);
        super.close();
    }
}
