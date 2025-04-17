package me.zyouime.eventnotifier.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.awt.*;

public class RenderHelper {

    private static final Tessellator tessellator = Tessellator.getInstance();

    public static void drawRect(MatrixStack matrixStack, float x, float y, float width, float height, Color color) {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;
        BufferBuilder vertexConsumer = tessellator.getBuffer();
        vertexConsumer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        vertexConsumer.vertex(matrix4f, x, y, 0).color(r, g, b, a).next();
        vertexConsumer.vertex(matrix4f, x, y + height, 0).color(r, g, b, a).next();
        vertexConsumer.vertex(matrix4f,x + width, y + height, 0).color(r, g, b, a).next();
        vertexConsumer.vertex(matrix4f,x + width, y, 0).color(r, g, b, a).next();
        tessellator.draw();
        RenderSystem.disableBlend();
    }

    public static void drawRoundedQuadInternal(Matrix4f matrix, double fromX, double fromY, double toX, double toY, double radius, double samples, Color color) {
        float cr = color.getRed() / 255f;
        float cg = color.getGreen() / 255f;
        float cb = color.getBlue() / 255f;
        float ca = color.getAlpha() / 255f;
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        double[][] map = new double[][]{new double[]{toX - radius, toY - radius, radius}, new double[]{toX - radius, fromY + radius, radius}, new double[]{fromX + radius, fromY + radius, radius}, new double[]{fromX + radius, toY - radius, radius}};
        for (int i = 0; i < 4; i++) {
            double[] current = map[i];
            double rad = current[2];
            for (double r = i * 90d; r < (360 / 4d + i * 90d); r += (90 / samples)) {
                float rad1 = (float) Math.toRadians(r);
                float sin = (float) (Math.sin(rad1) * rad);
                float cos = (float) (Math.cos(rad1) * rad);
                bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
            }
            float rad1 = (float) Math.toRadians((360 / 4d + i * 90d));
            float sin = (float) (Math.sin(rad1) * rad);
            float cos = (float) (Math.cos(rad1) * rad);
            bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
        }
        tessellator.draw();
        RenderSystem.disableBlend();
    }

    public static void drawTexture(DrawContext context, float x, float y, float width, float height, float u, float v, float regionWidth, float regionHeight, float textureWidth, float textureHeight, Identifier texture) {
        MatrixStack matrixStack = context.getMatrices();
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, x, y, 0).texture(u / textureHeight, v / textureHeight).next();
        bufferBuilder.vertex(matrix4f, x, y + height, 0).texture(u / textureWidth, (v + regionHeight) / textureHeight).next();
        bufferBuilder.vertex(matrix4f, x + width, y + height, 0).texture((u + regionWidth) / textureWidth, (v + regionHeight) / textureHeight).next();
        bufferBuilder.vertex(matrix4f, x + width, y, 0).texture((u + regionWidth) / textureWidth, v / textureHeight).next();
        tessellator.draw();
    }
}
