package com.globant.events.activities;

import android.os.Bundle;

import com.globant.events.fragments.EventListClientFragment;
import com.globant.events.R;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.baseActivities.BaseEventListActivity;


public class EventListClientActivity extends BaseEventListActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_stream);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new EventListClientFragment())
                    .commit();
        }
    }
}
