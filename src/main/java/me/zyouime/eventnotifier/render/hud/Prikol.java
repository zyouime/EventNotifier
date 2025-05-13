package me.zyouime.eventnotifier.render.hud;

import me.zyouime.eventnotifier.render.RenderHelper;
import me.zyouime.eventnotifier.util.UrlTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

import static me.zyouime.eventnotifier.render.font.FontRenderers.mainFont;

public class Prikol {
    private final String message;
    private final long aliveTime;
    private UrlTexture texture = null;

    public Prikol(String message) {
        this.message = message;
        this.aliveTime = System.currentTimeMillis() + 5000;
    }

    public Prikol(String message, UrlTexture texture) {
        this(message);
        this.texture = texture;
    }

    public boolean isEnded() {
        if (System.currentTimeMillis() >= aliveTime) {
            if (texture != null) {
                MinecraftClient.getInstance().getTextureManager().destroyTexture(texture.texture());
            }
            return true;
        }
        return false;
    }

    public void render(DrawContext context) {
        float centerX = context.getScaledWindowWidth() / 2f;
        float centerY = context.getScaledWindowHeight() / 2f;
        StringBuilder stringBuilder = new StringBuilder();
        int prikolHeigth = 18;
        for (char c : message.toCharArray()) {
            stringBuilder.append(c);
            if (mainFont.getStringWidth(stringBuilder.toString()) > 90) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                stringBuilder.append('\n');
                stringBuilder.append(c);
                prikolHeigth += 10;
            }
        }
        RenderHelper.drawRoundedQuadInternal(context.getMatrices().peek().getPositionMatrix(), centerX - 50, centerY - 50, centerX + 50, centerY - 50 + prikolHeigth, 5, 4, Color.BLACK);
        mainFont.drawString(context.getMatrices(), stringBuilder.toString(), centerX - 47, centerY - 45, Color.WHITE.getRGB());
        if (texture != null) {
            RenderHelper.drawTexture(context, centerX - 32, centerY - 125, 64, 64, 0, 0, texture.width(), texture.height(), texture.width(), texture.height(), texture.texture());
        }
    }
}

