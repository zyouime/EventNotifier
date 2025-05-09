package me.zyouime.eventnotifier.render.hud;

import me.zyouime.eventnotifier.render.RenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

import static me.zyouime.eventnotifier.render.font.FontRenderers.mainFont;

public class Prikol {
    private final String message;
    private final long aliveTime;

    public Prikol(String message) {
        this.message = message;
        this.aliveTime = System.currentTimeMillis() + 3000;
    }

    public boolean isEnded() {
        return System.currentTimeMillis() >= aliveTime;
    }

    public void render(DrawContext context) {
        float centerX = context.getScaledWindowWidth() / 2f;
        float centerY = context.getScaledWindowHeight() / 2f;
        RenderHelper.drawRoundedQuadInternal(context.getMatrices().peek().getPositionMatrix(), centerX - 50, centerY - 50, centerX + 50, centerY + 50, 5, 4, Color.BLACK);
        mainFont.drawString(context.getMatrices(), message, centerX, centerY, Color.WHITE.getRGB());
    }
}

