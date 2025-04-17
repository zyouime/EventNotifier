package me.zyouime.eventnotifier.setting;

import me.zyouime.eventnotifier.config.ModConfig;
import me.zyouime.eventnotifier.util.EventDisplayInfo;

public class Setting<T> {

    private T value;
    private final String configKey;
    private final EventDisplayInfo displayInfo;

    public Setting(String configKey, Class<T> type, EventDisplayInfo displayInfo) {
        this.configKey = configKey;
        Object obj = ModConfig.configData.getField(configKey);
        this.value = type.isInstance(obj) ? type.cast(obj) : null;
        this.displayInfo = displayInfo;
    }

    public void save() {
        ModConfig.loadConfig();
        ModConfig.configData.setField(configKey, value);
        ModConfig.saveConfig();
    }

    public T getValue() {
        return value;
    }

    public EventDisplayInfo getDisplayInfo() {
        return displayInfo;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
