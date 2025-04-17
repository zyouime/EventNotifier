package me.zyouime.eventnotifier.config;

import me.zyouime.eventnotifier.util.EventNotifierType;

import java.lang.reflect.Field;

public class ConfigData {

    float height = 260;
    float width = 100;
    float x = 50;
    float y = -50;
    EventNotifierType eventType = EventNotifierType.CURRENT;
    boolean showBoss = true;
    boolean showDeathMine = true;
    boolean showGlade = true;
    boolean showParcel = true;
    boolean showCargo = true;
    boolean showShip = true;
    boolean showVote = true;
    boolean showContainer = true;
    boolean showFever = true;
    boolean showTipo = true;


    public void setField(String fieldName, Object value) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(this, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object getField(String fieldName) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
