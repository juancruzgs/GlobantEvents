package com.globant.eventscorelib.baseActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.globant.eventscorelib.baseFragments.BaseEventsFragment;
import com.globant.eventscorelib.baseFragments.BaseSpeakersListFragment;
import com.globant.eventscorelib.controllers.CacheObjectsController;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david.burgos
 */
public class BaseEventsManagerPagerActivity extends BasePagerActivity {

    private static BaseEventsManagerPagerActivity ourInstance;
    private CacheObjectsController mCacheObjectsController;
    List<Fragment> fragmentList;
    Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
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
            fragmentList = new ArrayList<>();
            if (mSavedInstanceState == null){
                fragmentList.add(new BaseEventsFragment());
            }
            else {
                fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, BaseEventsFragment.class.getName()));
            }
            return fragmentList;

    }

    @Override
    protected List<String> getTitlesList() {
        ArrayList fragmentsTitles = new ArrayList<>();
        fragmentsTitles.add("Event");
        return fragmentsTitles;


    }
}