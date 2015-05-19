package com.globant.eventscorelib.baseAdapters;

import com.globant.eventscorelib.domainObjects.Event;

public interface GetEventInformation {
    Event getEvent(int position);
    void getEvent(String eventId);
}
