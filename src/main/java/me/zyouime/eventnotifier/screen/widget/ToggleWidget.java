package me.zyouime.eventnotifier.screen.widget;

import me.zyouime.eventnotifier.render.RenderHelper;
import me.zyouime.eventnotifier.setting.BooleanSetting;
import me.zyouime.eventnotifier.util.ColorHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

import java.awt.*;

public class ToggleWidget extends ButtonWidget {

    private final BooleanSetting setting;

    public ToggleWidget(float x, float y, BooleanSetting setting) {
        super(x, y, 18, 8, () -> {
            setting.setValue(!setting.getValue());
            setting.save();
            eventNotifier.eventList.setScrollAmount(0);
        });
        this.setting = setting;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY) {
        MatrixStack matrixStack = context.getMatrices();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        Color circuit =  ColorHelper.injectAlpha((setting.getValue() ? Color.GREEN : Color.GRAY), 196);
        RenderHelper.drawRoundedQuadInternal(matrix4f, x, y, x + width, y + height, 4, 4, circuit);
        float circlePosX = x + (setting.getValue() ? 11.5f : 1.5f);
        RenderHelper.drawRoundedQuadInternal(matrix4f, circlePosX, y + 1.5f, circlePosX + 5, y + 6.5f, 2f, 4, ColorHelper.injectAlpha(Color.WHITE, 196));
        super.render(context, mouseX, mouseY);
    }
}
