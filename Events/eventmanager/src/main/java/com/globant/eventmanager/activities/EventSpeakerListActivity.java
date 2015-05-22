package com.globant.eventmanager.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.globant.eventmanager.R;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.baseFragments.BaseFragment;
import com.globant.eventscorelib.baseFragments.BaseSpeakerFragment;
import com.globant.eventscorelib.utils.BaseEasterEggsBasket;

public class EventSpeakerListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_speaker_list);
        BaseFragment fragment = new BaseSpeakerFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    protected boolean usesEgg() {
        return false;
    }

    @Override
    protected BaseEasterEggsBasket.EASTEREGGS whichEgg() {
        return null;
    }
}
