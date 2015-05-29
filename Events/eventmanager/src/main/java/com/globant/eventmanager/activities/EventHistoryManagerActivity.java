package com.globant.eventmanager.activities;

import android.os.Bundle;

import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.EventHistoryManagerFragment;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.controllers.CacheObjectsController;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.BaseEasterEggsBasket;
import com.globant.eventscorelib.utils.CoreConstants;

import java.util.List;


public class EventHistoryManagerActivity extends BaseActivity {

    private CacheObjectsController mCacheObjectsController;
    private static EventHistoryManagerActivity ourInstance;

    public static EventHistoryManagerActivity getInstance() {
        return ourInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_event_history);
        ourInstance = this;
        if (savedInstanceState != null) {
            mCacheObjectsController = savedInstanceState.getParcelable(CoreConstants.SAVE_INSTANCE_CACHE_OBJECTS);
        } else {
            mCacheObjectsController = new CacheObjectsController();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new EventHistoryManagerFragment())
                    .commit();
        }
    }

    public void setEventHistory(List<Event> eventHistory) {
        ourInstance.mCacheObjectsController.setEventHistory(eventHistory);
    }

    public List<Event> getEventHistory () {
        return ourInstance.mCacheObjectsController.getEventHistory();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mCacheObjectsController = savedInstanceState.getParcelable(CoreConstants.SAVE_INSTANCE_CACHE_OBJECTS);
        }
    }

    @Override
    protected boolean usesEgg() {
        return false;
    }

    @Override
    protected BaseEasterEggsBasket.EASTEREGGS whichEgg() {
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CoreConstants.SAVE_INSTANCE_CACHE_OBJECTS, ourInstance.mCacheObjectsController);
    }
}