package com.globant.events.activities;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.globant.events.fragments.StreamTwitterClientFragment;
import com.globant.events.fragments.EventDescriptionClientFragment;
import com.globant.events.R;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseFragments.BaseSpeakersListFragment;

import java.util.ArrayList;
import java.util.List;


public class EventDetailClientActivity extends BasePagerActivity{

    @Override
    protected List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new EventDescriptionClientFragment());
        //TODO: Add EventParticipantsFragment
//        fragmentList.add(new EventParticipantsFragment());
        fragmentList.add(new BaseSpeakersListFragment());
        fragmentList.add(new StreamTwitterClientFragment());
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
