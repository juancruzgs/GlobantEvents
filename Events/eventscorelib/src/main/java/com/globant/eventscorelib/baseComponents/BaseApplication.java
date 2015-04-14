package com.globant.eventscorelib.baseComponents;

import android.app.Application;

import com.globant.eventscorelib.managers.SharedPreferencesManager;
import com.globant.eventscorelib.managers.TwitterManager;
import com.globant.eventscorelib.utils.CoreConstants;
import com.parse.Parse;

/**
 * Created by ignaciopena on 4/1/15.
 */
public class BaseApplication extends Application{
    private static BaseApplication ourInstance = new BaseApplication();

    private SharedPreferencesManager mSharedPreferencesManager;
    private TwitterManager mTwitterManager;

    public static BaseApplication getInstance()
    {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        Parse.initialize(this, CoreConstants.APPLICATION_ID, CoreConstants.CLIENT_KEY);
        mTwitterManager = new TwitterManager();
        mSharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
    }

    public SharedPreferencesManager getSharedPreferencesManager() {
        return mSharedPreferencesManager;
    }

    public TwitterManager getTwitterManager() {
        return mTwitterManager;
    }
}




