package com.globant.eventscorelib.baseComponents;

import android.app.Application;

import com.globant.eventscorelib.controllers.CacheObjectsController;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import twitter4j.User;

import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.utils.CoreConstants;
import com.parse.Parse;

import java.util.List;

import twitter4j.Status;

abstract public class BaseApplication extends Application {
    private static BaseApplication ourInstance;

    private SharedPreferencesController mSharedPreferencesController;
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
        mSharedPreferencesController = new SharedPreferencesController(getApplicationContext());
        mCacheObjectsController = new CacheObjectsController();
    }

    public SharedPreferencesController getSharedPreferencesController() {
        return mSharedPreferencesController;
    }

    public Event getEvent() {
        return mCacheObjectsController.getEvent();
    }

//    public List<Status> getTweetList () {
//        return mCacheObjectsController.getTweetList();
//    }

    public List<Speaker> getSpeakersList(){
        return mCacheObjectsController.getSpeakersList();
    }

    public User getTwitterUser () {
        return mCacheObjectsController.getUser();
    }

    public void setEvent (Event event) {
        mCacheObjectsController.setEvent(event);
    }

    public void setSpeakersList (List<Speaker> speakersList){
        mCacheObjectsController.setSpeakersList(speakersList);
    }

//    public void setTweetList (List<Status> twitterList) {
//        mCacheObjectsController.setTweetList(twitterList);
//    }

    public void setTwitterUser (User user) {
        mCacheObjectsController.setUser(user);
    }
}




