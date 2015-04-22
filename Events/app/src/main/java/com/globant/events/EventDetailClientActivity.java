package com.globant.events;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.globant.eventscorelib.baseComponents.BaseEventDescriptionFragment;
import com.globant.eventscorelib.baseComponents.BasePagerActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.fragments.SpeakersListFragment;

import java.util.ArrayList;
import java.util.List;


public class EventDetailClientActivity extends BasePagerActivity {

    @Override
    public String getActivityTitle() {
        return "Description";
    }

    @Override
    protected Class<? extends BaseService> getServiceClass() {
        return ClientDataService.class;
    }

    @Override
    protected List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new EventDescriptionClientFragment());
        //TODO: Add EventParticipantsFragment
//        fragmentList.add(new EventParticipantsFragment());
        fragmentList.add(new SpeakersListFragment());
        fragmentList.add(new ClientStreamTwitterFragment());
        return fragmentList;
    }

    @Override
    protected List<String> getFragmentPageTitles() {
        List<String> list = new ArrayList<>();
        list.add("DESCRIPTION");
        list.add("SPEAKERS");
        list.add("TWITTER");
        return list;
    }
}
