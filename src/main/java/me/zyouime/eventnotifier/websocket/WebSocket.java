package me.zyouime.eventnotifier.websocket;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.zyouime.eventnotifier.util.Event;
import me.zyouime.eventnotifier.util.EventNotifierType;
import me.zyouime.eventnotifier.util.Wrapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

public class WebSocket extends WebSocketClient implements Wrapper {

    public static boolean update;
    private TimerTask timerTask;
    private boolean reconnecting = false;

    public WebSocket(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LOGGER.info("Подключено к серверу");
    }

    @Override
    public void onMessage(String message) {
        handleEventMsg(JsonParser.parseString(message).getAsJsonObject());
//        JsonObject json = JsonParser.parseString(message).getAsJsonObject();
//        String eventType = json.get("eventType").getAsString();
//        if (eventType.equals("prikol")) {
//            handlePrikolMsg(json);
//        } else handleEventMsg(json);
    }

//    private void handlePrikolMsg(JsonObject json) {
//        String prikolMsg = json.get("message").getAsString();
//        String url = json.get("url").getAsString();
//        Prikol prikol;
//        if (!url.isEmpty()) {
//            prikol = new Prikol(prikolMsg, TextureLoader.loadTextureFromUrl(url, "prikol"));
//        } else prikol = new Prikol(prikolMsg);
//        eventNotifier.prikol = prikol;
//    }

    private void handleEventMsg(JsonObject json) {
        String type = json.get("eventType").getAsString();
        if (this.getType(type) != eventNotifier.eventType) return;
        this.setUpdate();
        JsonArray jsonArray = json.get("events").getAsJsonArray();
        if (type.equals("current") && eventNotifier.eventType == EventNotifierType.CURRENT) events.clear();
        for (JsonElement element : jsonArray.asList()) {
            JsonObject jsonObject = element.getAsJsonObject();
            String eventName = jsonObject.get("event").getAsString();
            int anarchy = jsonObject.get("anarchy").getAsInt();
            Event event = new Event(eventName, anarchy);
            if (eventNotifier.eventType == EventNotifierType.UPCOMING && type.equals("upcoming")) {
                event.setSeconds(60 * 20);
            }
            if (events.isEmpty()) {
                events.add(event);
            } else events.add(0, event);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LOGGER.error("Соединение закрыто: " + reason);
        this.reconnectLoop();
    }

    @Override
    public void onError(Exception ex) {
        LOGGER.error("Ошибка с соединением с сервером: " + ex.getMessage());
        this.reconnectLoop();
    }

    private EventNotifierType getType(String type) {
        if (type.equals("upcoming")) {
            return EventNotifierType.UPCOMING;
        } else if (type.equals("current")) {
            return EventNotifierType.CURRENT;
        }
        return null;
    }

    private void setUpdate() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        update = true;
        Timer timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                update = false;
                timerTask = null;
            }
        };
        timer.schedule(timerTask, 2000);
    }

    private synchronized void reconnectLoop() {
        if (reconnecting) return;
        reconnecting = true;
        new Thread(() -> {
            while (!eventNotifier.WEB_SOCKET.getConnection().isOpen()) {
                try {
                    LOGGER.info("Пробую переподключиться..");
                    eventNotifier.WEB_SOCKET.reconnectBlocking();
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    LOGGER.error("Хуйня с потоком: " + e.getMessage());
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    LOGGER.error("Ошибка при переподключении: " + e.getMessage());
                }
            }
            reconnecting = false;
        }).start();
    }
}
