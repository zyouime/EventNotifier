package me.zyouime.eventnotifier.util;

import me.zyouime.eventnotifier.EventNotifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TextureLoader {

    public static UrlTexture loadTextureFromUrl(String url, String textureName) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setInstanceFollowRedirects(true);          
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " + "AppleWebKit/537.36 (KHTML, like Gecko) " + "Chrome/115.0.0.0 Safari/537.36");
            connection.setRequestProperty("Accept", "image/avif,image/webp,image/apng,image/*,*/*;q=0.8");
            connection.setRequestProperty("Referer", "https://postimages.org/");
            connection.connect();
            try (InputStream is = connection.getInputStream()) {
                NativeImage image = NativeImage.read(is);
                Identifier id = new Identifier("eventnotifier", "temp/" + textureName);
                MinecraftClient.getInstance().execute(() -> {
                    try {
                        NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
                        texture.upload(); 
                        MinecraftClient.getInstance().getTextureManager().registerTexture(id, texture);
                    } catch (Exception e) {
                        EventNotifier.LOGGER.error("Не получилось загрузить текстуру, ошибка: " + e.getMessage());
                    }
                });
                UrlTexture urlTexture = new UrlTexture(id, image.getWidth(), image.getHeight());
                connection.disconnect();
                return urlTexture;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
