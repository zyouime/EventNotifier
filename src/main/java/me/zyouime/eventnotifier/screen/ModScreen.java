package me.zyouime.eventnotifier.screen;

import me.zyouime.eventnotifier.EventNotifier;
import me.zyouime.eventnotifier.config.ConfigData;
import me.zyouime.eventnotifier.config.ModConfig;
import me.zyouime.eventnotifier.render.RenderHelper;
import me.zyouime.eventnotifier.render.font.FontRenderer;
import me.zyouime.eventnotifier.render.font.FontRenderers;
import me.zyouime.eventnotifier.screen.widget.*;
import me.zyouime.eventnotifier.setting.BooleanSetting;
import me.zyouime.eventnotifier.setting.NumberSetting;
import me.zyouime.eventnotifier.setting.Setting;
import me.zyouime.eventnotifier.util.EventDisplayInfo;
import me.zyouime.eventnotifier.util.EventNotifierType;
import me.zyouime.eventnotifier.util.ScalingHelper;
import me.zyouime.eventnotifier.util.Wrapper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ModScreen extends Screen implements Wrapper {

    private final List<Widget> widgets = new ArrayList<>();
    private float scaledCenterX, scaledCenterY;
    private FontRenderer fontRenderer;
    private float scale;

    public ModScreen(Screen parent) {
        super(Text.empty());
    }

    @Override
    protected void init() {
        Window window = this.client.getWindow();
        scaledCenterX = window.getScaledWidth() / 2f - 20;
        scaledCenterY = window.getScaledHeight() / 2f - 50;
        fontRenderer = FontRenderers.mainFont;
        this.scale = ScalingHelper.getScale();
        widgets.add(new EventTypeWidget(scaledCenterX + 30, scaledCenterY - 70, () -> setTypeAndSave(EventNotifierType.UPCOMING), EventNotifierType.UPCOMING));
        widgets.add(new EventTypeWidget(scaledCenterX + 95, scaledCenterY - 70, () -> setTypeAndSave(EventNotifierType.CURRENT), EventNotifierType.CURRENT));
        float yOffset = 0;
        for (Setting<?> setting : eventNotifier.settings.optionsList) {
            if (setting instanceof NumberSetting) {
                int limiter = setting.getDisplayInfo() == EventDisplayInfo.WIDTH ? 120 : 100;
                widgets.add(new SliderWidget(scaledCenterX + 115, scaledCenterY - 38 + yOffset, limiter, limiter * 3, false, (NumberSetting) setting, setting.getDisplayInfo().color));
            } else if (setting instanceof BooleanSetting) {
                widgets.add(new ToggleWidget(scaledCenterX + 131, scaledCenterY - 38 + yOffset, (BooleanSetting) setting));
            }
            yOffset += 20;
        }
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        MatrixStack matrixStack = context.getMatrices();
        RenderHelper.drawRoundedQuadInternal(matrixStack.peek().getPositionMatrix(), scaledCenterX - 60, scaledCenterY - 80, scaledCenterX + 160, scaledCenterY + 202, 5, 4, Color.BLACK);
        fontRenderer.drawString(matrixStack, "Тип ивентов", scaledCenterX - 45, scaledCenterY - 61, Color.WHITE.getRGB());
        for (Widget widget : widgets) {
            widget.render(context, mouseX, mouseY);
        }
        float yOffset = 0;
        for (Setting<?> setting : eventNotifier.settings.optionsList) {
            int u = setting.getDisplayInfo().u;
            int v = setting.getDisplayInfo().v;
            int rw, rh, tw, th;
            EventNotifier.Settings s = eventNotifier.settings;
            if (setting == s.showVote || setting == s.width || setting == s.height) {
                rw = 512; rh = 512; tw = 512; th = 512;
            } else {
                rw = 600; rh = 600; tw = 2048; th = 2048;
            }
            EventDisplayInfo displayInfo = setting.getDisplayInfo();
            RenderHelper.drawTexture(context, scaledCenterX - 54, scaledCenterY - 40 + yOffset, 9, 9, u, v, rw, rh, tw, th, new Identifier("eventnotifier", displayInfo.texturePath));
            fontRenderer.drawString(matrixStack, displayInfo.settingName, scaledCenterX - 42, scaledCenterY - 37 + yOffset, displayInfo.color.getRGB());
            yOffset += 20;
        }
        matrixStack.push();
        matrixStack.scale(scale, scale, 1f);
        RenderHelper.drawTexture(context, 10, 10, 18, 18, 0, 0, 128, 128, 128, 128, new Identifier("eventnotifier", "textures/telegram.png"));
        matrixStack.pop();
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Widget widget : widgets) {
            if (widget.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        mouseX /= scale;
        mouseY /= scale;
        if (mouseX >= 10 && mouseX <= 28 && mouseY >= 10 && mouseY <= 28) {
            this.client.setScreen(new ConfirmLinkScreen(result -> {
                if (result) {
                    try {
                        Util.getOperatingSystem().open(new URI("https://t.me/zyouime13"));
                    } catch (URISyntaxException e) {
                        LOGGER.error("Не получилось открыть ссылку");
                    }
                }
                this.client.setScreen(new ModScreen(this.client.currentScreen));
            }, "§bЗмееныш13 телеграмм", true));
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (Widget widget : widgets) {
            if (widget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                eventNotifier.eventList.updateSize(eventNotifier.settings.width.getValue(), eventNotifier.settings.height.getValue());
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (Widget widget : widgets) {
            if (widget.mouseReleased(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        eventNotifier.settings.settingsList.forEach(Setting::save);
        super.close();
    }

    private void setTypeAndSave(EventNotifierType type) {
        eventNotifier.events.clear();
        eventNotifier.eventType = type;
        ModConfig.loadConfig();
        ConfigData configData = ModConfig.configData;
        configData.setField("eventType", type);
        ModConfig.saveConfig();
    }
}
