package com.globant.events.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.globant.events.R;
import com.globant.events.fragments.EventDescriptionClientFragment;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.events.fragments.TwitterStreamClientFragment;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseFragments.BaseSpeakersListFragment;

import java.util.ArrayList;
import java.util.List;


public class EventDetailClientActivity extends BaseEventDetailPagerActivity {

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
            fragmentList.add(new EventDescriptionClientFragment());
            fragmentList.add(new BaseSpeakersListFragment());
            fragmentList.add(new TwitterStreamClientFragment());
        }
        else {
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, EventDescriptionClientFragment.class.getName()));
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, BaseSpeakersListFragment.class.getName()));
            fragmentList.add(getSupportFragmentManager().getFragment(mSavedInstanceState, TwitterStreamClientFragment.class.getName()));
        }
        return fragmentList;
    }

    @Override
    protected List<String> getTitlesList() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.title_page_description));
//        titles.add(getString(R.string.title_page_participants));
        titles.add(getString(R.string.title_page_speakers));
        titles.add(getString(R.string.title_page_twitter));
        return titles;
    }
}
