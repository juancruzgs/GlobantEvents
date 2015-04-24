package com.globant.eventmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.globant.eventscorelib.baseComponents.BaseEventDescriptionFragment;
import com.globant.eventscorelib.baseComponents.BasePagerActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.fragments.SpeakersListFragment;
import com.globant.eventscorelib.fragments.TweetFragment;

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
        fragmentList.add(new EventParticipantsFragment());
        fragmentList.add(new SpeakersListFragment());
        fragmentList.add(new ManagerTwitterStreamFragment());
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
