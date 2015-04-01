package com.globant.eventscorelib.baseComponents;

import android.app.Application;

/**
 * Created by ignaciopena on 4/1/15.
 */
public class BaseApplication extends Application{
    private static BaseApplication ourInstance = new BaseApplication();

    public static BaseApplication getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
    }
}
