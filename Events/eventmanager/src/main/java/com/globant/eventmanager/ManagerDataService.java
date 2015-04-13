package com.globant.eventmanager;

import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.domainObjects.Event;

/**
 * Created by ariel.cattaneo on 13/04/2015.
 */
public class ManagerDataService extends BaseService {
    public void addEvent(Event event) {
        mDatabaseController.putObject(event, "event");
    }

    public void updateEvent(Event event, String id) {
        mDatabaseController.setObject(event, id, "event");
    }
}
