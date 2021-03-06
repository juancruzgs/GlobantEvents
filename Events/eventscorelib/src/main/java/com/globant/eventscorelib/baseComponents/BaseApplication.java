package com.globant.eventscorelib.baseComponents;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.globant.eventscorelib.controllers.SharedPreferencesController;

import com.globant.eventscorelib.utils.CoreConstants;
import com.parse.Parse;


abstract public class BaseApplication extends Application {
    private static BaseApplication ourInstance;

    private SharedPreferencesController mSharedPreferencesController;

    abstract public Class<? extends BaseService> getServiceClass();

    public static BaseApplication getInstance()
    {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
            Parse.enableLocalDatastore(this);
        Parse.initialize(this, CoreConstants.APPLICATION_ID, CoreConstants.CLIENT_KEY);
        mSharedPreferencesController = new SharedPreferencesController(getApplicationContext());
    }

    public SharedPreferencesController getSharedPreferencesController() {
        return mSharedPreferencesController;
    }

}




