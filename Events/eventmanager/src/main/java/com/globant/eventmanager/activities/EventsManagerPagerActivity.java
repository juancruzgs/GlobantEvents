package com.globant.eventmanager.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.EventSpeakersList;
import com.globant.eventmanager.fragments.EventsFragment;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.controllers.CacheObjectsController;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.utils.CoreConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david.burgos
 */
public class EventsManagerPagerActivity extends BasePagerActivity {

    private static EventsManagerPagerActivity ourInstance;
    private CacheObjectsController mCacheObjectsController;

    Event mEvent;
    List<Fragment> fragmentList;
    Bundle mSavedInstanceState;

    public enum ActionType {EDIT_EVENT, CREATE_EVENT}
    public static ActionType mEventAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        ourInstance = this;

        if ((getIntent().getExtras() != null) && (savedInstanceState == null)) {
            mEvent = getIntent().getExtras().getParcelable(CoreConstants.FIELD_EVENTS);
            mCacheObjectsController = new CacheObjectsController();
            mEventAction = ActionType.EDIT_EVENT;
            getInstance().setEvent(mEvent);
            getInstance().setSpeakersList(mEvent.getSpeakers());
        }
        else
        if (savedInstanceState != null) {
            mCacheObjectsController = savedInstanceState.getParcelable(CoreConstants.SAVE_INSTANCE_CACHE_OBJECTS);
            mEvent = mCacheObjectsController.getEvent();
            int type = mSavedInstanceState.getInt(CoreConstants.SAVE_INSTANCE_EVENT_ACTION);
            switch (type){
                case 0:
                    mEventAction = ActionType.EDIT_EVENT;
                    break;
                default:
                    mEventAction = ActionType.CREATE_EVENT;
                    break;
            }
        }
        else{
            mEventAction = ActionType.CREATE_EVENT;
            List<Speaker> mSpeakerList = new ArrayList<>();
            mEvent = new Event();
            mEvent.setPublic(true);
            mEvent.setSpeakers(mSpeakerList);
            mCacheObjectsController = new CacheObjectsController();
            getInstance().setEvent(mEvent);
            getInstance().setSpeakersList(mEvent.getSpeakers());
        }
        if (mEvent != null) {
            getInstance().setEvent(mEvent);
            getInstance().setSpeakersList(mEvent.getSpeakers());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        for (Fragment fragment : fragmentList){
            getSupportFragmentManager().putFragment(outState,fragment.getClass().getName(), fragment);
        }
        outState.putParcelable(CoreConstants.SAVE_INSTANCE_CACHE_OBJECTS, mCacheObjectsController);
        outState.putInt(CoreConstants.SAVE_INSTANCE_EVENT_ACTION, EventsManagerPagerActivity.mEventAction.ordinal());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mCacheObjectsController = savedInstanceState.getParcelable(CoreConstants.SAVE_INSTANCE_CACHE_OBJECTS);
        mEvent = mCacheObjectsController.getEvent();
        int type = mSavedInstanceState.getInt(CoreConstants.SAVE_INSTANCE_EVENT_ACTION);
        switch (type){
            case 0:
                mEventAction = ActionType.EDIT_EVENT;
                break;
            default:
                mEventAction = ActionType.CREATE_EVENT;
                break;
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public static EventsManagerPagerActivity getInstance(){return ourInstance;}

    public void setSpeakersList (List<Speaker> speakersList){ourInstance.mCacheObjectsController.setSpeakersList(speakersList);}

    public List<Speaker> getSpeakersList(){return ourInstance.mCacheObjectsController.getSpeakersList();}

    public void setEvent (Event event) {ourInstance.mCacheObjectsController.setEvent(event);}

    public Event getEvent() {return ourInstance.mCacheObjectsController.getEvent();}

    @Override
    protected List<Fragment> getFragments() {
        fragmentList = new ArrayList<>();
        if (mSavedInstanceState == null){
            fragmentList.add(new EventsFragment());
            fragmentList.add(new EventSpeakersList());
        }
        else {
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, EventsFragment.class.getName()));
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, EventSpeakersList.class.getName()));
        }
        return fragmentList;
    }

    @Override
    protected List<String> getTitlesList() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.title_activity_event_detail));
        titles.add(getString(R.string.title_page_speakers));
        return titles;
    }
}