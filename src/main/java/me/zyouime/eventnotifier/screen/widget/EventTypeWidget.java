package me.zyouime.eventnotifier.screen.widget;

import me.zyouime.eventnotifier.render.RenderHelper;
import me.zyouime.eventnotifier.render.font.FontRenderers;
import me.zyouime.eventnotifier.util.EventNotifierType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

import java.awt.*;

public class EventTypeWidget extends ButtonWidget {

    private final EventNotifierType eventType;

    public EventTypeWidget(float x, float y, Runnable onPress, EventNotifierType type) {
        super(x, y, 60, 20, onPress);
        this.eventType = type;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY) {
        MatrixStack matrixStack = context.getMatrices();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        Color bg = eventType == eventNotifier.eventType ? Color.GREEN : Color.WHITE;
        RenderHelper.drawRoundedQuadInternal(matrix4f, x, y, x + width, y + height, 3, 4, bg);
        RenderHelper.drawRoundedQuadInternal(matrix4f, x + 0.5f, y + 0.5f, x + (width - 0.5f), y + (height - 0.5f), 3, 4, Color.BLACK);
        String[] renderText = eventType.name.split(" ");
        FontRenderers.mainFont.drawCenteredString(matrixStack, renderText[0], x + width / 2, (y + height / 2) - 2, Color.WHITE.getRGB());
        super.render(context, mouseX, mouseY);
    }
}
