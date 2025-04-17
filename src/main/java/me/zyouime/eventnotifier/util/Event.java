package me.zyouime.eventnotifier.util;

public class Event {

    private final String event;
    private final int anarchy;
    private int seconds;

    public Event(String event, int anarchy) {
        this.event = event;
        this.anarchy = anarchy;
    }

    public void tick() {
        seconds--;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
    public int getSeconds() {
        return seconds;
    }

    public int getAnarchy() {
        return anarchy;
    }

    public String getEvent() {
        return event;
    }
}
