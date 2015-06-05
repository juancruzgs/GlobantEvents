package com.globant.eventscorelib.utils;

import android.content.Context;
import android.content.SharedPreferences;

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
}
