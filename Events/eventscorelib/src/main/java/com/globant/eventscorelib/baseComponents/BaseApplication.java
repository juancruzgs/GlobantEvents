package com.globant.eventscorelib.baseComponents;

import android.app.Application;

import com.globant.eventscorelib.controllers.CacheObjectsController;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.controllers.TwitterController;
import com.globant.eventscorelib.utils.CoreConstants;
import com.parse.Parse;

/**
 * Created by ignaciopena on 4/1/15.
 */
abstract public class BaseApplication extends Application {
    private static BaseApplication ourInstance;

    private SharedPreferencesController mSharedPreferencesController;
    private TwitterController mTwitterController;
    private CacheObjectsController mCacheObjectsController;

    abstract public Class<? extends BaseService> getServiceClass();

    public static BaseApplication getInstance()
    {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        Parse.initialize(this, CoreConstants.APPLICATION_ID, CoreConstants.CLIENT_KEY);
        mTwitterController = new TwitterController();
        mSharedPreferencesController = new SharedPreferencesController(getApplicationContext());
        mCacheObjectsController = new CacheObjectsController();
    }

    public SharedPreferencesController getSharedPreferencesController() {
        return mSharedPreferencesController;
    }

    public CacheObjectsController getCacheObjectsController() {
        return mCacheObjectsController;
    }

    public TwitterController getTwitterController() {
        return mTwitterController;
    }
}




