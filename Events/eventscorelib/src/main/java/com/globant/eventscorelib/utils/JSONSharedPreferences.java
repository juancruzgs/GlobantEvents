package com.globant.eventscorelib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.globant.eventscorelib.domainObjects.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ariel.cattaneo on 04/06/2015.
 *
 * From StackOverflow user Mostafa (Nov 27 - Dec 15 2011)
 */
public class JSONSharedPreferences {
    private static final String PREFIX = "json";

    public final static String KEY_CALENDAR = "KEY_CALENDAR";

    public static void saveJSONObject(Context c, String prefName, String key, JSONObject object) {
        SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(JSONSharedPreferences.PREFIX+key, object.toString());
        editor.apply();
    }

    public static void saveJSONArray(Context c, String prefName, String key, JSONArray array) {
        SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(JSONSharedPreferences.PREFIX+key, array.toString());
        editor.apply();
    }

    public static JSONObject loadJSONObject(Context c, String prefName, String key) throws JSONException {
        SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        return new JSONObject(settings.getString(JSONSharedPreferences.PREFIX+key, "{}"));
    }

    public static JSONArray loadJSONArray(Context c, String prefName, String key) throws JSONException {
        SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        return new JSONArray(settings.getString(JSONSharedPreferences.PREFIX+key, "[]"));
    }

    public static void remove(Context c, String prefName, String key) {
        SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        if (settings.contains(JSONSharedPreferences.PREFIX+key)) {
            SharedPreferences.Editor editor = settings.edit();
            editor.remove(JSONSharedPreferences.PREFIX+key);
            editor.apply();
        }
    }

    public static boolean addEvent(Context context, long nCalendar, long eventId, Event event) {
        JSONObject eventsArray;
        try {
            eventsArray = JSONSharedPreferences.loadJSONObject(context,
                    context.getApplicationInfo().name, JSONSharedPreferences.KEY_CALENDAR);

            JSONObject calendarData = new JSONObject();
            calendarData.put(CoreConstants.CALENDAR_SELF_ID, nCalendar);
            calendarData.put(CoreConstants.CALENDAR_EVENT_ID, eventId);
            calendarData.put(CoreConstants.CALENDAR_EVENT_LAST_UPDATE,
                    CustomDateFormat.getCompleteDate(event.getUpdatedAt(), context));
            eventsArray.put(event.getObjectID(), calendarData);
            JSONSharedPreferences.saveJSONObject(context, context.getApplicationInfo().name,
                    JSONSharedPreferences.KEY_CALENDAR, eventsArray);

            return true;

            //mAddedToCalendar = true;
        } catch (JSONException e) {
            Logger.e("Error trying to get this event's calendar id", e);
            return false;
        }
    }

    public static boolean removeEvent(Context context, Event event) {
        JSONObject eventsArray;
        try {
            eventsArray = JSONSharedPreferences.loadJSONObject(context,
                    context.getApplicationInfo().name, JSONSharedPreferences.KEY_CALENDAR);

            eventsArray.remove(event.getObjectID());
            JSONSharedPreferences.saveJSONObject(context, context.getApplicationInfo().name,
                    JSONSharedPreferences.KEY_CALENDAR, eventsArray);

            return true;
        } catch (JSONException e) {
            Logger.e("Error trying to get this event's calendar id", e);
            return false;
        }
    }

    public static boolean updateEvent(Context context, Event event) {
        JSONObject eventsArray;
        try {
            eventsArray = JSONSharedPreferences.loadJSONObject(context,
                    context.getApplicationInfo().name, JSONSharedPreferences.KEY_CALENDAR);

            JSONObject calendarData = eventsArray.getJSONObject(event.getObjectID());
            calendarData.put(CoreConstants.CALENDAR_EVENT_LAST_UPDATE,
                    CustomDateFormat.getCompleteDate(event.getUpdatedAt(), context));
            eventsArray.put(event.getObjectID(), calendarData);
            JSONSharedPreferences.saveJSONObject(context, context.getApplicationInfo().name,
                    JSONSharedPreferences.KEY_CALENDAR, eventsArray);

            return true;
        } catch (JSONException e) {
            Logger.e("Error trying to get this event's calendar id", e);
            return false;
        }
    }

    public static Long getCalendarIdFromEventId(Context context, String eventId) {
        JSONObject eventsArray;
        try {
            eventsArray = JSONSharedPreferences.loadJSONObject(context,
                    context.getApplicationInfo().name, JSONSharedPreferences.KEY_CALENDAR);
            if (eventsArray.has(eventId)) {
                JSONObject calendarData = eventsArray.getJSONObject(eventId);
                return calendarData.getLong(CoreConstants.CALENDAR_EVENT_ID);
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            Logger.e("Error trying to get the calendar id", e);
            return null;
        }
    }
}
