package com.globant.eventscorelib.controllers;

import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.JSONSharedPreferences;
import com.globant.eventscorelib.utils.Logger;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Iterator;
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
                // TODO: I suppose that, if there is an error with the local database, there are no events in the Calendar
                List<Event> events = mCloudDatabaseController.getEvents(isGlober);
                // TODO: Get the list of "calendared" events
                try {
                    JSONObject eventArray = JSONSharedPreferences.loadJSONObject(BaseApplication.getInstance(),
                            BaseApplication.getInstance().getApplicationInfo().name, JSONSharedPreferences.KEY_CALENDAR);

                    for (Event event : events) {
                        String eventId = event.getObjectID();
                        JSONObject eventJSON = eventArray.getJSONObject(eventId);
                        Date lastUpdateDb = event.getUpdatedAt();
                        Date lastUpdateCal = (Date) eventJSON.get("eventLastUpdate");
                        if (lastUpdateCal.before(lastUpdateDb)) {
                            // TODO: Force update every one, or try to check somehow which ones changed
                        }
                    }
                }
                catch (JSONException e) {
                    Logger.e("Problems with the internal event info while trying to update the event", e);
                }
                return events;
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
