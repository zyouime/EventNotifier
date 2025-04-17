package me.zyouime.eventnotifier.util;

import me.zyouime.eventnotifier.EventNotifier;
import org.slf4j.Logger;

import java.util.List;

public interface Wrapper {

    EventNotifier eventNotifier = EventNotifier.getInstance();
    List<Event> events = eventNotifier.events;
    Logger LOGGER = EventNotifier.LOGGER;

}
