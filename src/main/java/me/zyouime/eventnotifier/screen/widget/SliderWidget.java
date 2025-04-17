package me.zyouime.eventnotifier.screen.widget;

import me.zyouime.eventnotifier.render.RenderHelper;
import me.zyouime.eventnotifier.render.font.FontRenderers;
import me.zyouime.eventnotifier.setting.NumberSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Locale;

public class SliderWidget extends Widget {
    private final float width, height;
    private float value;
    private final float x;
    private final float y;
    private final int min, max;
    private final boolean isVertical;
    private final NumberSetting setting;
    private final Color color;
    private boolean dragging;

    public SliderWidget(float x, float y, int min, int max, boolean isVertical, NumberSetting setting, Color color) {
        this.x = x;
        this.y = y;
        this.width = 36;
        this.height = 3;
        this.min = min;
        this.max = max;
        this.isVertical = isVertical;
        this.setting = setting;
        this.value = setting.getValue();
        this.color = color;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY) {
        MatrixStack matrixStack = context.getMatrices();
        RenderHelper.drawRect(matrixStack, x, y, width, height, new Color(80, 80, 80, 156));
        float sliderPosition = isVertical ? MathHelper.clamp(y + height - (height * (value - min) / (max - min)), y, y + height - 1.8f) : MathHelper.clamp(x + (width * (value - min) / (max - min)), x, x + width - 1.8f);
        if (isVertical) {
            RenderHelper.drawRect(matrixStack, x, sliderPosition, width, height - (sliderPosition - y), color);
            RenderHelper.drawRect(matrixStack, x - 1, sliderPosition, width + 2.2f, 2, Color.WHITE);
        } else {
            FontRenderers.mainFont.drawString(matrixStack, setting.toString(), x, y + 8f, Color.WHITE.getRGB());
            RenderHelper.drawRect(matrixStack, x, y, (sliderPosition - x), height, color);
            RenderHelper.drawRect(matrixStack, sliderPosition, y - 1, 2, height + 2, Color.WHITE);
        }
    }

    private void applyValue() {
        float saveValue = Float.parseFloat(String.format(Locale.US, "%.2f", value));
        setting.setValue(saveValue);
    }

    private void setValue(double value) {
        double d = this.value;
        this.value = (float) MathHelper.clamp(value, min, max);
        if (d != this.value) {
            this.applyValue();
        }
    }

    private void setValueFromMouse(double mouseX, double mouseY) {
        if (isVertical) {
            this.setValue(max - ((mouseY - (this.y + 1)) / (this.height - 2)) * (max - min));
        } else {
            this.setValue(((mouseX - (this.x + 1)) / (this.width - 2)) * (max - min) + min);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= this.x && mouseX <= this.x + width && mouseY >= this.y && mouseY <= this.y + height) {
            dragging = true;
            this.setValueFromMouse(mouseX, mouseY);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) {
            this.setValueFromMouse(mouseX, mouseY);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (dragging) {
            dragging = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
