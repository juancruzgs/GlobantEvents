package com.globant.eventscorelib.baseComponents;

import com.globant.eventscorelib.domainObjects.Event;

import java.util.List;

/**
 * Created by ariel.cattaneo on 10/04/2015.
 */
abstract public class DatabaseController {
    abstract void init();

    abstract public List<Event> getEvents();

    abstract public Event getEvent(String eventId);

    abstract public void addEvent(Event event);

    // TODO: Methods to access the rest of the objects
}
