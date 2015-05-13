package com.globant.eventmanager.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.EventParticipantsManagerFragment;
import com.globant.eventmanager.fragments.EventsFragment;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseFragments.BaseSpeakersListFragment;
import com.globant.eventscorelib.controllers.CacheObjectsController;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david.burgos
 */
public class EventsManagerPagerActivity extends BasePagerActivity {

    private static EventsManagerPagerActivity ourInstance;
    private CacheObjectsController mCacheObjectsController;

    public static Event mEvent;
    List<Fragment> fragmentList;
    Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        ourInstance = this;
        mCacheObjectsController = new CacheObjectsController();
        if (mEvent != null) {
            EventsManagerPagerActivity.getInstance().setEvent(mEvent);
            EventsManagerPagerActivity.getInstance().setSpeakersList(mEvent.getSpeakers());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        for (Fragment fragment : fragmentList){
            getSupportFragmentManager().putFragment(outState,fragment.getClass().getName(), fragment);
        }
        super.onSaveInstanceState(outState);
    }

    public static EventsManagerPagerActivity getInstance(){return ourInstance;}

    public void setSpeakersList (List<Speaker> speakersList){
        mCacheObjectsController.setSpeakersList(speakersList);
    }

    public List<Speaker> getSpeakersList(){return mCacheObjectsController.getSpeakersList();}

    public void setEvent (Event event) {mCacheObjectsController.setEvent(event);}

    public Event getEvent() {return mCacheObjectsController.getEvent();}

    @Override
    protected List<Fragment> getFragments() {
        fragmentList = new ArrayList<>();
        if (mSavedInstanceState == null){
            fragmentList.add(new EventsFragment());
            fragmentList.add(new BaseSpeakersListFragment());
            fragmentList.add(new EventParticipantsManagerFragment());
        }
        else {
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, EventsFragment.class.getName()));
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, BaseSpeakersListFragment.class.getName()));
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, EventParticipantsManagerFragment.class.getName()));
        }
        return fragmentList;
    }

    @Override
    protected List<String> getTitlesList() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.title_activity_event_detail));
        titles.add(getString(R.string.title_page_speakers));
        titles.add(getString(R.string.title_page_participants));
        return titles;
    }
}