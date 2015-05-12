package com.globant.eventscorelib.baseActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.globant.eventscorelib.controllers.CacheObjectsController;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;

import java.util.List;

/**
 * Created by david.burgos
 */
public class BaseEventsManagerPagerActivity extends BasePagerActivity {

    private static BaseEventsManagerPagerActivity ourInstance;
    private CacheObjectsController mCacheObjectsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ourInstance = this;
        mCacheObjectsController = new CacheObjectsController();
    }

    public static BaseEventsManagerPagerActivity getInstance(){return ourInstance;}

    public void setSpeakersList (List<Speaker> speakersList){
        mCacheObjectsController.setSpeakersList(speakersList);
    }

    public List<Speaker> getSpeakersList(){return mCacheObjectsController.getSpeakersList();}

    public void setEvent (Event event) {mCacheObjectsController.setEvent(event);}

    public Event getEvent() {return mCacheObjectsController.getEvent();}

    @Override
    protected List<Fragment> getFragments() {
        return null;
    }

    @Override
    protected List<String> getTitlesList() {
        return null;
    }
}