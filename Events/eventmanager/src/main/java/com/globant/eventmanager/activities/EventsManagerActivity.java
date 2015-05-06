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
 * Created by david.burgos on 04/05/2015.
 */
public class EventsManagerActivity extends BasePagerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new EventsFragment());
        fragmentList.add(new BaseSpeakersListFragment());
        fragmentList.add(new EventParticipantsManagerFragment());
        fragmentList.add(new EventDescriptionManagerFragment());
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