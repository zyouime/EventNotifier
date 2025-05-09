package me.zyouime.eventnotifier.render.hud;

import me.zyouime.eventnotifier.render.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.awt.*;

import static me.zyouime.eventnotifier.render.font.FontRenderers.mainFont;

public class Prikol {
    private final String message;
    private final long aliveTime;
    private final Identifier texture;

    public Prikol(String message) {
        this.message = message;
        this.aliveTime = System.currentTimeMillis() + 3000;
        this.texture = null;
    }

    public Prikol(String message, Identifier texture) {
        this.message = message;
        this.aliveTime = System.currentTimeMillis() + 3000;
        this.texture = texture;
    }

    public boolean isEnded() {
        if (System.currentTimeMillis() >= aliveTime) {
            if (texture != null) {
                MinecraftClient.getInstance().getTextureManager().destroyTexture(texture);
            }
            return true;
        }
        return false;
    }

    public void render(DrawContext context) {
        float centerX = context.getScaledWindowWidth() / 2f;
        float centerY = context.getScaledWindowHeight() / 2f;
        RenderHelper.drawRoundedQuadInternal(context.getMatrices().peek().getPositionMatrix(), centerX - 50, centerY - 50, centerX + 50, centerY + 50, 5, 4, Color.BLACK);
        mainFont.drawString(context.getMatrices(), message, centerX, centerY, Color.WHITE.getRGB());
        if (texture != null) {
            RenderHelper.drawTexture(context, );
        }
    }
}

