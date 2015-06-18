package com.globant.eventscorelib.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.ConvertImage;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.CustomDateFormat;
import com.globant.eventscorelib.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SharedPreferencesController {

    public static String getUserFirstName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_FIRST_NAME, null);
    }

    public static String getUserLastName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_LAST_NAME, null);
    }

    public static String getUserEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_EMAIL, null);
    }

    public static String getUserOccupation(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_OCCUPATION_NAME, null);
    }

    public static String getUserPhone(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_PHONE, null);
    }

    public static String getUserCountry(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_COUNTRY, null);
    }

    public static String getUserCity(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_CITY, null);
    }

    public static Boolean getUserEnglishKnowledge(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CoreConstants.PREFERENCE_USER_ENGLISH_KNOWLEDGE, false);
    }

    public static String getUserTwitter(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.PREFERENCE_USER_TWITTER, null);
    }

    public static void setSubscriberInformation(Subscriber subscriber, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CoreConstants.PREFERENCE_USER_FIRST_NAME, subscriber.getName());
        editor.putString(CoreConstants.PREFERENCE_USER_LAST_NAME, subscriber.getLastName());
        editor.putString(CoreConstants.PREFERENCE_USER_EMAIL, subscriber.getEmail());
        editor.putString(CoreConstants.PREFERENCE_USER_OCCUPATION_NAME, subscriber.getOccupation());
        editor.putString(CoreConstants.PREFERENCE_USER_PHONE, subscriber.getPhone());
        editor.putBoolean(CoreConstants.PREFERENCE_USER_ENGLISH_KNOWLEDGE, subscriber.speaksEnglish());
        editor.putString(CoreConstants.PREFERENCE_USER_TWITTER, subscriber.getTwitterUser());
        editor.putString(CoreConstants.PREFERENCE_USER_COUNTRY, subscriber.getCountry());
        editor.putString(CoreConstants.PREFERENCE_USER_CITY, subscriber.getCity());
        byte[] image = ConvertImage.convertBitmapToByteArrayAndCompress(subscriber.getPicture());
        String encoded = Base64.encodeToString(image, Base64.DEFAULT);
        editor.putString(CoreConstants.PREFERENCE_USER_PICTURE, encoded);
        editor.putBoolean(CoreConstants.PREFERENCE_USER_IS_GLOBER, subscriber.isGlober());
        //TODO add field is public

        editor.apply();
    }

    public static void clearPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static byte[] getUserImage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String encoded = sharedPreferences.getString(CoreConstants.PREFERENCE_USER_PICTURE, null);
        byte[] image = null;
        if (encoded != null) {
            image = Base64.decode(encoded, Base64.DEFAULT);
        }
        return image;
    }

    public static boolean isGlober(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CoreConstants.PREFERENCE_USER_IS_GLOBER, false);
    }

    public static void setTwitterStatusResponse(String token, String tokenSecret, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CoreConstants.TWITTER_PREF_KEY_OAUTH_TOKEN, token);
        editor.putString(CoreConstants.TWITTER_PREF_KEY_OAUTH_SECRET, tokenSecret);
        editor.putBoolean(CoreConstants.TWITTER_IS_LOGGED_IN, true);
        editor.apply();
    }

    public static void setHintParticipantsShowed(boolean showed, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CoreConstants.PREFERENCE_PARTICIPANT_HINT_SHOWED, showed);
        editor.apply();
    }

    public static boolean isHintParticipantsShowed(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CoreConstants.PREFERENCE_PARTICIPANT_HINT_SHOWED, false);
    }

    public static String getAccessToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.TWITTER_PREF_KEY_OAUTH_TOKEN, null);
    }

    public static String getAccessTokenSecret(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CoreConstants.TWITTER_PREF_KEY_OAUTH_SECRET, null);
    }

    public static boolean isAlreadyTwitterLogged(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CoreConstants.TWITTER_IS_LOGGED_IN, false);
    }

    public static long getCleanChannelDate(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(CoreConstants.PREFERENCE_TIME, 0);
    }

    public static void setCleanChannelDate(Context context, Long time) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CoreConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(CoreConstants.PREFERENCE_TIME, time);
        editor.apply();
    }

    // JSON part: From StackOverflow user Mostafa (Nov 27 - Dec 15 2011) (added by Ariel Cattaneo)

    public final static String KEY_CALENDAR = "KEY_CALENDAR";
    private static final String PREFIX = "json";

    public static void saveJSONObject(Context c, String prefName, String key, JSONObject object) {
        SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREFIX +key, object.toString());
        editor.apply();
    }

    public static void saveJSONArray(Context c, String prefName, String key, JSONArray array) {
        SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREFIX +key, array.toString());
        editor.apply();
    }

    public static JSONObject loadJSONObject(Context c, String prefName, String key) throws JSONException {
        SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        return new JSONObject(settings.getString(PREFIX +key, "{}"));
    }

    public static JSONArray loadJSONArray(Context c, String prefName, String key) throws JSONException {
        SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        return new JSONArray(settings.getString(PREFIX +key, "[]"));
    }

    public static void removeJSON(Context c, String prefName, String key) {
        SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        if (settings.contains(PREFIX +key)) {
            SharedPreferences.Editor editor = settings.edit();
            editor.remove(PREFIX +key);
            editor.apply();
        }
    }

    public static boolean addEventJsonInfo(Context context, long nCalendar, long eventId, Event event) {
        JSONObject eventsArray;
        try {
            eventsArray = loadJSONObject(context,
                    context.getApplicationInfo().name, KEY_CALENDAR);

            JSONObject calendarData = new JSONObject();
            calendarData.put(CoreConstants.CALENDAR_SELF_ID, nCalendar);
            calendarData.put(CoreConstants.CALENDAR_EVENT_ID, eventId);
            calendarData.put(CoreConstants.CALENDAR_EVENT_LAST_UPDATE,
                    CustomDateFormat.getCompleteDate(event.getUpdatedAt(), context));
            eventsArray.put(event.getObjectID(), calendarData);
            saveJSONObject(context, context.getApplicationInfo().name,
                    KEY_CALENDAR, eventsArray);

            return true;

            //mAddedToCalendar = true;
        } catch (JSONException e) {
            Logger.e("Error trying to get this event's calendar id", e);
            return false;
        }
    }

    public static boolean removeEventJsonInfo(Context context, Event event) {
        JSONObject eventsArray;
        try {
            eventsArray = loadJSONObject(context,
                    context.getApplicationInfo().name, KEY_CALENDAR);

            eventsArray.remove(event.getObjectID());
            saveJSONObject(context, context.getApplicationInfo().name,
                    KEY_CALENDAR, eventsArray);

            return true;
        } catch (JSONException e) {
            Logger.e("Error trying to get this event's calendar id", e);
            return false;
        }
    }

    public static boolean updateEventJsonInfo(Context context, Event event) {
        JSONObject eventsArray;
        try {
            eventsArray = loadJSONObject(context,
                    context.getApplicationInfo().name, KEY_CALENDAR);

            JSONObject calendarData = eventsArray.getJSONObject(event.getObjectID());
            calendarData.put(CoreConstants.CALENDAR_EVENT_LAST_UPDATE,
                    CustomDateFormat.getCompleteDate(event.getUpdatedAt(), context));
            eventsArray.put(event.getObjectID(), calendarData);
            saveJSONObject(context, context.getApplicationInfo().name,
                    KEY_CALENDAR, eventsArray);

            return true;
        } catch (JSONException e) {
            Logger.e("Error trying to get this event's calendar id", e);
            return false;
        }
    }

    public static Long getCalendarIdFromEventId(Context context, String eventId) {
        JSONObject eventsArray;
        try {
            eventsArray = loadJSONObject(context,
                    context.getApplicationInfo().name, KEY_CALENDAR);
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


