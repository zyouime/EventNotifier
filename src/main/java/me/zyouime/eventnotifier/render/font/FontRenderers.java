package me.zyouime.eventnotifier.render.font;

import me.zyouime.eventnotifier.EventNotifier;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class FontRenderers {

    public static FontRenderer mainFont;

    public static @NotNull FontRenderer create(float size) throws IOException, FontFormatException {
        return new FontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(EventNotifier.class.getClassLoader().getResourceAsStream("assets/eventnotifier/fonts/sf_medium.ttf"))).deriveFont(Font.PLAIN, size / 2f), size / 2f);
    }
}
