package me.zyouime.eventnotifier.screen.widget;

import me.zyouime.eventnotifier.util.Wrapper;
import net.minecraft.client.gui.DrawContext;

public abstract class Widget implements Wrapper {

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    public abstract void render(DrawContext context, double mouseX, double mouseY);

}
