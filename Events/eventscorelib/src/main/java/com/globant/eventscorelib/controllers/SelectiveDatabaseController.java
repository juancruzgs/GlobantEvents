package com.globant.eventscorelib.controllers;

import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.CoreConstants;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;

/**
 * Created by juan.soler on 5/14/2015.
 */
public class SelectiveDatabaseController {

    LocalDatabaseController mLocalDatabaseController;
    CloudDatabaseController mCloudDatabaseController;

    public SelectiveDatabaseController(LocalDatabaseController localDatabaseController, CloudDatabaseController cloudDatabaseController) {
        mLocalDatabaseController = localDatabaseController;
        mCloudDatabaseController = cloudDatabaseController;
    }

    public List<Event> getEvents(boolean isGlober, boolean isOnline) throws ParseException {
        if (isOnline) {
            Date localDatabaseDate;
            try {
                localDatabaseDate = getDatabaseDate(false);
            } catch (ParseException e) {
                return mCloudDatabaseController.getEvents(isGlober);
            }
            Date cloudDatabaseDate = getDatabaseDate(true);
            if (cloudDatabaseDate.compareTo(localDatabaseDate) > 0) {
                return mCloudDatabaseController.getEvents(isGlober);
            } else {
                return mLocalDatabaseController.getEvents(isGlober);
            }
        } else {
            return mLocalDatabaseController.getEvents(isGlober);
        }
    }

    private Date getDatabaseDate(boolean cloudDatabase) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        ParseObject event;
        query.orderByDescending(CoreConstants.FIELD_UPDATED_AT);
        if (!cloudDatabase) {
            query.fromLocalDatastore();
        }
        event = query.getFirst();
        return event.getUpdatedAt();
    }
}
