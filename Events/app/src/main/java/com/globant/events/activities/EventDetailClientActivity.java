package com.globant.events.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.globant.events.R;
import com.globant.events.fragments.EventDescriptionClientFragment;
import com.globant.events.fragments.StreamTwitterClientFragment;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseFragments.BaseSpeakersListFragment;

import java.util.ArrayList;
import java.util.List;


public class EventDetailClientActivity extends BaseEventDetailPagerActivity {

    List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentList = new ArrayList<>();
        if (savedInstanceState == null){
            fragmentList.add(new EventDescriptionClientFragment());
            fragmentList.add(new BaseSpeakersListFragment());
            fragmentList.add(new StreamTwitterClientFragment());
        }
        else {
            fragmentList.add(getSupportFragmentManager().getFragment(savedInstanceState, EventDescriptionClientFragment.class.getName()));
            fragmentList.add(getSupportFragmentManager().getFragment(savedInstanceState, BaseSpeakersListFragment.class.getName()));
            fragmentList.add(getSupportFragmentManager().getFragment(savedInstanceState, StreamTwitterClientFragment.class.getName()));
        }
        prepareAdapter();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState, EventDescriptionClientFragment.class.getName(), fragmentList.get(0));
        getSupportFragmentManager().putFragment(outState, BaseSpeakersListFragment.class.getName(), fragmentList.get(1));
        getSupportFragmentManager().putFragment(outState, StreamTwitterClientFragment.class.getName(), fragmentList.get(2));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected List<Fragment> getFragments() {
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
