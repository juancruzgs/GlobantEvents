package com.globant.eventscorelib.baseActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.globant.eventscorelib.controllers.CacheObjectsController;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.CoreConstants;

import java.util.List;

import twitter4j.Status;
import twitter4j.User;

/**
 * Created by paula.baudo on 5/4/2015.
 */
public class BaseEventDetailPagerActivity extends BasePagerActivity {

    private static BaseEventDetailPagerActivity ourInstance;
    private CacheObjectsController mCacheObjectsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ourInstance = this;
        if (savedInstanceState != null) {
            mCacheObjectsController = savedInstanceState.getParcelable(CoreConstants.SAVE_INSTANCE_CACHE_OBJECTS);
        } else {
            mCacheObjectsController = new CacheObjectsController();
        }
    }

    public static BaseEventDetailPagerActivity getInstance() {
        return ourInstance;
    }

    public void setTweetList(List<Status> twitterList) {
        ourInstance.mCacheObjectsController.setTweetList(twitterList);
    }

    public List<Status> getTweetList() {
        return ourInstance.mCacheObjectsController.getTweetList();
    }

    public void setSpeakersList(List<Speaker> speakersList) {
        ourInstance.mCacheObjectsController.setSpeakersList(speakersList);
    }

    public User getTwitterUser() {
        return ourInstance.mCacheObjectsController.getUser();
    }

    public void setEvent(Event event) {
        ourInstance.mCacheObjectsController.setEvent(event);
    }

    public Event getEvent() {
        return ourInstance.mCacheObjectsController.getEvent();
    }

    public void setTwitterUser(User user) {
        ourInstance.mCacheObjectsController.setUser(user);
    }

    public List<Speaker> getSpeakersList() {
        return ourInstance.mCacheObjectsController.getSpeakersList();
    }

    public List<Subscriber> getSubscriberList() {
        return ourInstance.mCacheObjectsController.getSubscriberList();
    }

    public void setSubscriberList(List<Subscriber> subscriberList) {
        ourInstance.mCacheObjectsController.setSubscriberList(subscriberList);
    }

    @Override
    protected List<Fragment> getFragments() {
        return null;
    }

    @Override
    protected List<String> getTitlesList() {
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CoreConstants.SAVE_INSTANCE_CACHE_OBJECTS, mCacheObjectsController);
    }
}
