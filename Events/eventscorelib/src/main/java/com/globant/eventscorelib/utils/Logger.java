package com.globant.eventscorelib.utils;

import android.util.Log;

public class Logger {

    public static final String LOG_TAG = "GlobantEventsApp";

    public static void e(String message, Exception exception){
        Log.e(LOG_TAG, message + " - " + exception.toString() + " - " + exception.getMessage());
    }

    public static void d(String message){
        Log.d(LOG_TAG, message);
    }
}