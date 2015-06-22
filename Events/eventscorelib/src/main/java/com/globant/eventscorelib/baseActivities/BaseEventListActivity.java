package com.globant.eventscorelib.baseActivities;


import android.os.Bundle;

import com.globant.eventscorelib.controllers.CacheObjectsController;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.easterEggs.BaseEasterEggsBasket;
import com.globant.eventscorelib.utils.CoreConstants;

import java.util.List;

public class BaseEventListActivity extends  BaseActivity{
    private CacheObjectsController mCacheObjectsController;
    private static BaseEventListActivity ourInstance;

    public static BaseEventListActivity getInstance() {
        return ourInstance;
    }

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

    public void setEventList (List<Event> eventList) {
        ourInstance.mCacheObjectsController.setEventList(eventList);
    }

    public List<Event> getEventList () {
        return ourInstance.mCacheObjectsController.getEventList();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mCacheObjectsController = savedInstanceState.getParcelable(CoreConstants.SAVE_INSTANCE_CACHE_OBJECTS);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CoreConstants.SAVE_INSTANCE_CACHE_OBJECTS, ourInstance.mCacheObjectsController);
    }

    @Override
    protected boolean usesEgg() {
        return false;
    }

    @Override
    protected BaseEasterEggsBasket.EASTEREGGS whichEgg() {
        return null;
    }
}
