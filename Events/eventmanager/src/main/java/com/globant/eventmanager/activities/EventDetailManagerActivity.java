package com.globant.eventmanager.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.EventDescriptionManagerFragment;
import com.globant.eventmanager.fragments.EventParticipantsManagerFragment;
import com.globant.eventmanager.fragments.TwitterStreamManagerFragment;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseFragments.BaseSpeakersListFragment;

import java.util.ArrayList;
import java.util.List;


public class EventDetailManagerActivity extends BasePagerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new EventDescriptionManagerFragment());
        fragmentList.add(new EventParticipantsManagerFragment());
        fragmentList.add(new BaseSpeakersListFragment());
        fragmentList.add(new TwitterStreamManagerFragment());
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
