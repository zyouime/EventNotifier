package me.zyouime.eventnotifier.setting;

import me.zyouime.eventnotifier.util.EventDisplayInfo;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String configKey, EventDisplayInfo displayInfo) {
        super(configKey, Boolean.class, displayInfo);
    }
}
