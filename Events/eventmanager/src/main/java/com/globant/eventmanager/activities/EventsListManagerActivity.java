package com.globant.eventmanager.activities;

import android.os.Bundle;

import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.EventListManagerFragment;
import com.globant.eventscorelib.baseActivities.BaseActivity;


public class EventsListManagerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new EventListManagerFragment())
                    .commit();
        }
    }
}
