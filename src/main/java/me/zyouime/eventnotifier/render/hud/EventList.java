package me.zyouime.eventnotifier.render.hud;

import me.zyouime.eventnotifier.EventNotifier;
import me.zyouime.eventnotifier.render.RenderHelper;
import me.zyouime.eventnotifier.util.Event;
import me.zyouime.eventnotifier.util.EventDisplayInfo;
import me.zyouime.eventnotifier.util.Wrapper;
import me.zyouime.eventnotifier.websocket.WebSocket;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static me.zyouime.eventnotifier.render.font.FontRenderers.mainFont;

public class EventList implements Wrapper {

    private float x, y;
    private float width;
    private float height;
    private float maxHeight;
    private double scrollAmount;
    private float f;
    public boolean render;
    private final Map<String, EventDisplayInfo> eventFormatting = Map.of(
            "Босс", EventDisplayInfo.BOSS,
            "Опытный Тыпо", EventDisplayInfo.TIPO,
            "Корабль", EventDisplayInfo.SHIP,
            "Контейнер", EventDisplayInfo.CONTAINER,
            "Золотая лихорадка", EventDisplayInfo.FEVER,
            "Посылка", EventDisplayInfo.PARCEL,
            "Груз", EventDisplayInfo.CARGO,
            "Цветочная поляна", EventDisplayInfo.GLADE,
            "Смертельная шахта", EventDisplayInfo.DEATH_MINE,
            "Голосование", EventDisplayInfo.VOTE
    );


    public EventList() {
        EventNotifier.Settings settings = eventNotifier.settings;
        this.x = settings.x.getValue();
        this.y = settings.y.getValue();
        this.width = settings.width.getValue();
        this.maxHeight = settings.height.getValue();
    }

    public void render(DrawContext context) {
        if (!render) return;
        MatrixStack matrixStack = context.getMatrices();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        if (!events.isEmpty()) {
            int count = 0;
            for (Event event : events) {
                if (eventNotifier.settings.isRenderEvent(event.getEvent())) continue;
                count++;
            }
            f = count;
        } else f = 0.1f;
        height = (7 + 17 * f) + 26;
        if (height > maxHeight) height = maxHeight;
        float maxWidth = 360;
        if (width > maxWidth) width = maxWidth;
        float scale = 1f;
        float sWidth = context.getScaledWindowWidth();
        float sHeight = context.getScaledWindowHeight();
        float centerX = sWidth / 2f;
        float centerY = sHeight / 2f;
        float maxX = (sWidth / scale) - width;
        float maxY = (sHeight / scale) - height;
        float renderX = Math.max(0, Math.min(centerX / scale + x, maxX));
        float renderY = Math.max(0, Math.min(centerY / scale + y, maxY));
        RenderHelper.drawRoundedQuadInternal(matrix4f, renderX, renderY, renderX + width, renderY + height, 10, 7, new Color(32, 32, 32, 196));
        float offsetY = 30;
        context.enableScissor((int) renderX + 2, (int) renderY + 26, (int) (renderX + width - 2), (int) (renderY + height - 8));
        for (Event event : events) {
            if (eventNotifier.settings.isRenderEvent(event.getEvent())) continue;
            float eventY = renderY + offsetY - (float) scrollAmount;
            Color eventColor = eventFormatting.getOrDefault(event.getEvent(), EventDisplayInfo.UNKNOWN).color;
            int u = eventFormatting.getOrDefault(event.getEvent(), EventDisplayInfo.UNKNOWN).u;
            int v = eventFormatting.getOrDefault(event.getEvent(), EventDisplayInfo.UNKNOWN).v;
            String texturePath = eventFormatting.getOrDefault(event.getEvent(), EventDisplayInfo.UNKNOWN).texturePath;
            RenderHelper.drawRoundedQuadInternal(matrix4f, renderX + 2, eventY - 4, renderX + width - 2, eventY + 8, 6, 5, new Color(100, 100, 100, 96));
            String renderText = event.getEvent();
//            if (mainFont.getStringWidth(event.getEvent()) > width - 40) {
//                char[] c = event.getEvent().toCharArray();
//                StringBuilder sb = new StringBuilder();
//                int index = 0;
//                while (mainFont.getStringWidth(sb.toString()) < width - 45) {
//                    sb.append(c[index]);
//                    index++;
//                }
//                sb.append("...");
//                renderText = sb.toString();
//            }
            if (event.getEvent().equals("Голосование")) {
                RenderHelper.drawTexture(context, renderX + 3, eventY - 2.5f, 9, 9, u, v, 512, 512, 512, 512, new Identifier("eventnotifier", texturePath));
            } else {
                RenderHelper.drawTexture(context, renderX + 3, eventY - 2.5f, 9, 9, u, v, 600, 600, 2048, 2048, new Identifier("eventnotifier", texturePath));
            }
            mainFont.drawString(matrixStack, renderText, renderX + 14, eventY, eventColor.getRGB());
            mainFont.drawString(matrixStack, String.valueOf(event.getAnarchy()), (renderX + width - 5) - mainFont.getStringWidth(String.valueOf(event.getAnarchy())), eventY, Color.WHITE.getRGB());
            offsetY += 17;
        }
        context.disableScissor();
        mainFont.drawCenteredString(matrixStack, eventNotifier.eventType.name, renderX + width / 2f, renderY + 4, Color.WHITE.getRGB());
        String update = WebSocket.update ? "Обновление!" : "Ждём обновления..";
        Color updateColor = WebSocket.update ? Color.WHITE : Color.GRAY;
        mainFont.drawCenteredString(matrixStack, update, renderX + width / 2f, renderY + 15, updateColor.getRGB());
        Color connStatus = eventNotifier.WEB_SOCKET.getConnection().isOpen() ? Color.GREEN : Color.RED;
        RenderHelper.drawRoundedQuadInternal(matrix4f, renderX + 6, renderY + 6, renderX + 10, renderY + 10, 2, 4, connStatus);
        RenderHelper.drawTexture(context, renderX + width - 14, renderY + 5, 9, 9, 0, 0, 256, 256, 256, 256, new Identifier("eventnotifier", "textures/gear.png"));
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public void updateSize(float width, float height) {
        this.width = width;
        this.maxHeight = height;
    }

    public void setScrollAmount(double scrollAmount) {
        float totalHeight = (f * 17) + 26;
        if (totalHeight > maxHeight) {
            float maxScroll = totalHeight - maxHeight + 5;
            this.scrollAmount = Math.max(0, Math.min(scrollAmount, maxScroll));
        } else {
            this.scrollAmount = 0;
        }
    }

    public boolean mouseScrolled(double amount) {
        this.setScrollAmount(scrollAmount - amount * 12);
        return true;
    }
}
