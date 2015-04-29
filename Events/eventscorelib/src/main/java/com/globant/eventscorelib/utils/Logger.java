package com.globant.eventscorelib.utils;

import android.util.Log;

public class Logger {

    public static void e(String message, Exception exception){
        Log.e(CoreConstants.LOG_TAG, message + " - " , exception);
    }

    public static void d(String message){
        Log.d(CoreConstants.LOG_TAG, message);
    }

    public static void i(String message) { Log.i(CoreConstants.LOG_TAG, message); }
}