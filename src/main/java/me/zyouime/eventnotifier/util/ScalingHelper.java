package me.zyouime.eventnotifier.util;

import net.minecraft.client.MinecraftClient;

public class ScalingHelper {

    public static float getScale() {
        float scale = 2f;
        int i = (int) MinecraftClient.getInstance().getWindow().getScaleFactor();
        switch (i) {
            case 1 -> scale = 1f;
            case 3 -> scale = 1.4f;
            case 4 -> scale = 0.79f;
        }
        return scale;
    }
}
