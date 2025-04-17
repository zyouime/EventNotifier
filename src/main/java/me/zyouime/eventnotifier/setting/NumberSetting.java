package me.zyouime.eventnotifier.setting;

import me.zyouime.eventnotifier.util.EventDisplayInfo;

public class NumberSetting extends Setting<Float> {

    public NumberSetting(String configKey, EventDisplayInfo displayInfo) {
        super(configKey, Float.class, displayInfo);
    }

    public NumberSetting(String configKey) {
        super(configKey, Float.class, null);
    }

    public String toString() {
        return Float.toString(this.getValue());
    }

}
