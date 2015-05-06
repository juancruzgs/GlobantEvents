package com.globant.eventmanager.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.EventDescriptionManagerFragment;
import com.globant.eventmanager.fragments.EventParticipantsManagerFragment;
import com.globant.eventmanager.fragments.EventsFragment;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseFragments.BaseSpeakersListFragment;
import com.globant.eventscorelib.domainObjects.Event;

import java.util.ArrayList;
import java.util.Date;
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
        Event mEvent = new Event();
        mEvent.setTitle("Salida con Kt");
        mEvent.setFullDescription("Salida con Katherine Martinez a Santa Rosa de Cabal a termales.");
        mEvent.setShortDescription("Salida con Katherine Martinez");
        mEvent.setEndDate(new Date(2015, 04, 10, 12, 30));
        mEvent.setStartDate(new Date(2015, 04, 11, 2, 0));

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new EventsFragment().Edit(mEvent));
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