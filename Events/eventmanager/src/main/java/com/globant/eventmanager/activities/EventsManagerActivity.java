package com.globant.eventmanager.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.EventDescriptionManagerFragment;
import com.globant.eventmanager.fragments.EventParticipantsManagerFragment;
import com.globant.eventmanager.fragments.EventsFragment;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseFragments.BaseSpeakersListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david.burgos
 */
public class EventsManagerActivity extends BasePagerActivity {

    List<Fragment> fragmentList;
    Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        for (Fragment fragment : fragmentList){
            getSupportFragmentManager().putFragment(outState,fragment.getClass().getName(), fragment);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected List<Fragment> getFragments() {
        fragmentList = new ArrayList<>();
        if (mSavedInstanceState == null){
            fragmentList.add(new EventsFragment());
            fragmentList.add(new BaseSpeakersListFragment());
            fragmentList.add(new EventParticipantsManagerFragment());
            fragmentList.add(new EventDescriptionManagerFragment());
        }
        else {
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, EventsFragment.class.getName()));
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, BaseSpeakersListFragment.class.getName()));
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, EventParticipantsManagerFragment.class.getName()));
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, EventDescriptionManagerFragment.class.getName()));
        }
        return fragmentList;
    }

    @Override
    protected List<String> getTitlesList() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.title_activity_event_detail));
        titles.add(getString(R.string.title_page_speakers));
        titles.add(getString(R.string.title_page_participants));
        titles.add(getString(R.string.title_activity_event_detail));
        return titles;
    }
}