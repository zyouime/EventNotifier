package me.zyouime.eventnotifier.screen.widget;

import net.minecraft.client.gui.DrawContext;

public class ButtonWidget extends Widget {
    public float x;
    public float y;
    public final float width;
    public final float height;
    public Runnable runnable;

    public ButtonWidget(float x, float y, float width, float height, Runnable onPress) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.runnable = onPress;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY) {

    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && mouseX >= this.x && mouseX <= this.x + width && mouseY >= this.y && mouseY <= this.y + height) {
            this.runnable.run();
            return true;
        }
        return false;
    }

    public void update(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
