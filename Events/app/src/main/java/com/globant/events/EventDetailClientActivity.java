package com.globant.events;

import android.support.v4.app.Fragment;

import com.globant.eventscorelib.baseComponents.BasePagerActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.fragments.SpeakersListFragment;

import java.util.ArrayList;
import java.util.List;


public class EventDetailClientActivity extends BasePagerActivity {

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
}
