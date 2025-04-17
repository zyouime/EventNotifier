package me.zyouime.eventnotifier.util;

import java.awt.*;

public class ColorHelper {

    public static Color injectAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
