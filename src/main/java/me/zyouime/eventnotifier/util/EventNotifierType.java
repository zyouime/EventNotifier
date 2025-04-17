package me.zyouime.eventnotifier.util;

public enum EventNotifierType {

    CURRENT("Текущие ивенты"),
    UPCOMING("Предстоящие ивенты");
    public final String name;
    EventNotifierType(String name) {
        this.name = name;
    }
}