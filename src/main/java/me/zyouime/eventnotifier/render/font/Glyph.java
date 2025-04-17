package me.zyouime.eventnotifier.render.font;

import me.zyouime.eventnotifier.render.font.GlyphMap;

record Glyph(int u, int v, int width, int height, char value, GlyphMap owner) {
}