package com.globant.eventmanager.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;


import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.EventDescriptionManagerFragment;
import com.globant.eventmanager.fragments.EventParticipantsManagerFragment;
import com.globant.eventmanager.fragments.TwitterStreamManagerFragment;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseFragments.BaseSpeakersListFragment;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.CoreConstants;

import java.util.ArrayList;
import java.util.List;


public class EventDetailManagerActivity extends BaseEventDetailPagerActivity {

    List<Fragment> fragmentList;
    Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        Event event = getIntent().getExtras().getParcelable(CoreConstants.FIELD_EVENTS);
        this.setEvent(event);
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
            fragmentList.add(new EventDescriptionManagerFragment());
            fragmentList.add(new EventParticipantsManagerFragment());
            fragmentList.add(new BaseSpeakersListFragment());
            fragmentList.add(new TwitterStreamManagerFragment());
        }
        else {
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, EventDescriptionManagerFragment.class.getName()));
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, EventParticipantsManagerFragment.class.getName()));
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, BaseSpeakersListFragment.class.getName()));
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, TwitterStreamManagerFragment.class.getName()));
        }
        return fragmentList;
    }

    @Override
    protected List<String> getTitlesList() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.title_page_description));
        titles.add(getString(R.string.title_page_participants));
        titles.add(getString(R.string.title_page_speakers));
        titles.add(getString(R.string.title_page_twitter));
        return titles;
    }
}
