package me.zyouime.eventnotifier;

import me.zyouime.eventnotifier.config.ModConfig;
import me.zyouime.eventnotifier.render.hud.EventList;
import me.zyouime.eventnotifier.setting.BooleanSetting;
import me.zyouime.eventnotifier.setting.NumberSetting;
import me.zyouime.eventnotifier.setting.Setting;
import me.zyouime.eventnotifier.util.Event;
import me.zyouime.eventnotifier.util.EventDisplayInfo;
import me.zyouime.eventnotifier.util.EventNotifierType;
import me.zyouime.eventnotifier.websocket.WebSocket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventNotifier implements ModInitializer {

    public WebSocket WEB_SOCKET;
    public final List<Event> events = new CopyOnWriteArrayList<>();
    public EventList eventList;
    public EventNotifierType eventType;
    private static EventNotifier instance;
    public Settings settings;
    public static final Logger LOGGER = LoggerFactory.getLogger("EventNotifier");
//    public Prikol prikol = null;
//    public static boolean initialized;

    public EventNotifier() {
        instance = this;
    }

    @Override
    public void onInitialize() {
        try {
            WEB_SOCKET = new WebSocket(new URI("ws://localhost:8080/events"));
            WEB_SOCKET.connect();
        } catch (URISyntaxException e) {
            LOGGER.error(e.getMessage());
        }
        ModConfig.initialize();
        settings = new Settings();
        eventType = (EventNotifierType) ModConfig.configData.getField("eventType");
        eventList = new EventList();
        this.registerEvents();
    }

    private void registerEvents() {
        HudRenderCallback.EVENT.register(((drawContext, tickDelta) -> {
            if (MinecraftClient.getInstance().currentScreen == null) {
                eventList.render(drawContext);
            }
//            if (prikol != null) {
//                prikol.render(drawContext);
//            }
        }));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            if (prikol != null && prikol.isEnded()) {
//                prikol = null;
//            }

            if (eventType == EventNotifierType.UPCOMING) {
                events.removeIf(event -> {
                    event.tick();
                    return event.getSeconds() <= 0;
                });
            }
        });
    }


    public static EventNotifier getInstance() {
        return instance;
    }

//    public void sendRegisterMsg(String user) {
//        JsonObject json = new JsonObject();
//        json.addProperty("nick", user);
//        if (WEB_SOCKET.getConnection().isOpen()) {
//            WEB_SOCKET.send(json.toString());
//        }
//    }

    public static class Settings {

        public List<Setting<?>> settingsList = new ArrayList<>();
        public List<Setting<?>> optionsList = new ArrayList<>();

        public BooleanSetting showBoss = registerToOptions(new BooleanSetting("showBoss", EventDisplayInfo.BOSS));
        public BooleanSetting showFever = registerToOptions(new BooleanSetting("showFever", EventDisplayInfo.FEVER));
        public BooleanSetting showTipo = registerToOptions(new BooleanSetting("showTipo", EventDisplayInfo.TIPO));
        public BooleanSetting showContainer = registerToOptions(new BooleanSetting("showContainer", EventDisplayInfo.CONTAINER));
        public BooleanSetting showGlade = registerToOptions(new BooleanSetting("showGlade", EventDisplayInfo.GLADE));
        public BooleanSetting showDeathMine = registerToOptions(new BooleanSetting("showDeathMine", EventDisplayInfo.DEATH_MINE));
        public BooleanSetting showParcel = registerToOptions(new BooleanSetting("showParcel", EventDisplayInfo.PARCEL));
        public BooleanSetting showVote = registerToOptions(new BooleanSetting("showVote", EventDisplayInfo.VOTE));
        public BooleanSetting showCargo = registerToOptions(new BooleanSetting("showCargo", EventDisplayInfo.CARGO));
        public BooleanSetting showShip = registerToOptions(new BooleanSetting("showShip", EventDisplayInfo.SHIP));
        public NumberSetting width = registerToOptions(new NumberSetting("width", EventDisplayInfo.WIDTH));
        public NumberSetting height = registerToOptions(new NumberSetting("height", EventDisplayInfo.HEIGHT));
        public NumberSetting x = register(new NumberSetting("x"));
        public NumberSetting y = register(new NumberSetting("y"));

        public final Map<String, BooleanSetting> eventRenderMap = Map.of(
                "Босс", showBoss,
                "Смертельная шахта", showDeathMine,
                "Цветочная поляна", showGlade,
                "Посылка", showParcel,
                "Груз", showCargo,
                "Корабль", showShip,
                "Голосование", showVote,
                "Контейнер", showContainer,
                "Золотая лихорадка", showFever,
                "Опытный Тыпо", showTipo
        );


        public boolean isRenderEvent(String eventName) {
            BooleanSetting setting = eventRenderMap.get(eventName);
            return setting != null && !setting.getValue();
        }

        private <T extends Setting<?>> T register(T t) {
            this.settingsList.add(t);
            return t;
        }

        private <T extends Setting<?>> T registerToOptions(T t) {
            this.optionsList.add(t);
            this.settingsList.add(t);
            return t;
        }
    }
}
